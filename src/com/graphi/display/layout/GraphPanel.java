//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import com.graphi.sim.GraphPlayback;
import com.graphi.sim.PlaybackEntry;
import com.graphi.util.ComponentUtils;
import com.graphi.util.Edge;
import com.graphi.util.EdgeLabelTransformer;
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
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.text.MessageFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.collections15.Transformer;

public class GraphPanel extends JPanel implements ItemListener, GraphMouseListener, ActionListener, ChangeListener
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
    protected MainPanel mainPanel;

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

        pbControls      =   new JPanel(new MigLayout("fillx"));
        pbToggle        =   new JButton("Play");
        pbProgress      =   new JSlider();
        pbProgressSpeed =   new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        pbName          =   new JLabel("N/A");
        pbDate          =   new JLabel("N/A");
        pbPlaying       =   false;

        pbToggle.setIcon(new ImageIcon(mainPanel.playIcon));
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
        gpRecSaveBtn.setIcon(new ImageIcon(mainPanel.addIcon));
        gpRecRemoveBtn.setIcon(new ImageIcon(mainPanel.removeIcon));
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
        gpCtrlsClose.setIcon(new ImageIcon(mainPanel.closeIcon));

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
            GraphUtilities.copyGraph(mainPanel.data.getGraph(), graph);

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
            entry.setGraph(GraphUtilities.copyNewGraph(mainPanel.data.getGraph()));
        }
    }

    protected void togglePlayback()
    {
        if(pbPlaying)
        {
            pbPlaying = false;
            pbToggle.setIcon(new ImageIcon(mainPanel.playIcon));
            pbToggle.setText("Play");
            stopPlayback();
        }

        else
        {
            pbPlaying = true;
            pbToggle.setIcon(new ImageIcon(mainPanel.stopIcon));
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
                mainPanel.data.setGraph(GraphUtilities.copyNewGraph(entry.getGraph()));
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
        if(mainPanel.controlPanel.editCheck.isSelected())
        {
            mainPanel.screenPanel.dataPanel.loadNodes(mainPanel.data.getGraph());
            mainPanel.screenPanel.dataPanel.loadEdges(mainPanel.data.getGraph());
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

                mainPanel.data.setGraph(GraphUtilities.copyNewGraph(entry.getGraph()));
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
            vertices   =   mainPanel.data.getGraph().getVertices();

        for(Node vertex : vertices)
            vertex.setFill(colour);

        gViewer.repaint();
    }

    protected void setEdgeColour(Color colour, Collection<Edge> edges)
    {
        if(edges == null)
            edges   =   mainPanel.data.getGraph().getEdges();

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
        int numRemoved  =   (int) mainPanel.controlPanel.clusterEdgeRemoveSpinner.getValue();
        boolean group   =   mainPanel.controlPanel.clusterTransformCheck.isSelected();
        GraphUtilities.cluster(gLayout, mainPanel.data.getGraph(), numRemoved, group);
        gViewer.repaint();
    }

    protected void showCentrality()
    {
        Map<Node, Double> centrality;
        if(mainPanel.data.getGraph().getVertexCount() <= 1) return;

        SparseDoubleMatrix2D matrix =   GraphMatrixOperations.graphToSparseMatrix(mainPanel.data.getGraph());
        int selectedCentrality      =   mainPanel.controlPanel.centralityTypeBox.getSelectedIndex();
        boolean transform           =   mainPanel.controlPanel.centralityMorphCheck.isSelected();
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
        PriorityQueue<AbstractMap.SimpleEntry<Node, Double>> scores = null;

        if(transform)
        {
            scores =   new PriorityQueue<>((AbstractMap.SimpleEntry<Node, Double> a1, AbstractMap.SimpleEntry<Node, Double> a2) 
            -> Double.compare(a2.getValue(), a1.getValue()));
        }

        for(Node node : vertices)
        {
            double score    =   centrality.get(node);
            String output   =   MessageFormat.format("({0}) Vertex: {1}, Score: {2}", prefix, node.getID(), score);
            ComponentUtils.sendToOutput(output, mainPanel.screenPanel.outputPanel.outputArea);

            if(transform)
                scores.add(new AbstractMap.SimpleEntry(node, score));
        }

        if(transform)
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


    protected void reloadGraph()
    {
        gViewer.getPickedVertexState().clear();
        gViewer.getPickedEdgeState().clear();
        mainPanel.data.setSelectedItems(null);

        gLayout.removeAll();
        gLayout.setGraph(mainPanel.data.getGraph());
        gViewer.repaint();
        mainPanel.screenPanel.dataPanel.loadNodes(mainPanel.data.getGraph());
        mainPanel.screenPanel.dataPanel.loadEdges(mainPanel.data.getGraph());
    }

    protected void resetGraph()
    {
        mainPanel.data.setGraph(new SparseMultigraph<>());
        reloadGraph();
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        if(mainPanel.controlPanel.selectCheck.isSelected())
        {
            mainPanel.data.setSelectedItems(e.getItemSelectable().getSelectedObjects());
            mainPanel.controlPanel.updateSelectedComponents();
        }
    }
}