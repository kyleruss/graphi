package com.graphi.display;

import com.graphi.io.Storage;
import com.graphi.sim.Edge;
import com.graphi.sim.Network;
import com.graphi.sim.Node;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.algorithms.scoring.DistanceCentralityScorer;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.algorithms.scoring.VertexScorer;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

public class LayoutPanel extends JPanel
{
    private final ControlPanel controlPanel;
    private final ScreenPanel screenPanel;
    private final JSplitPane splitPane;
    private final JScrollPane controlScroll;
    
    public static final Color TRANSPARENT   =   new Color(255, 255, 255, 0);
    private Graph<Node, Edge> currentGraph;
    private File currentGraphFile, currentLogFile;
    private Map<Integer, Node> currentNodes;
    private Map<Integer, Edge> currentEdges;
    private int lastNodeID;
    private int lastEdgeID;
    private Object[] selectedItems;
    
    public LayoutPanel()
    {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(930, 650));
        
        currentGraph    =   new SparseMultigraph<>();
        currentNodes    =   new HashMap<>();
        currentEdges    =   new HashMap<>();
        controlPanel    =   new ControlPanel();
        screenPanel     =   new ScreenPanel();
        splitPane       =   new JSplitPane();
        controlScroll   =   new JScrollPane(controlPanel);
        lastNodeID      =   0;
        lastEdgeID      =   0;

        controlScroll.setBorder(null);
        splitPane.setLeftComponent(screenPanel);
        splitPane.setRightComponent(controlScroll); 
        splitPane.setDividerLocation(670);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void sendToOutput(String output)
    {
        SimpleDateFormat sdf    =   new SimpleDateFormat("K:MM a dd.MM.yy");
        String date             =   sdf.format(new Date());
        String prefix           =   "\n[" + date + "] ";
        JTextArea outputArea    =   screenPanel.outputPanel.outputArea;
        
        SwingUtilities.invokeLater(()->
        {
            outputArea.setText(outputArea.getText() + prefix + output);
        });
    }
    
    private File getFile(boolean open)
    {
        JFileChooser jfc    =   new JFileChooser();
        if(open)
            jfc.showOpenDialog(null);
        else
            jfc.showSaveDialog(null);
        
        return jfc.getSelectedFile();
    }

    public static JPanel wrapComponents(Border border, Component... components)
    {
        JPanel panel    =   new JPanel();
        panel.setBorder(border);
        
        for(Component component : components)
            panel.add(component);
        
        return panel;
    }
    
    private class ControlPanel extends JPanel implements ActionListener
    {
        private final String BA_PANEL_CARD          =   "ba_panel";
        private final String KL_PANEL_CARD          =   "kl_panel";
        
        private final String CLUSTER_PANEL_CARD     =   "cluster_panel";
        private final String SPATH_PANEL_CARD       =   "spath_panel";
        private final String CENTRALITY_PANEL_CARD  =   "centrality_panel";   
        
        private JPanel dataControlPanel, outputControlPanel, displayControlPanel;
        private JPanel modePanel;
        private JPanel simPanel;
        private JRadioButton editCheck, selectCheck, moveCheck;
        private ButtonGroup modeGroup;
        private JComboBox genAlgorithmsBox;
        private JButton resetGeneratorBtn, executeGeneratorBtn;
        private JPanel genPanel, baGenPanel, klGenPanel;
        
        private JSpinner latticeSpinner, clusteringSpinner;
        
        private IOPanel ioPanel;
        private JPanel editPanel;
        private JLabel selectedLabel;
        private JButton gObjAddBtn, gObjEditBtn, gObjRemoveBtn;
        
        private JPanel computePanel;
        private JPanel computeInnerPanel;
        private JPanel clusterPanel, spathPanel;
        private JSpinner clusterEdgeRemoveSpinner;
        private JCheckBox clusterTransformCheck;
        private JComboBox computeBox;
        private JTextField spathFromField, spathToField;
        private JButton computeBtn;
        private JPanel centralityPanel;
        private JComboBox centralityTypeBox;
        private ButtonGroup centralityOptions;
        private JRadioButton centralityAllRadio, centralitySelectedRadio;
        private JCheckBox centralityMorphCheck;
        private ButtonGroup editObjGroup;
        private JRadioButton editVertexRadio, editEdgeRadio;
        
        public ControlPanel() 
        {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEmptyBorder(15, 0, 3, 8));
            setPreferredSize(new Dimension(230, 800));
            setMinimumSize(new Dimension(230, 650));
            
            ioPanel         =   new IOPanel();
            
            modePanel       =   new JPanel();
            simPanel        =   new JPanel();
            modePanel.setPreferredSize(new Dimension(230, 100));
            modePanel.setBorder(BorderFactory.createTitledBorder("Mode controls"));
            simPanel.setBorder(BorderFactory.createTitledBorder("Simulation controls"));
            simPanel.setPreferredSize(new Dimension(230, 500));
  
            modeGroup       =   new ButtonGroup();
            editCheck       =   new JRadioButton("Edit");
            selectCheck     =   new JRadioButton("Select");
            moveCheck       =   new JRadioButton("Move");
            
            editCheck.addActionListener(this);
            selectCheck.addActionListener(this);
            moveCheck.addActionListener(this);
            
            modeGroup.add(editCheck);
            modeGroup.add(selectCheck);
            modeGroup.add(moveCheck);
            modePanel.add(editCheck);
            modePanel.add(selectCheck);
            modePanel.add(moveCheck);
            selectCheck.setSelected(true);
            
            genAlgorithmsBox        =   new JComboBox();
            genAlgorithmsBox.addItem("Kleinberg");
            genAlgorithmsBox.addItem("Barabasi-Albert");
            JPanel generatorPanel   =   new JPanel();
            generatorPanel.add(new JLabel("Generator"));
            generatorPanel.add(genAlgorithmsBox);
            generatorPanel.setBackground(TRANSPARENT);
            
            JPanel generatorControls    =   new JPanel();
            resetGeneratorBtn           =   new JButton("Reset");
            executeGeneratorBtn         =   new JButton("Generate");
            executeGeneratorBtn.addActionListener(this);
            resetGeneratorBtn.addActionListener(this);
            generatorControls.add(resetGeneratorBtn);
            generatorControls.add(executeGeneratorBtn);
            resetGeneratorBtn.setBackground(Color.WHITE);
            executeGeneratorBtn.setBackground(Color.WHITE);
            generatorControls.setBackground(TRANSPARENT);
            
            genPanel    =   new JPanel(new CardLayout());
            baGenPanel  =   new JPanel();
            klGenPanel  =   new JPanel(new BorderLayout());
            baGenPanel.setBackground(TRANSPARENT);
            klGenPanel.setBackground(TRANSPARENT);
            klGenPanel.setBorder(null);
            genPanel.setBorder(null);
            genPanel.add(baGenPanel, BA_PANEL_CARD);
            genPanel.add(klGenPanel, KL_PANEL_CARD);
            
            latticeSpinner      =   new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
            clusteringSpinner   =   new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));   
            latticeSpinner.setPreferredSize(new Dimension(50, 20));
            clusteringSpinner.setPreferredSize(new Dimension(50, 20));
            latticeSpinner.setValue(15);
            clusteringSpinner.setValue(2);
            
            JPanel klOptWrapper     =   new JPanel(new GridLayout(2, 2, 5, 10));
            klOptWrapper.add(new JLabel("Lattice size"));
            klOptWrapper.add(latticeSpinner);
            klOptWrapper.add(new JLabel("Clustering exp."));
            klOptWrapper.add(clusteringSpinner);
            klOptWrapper.setBackground(TRANSPARENT);
            klGenPanel.add(klOptWrapper, BorderLayout.WEST);
            
            simPanel.add(generatorPanel);
            simPanel.add(genPanel);
            simPanel.add(generatorControls);
            CardLayout genCLayout   =   (CardLayout) genPanel.getLayout();
            genCLayout.show(genPanel, KL_PANEL_CARD);
            
            editPanel       =   new JPanel(new GridLayout(3, 1));
            editPanel.setBorder(BorderFactory.createTitledBorder("Graph object Controls"));
            editPanel.setBackground(TRANSPARENT);
            
            editObjGroup    =   new ButtonGroup();
            editVertexRadio =   new JRadioButton("Vertex");
            editEdgeRadio   =   new JRadioButton("Edge");
            gObjAddBtn      =   new JButton("Add");
            gObjEditBtn     =   new JButton("Edit");
            gObjRemoveBtn   =   new JButton("Delete");
            selectedLabel   =   new JLabel("None");
            gObjAddBtn.setBackground(Color.WHITE);
            gObjEditBtn.setBackground(Color.WHITE);
            gObjRemoveBtn.setBackground(Color.WHITE);
            selectedLabel.setFont(new Font("Arial", Font.BOLD, 12));
            
            gObjAddBtn.addActionListener(this);
            gObjEditBtn.addActionListener(this);
            gObjRemoveBtn.addActionListener(this);
            
            editObjGroup.add(editVertexRadio);
            editObjGroup.add(editEdgeRadio);
            editVertexRadio.setSelected(true);
            
            JPanel selectedPanel        =   wrapComponents(null, new JLabel("Selected: "), selectedLabel);
            JPanel editObjPanel         =   wrapComponents(null, editVertexRadio, editEdgeRadio);
            JPanel gObjOptsPanel        =   wrapComponents(null, gObjAddBtn, gObjEditBtn, gObjRemoveBtn);
            selectedPanel.setBackground(new Color(200, 200, 200));
            gObjOptsPanel.setBackground(TRANSPARENT);
            editObjPanel.setBackground(new Color(200, 200, 200));

            editPanel.add(selectedPanel);
            editPanel.add(editObjPanel);
            editPanel.add(gObjOptsPanel);
            
            computePanel        =   new JPanel();
            computeInnerPanel   =   new JPanel(new CardLayout());
            clusterPanel        =   new JPanel();
            centralityPanel     =   new JPanel(new MigLayout());
            spathPanel          =   new JPanel();
            computeBox          =   new JComboBox();
            computeBtn          =   new JButton("Execute");
            computePanel.setPreferredSize(new Dimension(230, 500));
            computePanel.setBorder(BorderFactory.createTitledBorder("Computation controls"));
            spathPanel.setLayout(new BoxLayout(spathPanel, BoxLayout.Y_AXIS));
            spathPanel.setBackground(TRANSPARENT);
            computeBtn.setBackground(Color.WHITE);
            computeBtn.addActionListener(this);
            
            computeBox.addItem("Clusters");
            computeBox.addItem("Centrality");
            computeBox.addActionListener(this);
            
            clusterEdgeRemoveSpinner    =   new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
            clusterTransformCheck       =   new JCheckBox("Transform graph");
            clusterEdgeRemoveSpinner.setPreferredSize(new Dimension(50, 20));
            
            JPanel clusterEdgesPanel        =   wrapComponents(null, new JLabel("Delete edges"), clusterEdgeRemoveSpinner);
            clusterPanel.setLayout(new MigLayout());
            clusterPanel.add(clusterEdgesPanel, "wrap");
            clusterPanel.add(clusterTransformCheck);
            clusterEdgesPanel.setBackground(new Color(200, 200, 200));
            clusterPanel.setBackground(new Color(200, 200, 200));
            
            spathFromField  =   new JTextField();
            spathToField    =   new JTextField();
            spathFromField.setPreferredSize(new Dimension(50, 20));
            spathToField.setPreferredSize(new Dimension(50, 20));
            JLabel tLabel = new JLabel("To ID");
            JPanel spathFromPanel   =   wrapComponents(null, new JLabel("From ID"), spathFromField);
            JPanel spathToPanel     =   wrapComponents(null, tLabel, spathToField);
            JPanel spathWrapper     =   new JPanel(new MigLayout()); 
            spathWrapper.add(spathFromPanel, "wrap");
            spathWrapper.add(spathToPanel);
            spathWrapper.setBackground(TRANSPARENT);
            spathFromPanel.setBackground(TRANSPARENT);
            spathToPanel.setBackground(TRANSPARENT);
            spathPanel.add(spathWrapper); 
            
            centralityTypeBox       =   new JComboBox();
            centralityOptions       =   new ButtonGroup();
            centralityAllRadio      =   new JRadioButton("All");
            centralitySelectedRadio =   new JRadioButton("Selected");
            centralityMorphCheck    =   new JCheckBox("Transform graph");
            centralityOptions.add(centralityAllRadio);
            centralityOptions.add(centralitySelectedRadio);
            centralityTypeBox.addItem("Eigenvector");
            centralityTypeBox.addItem("PageRank");
            centralityTypeBox.addItem("Closeness");
            centralityTypeBox.addItem("Betweenness");
            centralityTypeBox.addActionListener(this);
            centralityAllRadio.setSelected(true);
            
            JPanel cenTypePanel     =   wrapComponents(null, new JLabel("Type"), centralityTypeBox);
            JPanel cenOptPanel      =   wrapComponents(null, centralityAllRadio, centralitySelectedRadio);
            centralityPanel.add(cenTypePanel, "wrap");
            centralityPanel.add(cenOptPanel, "wrap");
            centralityPanel.add(centralityMorphCheck);
            cenTypePanel.setBackground(new Color(200, 200, 200));
            cenOptPanel.setBackground(new Color(200, 200, 200));
            centralityPanel.setBackground(new Color(200, 200, 200));
            
            computeInnerPanel.add(clusterPanel, CLUSTER_PANEL_CARD);
            computeInnerPanel.add(spathPanel, SPATH_PANEL_CARD);
            computeInnerPanel.add(centralityPanel, CENTRALITY_PANEL_CARD);
            
            computePanel.add(new JLabel("Compute "));
            computePanel.add(computeBox);
            computePanel.add(computeInnerPanel);
            computePanel.add(computeBtn);
            
            CardLayout clusterInnerLayout   =   (CardLayout) computeInnerPanel.getLayout();
            clusterInnerLayout.show(computeInnerPanel, CLUSTER_PANEL_CARD);
            
            add(modePanel);
            add(simPanel);
            add(ioPanel);
            add(editPanel);
            add(computePanel);
        }
        
        private void updateSelectedComponents()
        {
            if(selectedItems == null || selectedItems.length == 0)
                selectedLabel.setText("None");
            else
            {
                if(selectedItems.length > 1)
                    selectedLabel.setText(selectedItems.length + " objects");
                else
                {
                    Object selectedObj  =   selectedItems[0];
                    if(selectedObj instanceof Node)
                        selectedLabel.setText("Node (ID=" + ((Node) selectedObj).getID() + ")");
                        
                    
                    else if(selectedObj instanceof Edge)
                        selectedLabel.setText("Edge (ID=" + ((Edge) selectedObj).getID() + ")");
                }
            }
        }
        
        private void computeExecute()
        {
            int selectedIndex   =   computeBox.getSelectedIndex();
            
            switch(selectedIndex)
            {
                case 0: screenPanel.graphPanel.showCluster();
                case 1: screenPanel.graphPanel.showCentrality();
            }
        }
        
        private void showGeneratorSim()
        {
            int genIndex    =   genAlgorithmsBox.getSelectedIndex();
            lastNodeID      =   0;
            lastEdgeID      =   0;
            switch(genIndex)
            {
                case 0: showKleinbergSim(); break;
                case 1: showBASim(); break;
            }
            
            screenPanel.graphPanel.reloadGraph();
        }
        
        private void resetSim()
        {
            currentGraph    =   new SparseMultigraph();
            screenPanel.graphPanel.reloadGraph();
        }
        
        private void showKleinbergSim()
        {
            int latticeSize =   (int) latticeSpinner.getValue();
            int clusterExp  =   (int) clusteringSpinner.getValue();
            currentGraph    =   Network.generateKleinberg(latticeSize, clusterExp, new NodeFactory(), new EdgeFactory());
        }
        
        private void showBASim()
        {
            currentGraph    =   Network.generateBerbasiAlbert(new NodeFactory(), new EdgeFactory(), 5, 5);
        }
        
        private class IOPanel extends JPanel implements ActionListener
        {
            private JButton exportBtn, importBtn;
            private JLabel currentStorageLabel;
            private ButtonGroup storageGroup;
            private JRadioButton storageGraphRadio, storageLogRadio;
            
            public IOPanel()
            {
                setLayout(new GridLayout(3, 1));
                setBackground(TRANSPARENT);
                setBorder(BorderFactory.createTitledBorder("I/O Controls"));
                currentStorageLabel     =   new JLabel("None");
                importBtn               =   new JButton("Import");
                exportBtn               =   new JButton("Export");
                storageGroup            =   new ButtonGroup();
                storageGraphRadio       =   new JRadioButton("Graph");
                storageLogRadio         =   new JRadioButton("Log");
                storageGroup.add(storageGraphRadio);
                storageGroup.add(storageLogRadio);
                importBtn.addActionListener(this);
                exportBtn.addActionListener(this);
                storageGraphRadio.addActionListener(this);
                storageLogRadio.addActionListener(this);
                importBtn.setBackground(Color.WHITE);
                exportBtn.setBackground(Color.WHITE);
                storageGraphRadio.setSelected(true);

                JPanel storageBtnWrapper    =   wrapComponents(null, importBtn, exportBtn);
                JPanel currentGraphWrapper  =   wrapComponents(null, new JLabel("Active: "), currentStorageLabel);
                JPanel storageOptsWrapper   =   wrapComponents(null, storageGraphRadio, storageLogRadio);
                storageBtnWrapper.setBackground(TRANSPARENT);
                currentGraphWrapper.setBackground(TRANSPARENT);
                storageOptsWrapper.setBackground(new Color(200, 200, 200));
                add(currentGraphWrapper);
                add(storageOptsWrapper);
                add(storageBtnWrapper);
                currentStorageLabel.setFont(new Font("Arial", Font.BOLD, 12));
            }

            @Override
            public void actionPerformed(ActionEvent e)
            {
                Object src  =   e.getSource();
                
                if(src == importBtn)
                {
                    if(storageGraphRadio.isSelected())
                        importGraph();
                    else
                        importLog();
                }

                else if(src == exportBtn)
                {
                    if(storageGraphRadio.isSelected())
                        exportGraph();
                    else
                        exportLog();
                }
            }
        }
        
        private void showCurrentComputePanel()
        {
            int selectedIndex   =   computeBox.getSelectedIndex();
            String card;
            
            switch(selectedIndex)
            {
                case 0: card = CLUSTER_PANEL_CARD; break;
                case 1: card = CENTRALITY_PANEL_CARD; break;
                case 2: card = SPATH_PANEL_CARD; break;
                default: return;
            }
            
            CardLayout clusterInnerLayout   =   (CardLayout) computeInnerPanel.getLayout();
            clusterInnerLayout.show(computeInnerPanel, card);
        }
        
        private void exportGraph()
        {
            File file   =   getFile(false);
            if(file != null && currentGraph != null)
                Storage.saveGraph(currentGraph, file);
        }
        
        private void importGraph()
        {
            File file   =   getFile(true);
            if(file != null)
            {
                lastNodeID          =   0;
                lastEdgeID          =   0;
                currentGraph        =   Storage.openGraph(file);
                currentGraphFile    =   file;   
                ioPanel.currentStorageLabel.setText(file.getName());
                
                initCurrentNodes();
                initCurrentEdges();
                
                screenPanel.graphPanel.gLayout.setGraph(currentGraph);
                screenPanel.graphPanel.gViewer.repaint();
                screenPanel.dataPanel.loadNodes(currentGraph);
                screenPanel.dataPanel.loadEdges(currentGraph);
            }
        }
        
        private void initCurrentNodes()
        {
            if(currentGraph == null) return;
            
            currentNodes.clear();
            Collection<Node> nodes  =   currentGraph.getVertices();
            for(Node node : nodes)
                currentNodes.put(node.getID(), node);
        }
        
        private void initCurrentEdges()
        {
            if(currentGraph == null) return;
            
            currentEdges.clear();
            Collection<Edge> edges  =   currentGraph.getEdges();
            for(Edge edge : edges)
                currentEdges.put(edge.getID(), edge);
        }
        
        private void exportLog()
        {
            File file   =   getFile(false);
            if(file != null)
                Storage.saveOutputLog(screenPanel.outputPanel.outputArea.getText(), file);
        }
        
        private void importLog()
        {
            File file   =   getFile(true);
            if(file != null)
            {
                currentLogFile  =   file;
                ioPanel.currentStorageLabel.setText(file.getName());
                screenPanel.outputPanel.outputArea.setText(Storage.openOutputLog(file));
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object src  =   e.getSource();
            
            if(src == computeBox)
                showCurrentComputePanel();
            
            else if(src == gObjAddBtn)
            {
                if(editVertexRadio.isSelected())
                    screenPanel.dataPanel.addVertex();
                else
                    screenPanel.dataPanel.addEdge();
            }
            
            else if(src == gObjEditBtn)
            {
                if(editVertexRadio.isSelected())
                    screenPanel.dataPanel.editVertex();
                else
                    screenPanel.dataPanel.editEdge();
            }
            
            else if(src == gObjRemoveBtn)
            {
                if(editVertexRadio.isSelected())
                    screenPanel.dataPanel.removeVertex();
                else
                    screenPanel.dataPanel.removeEdge();
            }
            
            else if(src == editCheck)
                screenPanel.graphPanel.mouse.setMode(ModalGraphMouse.Mode.EDITING);
            
            else if(src == moveCheck)
                screenPanel.graphPanel.mouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
            
            else if(src == selectCheck)
                screenPanel.graphPanel.mouse.setMode(ModalGraphMouse.Mode.PICKING);
            
            else if(src == executeGeneratorBtn)
                showGeneratorSim();
            
            else if(src == resetGeneratorBtn)
                resetSim();
            
            else if(src == computeBtn)
                computeExecute();
        }
    }
    
    private class ScreenPanel extends JPanel
    {
        private final DataPanel dataPanel;
        private final GraphPanel graphPanel;
        private final OutputPanel outputPanel;
        private final JTabbedPane tabPane;
        
        public ScreenPanel()
        {            
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(15, 5, 5, 5));
            tabPane     =   new JTabbedPane();
            dataPanel   =   new DataPanel();
            graphPanel  =   new GraphPanel();
            outputPanel =   new OutputPanel();
            
            
            tabPane.addTab("Display", graphPanel);
            tabPane.addTab("Data", dataPanel);
            tabPane.addTab("Output", outputPanel);
            add(tabPane);
        }
        
        private class DataPanel extends JPanel
        {
            private final JTable vertexTable, edgeTable;
            private final DefaultTableModel vertexDataModel, edgeDataModel;
            private final JTabbedPane dataTabPane;
            private final JScrollPane vertexScroller, edgeScroller;
            
            public DataPanel()
            {
                dataTabPane         =   new JTabbedPane();
                
                vertexDataModel     =   new DefaultTableModel();
                vertexTable         =   new JTable(vertexDataModel)
                {
                    @Override
                    public boolean isCellEditable(int row, int col)
                    {
                        return false;
                    };
                };
                
                edgeDataModel       =   new DefaultTableModel();
                edgeTable           =   new JTable(edgeDataModel)
                {
                    @Override
                    public boolean isCellEditable(int row, int col)
                    {
                        return false;
                    }
                };
                
                vertexScroller      =   new JScrollPane(vertexTable);
                edgeScroller        =   new JScrollPane(edgeTable);
                vertexTable.setPreferredScrollableViewportSize(new Dimension(630, 500));
                
                vertexDataModel.addColumn("ID");
                vertexDataModel.addColumn("Name");
                
                edgeDataModel.addColumn("ID");
                edgeDataModel.addColumn("FromVertex");
                edgeDataModel.addColumn("ToVertex");
                edgeDataModel.addColumn("Weight");
                edgeDataModel.addColumn("EdgeType");
                
                dataTabPane.addTab("Vertex table", vertexScroller);
                dataTabPane.addTab("Edge table", edgeScroller);
                
                add(dataTabPane);
            }
            
            private void loadNodes(Graph graph)
            {
                ArrayList<Node> vertices   =   new ArrayList<>(graph.getVertices());
                Collections.sort(vertices, (Node n1, Node n2) -> Integer.compare(n1.getID(), n2.getID()));
                
                currentNodes.clear();
                SwingUtilities.invokeLater(() -> 
                {
                    vertexDataModel.setRowCount(0);
                    for(Node vertex : vertices)
                    {
                        int vID         =   vertex.getID();
                        String vName    =   vertex.getName();
                        
                        currentNodes.put(vID, vertex);
                        vertexDataModel.addRow(new Object[] { vID, vName });
                        
                        if(vID > lastNodeID)
                            lastNodeID = vID;
                    }
                });
            }
            
            private void loadEdges(Graph graph)
            {
                ArrayList<Edge> edges  =   new ArrayList<>(graph.getEdges());
                Collections.sort(edges, (Edge e1, Edge e2) -> Integer.compare(e1.getID(), e2.getID())); 
                
                currentEdges.clear();
                
                SwingUtilities.invokeLater(() ->
                {
                    edgeDataModel.setRowCount(0);
                    for(Edge edge : edges)
                    {
                        int eID                     =   edge.getID();
                        double weight               =   edge.getWeight();
                        Collection<Node> vertices   =   graph.getIncidentVertices(edge);
                        String edgeType             =   graph.getEdgeType(edge).toString();
                        Node n1, n2;
                        int n1_id, n2_id;
                        Iterator<Node> iter         =   vertices.iterator();
                        n1  =   iter.next();
                        n2  =   iter.next();
                        
                        edge.setSourceNode(n1);
                        edge.setDestNode(n2);
                        
                        if(n1 != null)
                            n1_id   =   n1.getID();
                        else
                            n1_id   =   -1;
                        
                        if(n2 != null)
                            n2_id   =   n2.getID();
                        else
                            n2_id   =   -1;
                        
                        currentEdges.put(eID, edge);
                        edgeDataModel.addRow(new Object[] { eID, n1_id, n2_id, weight, edgeType });
                        
                        if(eID > lastEdgeID)
                            lastEdgeID = eID;
                    }
                });
            }
            
            private void addVertex()
            {
                VertexAddPanel addPanel    =   new VertexAddPanel();
                
                int option  =   JOptionPane.showConfirmDialog(null, addPanel, "Add vertex", JOptionPane.OK_CANCEL_OPTION);
                
                if(option == JOptionPane.OK_OPTION)
                {
                    int id      =   (int) addPanel.idSpinner.getValue();
                    if(currentNodes.containsKey(id))
                    {
                        JOptionPane.showMessageDialog(null, "Vertex already exists");
                        return;
                    }
                    
                    String name =   addPanel.nameField.getText();
                    Node node   =   new Node(id, name);
                    currentGraph.addVertex(node);
                    graphPanel.gViewer.repaint();
                    loadNodes(currentGraph);
                    currentNodes.put(id, node);
                }
            }
            
            private void editVertex()
            {
                Node editNode;
                Set<Node> selectedVertices      =   graphPanel.gViewer.getPickedVertexState().getPicked();
                
                if(selectedVertices.size() == 1)
                    editNode = selectedVertices.iterator().next();
                else
                {
                    int[] selectedRows  =   dataPanel.vertexTable.getSelectedRows();
                    if(selectedRows.length == 1)
                    {
                        int id      =   (int) dataPanel.vertexDataModel.getValueAt(selectedRows[0], 0);
                        editNode    =   currentNodes.get(id);   
                    }
                    
                    else
                    {
                        int id      =   getDialogID("Enter vertex ID to edit", currentNodes);

                        if(id != -1)
                            editNode    =   currentNodes.get(id);
                        else
                            return;
                    }
                }
                    
                VertexAddPanel editPanel    =   new VertexAddPanel();
                editPanel.idSpinner.setValue(editNode.getID());
                editPanel.nameField.setText(editNode.getName());
                editPanel.idSpinner.setEnabled(false);
                editPanel.autoCheck.setVisible(false);

                int option  =   JOptionPane.showConfirmDialog(null, editPanel, "Edit vertex", JOptionPane.OK_CANCEL_OPTION);
                if(option == JOptionPane.OK_OPTION)
                {
                    editNode.setID((int) editPanel.idSpinner.getValue());
                    editNode.setName(editPanel.nameField.getText());
                    loadNodes(currentGraph);
                }
            }
            
            private void removeVertices(Set<Node> vertices)
            {
                if(vertices.isEmpty()) return;
                
                for(Node node : vertices)
                {
                    int id  =   node.getID();
                    currentNodes.remove(id);
                    currentGraph.removeVertex(node);
                    graphPanel.gViewer.repaint();
                    loadNodes(currentGraph);
                }
            }
            
            private void removeVertex()
            {
                Set<Node> pickedNodes    =   graphPanel.gViewer.getPickedVertexState().getPicked();
                if(!pickedNodes.isEmpty())
                    removeVertices(pickedNodes);
                
                else
                {
                    int[] selectedRows  =   dataPanel.vertexTable.getSelectedRows();
                    if(selectedRows.length > 0)
                    {
                        Set<Node> selectedNodes  =   new HashSet<>();
                        for(int row : selectedRows)
                        {
                            int id          =   (int) dataPanel.vertexDataModel.getValueAt(row, 0);
                            Node current    =   currentNodes.get(id);
                            
                            if(current != null)
                                selectedNodes.add(current);
                        }
                        
                        removeVertices(selectedNodes);
                    }
                    
                    else
                    {
                        int id  =   getDialogID("Enter vertex ID to remove", currentNodes);
                        if(id != -1)
                        {
                            Node removedNode    =   currentNodes.remove(id);
                            currentGraph.removeVertex(removedNode);
                            loadNodes(currentGraph);
                            graphPanel.gViewer.repaint();
                        }
                    }
                }
            }
            
            private void addEdge()
            {
                EdgeAddPanel addPanel   =   new EdgeAddPanel();
                
                int option  =   JOptionPane.showConfirmDialog(null, addPanel, "Add edge", JOptionPane.OK_CANCEL_OPTION);
                
                if(option == JOptionPane.OK_OPTION)
                {
                    int id  =   (int) addPanel.idSpinner.getValue();
                    if(currentEdges.containsKey(id))
                    {
                        JOptionPane.showMessageDialog(null, "Edge ID already exists");
                        return;
                    }
                    
                    int fromID          =   (int) addPanel.fromSpinner.getValue();
                    int toID            =   (int) addPanel.toSpinner.getValue();
                    double weight       =   (double) addPanel.weightSpinner.getValue();
                    int eType           =   addPanel.edgeTypeBox.getSelectedIndex();
                    EdgeType edgeType   =   (eType == 0)? EdgeType.UNDIRECTED : EdgeType.DIRECTED;
                    
                    if(currentNodes.containsKey(fromID) && currentNodes.containsKey(toID))
                    {
                        Edge edge       =   new Edge(id, weight, edgeType);
                        Node n1         =   currentNodes.get(fromID);
                        Node n2         =   currentNodes.get(toID);
                        edge.setSourceNode(n1);
                        edge.setDestNode(n2);
                        
                        currentEdges.put(id, edge);
                        currentGraph.addEdge(edge, n1, n2, edgeType);
                        loadEdges(currentGraph);
                        graphPanel.gViewer.repaint();
                    }
                    
                    else JOptionPane.showMessageDialog(null, "Vertex ID does not exist");
                }
            }
            
            private void editEdge()
            {
                Edge editEdge;
                Set<Edge> selectedEdges =   graphPanel.gViewer.getPickedEdgeState().getPicked();
                if(selectedEdges.size() == 1)
                    editEdge    =   selectedEdges.iterator().next();
                else
                {
                    int[] selectedRows  =   dataPanel.edgeTable.getSelectedRows();
                    if(selectedRows.length == 1)
                    {
                        int id      =   (int) dataPanel.edgeDataModel.getValueAt(selectedRows[0], 0);
                        editEdge    =   currentEdges.get(id);
                    }
                    
                    else
                    {
                        int id  =   getDialogID("Enter edge ID to edit", currentEdges);

                        if(id != -1)
                            editEdge    =   currentEdges.get(id);
                        else
                            return;
                    }
                }
                
                EdgeAddPanel editPanel  =   new EdgeAddPanel();
                editPanel.idSpinner.setValue(editEdge.getID());
                editPanel.fromSpinner.setValue(editEdge.getSourceNode().getID());
                editPanel.toSpinner.setValue(editEdge.getDestNode().getID());
                editPanel.weightSpinner.setValue(editEdge.getWeight());
                editPanel.edgeTypeBox.setSelectedIndex(editEdge.getEdgeType() == EdgeType.UNDIRECTED? 0 : 1);

                editPanel.fromSpinner.setEnabled(false);
                editPanel.toSpinner.setEnabled(false);
                editPanel.idSpinner.setEnabled(false);
                editPanel.autoCheck.setVisible(false);

                int option  =   JOptionPane.showConfirmDialog(null, editPanel, "Edit edge", JOptionPane.OK_CANCEL_OPTION);
                if(option == JOptionPane.OK_OPTION)
                {
                    editEdge.setWeight((double) editPanel.weightSpinner.getValue());
                    editEdge.setEdgeType(editPanel.edgeTypeBox.getSelectedIndex() == 0? EdgeType.UNDIRECTED : EdgeType.DIRECTED);
                    loadEdges(currentGraph);
                    graphPanel.gViewer.repaint();
                }
            }
            
            private void removeEdge()
            {
                Set<Edge> selectedEdges =   graphPanel.gViewer.getPickedEdgeState().getPicked();
                
                if(selectedEdges.isEmpty())
                {
                    int[] selectedRows  =   dataPanel.edgeTable.getSelectedRows();
                    if(selectedRows.length > 0)
                    {
                        for(int row : selectedRows)
                        {
                            int id          =   (int) dataPanel.edgeDataModel.getValueAt(row, 0);
                            Edge current    =   currentEdges.remove(id);
                            currentGraph.removeEdge(current);
                        }
                    }
                    
                    else
                    {
                        int id  =   getDialogID("Enter edge ID to remove", currentEdges);

                        if(id != -1)
                        {
                            Edge removeEdge =   currentEdges.remove(id);
                            currentGraph.removeEdge(removeEdge);
                        }

                        else return;
                    }
                }
                
                else
                {
                    for(Edge edge : selectedEdges)
                    {
                        currentEdges.remove(edge.getID());
                        currentGraph.removeEdge(edge);
                    }
                }
                
                loadEdges(currentGraph);
                graphPanel.gViewer.repaint();
            }
            
            private int getDialogID(String message, Map collection)
            {
                String idStr = JOptionPane.showInputDialog(message);
                int id = -1;
                while(idStr != null && id == -1)
                {
                    try
                    {
                        id  =   Integer.parseInt(idStr);
                        if(collection.containsKey(id))
                            break;
                        else
                        {
                            id      =   -1;
                            JOptionPane.showMessageDialog(null, "ID was not found");
                            idStr   =   JOptionPane.showInputDialog(message);
                        }
                    }
                    
                    catch(NumberFormatException e)
                    {
                        JOptionPane.showMessageDialog(null, "Invalid input found");
                        idStr = JOptionPane.showInputDialog(message);
                    }
                }
                
                return id;
            }
           
            
            private class VertexAddPanel extends JPanel implements ActionListener
            {
                private JSpinner idSpinner;
                private JCheckBox autoCheck;
                private JTextField nameField;
                
                public VertexAddPanel()
                {
                    setLayout(new MigLayout());
                    idSpinner      =   new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));   
                    autoCheck      =   new JCheckBox("Auto");
                    nameField      =   new JTextField();
                    
                    autoCheck.setSelected(true);
                    idSpinner.setValue(lastNodeID + 1);
                    idSpinner.setEnabled(false);
                    
                    add(new JLabel("ID "));
                    add(idSpinner, "width :40:");
                    add(autoCheck, "wrap");
                    add(new JLabel("Name: "));
                    add(nameField, "span 3, width :100:");
                    autoCheck.addActionListener(this);
                }

                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    Object src  =   e.getSource();
                    if(src == autoCheck)
                        idSpinner.setEnabled(!autoCheck.isSelected());
                }
            }
            
            private class EdgeAddPanel extends JPanel implements ActionListener
            {
                private JSpinner idSpinner, fromSpinner, toSpinner, weightSpinner;
                private JCheckBox autoCheck;
                private JComboBox edgeTypeBox;
                
                public EdgeAddPanel()
                {
                    setLayout(new MigLayout());
                    idSpinner       =   new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
                    fromSpinner     =   new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
                    toSpinner       =   new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
                    weightSpinner   =   new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10000.0, 0.1));
                    autoCheck       =   new JCheckBox("Auto");
                    edgeTypeBox     =   new JComboBox();
                    
                    edgeTypeBox.addItem("Undirected");
                    edgeTypeBox.addItem("Directed");
                    
                    idSpinner.setValue(lastEdgeID + 1);
                    idSpinner.setEnabled(false);
                    autoCheck.setSelected(true);
                    autoCheck.addActionListener(this);
                    
                    add(new JLabel("ID "));
                    add(idSpinner, "width :40:");
                    add(autoCheck, "wrap");
                    add(new JLabel("From vertex ID"));
                    add(fromSpinner, "span 2, width :40:, wrap");
                    add(new JLabel("To vertex ID"));
                    add(toSpinner, "span 2, width :40:, wrap");
                    add(new JLabel("Weight"));
                    add(weightSpinner, "span 2, width :70:, wrap");
                    add(new JLabel("Type"));
                    add(edgeTypeBox, "span 2");
                }

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Object src  =   e.getSource();
                    if(src == autoCheck)
                        idSpinner.setEnabled(!autoCheck.isSelected());
                }
            }
        }
        
        private class GraphPanel extends JPanel implements ItemListener
        {
            private final VisualizationViewer<Node, Edge> gViewer;
            private AggregateLayout<Node, Edge> gLayout;
            private EditingModalGraphMouse mouse;
            
            public GraphPanel()
            {
                gLayout     =   new AggregateLayout(new FRLayout(currentGraph));
                gViewer     =   new VisualizationViewer<>(gLayout);
                gLayout.setSize(new Dimension(670, 650));
                gViewer.setPreferredSize(new Dimension(670, 650));
                ScalingControl scaler   =   new CrossoverScalingControl();
                scaler.scale(gViewer, 0.7f, gViewer.getCenter());
                gViewer.scaleToLayout(scaler);
                gViewer.setBackground(Color.WHITE);
                
                Factory<Node> nFactory  =   () -> new Node();
                Factory<Edge> eFactory  =   () -> new Edge();
                
                mouse       =   new EditingModalGraphMouse(gViewer.getRenderContext(), nFactory, eFactory);
                gViewer.setGraphMouse(mouse);
                gViewer.getRenderContext().setVertexFillPaintTransformer(new ColorTransformer(gViewer.getPickedVertexState()));
                gViewer.getPickedVertexState().addItemListener(this);
                gViewer.getPickedEdgeState().addItemListener(this);
                mouse.setMode(ModalGraphMouse.Mode.PICKING);
                add(gViewer);
            }
            
            private class ColorTransformer implements Transformer<Node, Paint>
            {
                PickedInfo pickedInfo;
                public ColorTransformer(PickedInfo pickedInfo)
                {
                    this.pickedInfo =   pickedInfo;
                }

                @Override
                public Paint transform(Node i)
                {
                    if(pickedInfo.isPicked(i))
                        return Color.YELLOW;
                    else
                        return i.getColor();
                }
            }
            
            private void showCluster()
            {
                int numRemoved  =   (int) controlPanel.clusterEdgeRemoveSpinner.getValue();
                boolean group   =   controlPanel.clusterTransformCheck.isSelected();
                Network.cluster(gLayout, currentGraph, numRemoved, group);
                gViewer.repaint();
            }
            
            private void showCentrality()
            {
                VertexScorer<Node, Double> centrality;
                int selectedCentrality  =   controlPanel.centralityTypeBox.getSelectedIndex();
                String prefix;
                
                switch(selectedCentrality)
                {
                    case 0: 
                        centrality  =   new EigenvectorCentrality(currentGraph, new WeightTransformer()); 
                        prefix      =   "EigenVector";
                        break;
                        
                    case 1: 
                        centrality  =   new PageRank<>(currentGraph, new WeightTransformer(), 0.15);
                        prefix      =   "PageRank";
                        break;
                        
                    case 3: 
                        centrality  =   new ClosenessCentrality(currentGraph, new WeightTransformer()); 
                        prefix      =   "Closeness";
                        break;
                        
                    case 2: 
                        centrality  =   new BetweennessCentrality(currentGraph, new WeightTransformer()); 
                        prefix      =   "Betweenness";
                        break;
                        
                    default: return;
                }
                
                
                Collection<Node> vertices                       =   currentGraph.getVertices();
                for(Node node : vertices)
                {
                    double score    =   centrality.getVertexScore(node);
                    String output   =   MessageFormat.format("({0}) Vertex: {1}, Score: {2}", prefix, node.getID(), score);
                    sendToOutput(output);
                }
            }
            
            private void reloadGraph()
            {
                gLayout.setGraph(currentGraph);
                gViewer.repaint();
                dataPanel.loadNodes(currentGraph);
                dataPanel.loadEdges(currentGraph);
            }

            @Override
            public void itemStateChanged(ItemEvent e)
            {
                selectedItems   =   e.getItemSelectable().getSelectedObjects();
                controlPanel.updateSelectedComponents();
            }
        }
        
        private class OutputPanel extends JPanel
        {
            private JTextArea outputArea;
            public OutputPanel()
            {
                outputArea  =   new JTextArea("");
                outputArea.setBackground(Color.WHITE);
                outputArea.setEditable(false);
                JScrollPane outputScroller  =   new JScrollPane(outputArea);
                outputScroller.setPreferredSize(new Dimension(650, 565));
                outputScroller.setBorder(null);
                
                add(outputScroller);
            }
        }
    }
    
    
    private class NodeFactory implements Factory<Node>
    {
        @Override
        public Node create() 
        {
            lastNodeID++;
            return new Node(lastNodeID, Integer.toHexString(lastNodeID));
        }
    }
    
    private class EdgeFactory implements Factory<Edge>
    {
        Random random   =   new Random();
        
        @Override
        public Edge create() 
        {
            lastEdgeID++;
            double weight   =   random.nextDouble() * 100.0;
            return new Edge(lastEdgeID, weight, EdgeType.UNDIRECTED);
        }
    }
    
    private class WeightTransformer implements Transformer<Edge, Double>
    {
        @Override
        public Double transform(Edge edge)
        {
            return 1.0 - (edge.getWeight() / 100.0);
        }
    }
}
