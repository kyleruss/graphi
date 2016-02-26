//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import com.graphi.app.AppManager;
import com.graphi.app.Consts;
import com.graphi.display.MainMenu;
import com.graphi.io.AdjMatrixParser;
import com.graphi.io.GMLParser;
import com.graphi.io.GraphMLParser;
import com.graphi.io.Storage;
import com.graphi.plugins.Plugin;
import com.graphi.plugins.PluginConfig;
import com.graphi.plugins.PluginManager;
import com.graphi.sim.GraphPlayback;
import com.graphi.util.Edge;
import com.graphi.sim.Network;
import com.graphi.sim.PlaybackEntry;
import com.graphi.util.EdgeLabelTransformer;
import com.graphi.util.GraphData;
import com.graphi.util.GraphUtilities;
import com.graphi.util.MatrixTools;
import com.graphi.util.Node;
import com.graphi.util.ObjectFillTransformer;
import com.graphi.util.VertexLabelTransformer;
import com.graphi.util.WeightTransformer;
import de.javasoft.swing.DateComboBox;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.matrix.GraphMatrixOperations;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.io.FilenameUtils;

public class MainPanel extends JPanel
{
    protected final ControlPanel controlPanel;
    protected final ScreenPanel screenPanel;
    protected final JSplitPane splitPane;
    protected final JScrollPane controlScroll;
    
    protected BufferedImage addIcon, removeIcon, colourIcon;
    protected BufferedImage clipIcon, openIcon, saveIcon;
    protected BufferedImage editBlackIcon, pointerIcon, moveIcon;
    protected BufferedImage moveSelectedIcon, editSelectedIcon, pointerSelectedIcon;
    protected BufferedImage graphIcon, tableIcon, resetIcon, executeIcon;
    protected BufferedImage editIcon, playIcon, stopIcon, recordIcon, closeIcon;
    
    protected GraphData data;
    protected MainMenu menu;
    protected JFrame frame; 
    protected AppManager appManager;
    
    public MainPanel(AppManager appManager)
    {
        setPreferredSize(new Dimension(Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT));
        setLayout(new BorderLayout());        
        initResources();
        
        this.appManager     =   appManager;
        menu                =   appManager.getWindow().getMenu();
        frame               =   appManager.getWindow().getFrame();
        data                =   new GraphData();
        controlPanel        =   new ControlPanel(this);
        screenPanel         =   new ScreenPanel();
        splitPane           =   new JSplitPane();
        controlScroll       =   new JScrollPane(controlPanel);

        controlScroll.setBorder(null);
        splitPane.setLeftComponent(screenPanel);
        splitPane.setRightComponent(controlScroll); 
        splitPane.setResizeWeight(Consts.MAIN_SPLIT_WG);
        add(splitPane, BorderLayout.CENTER);
    }
    
    public GraphData getGraphData()
    {
        return data;
    }
    
    public void setGraphData(GraphData data)
    {
        this.data   =   data;
    }
    
    protected void sendToOutput(String output)
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
    
    protected File getFile(boolean open, String desc, String...extensions)
    {
        JFileChooser jfc                =   new JFileChooser();
        FileNameExtensionFilter filter  =   new FileNameExtensionFilter(desc, extensions);
        jfc.setFileFilter(filter);
        
        if(open)
            jfc.showOpenDialog(null);
        else
            jfc.showSaveDialog(null);
        
        return jfc.getSelectedFile();
    }
    
    protected String getFileExtension(File file)
    {
        if(file == null) return "";
        return FilenameUtils.getExtension(file.getPath());
    }

    public static JPanel wrapComponents(Border border, Component... components)
    {
        JPanel panel    =   new JPanel();
        panel.setBorder(border);
        
        for(Component component : components)
            panel.add(component);
        
        return panel;
    }
    
    protected void initResources()
    {
        try
        {
            addIcon             =   ImageIO.read(new File(Consts.IMG_DIR + "addSmallIcon.png"));
            removeIcon          =   ImageIO.read(new File(Consts.IMG_DIR + "removeSmallIcon.png"));   
            colourIcon          =   ImageIO.read(new File(Consts.IMG_DIR + "color_icon.png"));   
            clipIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "clipboard.png"));  
            saveIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "new_file.png"));
            openIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "open_icon.png"));
            editBlackIcon       =   ImageIO.read(new File(Consts.IMG_DIR + "editblack.png"));
            pointerIcon         =   ImageIO.read(new File(Consts.IMG_DIR + "pointer.png"));
            moveIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "move.png"));
            moveSelectedIcon    =   ImageIO.read(new File(Consts.IMG_DIR + "move_selected.png"));
            editSelectedIcon    =   ImageIO.read(new File(Consts.IMG_DIR + "editblack_selected.png"));
            pointerSelectedIcon =   ImageIO.read(new File(Consts.IMG_DIR + "pointer_selected.png"));
            graphIcon           =   ImageIO.read(new File(Consts.IMG_DIR + "graph.png"));
            tableIcon           =   ImageIO.read(new File(Consts.IMG_DIR + "table.png"));
            executeIcon         =   ImageIO.read(new File(Consts.IMG_DIR + "execute.png"));
            resetIcon           =   ImageIO.read(new File(Consts.IMG_DIR + "reset.png"));
            editIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "edit.png"));
            playIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "play.png"));
            stopIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "stop.png"));
            recordIcon          =   ImageIO.read(new File(Consts.IMG_DIR + "record.png"));
            closeIcon           =   ImageIO.read(new File(Consts.IMG_DIR + "close.png"));
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to load resources: " + e.getMessage());
        }
    }
    
    public void initConfigPlugins(PluginManager pm)
    {
        controlPanel.loadConfigPlugins(pm);
    }
    
    
    
    //--------------------------------------
    //  SCREEN PANEL
    //--------------------------------------
    
    protected class ScreenPanel extends JPanel
    {
        protected final DataPanel dataPanel;
        protected final GraphPanel graphPanel;
        protected final OutputPanel outputPanel;
        protected final JTabbedPane tabPane;
        
        public ScreenPanel()
        {            
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(15, 5, 5, 5));
            tabPane     =   new JTabbedPane();
            dataPanel   =   new DataPanel();
            graphPanel  =   new GraphPanel();
            outputPanel =   new OutputPanel();
            
            
            tabPane.addTab("", graphPanel);
            tabPane.addTab("", dataPanel);
            tabPane.addTab("", outputPanel);
            
            JLabel dispLabel    =   new JLabel("Display", JLabel.CENTER);
            JLabel dataLabel    =   new JLabel("Data", JLabel.CENTER);
            JLabel outLabel     =   new JLabel("Output", JLabel.CENTER);
            
            dispLabel.setIcon(new ImageIcon(graphIcon));
            outLabel.setIcon(new ImageIcon(clipIcon));
            dataLabel.setIcon(new ImageIcon(tableIcon));
            
            tabPane.setTabComponentAt(0, dispLabel);
            tabPane.setTabComponentAt(1, dataLabel);
            tabPane.setTabComponentAt(2, outLabel);
            
            
            add(tabPane);
        }
        
        protected class DataPanel extends JPanel
        {
            protected final JTable vertexTable, edgeTable;
            protected final DefaultTableModel vertexDataModel, edgeDataModel;
            protected final JTabbedPane dataTabPane;
            protected final JScrollPane vertexScroller, edgeScroller;
            
            public DataPanel()
            {
                setLayout(new BorderLayout());
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
            
            protected void loadNodes(Graph graph)
            {
                ArrayList<Node> vertices   =   new ArrayList<>(graph.getVertices());
                Collections.sort(vertices, (Node n1, Node n2) -> Integer.compare(n1.getID(), n2.getID()));
                
                data.getNodeFactory().setLastID(0);
                data.getNodes().clear();
                SwingUtilities.invokeLater(() -> 
                {
                    vertexDataModel.setRowCount(0);
                    for(Node vertex : vertices)
                    {
                        int vID         =   vertex.getID();
                        String vName    =   vertex.getName();
                        
                        data.getNodes().put(vID, vertex);
                        vertexDataModel.addRow(new Object[] { vID, vName });
                        
                        if(vID > data.getNodeFactory().getLastID())
                            data.getNodeFactory().setLastID(vID);
                    }
                });
            }
            
            protected void loadEdges(Graph graph)
            {
                ArrayList<Edge> edges  =   new ArrayList<>(graph.getEdges());
                Collections.sort(edges, (Edge e1, Edge e2) -> Integer.compare(e1.getID(), e2.getID())); 
                
                data.getEdges().clear();
                
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
                        
                        data.getEdges().put(eID, edge);
                        edgeDataModel.addRow(new Object[] { eID, n1_id, n2_id, weight, edgeType });
                        
                        if(eID > data.getEdgeFactory().getLastID())
                            data.getEdgeFactory().setLastID(eID);
                    }
                });
            }
            
            protected void addVertex()
            {
                VertexAddPanel addPanel    =   new VertexAddPanel();
                
                int option  =   JOptionPane.showConfirmDialog(null, addPanel, "Add vertex", JOptionPane.OK_CANCEL_OPTION);
                
                if(option == JOptionPane.OK_OPTION)
                {
                    int id      =   (int) addPanel.idSpinner.getValue();
                    if(data.getNodes().containsKey(id))
                    {
                        JOptionPane.showMessageDialog(null, "Vertex already exists");
                        return;
                    }
                    
                    String name =   addPanel.nameField.getText();
                    Node node   =   new Node(id, name);
                    data.getGraph().addVertex(node);
                    graphPanel.gViewer.repaint();
                    loadNodes(data.getGraph());
                    data.getNodes().put(id, node);
                }
            }
            
            protected void editVertex()
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
                        editNode    =   data.getNodes().get(id);   
                    }
                    
                    else
                    {
                        int id      =   getDialogID("Enter vertex ID to edit", data.getNodes());

                        if(id != -1)
                            editNode    =   data.getNodes().get(id);
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
                    loadNodes(data.getGraph());
                }
            }
            
            protected void removeVertices(Set<Node> vertices)
            {
                if(vertices.isEmpty()) return;
                
                for(Node node : vertices)
                {
                    int id  =   node.getID();
                    data.getNodes().remove(id);
                    data.getGraph().removeVertex(node);
                    graphPanel.gViewer.repaint();
                    loadNodes(data.getGraph());
                }
            }
            
            protected void removeVertex()
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
                            Node current    =   data.getNodes().get(id);
                            
                            if(current != null)
                                selectedNodes.add(current);
                        }
                        
                        removeVertices(selectedNodes);
                    }
                    
                    else
                    {
                        int id  =   getDialogID("Enter vertex ID to remove", data.getNodes());
                        if(id != -1)
                        {
                            Node removedNode    =   data.getNodes().remove(id);
                            data.getGraph().removeVertex(removedNode);
                            loadNodes(data.getGraph());
                            graphPanel.gViewer.repaint();
                        }
                    }
                }
            }
            
            protected void addEdge()
            {
                EdgeAddPanel addPanel   =   new EdgeAddPanel();
                
                int option  =   JOptionPane.showConfirmDialog(null, addPanel, "Add edge", JOptionPane.OK_CANCEL_OPTION);
                
                if(option == JOptionPane.OK_OPTION)
                {
                    int id  =   (int) addPanel.idSpinner.getValue();
                    if(data.getEdges().containsKey(id))
                    {
                        JOptionPane.showMessageDialog(null, "Edge ID already exists");
                        return;
                    }
                    
                    int fromID          =   (int) addPanel.fromSpinner.getValue();
                    int toID            =   (int) addPanel.toSpinner.getValue();
                    double weight       =   (double) addPanel.weightSpinner.getValue();
                    int eType           =   addPanel.edgeTypeBox.getSelectedIndex();
                    EdgeType edgeType   =   (eType == 0)? EdgeType.UNDIRECTED : EdgeType.DIRECTED;
                    
                    if(data.getNodes().containsKey(fromID) && data.getNodes().containsKey(toID))
                    {
                        Edge edge       =   new Edge(id, weight, edgeType);
                        Node n1         =   data.getNodes().get(fromID);
                        Node n2         =   data.getNodes().get(toID);
                        edge.setSourceNode(n1);
                        edge.setDestNode(n2);
                        
                        data.getEdges().put(id, edge);
                        data.getGraph().addEdge(edge, n1, n2, edgeType);
                        loadEdges(data.getGraph());
                        graphPanel.gViewer.repaint();
                    }
                    
                    else JOptionPane.showMessageDialog(null, "Vertex ID does not exist");
                }
            }
            
            protected void editEdge()
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
                        editEdge    =   data.getEdges().get(id);
                    }
                    
                    else
                    {
                        int id  =   getDialogID("Enter edge ID to edit", data.getEdges());

                        if(id != -1)
                            editEdge    =   data.getEdges().get(id);
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

                editPanel.idSpinner.setEnabled(false);
                editPanel.autoCheck.setVisible(false);
                
                int option  =   JOptionPane.showConfirmDialog(null, editPanel, "Edit edge", JOptionPane.OK_CANCEL_OPTION);
                if(option == JOptionPane.OK_OPTION)
                {
                    editEdge.setWeight((double) editPanel.weightSpinner.getValue());
                    editEdge.setEdgeType(editPanel.edgeTypeBox.getSelectedIndex() == 0? EdgeType.UNDIRECTED : EdgeType.DIRECTED);
                    
                    Node from   =   data.getNodes().get(Integer.parseInt(editPanel.fromSpinner.getValue().toString()));
                    Node to     =   data.getNodes().get(Integer.parseInt(editPanel.toSpinner.getValue().toString()));
                    editEdge.setSourceNode(from);
                    editEdge.setDestNode(to);
                    
                    data.getGraph().removeEdge(editEdge);
                    data.getGraph().addEdge(editEdge, from, to, editEdge.getEdgeType());
                    
                    loadEdges(data.getGraph());
                    graphPanel.gViewer.repaint();
                }
            }
            
            protected void removeEdge()
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
                            Edge current    =   data.getEdges().remove(id);
                            data.getGraph().removeEdge(current);
                        }
                    }
                    
                    else
                    {
                        int id  =   getDialogID("Enter edge ID to remove", data.getEdges());

                        if(id != -1)
                        {
                            Edge removeEdge =   data.getEdges().remove(id);
                            data.getGraph().removeEdge(removeEdge);
                        }

                        else return;
                    }
                }
                
                else
                {
                    for(Edge edge : selectedEdges)
                    {
                        data.getEdges().remove(edge.getID());
                        data.getGraph().removeEdge(edge);
                    }
                }
                
                loadEdges(data.getGraph());
                graphPanel.gViewer.repaint();
            }
            
            protected int getDialogID(String message, Map collection)
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
           
            //--------------------------------------
            //  VERTEX ADD PANEL
            //--------------------------------------
            
            protected class VertexAddPanel extends JPanel implements ActionListener
            {
                protected JSpinner idSpinner;
                protected JCheckBox autoCheck;
                protected JTextField nameField;
                
                public VertexAddPanel()
                {
                    setLayout(new MigLayout());
                    idSpinner      =   new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));   
                    autoCheck      =   new JCheckBox("Auto");
                    nameField      =   new JTextField();
                    
                    autoCheck.setSelected(true);
                    idSpinner.setValue(data.getNodeFactory().getLastID() + 1);
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
            
            //--------------------------------------
            //  EDGE ADD PANEL
            //--------------------------------------
            
            protected class EdgeAddPanel extends JPanel implements ActionListener
            {
                protected JSpinner idSpinner, fromSpinner, toSpinner, weightSpinner;
                protected JCheckBox autoCheck;
                protected JComboBox edgeTypeBox;
                
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
                    
                    idSpinner.setValue(data.getEdgeFactory().getLastID() + 1);
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
        
        
        //--------------------------------------
        //  GRAPH PANEL
        //--------------------------------------
        protected class GraphPanel extends JPanel implements ItemListener, GraphMouseListener, ActionListener, ChangeListener
        {
            protected final String RECORD_CARD    =   "rec";
            protected final String PLAYBACK_CARD  =   "pb";
            protected final int INITIAL_DELAY     =   500;
            
            protected final VisualizationViewer<Node, Edge> gViewer;
            protected AggregateLayout<Node, Edge> gLayout;
            protected EditingModalGraphMouse mouse;
            protected GraphPlayback gPlayback;
            protected JPanel gpControlsWrapper;
            protected JPanel gpControlsPanel;
            protected JButton gpCtrlsClose;

            protected JPanel pbControls;
            protected JButton pbToggle;
            protected JSlider pbProgress;
            protected JSpinner pbProgressSpeed;
            protected JLabel pbName, pbDate;
            protected boolean pbPlaying;
            
            protected JPanel gpRecControls;
            protected JButton gpRecSaveBtn;
            protected JButton gpRecRemoveBtn;
            protected JTextField gpRecEntryName;
            protected DateComboBox gpRecDatePicker;
            protected JComboBox gpRecEntries;
            
            public GraphPanel()
            {
                setLayout(new BorderLayout());
                gLayout     =   new AggregateLayout(new FRLayout(data.getGraph()));
                gViewer     =   new VisualizationViewer<>(gLayout);
                gPlayback   =   new GraphPlayback();
                
                ScalingControl scaler   =   new CrossoverScalingControl();
                scaler.scale(gViewer, 0.7f, gViewer.getCenter());
                gViewer.scaleToLayout(scaler); 

                gViewer.setBackground(Color.WHITE);
                gViewer.getRenderContext().setVertexFillPaintTransformer(new ObjectFillTransformer<>(gViewer.getPickedVertexState()));
                gViewer.getRenderContext().setEdgeDrawPaintTransformer(new ObjectFillTransformer(gViewer.getPickedEdgeState()));
                gViewer.getPickedVertexState().addItemListener(this);
                gViewer.getPickedEdgeState().addItemListener(this);
                
                mouse       =   new EditingModalGraphMouse(gViewer.getRenderContext(), data.getNodeFactory(), data.getEdgeFactory());
                mouse.setMode(ModalGraphMouse.Mode.PICKING);
                gViewer.addGraphMouseListener(this);
                mouse.remove(mouse.getPopupEditingPlugin());
                gViewer.setGraphMouse(mouse);
                
                pbControls      =   new JPanel(new MigLayout("fillx"));
                pbToggle        =   new JButton("Play");
                pbProgress      =   new JSlider();
                pbProgressSpeed =   new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
                pbName          =   new JLabel("N/A");
                pbDate          =   new JLabel("N/A");
                pbPlaying       =   false;
                
                pbToggle.setIcon(new ImageIcon(playIcon));
                pbProgress.addChangeListener(this);
                pbProgressSpeed.addChangeListener(this);

                pbProgressSpeed.setValue(INITIAL_DELAY);
                pbProgress.setPaintTicks(true);
                pbProgress.setValue(0);
                pbProgress.setMinimum(0);
                pbProgress.setPaintTrack(true);
                
                pbToggle.addActionListener(this);
                
                pbName.setFont(new Font("Arial", Font.BOLD, 12));
                pbDate.setFont(new Font("Arial", Font.BOLD, 12));
                
                JPanel pbInnerWrapper   =   new JPanel();
                pbInnerWrapper.add(pbToggle);
                pbInnerWrapper.add(new JLabel("Speed"));
                pbInnerWrapper.add(pbProgressSpeed);
                
                JPanel pbInfoWrapper    =   new JPanel(new MigLayout());
                pbInfoWrapper.add(new JLabel("Name: "));
                pbInfoWrapper.add(pbName, "wrap");
                pbInfoWrapper.add(new JLabel("Timestamp: "));
                pbInfoWrapper.add(pbDate);
                
                pbControls.add(pbProgress, "al center, wrap");
                pbControls.add(pbInnerWrapper, "al center, wrap");
                pbControls.add(pbInfoWrapper, "al center");
                
                JPanel pbControlsWrapper    =   new JPanel(new BorderLayout());
                pbControlsWrapper.add(pbControls);
                
                
                gpRecControls   =   new JPanel(new MigLayout("fillx"));
                gpRecSaveBtn    =   new JButton("Save entry");
                gpRecRemoveBtn  =   new JButton("Remove entry");
                gpRecDatePicker =   new DateComboBox();
                gpRecEntryName  =   new JTextField();
                gpRecEntries    =   new JComboBox();
                gpRecEntries.setPreferredSize(new Dimension(120, 20));
                gpRecEntryName.setPreferredSize(new Dimension(120, 20));
                gpRecSaveBtn.setIcon(new ImageIcon(addIcon));
                gpRecRemoveBtn.setIcon(new ImageIcon(removeIcon));
                gpRecEntries.addItem("-- New entry --");
                
                JPanel gpRecInnerWrapper    =   new JPanel(new MigLayout());
                gpRecInnerWrapper.add(gpRecSaveBtn);
                gpRecInnerWrapper.add(gpRecRemoveBtn);
                gpRecInnerWrapper.add(new JLabel("Entries"));
                gpRecInnerWrapper.add(gpRecEntries, "wrap");
                gpRecInnerWrapper.add(new JLabel("Entry date"));
                gpRecInnerWrapper.add(gpRecDatePicker, "wrap");
                gpRecInnerWrapper.add(new JLabel("Entry name (optional)"));
                gpRecInnerWrapper.add(gpRecEntryName, "span 2");
                gpRecControls.add(gpRecInnerWrapper, "al center");
                
                JPanel gpRecWrapper =   new JPanel(new BorderLayout());
                gpRecWrapper.add(gpRecControls);
                
                gpControlsWrapper   =   new JPanel(new CardLayout());
                gpControlsWrapper.add(pbControlsWrapper, PLAYBACK_CARD);
                gpControlsWrapper.add(gpRecWrapper, RECORD_CARD);
                
                gpControlsPanel             =   new JPanel(new BorderLayout());
                gpCtrlsClose                =   new JButton("Close");
                JPanel gpControlsExitPanel  =   new JPanel();
                gpCtrlsClose.setIcon(new ImageIcon(closeIcon));
                
                gpControlsExitPanel.add(gpCtrlsClose);
                gpControlsPanel.add(gpControlsWrapper, BorderLayout.CENTER);
                gpControlsPanel.add(gpControlsExitPanel, BorderLayout.EAST);
                gpControlsPanel.setVisible(false);
                
                gpRecSaveBtn.addActionListener(this);
                gpRecRemoveBtn.addActionListener(this);
                gpRecEntries.addActionListener(this);
                gpCtrlsClose.addActionListener(this);
                
                add(gViewer, BorderLayout.CENTER);
                add(gpControlsPanel, BorderLayout.SOUTH);
            }
            
            protected void addPlaybackEntries()
            {
                gpRecEntries.removeAllItems();
                gpRecEntries.addItem("-- New entry --");
                List<PlaybackEntry> entries =   gPlayback.getEntries();
                
                for(PlaybackEntry entry : entries)
                    gpRecEntries.addItem(entry);
                
                gpRecEntries.setSelectedIndex(0);
            }
            
            protected void changePlaybackPanel(String card)
            {
                CardLayout cLayout  =   (CardLayout) gpControlsWrapper.getLayout();
                cLayout.show(gpControlsWrapper, card);
                
                if(card.equals(PLAYBACK_CARD))
                {
                    pbProgress.setMinimum(0);
                    pbProgress.setValue(0);
                    pbProgress.setMaximum(gPlayback.getSize() - 1); 
                }
                
                gpControlsPanel.setVisible(true);
            }
            
            protected final Timer PB_TIMER =   new Timer(INITIAL_DELAY, (ActionEvent e) -> 
            {
                if(gPlayback.hasNext())
                    pbProgress.setValue(pbProgress.getValue() + 1);
                else
                    togglePlayback();
            });
            
            protected void startPlayback()
            {
                pbProgress.setMinimum(0);
                pbProgress.setMaximum(gPlayback.getSize() - 1);
                
                if(pbProgress.getValue() == pbProgress.getMaximum())
                {
                    gPlayback.setIndex(0);
                    pbProgress.setValue(0);
                }
                    
                PB_TIMER.setRepeats(true);
                PB_TIMER.start();
                PB_TIMER.setDelay((int) pbProgressSpeed.getValue());
            }
            
            protected void stopPlayback()
            {
                PB_TIMER.stop();
            }
            
            protected void addRecordedGraph()
            {
                PlaybackEntry entry;
                int selectedIndex   =   gpRecEntries.getSelectedIndex();
                if(selectedIndex == 0)
                {
                    Graph<Node, Edge> graph   =   new SparseMultigraph<>();
                    GraphUtilities.copyGraph(data.getGraph(), graph);

                    Date date       =   gpRecDatePicker.getDate();
                    String name     =   gpRecEntryName.getText();

                    if(name.equals(""))
                        entry   =   new PlaybackEntry(graph, date);
                    else
                        entry   =   new PlaybackEntry(graph, date, name);

                    gPlayback.add(entry);
                    gpRecEntries.addItem(entry);
                    gpRecEntryName.setText("");
                }
                
                else
                {
                    entry   =   (PlaybackEntry) gpRecEntries.getSelectedItem();
                    entry.setName(gpRecEntryName.getText());
                    entry.setDate(gpRecDatePicker.getDate());
                    entry.setGraph(GraphUtilities.copyNewGraph(data.getGraph()));
                }
            }
            
            protected void togglePlayback()
            {
                if(pbPlaying)
                {
                    pbPlaying = false;
                    pbToggle.setIcon(new ImageIcon(playIcon));
                    pbToggle.setText("Play");
                    stopPlayback();
                }
                
                else
                {
                    pbPlaying = true;
                    pbToggle.setIcon(new ImageIcon(stopIcon));
                    pbToggle.setText("Stop");
                    startPlayback();
                }
            }
            
            protected void displayRecordedGraph()
            {
               int selectedIndex    =   gpRecEntries.getSelectedIndex();
               if(selectedIndex != 0)
               {
                   PlaybackEntry entry  =   (PlaybackEntry) gpRecEntries.getSelectedItem();
                   if(entry != null)
                   {
                        gpRecEntryName.setText(entry.getName());
                        gpRecDatePicker.setDate(entry.getDate());
                        data.setGraph(GraphUtilities.copyNewGraph(entry.getGraph()));
                        reloadGraph();
                   }
               }
               
               else
               {
                   gpRecEntryName.setText("");
                   gpRecDatePicker.setDate(new Date());
               }
            }
            
            protected void removeRecordedGraph()
            {
                int selectedIndex   =   gpRecEntries.getSelectedIndex();
                if(selectedIndex != 0)
                {
                    PlaybackEntry entry =   (PlaybackEntry) gpRecEntries.getSelectedItem();
                    gPlayback.remove(entry);
                    gpRecEntries.removeItemAt(selectedIndex);
                    gpRecEntries.setSelectedIndex(0);
                    gpRecEntryName.setText("");
                    gpRecDatePicker.setDate(new Date());
                }
            }

            @Override
            public void graphClicked(Object v, MouseEvent me) {}

            @Override
            public void graphPressed(Object v, MouseEvent me) {}

            @Override
            public void graphReleased(Object v, MouseEvent me)
            {
                if(controlPanel.editCheck.isSelected())
                {
                    dataPanel.loadNodes(data.getGraph());
                    dataPanel.loadEdges(data.getGraph());
                }
            }

            @Override
            public void actionPerformed(ActionEvent e)
            {
                Object src  =   e.getSource();
                
                if(src == gpRecSaveBtn)
                    addRecordedGraph();
                
                else if(src == gpRecRemoveBtn)
                    removeRecordedGraph();
                
                else if(src == gpRecEntries)
                    displayRecordedGraph();
                
                else if(src == pbToggle)
                    togglePlayback();
                
                else if(src == gpCtrlsClose)
                    gpControlsPanel.setVisible(false);
            }

            @Override
            public void stateChanged(ChangeEvent e) 
            {
                Object src  =   e.getSource();
                
                if(src == pbProgressSpeed)
                    PB_TIMER.setDelay((int) pbProgressSpeed.getValue());
                
                else if(src == pbProgress)
                {
                    int index   =   pbProgress.getValue();
                    gPlayback.setIndex(index);
                    PlaybackEntry entry =   gPlayback.current();
                    
                    if(entry != null)
                    {
                        pbName.setText(entry.getName());
                        pbDate.setText(entry.getDateFormatted());

                        data.setGraph(GraphUtilities.copyNewGraph(entry.getGraph()));
                        reloadGraph();
                    }
                }
            }
            
            
            //--------------------------------------
            //  CENTRALITY TRANSFORMER
            //--------------------------------------
            
            protected class CentralityTransformer implements Transformer<Node, Shape>
            {
                List<Node> centralNodes;
                int numRanks;
                
                public CentralityTransformer(List<Node> centralNodes, int numRanks)
                {
                    this.centralNodes   =   centralNodes;
                    this.numRanks       =   numRanks;
                }
                
                @Override
                public Shape transform(Node node) 
                {
                    
                    for(int i = 0; i < numRanks; i++)
                    {
                        if(node.equals(centralNodes.get(i)))
                        {
                            int size    =   20 + ((numRanks - i) * 10);
                            return new Ellipse2D.Double(-10, -10, size, size);
                        }
                    }
                    
                    return new Ellipse2D.Double(-10, -10, 20, 20);
                }
            }
            
            protected void setVertexColour(Color colour, Collection<Node> vertices)
            {
                if(vertices == null)
                    vertices   =   data.getGraph().getVertices();
                
                for(Node vertex : vertices)
                    vertex.setFill(colour);
                
                gViewer.repaint();
            }
            
            protected void setEdgeColour(Color colour, Collection<Edge> edges)
            {
                if(edges == null)
                    edges   =   data.getGraph().getEdges();
                
                for(Edge edge : edges)
                    edge.setFill(colour);
                
                gViewer.repaint();
            }
            
            protected void showVertexLabels(boolean show)
            {
                gViewer.getRenderContext().setVertexLabelTransformer(new VertexLabelTransformer(show));
                gViewer.repaint();
            }
            
            protected void showEdgeLabels(boolean show)
            {
                gViewer.getRenderContext().setEdgeLabelTransformer(new EdgeLabelTransformer(show));
                gViewer.repaint();
            }
            
            
            protected void showCluster()
            {
                int numRemoved  =   (int) controlPanel.clusterEdgeRemoveSpinner.getValue();
                boolean group   =   controlPanel.clusterTransformCheck.isSelected();
                GraphUtilities.cluster(gLayout, data.getGraph(), numRemoved, group);
                gViewer.repaint();
            }
            
            protected void showCentrality()
            {
                Map<Node, Double> centrality;
                if(data.getGraph().getVertexCount() <= 1) return;
                
                SparseDoubleMatrix2D matrix =   GraphMatrixOperations.graphToSparseMatrix(data.getGraph());
                int selectedCentrality      =   controlPanel.centralityTypeBox.getSelectedIndex();
                boolean transform           =   controlPanel.centralityMorphCheck.isSelected();
                String prefix;
                
                switch(selectedCentrality)
                {
                    case 0: 
                        centrality  =   MatrixTools.getScores(MatrixTools.powerIterationFull(matrix), data.getGraph());
                        prefix      =   "EigenVector";
                        break;
                        
                    case 1: 
                        centrality  =   MatrixTools.getScores(new ClosenessCentrality(data.getGraph(), new WeightTransformer()), data.getGraph());
                        prefix      =   "Closeness";
                        break;
                        
                    case 2: 
                        centrality  =   MatrixTools.getScores(new BetweennessCentrality(data.getGraph(), new WeightTransformer()), data.getGraph());
                        prefix      =   "Betweenness";
                        break; 
                        
                    default: return;
                }
                
                
                Collection<Node> vertices     =   data.getGraph().getVertices();
                PriorityQueue<SimpleEntry<Node, Double>> scores = null;
                
                if(transform)
                {
                    scores =   new PriorityQueue<>((SimpleEntry<Node, Double> a1, SimpleEntry<Node, Double> a2) 
                    -> Double.compare(a2.getValue(), a1.getValue()));
                }
                
                for(Node node : vertices)
                {
                    double score    =   centrality.get(node);
                    String output   =   MessageFormat.format("({0}) Vertex: {1}, Score: {2}", prefix, node.getID(), score);
                    sendToOutput(output);
                    
                    if(transform)
                        scores.add(new SimpleEntry(node, score));
                }
                
                if(transform)
                {
                    ArrayList<Node> centralNodes    =   new ArrayList<>();
                    Color[] centralColours          =   new Color[] { Color.RED, Color.ORANGE, Color.BLUE };
                    
                    for(int i = 0; i < 3; i++)
                    {
                        SimpleEntry<Node, Double> entry = scores.poll();
                        centralNodes.add(entry.getKey());
                        entry.getKey().setFill(centralColours[i]);
                    }
                        
                    graphPanel.gViewer.getRenderContext().setVertexShapeTransformer(new CentralityTransformer(centralNodes, 3));
                    graphPanel.gViewer.repaint();
                }
            } 
            
            
            protected void reloadGraph()
            {
                gViewer.getPickedVertexState().clear();
                gViewer.getPickedEdgeState().clear();
                data.setSelectedItems(null);
                
                gLayout.removeAll();
                gLayout.setGraph(data.getGraph());
                gViewer.repaint();
                dataPanel.loadNodes(data.getGraph());
                dataPanel.loadEdges(data.getGraph());
            }
            
            protected void resetGraph()
            {
                data.setGraph(new SparseMultigraph<>());
                reloadGraph();
            }

            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if(controlPanel.selectCheck.isSelected())
                {
                    data.setSelectedItems(e.getItemSelectable().getSelectedObjects());
                    controlPanel.updateSelectedComponents();
                }
            }
        }
    }
}
