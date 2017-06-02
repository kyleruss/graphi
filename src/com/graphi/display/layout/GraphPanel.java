//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.AppResources;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import com.graphi.config.AppConfig;
import com.graphi.config.ConfigManager;
import com.graphi.display.menu.MainMenu;
import com.graphi.display.layout.controls.ComputeControlPanel;
import com.graphi.display.layout.controls.DisplayNavigationPanel;
import com.graphi.display.layout.controls.PlaybackControlPanel;
import com.graphi.util.transformer.CentralityTransformer;
import com.graphi.display.layout.util.ComponentUtils;
import com.graphi.graph.Edge;
import com.graphi.graph.GraphData;
import com.graphi.graph.GraphDataManager;
import com.graphi.graph.GraphObject;
import com.graphi.util.transformer.EdgeLabelTransformer;
import com.graphi.graph.GraphUtilities;
import com.graphi.graph.MatrixTools;
import com.graphi.graph.Node;
import com.graphi.util.transformer.ObjectFillTransformer;
import com.graphi.util.transformer.VertexLabelTransformer;
import com.graphi.util.transformer.WeightTransformer;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.matrix.GraphMatrixOperations;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class GraphPanel extends JPanel implements ItemListener, GraphMouseListener
{
    public static final int SELECT_MODE     =   0;
    public static final int MOVE_MODE       =   1;   
    public static final int EDIT_MODE       =   2;
    
    private final VisualizationViewer<Node, Edge> gViewer;
    private AggregateLayout<Node, Edge> gLayout;
    private EditingModalGraphMouse mouse;
    private PlaybackControlPanel playbackControlPanel;
    private final DisplayNavigationPanel displayNavPanel;
    private static GraphPanel instance;

    public GraphPanel()
    {
        setLayout(new BorderLayout());
        GraphData graphData =   GraphDataManager.getGraphDataInstance();
        gLayout             =   new AggregateLayout(new FRLayout(graphData.getGraph()));
        gViewer             =   new VisualizationViewer<>(gLayout);
        displayNavPanel     =   new DisplayNavigationPanel();

        ScalingControl scaler   =   new CrossoverScalingControl();
        scaler.scale(gViewer, 0.7f, gViewer.getCenter());
        gViewer.scaleToLayout(scaler); 

        gViewer.setBackground(ConfigManager.getInstance().getAppConfig().getDisplayBackground());
        gViewer.getRenderContext().setVertexFillPaintTransformer(new ObjectFillTransformer<>(gViewer.getPickedVertexState()));
        gViewer.getRenderContext().setEdgeDrawPaintTransformer(new ObjectFillTransformer(gViewer.getPickedEdgeState()));
        gViewer.getPickedVertexState().addItemListener(this);
        gViewer.getPickedEdgeState().addItemListener(this);
        gViewer.getRenderContext().setVertexLabelTransformer(new VertexLabelTransformer(false));
        gViewer.getRenderContext().setEdgeLabelTransformer(new EdgeLabelTransformer(false));
        

        mouse       =   new EditingModalGraphMouse(gViewer.getRenderContext(), graphData.getNodeFactory(), graphData.getEdgeFactory());
        mouse.setMode(ModalGraphMouse.Mode.PICKING);
        gViewer.addGraphMouseListener(this);
        mouse.remove(mouse.getPopupEditingPlugin());
        gViewer.setGraphMouse(mouse);
        
        playbackControlPanel    =   new PlaybackControlPanel();
        playbackControlPanel.setVisible(false);
        
        gViewer.setLayout(new FlowLayout(FlowLayout.RIGHT));
        gViewer.add(displayNavPanel);
        
        add(gViewer, BorderLayout.CENTER);
        add(playbackControlPanel, BorderLayout.SOUTH);
    }
    
    public PickedInfo<Node> getSelectedNodes()
    {
        return gViewer.getPickedVertexState();
    }

    public PickedInfo<Edge> getSelectedEdges()
    {
        return gViewer.getPickedEdgeState();
    }
    
    public void searchGraphObject()
    {
        JPanel searchDialog         =   new JPanel(new MigLayout("fillx"));
        JRadioButton vertexRadio    =   new JRadioButton("Vertex");
        JRadioButton edgeRadio      =   new JRadioButton("Edge");
        ButtonGroup gObjGroup       =   new ButtonGroup();
        JLabel searchLabel          =   new JLabel("Enter object ID");
        
        gObjGroup.add(vertexRadio);
        gObjGroup.add(edgeRadio);
        searchDialog.add(vertexRadio);
        searchDialog.add(edgeRadio, "wrap");
        searchDialog.add(searchLabel);
        vertexRadio.setSelected(true);
        
        String id   =   JOptionPane.showInputDialog(null, searchDialog, "Search graph object", JOptionPane.OK_CANCEL_OPTION);
        if(id != null && !id.equals(""))
        {
            try
            {
                boolean selectVertex    =   vertexRadio.isSelected();
                int gObjID              =   Integer.parseInt(id);

                if(selectVertex)
                {
                    Node node   =   GraphDataManager.getGraphDataInstance().getNodes().get(gObjID);
                    if(node != null)
                    {
                        gViewer.getPickedVertexState().clear();
                        gViewer.getPickedEdgeState().clear();
                        gViewer.getPickedVertexState().pick(node, true);
                    }
                    
                    else JOptionPane.showMessageDialog(null, "Node was not found");
                }

                else
                {
                    Edge edge   =   GraphDataManager.getGraphDataInstance().getEdges().get(gObjID);
                    if(edge != null)
                    {
                        gViewer.getPickedVertexState().clear();
                        gViewer.getPickedEdgeState().clear();
                        gViewer.getPickedEdgeState().pick(edge, true);
                    }
                    
                    else JOptionPane.showMessageDialog(null, "Edge was not found");
                }
            }
            
            catch(NumberFormatException e)
            {
                JOptionPane.showMessageDialog(null, "Invalid object ID");
            }
        }
    }
    
    public void refreshViewer()
    {
        gViewer.repaint();
    }

    @Override
    public void graphClicked(Object v, MouseEvent me) {}

    @Override
    public void graphPressed(Object v, MouseEvent me) {}

    @Override
    public void graphReleased(Object v, MouseEvent me)
    {
        if(displayNavPanel.getMouseMode() == EDIT_MODE)
        {
            Graph graph         =   GraphDataManager.getGraphDataInstance().getGraph();
            DataPanel dataPanel =   DataPanel.getInstance();
            dataPanel.loadNodes(graph);
            DataPanel.getInstance().loadEdges(graph);
        }
    }

    public void setVertexColour(Color colour, Collection<Node> vertices)
    {
        if(vertices == null)
            vertices   =   GraphDataManager.getGraphDataInstance().getGraph().getVertices();

        for(Node vertex : vertices)
            vertex.setFill(colour);

        gViewer.repaint();
    }
    
    public void setSelectionColour(Color colour)
    {
        if(colour == null)
        {
            colour  =   JColorChooser.showDialog(null, "Choose fill colour", Color.BLACK);
            if(colour == null) return;
        }
        
        AppConfig appConfig =   ConfigManager.getInstance().getAppConfig();
        appConfig.setSelectedBackground(colour);
        gViewer.repaint();
    }
    
    public void setSelectedObjectColour(Color colour, Class obj)
    {
        if(obj != null)
        {
            if(colour == null)
            {
                colour  =   JColorChooser.showDialog(null, "Choose fill colour", Color.BLACK);
                if(colour == null) return;
            }
            
            Object[] selectedObjects  =   GraphDataManager.getGraphDataInstance().getSelectedItems();
            
            if(selectedObjects == null || selectedObjects.length == 0)
            {
                AppConfig appConfig =   ConfigManager.getInstance().getAppConfig();
                
                if(obj == Node.class) appConfig.setNodeBackground(colour);
                else if(obj == Edge.class) appConfig.setEdgeBackground(colour);
            }
            
            else
            {
                for(Object selectedObj : selectedObjects)
                {
                    if(selectedObj.getClass() == obj)
                        ((GraphObject) selectedObj).setFill(colour);
                }
            }
            
            gViewer.repaint();
        }
    }

    public void setEdgeColour(Color colour, Collection<Edge> edges)
    {
        if(edges == null)
            edges   =   GraphDataManager.getGraphDataInstance().getGraph().getEdges();

        for(Edge edge : edges)
            edge.setFill(colour);

        gViewer.repaint();
    }
    
    public void repaintDisplay()
    {
        gViewer.repaint();
    }

    public void showObjectLabels(boolean show, boolean changeVertexLabels)
    {
        AppConfig appConfig =   ConfigManager.getInstance().getAppConfig();

        if(changeVertexLabels) appConfig.setViewNodeLabels(show);
        else appConfig.setViewEdgeLabels(show);
        
        gViewer.repaint();

        JMenuItem menu    =   MainMenu.getInstance().getMenuItem(changeVertexLabels? "vLabelsItem" : "eLabelsItem");
        if(show)
            menu.setIcon(new ImageIcon(AppResources.getInstance().getResource("tickIcon")));
        else
            menu.setIcon(null);
    }


    public void showCluster()
    {
        ControlPanel controlPanel   =   ControlPanel.getInstance();
        int numRemoved              =   (int) controlPanel.getComputePanel().getClusterEdgeRemoveSpinner().getValue();
        boolean group               =   controlPanel.getComputePanel().getClusterTransformCheck().isSelected();
        GraphUtilities.cluster(gLayout, GraphDataManager.getGraphDataInstance().getGraph(), numRemoved, group);
        gViewer.repaint();
    }

    public void showCentrality()
    {
        Graph graph                         =   GraphDataManager.getGraphDataInstance().getGraph();
        ComputeControlPanel computePanel    =   ControlPanel.getInstance().getComputePanel();
        
        Map<Node, Double> centrality;
        if(graph.getVertexCount() <= 1) return;

        SparseDoubleMatrix2D matrix =   GraphMatrixOperations.graphToSparseMatrix(graph);
        int selectedCentrality      =   computePanel.getCentralityTypeBox().getSelectedIndex();
        boolean transform           =   computePanel.getCentralityMorphCheck().isSelected();
        String prefix;

        switch(selectedCentrality)
        {
            case 0: 
                centrality  =   MatrixTools.getScores(MatrixTools.powerIterationFull(matrix), graph);
                prefix      =   "EigenVector";
                break;

            case 1: 
                centrality  =   MatrixTools.getScores(new ClosenessCentrality(graph, new WeightTransformer()), graph);
                prefix      =   "Closeness";
                break;

            case 2: 
                centrality  =   MatrixTools.getScores(new BetweennessCentrality(graph, new WeightTransformer()), graph);
                prefix      =   "Betweenness";
                break; 

            default: return;
        }


        Collection<Node> vertices     =   graph.getVertices();
        PriorityQueue<SimpleEntry<Node, Double>> scores = null;

        if(transform)
        {
            scores = new PriorityQueue<>(new Comparator<SimpleEntry<Node, Double>>()
            {
                @Override
                public int compare(SimpleEntry<Node, Double> a, SimpleEntry<Node, Double> b)
                {
                    return Double.compare(b.getValue(), a.getValue());
                }
            });
        }
        
        DecimalFormat formatter     =   new DecimalFormat("#.###");
        DefaultTableModel tModel    =   new DefaultTableModel();
        tModel.addColumn("NodeID");
        tModel.addColumn("Centrality");
        

        for(Node node : vertices)
        {
            double score    =   Double.parseDouble(formatter.format(centrality.get(node)));
            String output   =   MessageFormat.format("({0}) Vertex: {1}, Score: {2}", prefix, node.getID(), score);
            
            tModel.addRow(new Object[] { node.getID(), score });
            ComponentUtils.sendToOutput(output, OutputPanel.getInstance().getOutputArea());

            if(transform && scores != null)
                scores.add(new SimpleEntry(node, score));
        }
        
        DataPanel.getInstance().setComputationModel(tModel);
        DataPanel.getInstance().setComputationContext(prefix + " centrality");
        
        if(transform && scores != null)
        {
            ArrayList<Node> centralNodes    =   new ArrayList<>();
            Color[] centralColours          =   new Color[] { Color.RED, Color.ORANGE, Color.BLUE };

            for(int i = 0; i < 3; i++)
            {
                AbstractMap.SimpleEntry<Node, Double> entry = scores.poll();
                centralNodes.add(entry.getKey());
                entry.getKey().setFill(centralColours[i]);
            }

            GraphPanel.getInstance().gViewer.getRenderContext().setVertexShapeTransformer(new CentralityTransformer(centralNodes, 3));
            GraphPanel.getInstance().gViewer.repaint();
        }
    } 


    public void reloadGraph()
    {
        DataPanel dataPanel     =   DataPanel.getInstance();
        GraphData data          =   GraphDataManager.getGraphDataInstance();
        
        gViewer.getPickedVertexState().clear();
        gViewer.getPickedEdgeState().clear();
        data.setSelectedItems(null);

        if(ConfigManager.getInstance().getAppConfig().isDisplayVisuals())
        {
            gLayout.removeAll();
            gLayout.setGraph(data.getGraph());
            gViewer.repaint();
        }
        
        dataPanel.loadNodes(data.getGraph());
        dataPanel.loadEdges(data.getGraph());
        dataPanel.clearComputeTable();
        
    }

    public void resetGraph()
    {
        GraphDataManager.getGraphDataInstance().setGraph(new SparseMultigraph<>());
        reloadGraph();
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        if(displayNavPanel.getMouseMode() == SELECT_MODE)
        {
            GraphDataManager.getGraphDataInstance().setSelectedItems(e.getItemSelectable().getSelectedObjects());
            updateSelectedComponents();
        }
    }
    
    protected void updateSelectedComponents()
    {
        GraphData data          =   GraphDataManager.getGraphDataInstance();
        JLabel selectedLabel    =   ControlPanel.getInstance().getgObjPanel().getSelectedLabel();
        if(data.getSelectedItems() == null || data.getSelectedItems().length == 0)
            selectedLabel.setText("None");
        else
        {
            if(data.getSelectedItems().length > 1)
                selectedLabel.setText(data.getSelectedItems().length + " objects");
            else
            {
                Object selectedObj  =   data.getSelectedItems()[0];
                if(selectedObj instanceof Node)
                    selectedLabel.setText("Node (ID=" + ((Node) selectedObj).getID() + ")");


                else if(selectedObj instanceof Edge)
                    selectedLabel.setText("Edge (ID=" + ((Edge) selectedObj).getID() + ")");
            }
        }
    }
    
    public DisplayNavigationPanel getDisplayNavPanel()
    {
        return displayNavPanel;
    }
    
    public PlaybackControlPanel getPlaybackPanel()
    {
        return playbackControlPanel;
    }
    
    public VisualizationViewer<Node, Edge> getGraphViewer() 
    {
        return gViewer;
    }

    public AggregateLayout<Node, Edge> getGraphLayout() 
    {
        return gLayout;
    }

    public EditingModalGraphMouse getMouse()
    {
        return mouse;
    }

    public static GraphPanel getInstance()
    {
        if(instance == null) instance   =   new GraphPanel();
        return instance;
    }
}