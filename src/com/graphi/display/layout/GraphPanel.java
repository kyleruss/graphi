//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import com.graphi.sim.GraphPlayback;
import com.graphi.sim.PlaybackEntry;
import com.graphi.util.transformer.CentralityTransformer;
import com.graphi.util.ComponentUtils;
import com.graphi.util.Edge;
import com.graphi.util.transformer.EdgeLabelTransformer;
import com.graphi.util.GraphUtilities;
import com.graphi.util.MatrixTools;
import com.graphi.util.Node;
import com.graphi.util.TableModelBean;
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
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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
    public final String RECORD_CARD         =   "rec";
    public final String PLAYBACK_CARD       =   "pb";
    protected final int INITIAL_DELAY       =   500;
    
    protected final VisualizationViewer<Node, Edge> gViewer;
    protected AggregateLayout<Node, Edge> gLayout;
    protected EditingModalGraphMouse mouse;
    protected GraphPlayback gPlayback;
    protected JPanel gpControlsWrapper;
    protected JButton gpCtrlsClose;
    protected PlaybackControlPanel controlPanel;

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
    protected MainPanel mainPanel;
    protected JCheckBox recordComputeCheck, recordStateCheck;

    public GraphPanel(MainPanel mainPanel)
    {
        setLayout(new BorderLayout());
        this.mainPanel  =   mainPanel;
        gLayout         =   new AggregateLayout(new FRLayout(mainPanel.data.getGraph()));
        gViewer         =   new VisualizationViewer<>(gLayout);
        gPlayback       =   new GraphPlayback();

        ScalingControl scaler   =   new CrossoverScalingControl();
        scaler.scale(gViewer, 0.7f, gViewer.getCenter());
        gViewer.scaleToLayout(scaler); 

        gViewer.setBackground(Color.WHITE);
        gViewer.getRenderContext().setVertexFillPaintTransformer(new ObjectFillTransformer<>(gViewer.getPickedVertexState()));
        gViewer.getRenderContext().setEdgeDrawPaintTransformer(new ObjectFillTransformer(gViewer.getPickedEdgeState()));
        gViewer.getPickedVertexState().addItemListener(this);
        gViewer.getPickedEdgeState().addItemListener(this);

        mouse       =   new EditingModalGraphMouse(gViewer.getRenderContext(), mainPanel.data.getNodeFactory(), mainPanel.data.getEdgeFactory());
        mouse.setMode(ModalGraphMouse.Mode.PICKING);
        gViewer.addGraphMouseListener(this);
        mouse.remove(mouse.getPopupEditingPlugin());
        gViewer.setGraphMouse(mouse);
        
        controlPanel    =   new PlaybackControlPanel();
        controlPanel.setVisible(false);

        add(gViewer, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
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
                    Node node   =   mainPanel.getGraphData().getNodes().get(gObjID);
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
                    Edge edge   =   mainPanel.getGraphData().getEdges().get(gObjID);
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
        if(!controlPanel.isVisible())
            controlPanel.setPreviousState();
            
        CardLayout cLayout  =   (CardLayout) gpControlsWrapper.getLayout();
        cLayout.show(gpControlsWrapper, card);

        if(card.equals(PLAYBACK_CARD))
        {
            pbProgress.setMinimum(0);
            pbProgress.setValue(0);
            pbProgress.setMaximum(gPlayback.getSize() - 1); 
        }

        controlPanel.setVisible(true);
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

    public void addRecordedGraph()
    {
        PlaybackEntry entry;
        int selectedIndex   =   gpRecEntries.getSelectedIndex();
        if(selectedIndex == 0)
        {
            Graph<Node, Edge> graph     =   GraphUtilities.copyNewGraph(mainPanel.data.getGraph(), recordStateCheck.isSelected());

            Date date       =   gpRecDatePicker.getDate();
            String name     =   gpRecEntryName.getText();

            if(name.equals(""))
                entry   =   new PlaybackEntry(graph, date);
            else
                entry   =   new PlaybackEntry(graph, date, name);
            
            if(recordComputeCheck.isSelected())
            {
                DefaultTableModel tModel    =   mainPanel.screenPanel.dataPanel.computationModel;
                String context              =   mainPanel.screenPanel.dataPanel.getComputationContext();
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
            entry.setGraph(GraphUtilities.copyNewGraph(mainPanel.data.getGraph(), recordStateCheck.isSelected()));
        }
    }

    protected void togglePlayback()
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
        mainPanel.screenPanel.dataPanel.setComputationModel(bean == null? new DefaultTableModel() : bean.getModel());
        mainPanel.screenPanel.dataPanel.setComputationContext(bean == null? null : bean.getDescription());
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
                mainPanel.data.setGraph(GraphUtilities.copyNewGraph(entry.getGraph(), false));
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
        if(mainPanel.controlPanel.getModePanel().getEditCheck().isSelected())
        {
            mainPanel.screenPanel.dataPanel.loadNodes(mainPanel.data.getGraph());
            mainPanel.screenPanel.dataPanel.loadEdges(mainPanel.data.getGraph());
        }
    }

    public void setVertexColour(Color colour, Collection<Node> vertices)
    {
        if(vertices == null)
            vertices   =   mainPanel.data.getGraph().getVertices();

        for(Node vertex : vertices)
            vertex.setFill(colour);

        gViewer.repaint();
    }

    public void setEdgeColour(Color colour, Collection<Edge> edges)
    {
        if(edges == null)
            edges   =   mainPanel.data.getGraph().getEdges();

        for(Edge edge : edges)
            edge.setFill(colour);

        gViewer.repaint();
    }

    public void showVertexLabels(boolean show)
    {
        gViewer.getRenderContext().setVertexLabelTransformer(new VertexLabelTransformer(show));
        gViewer.repaint();
    }

    public void showEdgeLabels(boolean show)
    {
        gViewer.getRenderContext().setEdgeLabelTransformer(new EdgeLabelTransformer(show));
        gViewer.repaint();
    }


    public void showCluster()
    {
        int numRemoved  =   (int) mainPanel.controlPanel.getComputePanel().getClusterEdgeRemoveSpinner().getValue();
        boolean group   =   mainPanel.controlPanel.getComputePanel().getClusterTransformCheck().isSelected();
        GraphUtilities.cluster(gLayout, mainPanel.data.getGraph(), numRemoved, group);
        gViewer.repaint();
    }

    public void showCentrality()
    {
        Map<Node, Double> centrality;
        if(mainPanel.data.getGraph().getVertexCount() <= 1) return;

        SparseDoubleMatrix2D matrix =   GraphMatrixOperations.graphToSparseMatrix(mainPanel.data.getGraph());
        int selectedCentrality      =   mainPanel.controlPanel.getComputePanel().getCentralityTypeBox().getSelectedIndex();
        boolean transform           =   mainPanel.controlPanel.getComputePanel().getCentralityMorphCheck().isSelected();
        String prefix;

        switch(selectedCentrality)
        {
            case 0: 
                centrality  =   MatrixTools.getScores(MatrixTools.powerIterationFull(matrix), mainPanel.data.getGraph());
                prefix      =   "EigenVector";
                break;

            case 1: 
                centrality  =   MatrixTools.getScores(new ClosenessCentrality(mainPanel.data.getGraph(), new WeightTransformer()), mainPanel.data.getGraph());
                prefix      =   "Closeness";
                break;

            case 2: 
                centrality  =   MatrixTools.getScores(new BetweennessCentrality(mainPanel.data.getGraph(), new WeightTransformer()), mainPanel.data.getGraph());
                prefix      =   "Betweenness";
                break; 

            default: return;
        }


        Collection<Node> vertices     =   mainPanel.data.getGraph().getVertices();
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
            ComponentUtils.sendToOutput(output, mainPanel.screenPanel.outputPanel.outputArea);

            if(transform && scores != null)
                scores.add(new SimpleEntry(node, score));
        }
        
        mainPanel.screenPanel.dataPanel.setComputationModel(tModel);
        mainPanel.screenPanel.dataPanel.setComputationContext(prefix + " centrality");
        
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

            mainPanel.screenPanel.graphPanel.gViewer.getRenderContext().setVertexShapeTransformer(new CentralityTransformer(centralNodes, 3));
            mainPanel.screenPanel.graphPanel.gViewer.repaint();
        }
    } 


    public void reloadGraph()
    {
        gViewer.getPickedVertexState().clear();
        gViewer.getPickedEdgeState().clear();
        mainPanel.data.setSelectedItems(null);

        gLayout.removeAll();
        gLayout.setGraph(mainPanel.data.getGraph());
        gViewer.repaint();
        mainPanel.screenPanel.dataPanel.loadNodes(mainPanel.data.getGraph());
        mainPanel.screenPanel.dataPanel.loadEdges(mainPanel.data.getGraph());
        mainPanel.screenPanel.dataPanel.clearComputeTable();
    }

    public void resetGraph()
    {
        mainPanel.data.setGraph(new SparseMultigraph<>());
        reloadGraph();
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        if(mainPanel.controlPanel.getModePanel().getSelectCheck().isSelected())
        {
            mainPanel.data.setSelectedItems(e.getItemSelectable().getSelectedObjects());
            updateSelectedComponents();
        }
    }
    
    protected void updateSelectedComponents()
    {
        JLabel selectedLabel    =   mainPanel.controlPanel.getgObjPanel().getSelectedLabel();
        if(mainPanel.getGraphData().getSelectedItems() == null || mainPanel.getGraphData().getSelectedItems().length == 0)
            selectedLabel.setText("None");
        else
        {
            if(mainPanel.getGraphData().getSelectedItems().length > 1)
                selectedLabel.setText(mainPanel.getGraphData().getSelectedItems().length + " objects");
            else
            {
                Object selectedObj  =   mainPanel.getGraphData().getSelectedItems()[0];
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
            prevGraph   =   mainPanel.getGraphData().getGraph();
            prevModel   =   mainPanel.getScreenPanel().getDataPanel().getCompModelBean();
        }
        
        public void reloadPreviousState()
        {
            if(prevGraph != null && prevModel != null)
            {
                mainPanel.getScreenPanel().getDataPanel().loadCompModelBean(prevModel);
                mainPanel.getGraphData().setGraph(prevGraph);
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

                    mainPanel.data.setGraph(GraphUtilities.copyNewGraph(entry.getGraph(), recordStateCheck.isSelected()));
                    reloadGraph();
                    showEntryComputationModel(entry);
                }
            }
        }
    }
}