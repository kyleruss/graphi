//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.AppResources;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import com.graphi.app.AppManager;
import com.graphi.config.AppConfig;
import com.graphi.config.ConfigManager;
import com.graphi.display.menu.MainMenu;
import com.graphi.display.layout.controls.ComputeControlPanel;
import com.graphi.sim.GraphPlayback;
import com.graphi.sim.PlaybackEntry;
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
import com.graphi.graph.TableModelBean;
import com.graphi.util.transformer.ObjectFillTransformer;
import com.graphi.util.transformer.VertexLabelTransformer;
import com.graphi.util.transformer.WeightTransformer;
import de.javasoft.swing.DateComboBox;
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
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class GraphPanel extends JPanel implements ItemListener, GraphMouseListener
{
    public static final String RECORD_CARD         =   "rec";
    public static final String PLAYBACK_CARD       =   "pb";
    protected final int INITIAL_DELAY       =   500;
    public static final int SELECT_MODE     =   0;
    public static final int MOVE_MODE       =   1;   
    public static final int EDIT_MODE       =   2;
    
    protected final VisualizationViewer<Node, Edge> gViewer;
    protected AggregateLayout<Node, Edge> gLayout;
    protected EditingModalGraphMouse mouse;
    protected GraphPlayback gPlayback;
    protected JPanel gpControlsWrapper;
    protected JButton gpCtrlsClose;
    protected PlaybackControlPanel pbControlPanel;

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
    protected JCheckBox recordComputeCheck, recordStateCheck;
    private final DisplayNavigationPanel displayNavPanel;
    private static GraphPanel instance;

    public GraphPanel()
    {
        setLayout(new BorderLayout());
        GraphData graphData =   GraphDataManager.getGraphDataInstance();
        gLayout             =   new AggregateLayout(new FRLayout(graphData.getGraph()));
        gViewer             =   new VisualizationViewer<>(gLayout);
        gPlayback           =   new GraphPlayback();
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
        
        pbControlPanel    =   new PlaybackControlPanel();
        pbControlPanel.setVisible(false);
        
        recordComputeCheck.setSelected(true);
        recordStateCheck.setSelected(true);
        
        gViewer.setLayout(new FlowLayout(FlowLayout.RIGHT));
        gViewer.add(displayNavPanel);
        
        add(gViewer, BorderLayout.CENTER);
        add(pbControlPanel, BorderLayout.SOUTH);
    }
    
    public int getMouseMode()
    {
        return displayNavPanel.mode;
    }
    
    public PickedInfo<Node> getSelectedNodes()
    {
        return gViewer.getPickedVertexState();
    }

    public PickedInfo<Edge> getSelectedEdges()
    {
        return gViewer.getPickedEdgeState();
    }
    
    private class DisplayNavigationPanel extends JPanel implements ActionListener
    {
        private JButton moveBtn;
        private JButton selectBtn;
        private JButton editBtn;
        private BufferedImage currentBG;
        private int mode;
        
        private DisplayNavigationPanel()
        {
            setPreferredSize(new Dimension(120, 150));
            setLayout(new BorderLayout());
            
            moveBtn                 =   new JButton();
            selectBtn               =   new JButton();
            editBtn                 =   new JButton();
            currentBG               =   AppResources.getInstance().getResource("displayNavSelect");
            mode                    =   SELECT_MODE;
            
            ComponentUtils.setTransparentControl(moveBtn);
            ComponentUtils.setTransparentControl(selectBtn);
            ComponentUtils.setTransparentControl(editBtn);
            
            editBtn.setPreferredSize(new Dimension(40, 60));
            moveBtn.setPreferredSize(new Dimension(60, 40));
            selectBtn.setPreferredSize(new Dimension(60, 40));
            Component botPadding    =   Box.createRigidArea(new Dimension(1, 20));
            
            add(editBtn, BorderLayout.NORTH);
            add(moveBtn, BorderLayout.WEST);
            add(selectBtn, BorderLayout.EAST);
            add(botPadding, BorderLayout.SOUTH);
            
            editBtn.addActionListener(this);
            moveBtn.addActionListener(this);
            selectBtn.addActionListener(this);
        }
        
        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.drawImage(currentBG, 0, 0, null);
        }
        
        private void toggleSelect()
        {
            mouse.setMode(ModalGraphMouse.Mode.PICKING);
            currentBG   =   AppResources.getInstance().getResource("displayNavSelect");
            repaint();
            
        }
        
        private void toggleMove()
        {
            mouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
            currentBG   =   AppResources.getInstance().getResource("displayNavMove");   
            repaint();
            
        }

        private void toggleEdit()
        {
            mouse.setMode(ModalGraphMouse.Mode.EDITING);
            mouse.remove(mouse.getPopupEditingPlugin());
            currentBG   =   AppResources.getInstance().getResource("displayNavEdit");
            repaint();
        }
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            Object src  =   e.getSource();
            
            if(src == editBtn)
                toggleEdit();
            
            else if(src == moveBtn)
                toggleMove();
            
            else if(src == selectBtn)
                toggleSelect();
            
        }
    }
    
    public void resetEntries()
    {
        gpRecEntries.removeAllItems();
        GraphDataManager.getGraphDataInstance().setGraph(new SparseMultigraph());
        reloadGraph();
    }

    public void addPlaybackEntries()
    {
        gpRecEntries.removeAllItems();
        gpRecEntries.addItem("-- New entry --");
        List<PlaybackEntry> entries =   gPlayback.getEntries();

        for(PlaybackEntry entry : entries)
            gpRecEntries.addItem(entry);

        gpRecEntries.setSelectedIndex(0);
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

    public void changePlaybackPanel(String card)
    {
        if(!pbControlPanel.isVisible())
            pbControlPanel.setPreviousState();
            
        CardLayout cLayout  =   (CardLayout) gpControlsWrapper.getLayout();
        cLayout.show(gpControlsWrapper, card);

        if(card.equals(PLAYBACK_CARD))
        {
            pbProgress.setMinimum(0);
            pbProgress.setValue(0);
            pbProgress.setMaximum(gPlayback.getSize() - 1); 
        }

        pbControlPanel.setVisible(true);
    }

    protected final Timer PB_TIMER =   new Timer(INITIAL_DELAY, (ActionEvent e) -> 
    {
        if(gPlayback.hasNext())
            pbProgress.setValue(pbProgress.getValue() + 1);
        else
            togglePlayback();
    });

    public void startPlayback()
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

    public void stopPlayback()
    {
        PB_TIMER.stop();
    }
    
    public void addRecordedGraph(String entryName, Date date, boolean recordState, boolean recordTable, boolean newEntry)
    {
        PlaybackEntry entry;
        
        if(newEntry)
        {
            Graph<Node, Edge> graph     =   GraphUtilities.copyNewGraph(GraphDataManager.getGraphDataInstance().getGraph(), recordState);


            if(entryName.equals(""))
                entry   =   new PlaybackEntry(graph, date);
            else
                entry   =   new PlaybackEntry(graph, date, entryName);
            
            if(recordTable)
            {
                DefaultTableModel tModel    =   DataPanel.getInstance().computationModel;
                String context              =   DataPanel.getInstance().getComputationContext();
                entry.setComputationModel(new TableModelBean(tModel, context));
            }

            gPlayback.add(entry);
            gpRecEntries.addItem(entry);
            gpRecEntryName.setText("");
        }

        else
        {
            entry   =   (PlaybackEntry) gpRecEntries.getSelectedItem();
            entry.setName(gpRecEntryName.getText());
            entry.setDate(gpRecDatePicker.getDate());
            entry.setGraph(GraphUtilities.copyNewGraph(GraphDataManager.getGraphDataInstance().getGraph(), recordStateCheck.isSelected()));
        }
    }

    public void addRecordedGraph()
    {
        int selectedIndex   =   gpRecEntries.getSelectedIndex();
        Date date           =   gpRecDatePicker.getDate();
        String name         =   gpRecEntryName.getText();
        boolean recordState =   recordStateCheck.isSelected();
        boolean recordTable =   recordComputeCheck.isSelected();
        boolean newEntry    =   selectedIndex == 0;
        
        addRecordedGraph(name, date, recordState, recordTable, newEntry);
    }

    public void togglePlayback()
    {
        if(pbPlaying)
        {
            pbPlaying = false;
            pbToggle.setIcon(new ImageIcon(AppResources.getInstance().getResource("playIcon")));
            pbToggle.setText("Play");
            stopPlayback();
        }

        else
        {
            pbPlaying = true;
            pbToggle.setIcon(new ImageIcon(AppResources.getInstance().getResource("stopIcon")));
            pbToggle.setText("Stop");
            startPlayback();
        }
    }
    
    protected void showEntryComputationModel(PlaybackEntry entry)
    {
        if(entry != null)
        {
            TableModelBean modelContext   =   entry.getComputationModel();
            showComputationModel(modelContext);
        }
    }
    
    protected void showComputationModel(TableModelBean bean)
    {
        DataPanel dataPanel =   DataPanel.getInstance();
        dataPanel.setComputationModel(bean == null? new DefaultTableModel() : bean.getModel());
        dataPanel.setComputationContext(bean == null? null : bean.getDescription());
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
                GraphDataManager.getGraphDataInstance().setGraph(GraphUtilities.copyNewGraph(entry.getGraph(), false));
                reloadGraph();
                showEntryComputationModel(entry);
           }
       }

       else
       {
           gpRecEntryName.setText("");
           gpRecDatePicker.setDate(new Date());
       }
    }

    public void removeRecordedGraph()
    {
        int selectedIndex   =   gpRecEntries.getSelectedIndex();
        removeRecordedGraph(selectedIndex);
    }
    
    public void removeAllRecordedEntries()
    {
        while(gpRecEntries.getItemCount() > 1)
            removeRecordedGraph(1);
    }
    
    public void removeRecordedGraph(int index)
    {
        if(index != 0)
        {
            PlaybackEntry entry =   (PlaybackEntry) gpRecEntries.getItemAt(index);
            gPlayback.remove(entry);
            gpRecEntries.removeItemAt(index);
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
        if(getMouseMode() == EDIT_MODE)
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
        if(getMouseMode() == SELECT_MODE)
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

    public GraphPlayback getGraphPlayback()
    {
        return gPlayback;
    }
    
    public void setGraphPlayback(GraphPlayback gPlayback)
    {
        this.gPlayback  =   gPlayback;
    }
    
    private class PlaybackControlPanel extends JPanel implements ActionListener, ChangeListener
    {
        private Graph<Node, Edge> prevGraph;
        private TableModelBean prevModel;
        
        public PlaybackControlPanel()
        {
            setLayout(new BorderLayout());
            pbControls      =   new JPanel(new MigLayout("fillx"));
            pbToggle        =   new JButton("Play");
            pbProgress      =   new JSlider();
            pbProgressSpeed =   new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
            pbName          =   new JLabel("N/A");
            pbDate          =   new JLabel("N/A");
            pbPlaying       =   false;

            pbToggle.setIcon(new ImageIcon(AppResources.getInstance().getResource("playIcon")));
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


            gpRecControls       =   new JPanel(new MigLayout("fillx"));
            gpRecSaveBtn        =   new JButton("Save entry");
            gpRecRemoveBtn      =   new JButton("Remove entry");
            recordComputeCheck  =   new JCheckBox("Record computation table");
            recordStateCheck    =   new JCheckBox("Record object state");
            gpRecDatePicker     =   new DateComboBox();
            gpRecEntryName      =   new JTextField();
            gpRecEntries        =   new JComboBox();
            gpRecEntries.setPreferredSize(new Dimension(120, 20));
            gpRecEntryName.setPreferredSize(new Dimension(120, 20));
            gpRecSaveBtn.setIcon(new ImageIcon(AppResources.getInstance().getResource("addIcon")));
            gpRecRemoveBtn.setIcon(new ImageIcon(AppResources.getInstance().getResource("removeIcon")));
            gpRecEntries.addItem("-- New entry --");

            JPanel gpRecInnerWrapper    =   new JPanel(new MigLayout());
            gpRecInnerWrapper.add(gpRecSaveBtn);
            gpRecInnerWrapper.add(gpRecRemoveBtn);
            gpRecInnerWrapper.add(new JLabel("Entries"));
            gpRecInnerWrapper.add(gpRecEntries, "wrap");
            gpRecInnerWrapper.add(new JLabel("Entry date"));
            gpRecInnerWrapper.add(gpRecDatePicker, "wrap");
            gpRecInnerWrapper.add(new JLabel("Entry name (optional)"));
            gpRecInnerWrapper.add(gpRecEntryName, "span 2, wrap");
            gpRecInnerWrapper.add(recordComputeCheck, "span 2, wrap");
            gpRecInnerWrapper.add(recordStateCheck, "span 2");
            gpRecControls.add(gpRecInnerWrapper, "al center");

            JPanel gpRecWrapper =   new JPanel(new BorderLayout());
            gpRecWrapper.add(gpRecControls);

            gpControlsWrapper   =   new JPanel(new CardLayout());
            gpControlsWrapper.add(pbControlsWrapper, PLAYBACK_CARD);
            gpControlsWrapper.add(gpRecWrapper, RECORD_CARD);

            gpCtrlsClose                =   new JButton("Close");
            JPanel gpControlsExitPanel  =   new JPanel();
            gpCtrlsClose.setIcon(new ImageIcon(AppResources.getInstance().getResource("closeIcon")));

            gpControlsExitPanel.add(gpCtrlsClose);
            add(gpControlsWrapper, BorderLayout.CENTER);
            add(gpControlsExitPanel, BorderLayout.EAST);

            gpRecSaveBtn.addActionListener(this);
            gpRecRemoveBtn.addActionListener(this);
            gpRecEntries.addActionListener(this);
            gpCtrlsClose.addActionListener(this);
        }
        
        public void setPreviousState()
        {
            prevGraph   =   GraphDataManager.getGraphDataInstance().getGraph();
            prevModel   =   DataPanel.getInstance().getCompModelBean();
        }
        
        public void reloadPreviousState()
        {
            if(prevGraph != null && prevModel != null)
            {
                DataPanel.getInstance().loadCompModelBean(prevModel);
                GraphDataManager.getGraphDataInstance().setGraph(prevGraph);
                reloadGraph();
            }
        }
        
        public void closeControls()
        {
            setVisible(false);
            reloadPreviousState();
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
                closeControls();
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

                    GraphDataManager.getGraphDataInstance()
                    .setGraph(GraphUtilities.copyNewGraph(entry.getGraph(), recordStateCheck.isSelected()));
                    
                    reloadGraph();
                    showEntryComputationModel(entry);
                }
            }
        }
    }
    
    public static GraphPanel getInstance()
    {
        if(instance == null) instance   =   new GraphPanel();
        return instance;
    }
}