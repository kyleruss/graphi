package com.graphi.display;

import com.graphi.io.SerialGraph;
import com.graphi.io.Storage;
import com.graphi.sim.Edge;
import com.graphi.sim.Node;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
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
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.collections15.Factory;

public class LayoutPanel extends JPanel
{
    private final String DATA_PANEL_CARD        =   "data_panel";
    private final String OUTPUT_PANEL_CARD      =   "output_panel";
    private final String DISP_PANEL_CARD        =   "display_panel";
    
    private final ControlPanel controlPanel;
    private final ScreenPanel screenPanel;
    private final JSplitPane splitPane;
    private final JScrollPane controlScroll;
    
    public static final Color TRANSPARENT   =   new Color(255, 255, 255, 0);
    private SerialGraph<Node, Edge> currentGraph;
    private File currentGraphFile, currentLogFile;
    private Map<Integer, Node> currentNodes;
    private Map<Integer, Edge> currentEdges;
    
    public LayoutPanel()
    {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(930, 650));
        
        currentGraph    =   new SerialGraph<>();
        controlPanel    =   new ControlPanel();
        screenPanel     =   new ScreenPanel();
        splitPane       =   new JSplitPane();
        controlScroll   =   new JScrollPane(controlPanel);

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
        outputArea.setText(outputArea.getText() + prefix + output);
    }
    
    private File getFileName(boolean open)
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
        
        private JPanel ioPanel;
        private JButton exportBtn, importBtn;
        private JLabel currentGraphLabel;
        private ButtonGroup storageGroup;
        private JRadioButton storageGraphRadio, storageLogRadio;
        
        private JPanel editPanel;
        private JLabel selectedLabel;
        private JButton gObjAddBtn, gObjEditBtn, gObjRemoveBtn;
        
        private JPanel computePanel;
        private JPanel computeInnerPanel;
        private JPanel clusterPanel, spathPanel;
        private JSpinner clusterEdgeRemoveSpinner;
        private JRadioButton noClusterRadio, clusterRadio, clusterCirclesRadio;
        private ButtonGroup clusterGroup;
        private JComboBox computeBox;
        private JTextField spathFromField, spathToField;
        private JButton computeBtn;
        private JPanel centralityPanel;
        private JComboBox centralityTypeBox;
        private ButtonGroup centralityOptions;
        private JRadioButton centralityAllRadio, centralitySelectedRadio;
        private JCheckBox centralityMorphCheck;
        
        public ControlPanel() 
        {
            setLayout(new CardLayout());
            setBorder(BorderFactory.createEmptyBorder(15, 0, 3, 8));
            setPreferredSize(new Dimension(230, 700));
            setMinimumSize(new Dimension(230, 650));
            
            dataControlPanel    =   new JPanel();
            outputControlPanel  =   new JPanel();
            displayControlPanel =   new JPanel();
            dataControlPanel.setLayout(new BoxLayout(dataControlPanel, BoxLayout.Y_AXIS));
            outputControlPanel.setLayout(new BoxLayout(outputControlPanel, BoxLayout.Y_AXIS));
            displayControlPanel.setLayout(new BoxLayout(displayControlPanel, BoxLayout.Y_AXIS));
            add(displayControlPanel, DISP_PANEL_CARD);
            add(dataControlPanel, DATA_PANEL_CARD);
            add(outputControlPanel, OUTPUT_PANEL_CARD);
            
            modePanel   =   new JPanel();
            modePanel.setPreferredSize(new Dimension(230, 100));
            simPanel    =   new JPanel();
            modePanel.setBorder(BorderFactory.createTitledBorder("Mode controls"));
            simPanel.setBorder(BorderFactory.createTitledBorder("Simulation controls"));
            simPanel.setPreferredSize(new Dimension(230, 500));
  
            modeGroup     =   new ButtonGroup();
            editCheck     =   new JRadioButton("Edit");
            selectCheck   =   new JRadioButton("Select");
            moveCheck     =   new JRadioButton("Move");
            
            modeGroup.add(editCheck);
            modeGroup.add(selectCheck);
            modeGroup.add(moveCheck);
            modePanel.add(editCheck);
            modePanel.add(selectCheck);
            modePanel.add(moveCheck);
            
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
            
            latticeSpinner      =   new JSpinner();
            clusteringSpinner   =   new JSpinner();   
            latticeSpinner.setPreferredSize(new Dimension(50, 20));
            clusteringSpinner.setPreferredSize(new Dimension(50, 20));
            
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
            
            ioPanel =   new JPanel(new GridLayout(3, 1));
            ioPanel.setBackground(TRANSPARENT);
            ioPanel.setBorder(BorderFactory.createTitledBorder("I/O Controls"));
            currentGraphLabel       =   new JLabel("None");
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
            
            JPanel storageBtnWrapper    =   wrapComponents(null, importBtn, exportBtn);
            JPanel currentGraphWrapper  =   wrapComponents(null, new JLabel("Active: "), currentGraphLabel);
            JPanel storageOptsWrapper   =   wrapComponents(null, storageGraphRadio, storageLogRadio);
            storageBtnWrapper.setBackground(TRANSPARENT);
            currentGraphWrapper.setBackground(TRANSPARENT);
            storageOptsWrapper.setBackground(new Color(200, 200, 200));
            ioPanel.add(currentGraphWrapper);
            ioPanel.add(storageOptsWrapper);
            ioPanel.add(storageBtnWrapper);
            currentGraphLabel.setFont(new Font("Arial", Font.BOLD, 12));
            
            editPanel       =   new JPanel(new GridLayout(2, 1));
            editPanel.setBorder(BorderFactory.createTitledBorder("Graph object Controls"));
            editPanel.setBackground(TRANSPARENT);
            
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
            
            JPanel selectedPanel    =   wrapComponents(null, new JLabel("Selected: "), selectedLabel);
            JPanel gObjPanel        =   wrapComponents(null, gObjAddBtn, gObjEditBtn, gObjRemoveBtn);
            selectedPanel.setBackground(TRANSPARENT);
            gObjPanel.setBackground(TRANSPARENT);

            editPanel.add(selectedPanel);
            editPanel.add(gObjPanel);
            
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
            
            computeBox.addItem("Clusters");
            computeBox.addItem("Centrality");
            computeBox.addItem("Shortest path");
            computeBox.addActionListener(this);
            
            clusterEdgeRemoveSpinner    =   new JSpinner();
            noClusterRadio              =   new JRadioButton("None");
            clusterRadio                =   new JRadioButton("Clusters");
            clusterCirclesRadio         =   new JRadioButton("Cluster circles");
            clusterGroup                =   new ButtonGroup();
            clusterEdgeRemoveSpinner.setPreferredSize(new Dimension(50, 20));
            clusterGroup.add(noClusterRadio);
            clusterGroup.add(clusterRadio);
            clusterGroup.add(clusterCirclesRadio);
            
            JPanel clusterEdgesPanel        =   wrapComponents(null, new JLabel("Delete edges"), clusterEdgeRemoveSpinner);
            JPanel clusterOptionsPanel      =   wrapComponents(null, noClusterRadio, clusterRadio, clusterCirclesRadio);   
            clusterOptionsPanel.setLayout(new GridLayout(3, 1));
            clusterPanel.setLayout(new BoxLayout(clusterPanel, BoxLayout.Y_AXIS));
            clusterPanel.add(clusterEdgesPanel);
            clusterPanel.add(clusterOptionsPanel);
            clusterEdgesPanel.setBackground(new Color(200, 200, 200));
            clusterOptionsPanel.setBackground(new Color(200, 200, 200));  
            
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
            centralityTypeBox.addActionListener(this);
            
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
            clusterInnerLayout.show(computeInnerPanel, CENTRALITY_PANEL_CARD);
            
            displayControlPanel.add(modePanel);
            displayControlPanel.add(simPanel);
            displayControlPanel.add(ioPanel);
            displayControlPanel.add(editPanel);
            displayControlPanel.add(computePanel);
            outputControlPanel.add(ioPanel);
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
            
            CardLayout clusterInnerLayout   =   (CardLayout) this.getLayout();
            clusterInnerLayout.show(this, card);
        }
        
        private void exportGraph()
        {
            File file   =   getFileName(false);
            if(file != null && currentGraph != null)
                Storage.saveGraph(currentGraph, file);
        }
        
        private void importGraph()
        {
            File file   =   getFileName(true);
            if(file != null)
            {
                currentGraph        =   Storage.openGraph(file);
                currentGraphFile    =   file;   
            }
        }
        
        private void exportLog()
        {
            File file   =   getFileName(false);
            if(file != null)
                Storage.saveOutputLog(screenPanel.outputPanel.outputArea.getText(), file);
        }
        
        private void importLog()
        {
            File file   =   getFileName(true);
            if(file != null)
            {
                currentLogFile  =   file;
                screenPanel.outputPanel.outputArea.setText(Storage.openOutputLog(file));
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object src  =   e.getSource();
            
            if(src == computeBox)
                showCurrentComputePanel();
            
            else if(src == importBtn)
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
            
            else if(src == gObjAddBtn)
                screenPanel.dataPanel.addVertex();
        }
        
    }
    
    private class ScreenPanel extends JPanel implements ChangeListener
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
            tabPane.addChangeListener(this);
            add(tabPane);
        }
        
        private void showCurrentControlPanel()
        {
            int selectedIndex   =   screenPanel.tabPane.getSelectedIndex();
            String card;
            
            switch(selectedIndex)
            {
                case 0: card = DISP_PANEL_CARD; break;
                case 1: card = DATA_PANEL_CARD; break;
                case 2: card = OUTPUT_PANEL_CARD; break;
                default: return;
            }
            
            SwingUtilities.invokeLater(() ->
            {
                CardLayout clusterInnerLayout   =   (CardLayout) controlPanel.getLayout();
                controlPanel.setVisible(false);
                clusterInnerLayout.show(controlPanel, card);
                controlPanel.setVisible(true);
            });
        }

        @Override
        public void stateChanged(ChangeEvent e)
        {
            showCurrentControlPanel();
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
                vertexTable         =   new JTable(vertexDataModel);
                edgeDataModel       =   new DefaultTableModel();
                edgeTable           =   new JTable(edgeDataModel);
                vertexScroller      =   new JScrollPane(vertexTable);
                edgeScroller        =   new JScrollPane(edgeTable);
                
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
            
            private void loadNodes(SerialGraph graph)
            {
                Collection<Node> vertices   =   graph.getVertices();
                SwingUtilities.invokeLater(() -> 
                {
                    vertexDataModel.setRowCount(0);
                    for(Node vertex : vertices)
                    {
                        int vID         =   vertex.getID();
                        String vName    =   vertex.getName();
                        vertexDataModel.addRow(new Object[] { vID, vName });
                    }
                });
            }
            
            private void loadEdges(SerialGraph graph)
            {
                Collection<Edge> edges  =   graph.getEdges();
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
                        n1  =   vertices.iterator().next();
                        n2  =   vertices.iterator().next();
                        
                        if(n1 != null)
                            n1_id   =   n1.getID();
                        else
                            n1_id   =   -1;
                        
                        if(n2 != null)
                            n2_id   =   n2.getID();
                        else
                            n2_id   =   -1;
                        
                        edgeDataModel.addRow(new Object[] { eID, n1_id, n2_id, weight, edgeType });
                        
                    }
                });
            }
            
            private void addVertex()
            {
                JPanel addPanel         =   new JPanel(new MigLayout());
                JSpinner idSpinner      =   new JSpinner();   
                JCheckBox autoCheck     =   new JCheckBox("Auto");
                JTextField nameField    =   new JTextField();
                
                addPanel.add(new JLabel("ID: "));
                addPanel.add(idSpinner, "width :40:");
                addPanel.add(autoCheck, "wrap");
                addPanel.add(new JLabel("Name: "));
                addPanel.add(nameField, "span 3, width :100:");
                
                int option  =   JOptionPane.showConfirmDialog(null, addPanel, "Add vertex", JOptionPane.OK_CANCEL_OPTION);
                
                if(option == JOptionPane.OK_OPTION)
                {
                    Node node   =   new Node((int) idSpinner.getValue(), nameField.getText());
                    currentGraph.addVertex(node);
                    graphPanel.gViewer.repaint();
                    loadNodes(currentGraph);
                }
            }
            
        }
        
        private class GraphPanel extends JPanel
        {
            private final VisualizationViewer<Node, Edge> gViewer;
            private Layout<Node, Edge> gLayout;
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
                mouse.setMode(ModalGraphMouse.Mode.PICKING);
                add(gViewer);
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
                outputArea.setPreferredSize(new Dimension(650, 600));
                JScrollPane outputScroller  =   new JScrollPane(outputArea);
                outputScroller.setBorder(null);
                
                add(outputScroller);
            }
        }
    }
    
}
