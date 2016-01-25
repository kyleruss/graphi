//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display;

import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import com.graphi.io.AdjMatrixParser;
import com.graphi.io.GMLParser;
import com.graphi.io.GraphMLParser;
import com.graphi.io.Storage;
import com.graphi.sim.GraphPlayback;
import com.graphi.util.Edge;
import com.graphi.sim.Network;
import com.graphi.sim.PlaybackEntry;
import com.graphi.util.EdgeFactory;
import com.graphi.util.EdgeLabelTransformer;
import com.graphi.util.GraphUtilities;
import com.graphi.util.MatrixTools;
import com.graphi.util.Node;
import com.graphi.util.NodeFactory;
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
import edu.uci.ics.jung.graph.Hypergraph;
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
import java.util.HashMap;
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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
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

public class LayoutPanel extends JPanel
{
    private final ControlPanel controlPanel;
    private final ScreenPanel screenPanel;
    private final JSplitPane splitPane;
    private final JScrollPane controlScroll;
    
    private BufferedImage addIcon, removeIcon, colourIcon;
    private BufferedImage clipIcon, openIcon, saveIcon;
    private BufferedImage editBlackIcon, pointerIcon, moveIcon;
    private BufferedImage moveSelectedIcon, editSelectedIcon, pointerSelectedIcon;
    private BufferedImage graphIcon, tableIcon, resetIcon, executeIcon;
    private BufferedImage editIcon, playIcon, stopIcon, recordIcon, closeIcon;
    
    public static final Color TRANSPARENT   =   new Color(255, 255, 255, 0);
    public static final Color PRESET_BG     =   new Color(200, 200, 200);
    private Graph<Node, Edge> currentGraph;
    private final Map<Integer, Node> currentNodes;
    private final Map<Integer, Edge> currentEdges;
    private NodeFactory nodeFactory;
    private EdgeFactory edgeFactory;
    private Object[] selectedItems;
    private MainMenu menu;
    private JFrame frame;
    
    public LayoutPanel(MainMenu menu, JFrame frame)
    {
        setPreferredSize(new Dimension((int)(Window.WIDTH * 0.7), (int) (Window.HEIGHT * 0.85)));
        setLayout(new BorderLayout());        
        initResources();
        
        this.menu           =   menu;
        this.frame          =   frame;
        nodeFactory         =   new NodeFactory();
        edgeFactory         =   new EdgeFactory();
        currentGraph        =   new SparseMultigraph<>();
        currentNodes        =   new HashMap<>();
        currentEdges        =   new HashMap<>();
        controlPanel        =   new ControlPanel();
        screenPanel         =   new ScreenPanel();
        splitPane           =   new JSplitPane();
        controlScroll       =   new JScrollPane(controlPanel);

        controlScroll.setBorder(null);
        splitPane.setLeftComponent(screenPanel);
        splitPane.setRightComponent(controlScroll); 
        splitPane.setResizeWeight(0.7);
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
    
    private File getFile(boolean open, String desc, String...extensions)
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
    
    private String getFileExtension(File file)
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
    
    private void initResources()
    {
        try
        {
            addIcon             =   ImageIO.read(new File("resources/images/addSmallIcon.png"));
            removeIcon          =   ImageIO.read(new File("resources/images/removeSmallIcon.png"));   
            colourIcon          =   ImageIO.read(new File("resources/images/color_icon.png"));   
            clipIcon            =   ImageIO.read(new File("resources/images/clipboard.png"));  
            saveIcon            =   ImageIO.read(new File("resources/images/new_file.png"));
            openIcon            =   ImageIO.read(new File("resources/images/open_icon.png"));
            editBlackIcon       =   ImageIO.read(new File("resources/images/editblack.png"));
            pointerIcon         =   ImageIO.read(new File("resources/images/pointer.png"));
            moveIcon            =   ImageIO.read(new File("resources/images/move.png"));
            moveSelectedIcon    =   ImageIO.read(new File("resources/images/move_selected.png"));
            editSelectedIcon    =   ImageIO.read(new File("resources/images/editblack_selected.png"));
            pointerSelectedIcon =   ImageIO.read(new File("resources/images/pointer_selected.png"));
            graphIcon           =   ImageIO.read(new File("resources/images/graph.png"));
            tableIcon           =   ImageIO.read(new File("resources/images/table.png"));
            executeIcon         =   ImageIO.read(new File("resources/images/execute.png"));
            resetIcon           =   ImageIO.read(new File("resources/images/reset.png"));
            editIcon            =   ImageIO.read(new File("resources/images/edit.png"));
            playIcon            =   ImageIO.read(new File("resources/images/play.png"));
            stopIcon            =   ImageIO.read(new File("resources/images/stop.png"));
            recordIcon          =   ImageIO.read(new File("resources/images/record.png"));
            closeIcon           =   ImageIO.read(new File("resources/images/close.png"));
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to load resources: " + e.getMessage());
        }
    }
    
    //--------------------------------------
    //  CONTROL PANEL
    //--------------------------------------
    
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
        private JSpinner initialNSpinner, addNSpinner;
        private JCheckBox simTiesCheck;
        private JSpinner simTiesPSpinner;
        private JLabel simTiesPLabel;
        
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
        private JCheckBox centralityMorphCheck;
        private ButtonGroup editObjGroup;
        private JRadioButton editVertexRadio, editEdgeRadio;
        
        private JPanel viewerPanel;
        private JCheckBox viewerVLabelsCheck;
        private JCheckBox viewerELabelsCheck;
        private JButton viewerBGBtn, vertexBGBtn, edgeBGBtn;
        
        private JPanel playbackPanel;
        private JButton recordCtrlsBtn;
        private boolean recording;
        private JButton displayCtrlsBtn;
        private JLabel activeScriptLabel;
        
        public ControlPanel() 
        {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEmptyBorder(15, 0, 3, 8));
            setPreferredSize(new Dimension(230, 1650));
            
            ioPanel         =   new IOPanel();            
            modePanel       =   new JPanel();
            simPanel        =   new JPanel(new MigLayout("fillx"));
            modePanel.setPreferredSize(new Dimension(230, 100));
            modePanel.setBorder(BorderFactory.createTitledBorder("Mode controls"));
            simPanel.setBorder(BorderFactory.createTitledBorder("Simulation controls"));
  
            modeGroup       =   new ButtonGroup();
            editCheck       =   new JRadioButton("Edit");
            selectCheck     =   new JRadioButton("Select");
            moveCheck       =   new JRadioButton("Move");
            
            editCheck.setIcon(new ImageIcon(editBlackIcon));
            selectCheck.setIcon(new ImageIcon(pointerIcon));
            moveCheck.setIcon(new ImageIcon(moveIcon));
            
            moveCheck.setSelectedIcon(new ImageIcon(moveSelectedIcon));
            editCheck.setSelectedIcon(new ImageIcon(editSelectedIcon));
            selectCheck.setSelectedIcon(new ImageIcon(pointerSelectedIcon));
            
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
            simTiesCheck            =   new JCheckBox("Interpersonal ties");
            simTiesPLabel           =   new JLabel("P");
            simTiesPSpinner         =   new JSpinner(new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1));
            simTiesPSpinner.setPreferredSize(new Dimension(50, 25));
            simTiesPLabel.setVisible(false);
            simTiesPSpinner.setVisible(false);
            
            genAlgorithmsBox.addItem("Kleinberg");
            genAlgorithmsBox.addItem("Barabasi-Albert");
            genAlgorithmsBox.addActionListener(this);
            
            resetGeneratorBtn           =   new JButton("Reset");
            executeGeneratorBtn         =   new JButton("Generate");
            
            executeGeneratorBtn.addActionListener(this);
            resetGeneratorBtn.addActionListener(this);
            simTiesCheck.addActionListener(this);
            
            resetGeneratorBtn.setBackground(Color.WHITE);
            executeGeneratorBtn.setBackground(Color.WHITE);
            
            resetGeneratorBtn.setIcon(new ImageIcon(resetIcon));
            executeGeneratorBtn.setIcon(new ImageIcon(executeIcon));
            
            genPanel    =   new JPanel(new CardLayout());
            baGenPanel  =   new JPanel(new MigLayout());
            klGenPanel  =   new JPanel(new MigLayout());
            genPanel.add(klGenPanel, KL_PANEL_CARD);
            genPanel.add(baGenPanel, BA_PANEL_CARD);
            genPanel.setBackground(TRANSPARENT);
            baGenPanel.setBackground(TRANSPARENT);
            klGenPanel.setBackground(TRANSPARENT);
            
            latticeSpinner      =   new JSpinner(new SpinnerNumberModel(15, 0, 100, 1));
            clusteringSpinner   =   new JSpinner(new SpinnerNumberModel(2, 0, 10, 1));   
            latticeSpinner.setPreferredSize(new Dimension(50, 20));
            clusteringSpinner.setPreferredSize(new Dimension(50, 20));
            latticeSpinner.setOpaque(true);
            clusteringSpinner.setOpaque(true);
            
            initialNSpinner     =   new JSpinner(new SpinnerNumberModel(2, 0, 1000, 1));
            addNSpinner         =   new JSpinner(new SpinnerNumberModel(100, 0, 1000, 1));
            initialNSpinner.setOpaque(true);
            addNSpinner.setOpaque(true);
            
            baGenPanel.add(new JLabel("Initial nodes"));
            baGenPanel.add(initialNSpinner, "wrap");
            baGenPanel.add(new JLabel("Generated nodes"));
            baGenPanel.add(addNSpinner);
            
            klGenPanel.add(new JLabel("Lattice size"));
            klGenPanel.add(latticeSpinner, "wrap");
            klGenPanel.add(new JLabel("Clustering exp."));
            klGenPanel.add(clusteringSpinner);
            
            simPanel.add(new JLabel("Generator"), "al right");
            simPanel.add(genAlgorithmsBox, "wrap");
            simPanel.add(genPanel, "wrap, span 2, al center");
            simPanel.add(simTiesCheck, "al center, span 2, wrap");
            simPanel.add(simTiesPLabel, "al right");
            simPanel.add(simTiesPSpinner, "wrap");
            simPanel.add(resetGeneratorBtn, "al right");
            simPanel.add(executeGeneratorBtn, "");
            
            JPanel simWrapperPanel   =   new JPanel(new BorderLayout());
            simWrapperPanel.add(simPanel);
            
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
            gObjAddBtn.setIcon(new ImageIcon(addIcon));
            gObjRemoveBtn.setIcon(new ImageIcon(removeIcon));
            gObjEditBtn.setIcon(new ImageIcon(editIcon));
            
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
            selectedPanel.setBackground(PRESET_BG);
            gObjOptsPanel.setBackground(PRESET_BG);
            editObjPanel.setBackground(PRESET_BG);

            editPanel.add(selectedPanel);
            editPanel.add(editObjPanel);
            editPanel.add(gObjOptsPanel);
            
            computePanel        =   new JPanel(new MigLayout("fillx"));
            computeInnerPanel   =   new JPanel(new CardLayout());
            clusterPanel        =   new JPanel();
            centralityPanel     =   new JPanel(new MigLayout());
            spathPanel          =   new JPanel();
            computeBox          =   new JComboBox();
            computeBtn          =   new JButton("Execute");
            computePanel.setPreferredSize(new Dimension(230, 180));
            computePanel.setBorder(BorderFactory.createTitledBorder("Computation controls"));
            spathPanel.setLayout(new BoxLayout(spathPanel, BoxLayout.Y_AXIS));
            spathPanel.setBackground(TRANSPARENT);
            computeBtn.setBackground(Color.WHITE);
            computeBtn.addActionListener(this);
            computeBtn.setIcon(new ImageIcon(executeIcon));
            
            computeBox.addItem("Clusters");
            computeBox.addItem("Centrality");
            computeBox.addActionListener(this);
            
            clusterEdgeRemoveSpinner    =   new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
            clusterTransformCheck       =   new JCheckBox("Transform graph");
            clusterEdgeRemoveSpinner.setPreferredSize(new Dimension(50, 25));
            
            JPanel clusterEdgesPanel        =   wrapComponents(null, new JLabel("Delete edges"), clusterEdgeRemoveSpinner);
            clusterPanel.setLayout(new MigLayout());
            clusterPanel.add(clusterEdgesPanel, "wrap");
            clusterPanel.add(clusterTransformCheck);
            clusterEdgesPanel.setBackground(PRESET_BG);
            clusterPanel.setBackground(PRESET_BG);
            
            spathFromField  =   new JTextField();
            spathToField    =   new JTextField();
            spathFromField.setPreferredSize(new Dimension(50, 20));
            spathToField.setPreferredSize(new Dimension(50, 20));
            
            JLabel tLabel           =   new JLabel("To ID");
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
            centralityMorphCheck    =   new JCheckBox("Transform graph");
            centralityTypeBox.addItem("Eigenvector");
            centralityTypeBox.addItem("Closeness");
            centralityTypeBox.addItem("Betweenness");
            centralityTypeBox.addActionListener(this);
            
            JPanel cenTypePanel     =   wrapComponents(null, new JLabel("Type"), centralityTypeBox);
            centralityPanel.add(cenTypePanel, "wrap");
            centralityPanel.add(centralityMorphCheck);
            cenTypePanel.setBackground(PRESET_BG);
            centralityPanel.setBackground(PRESET_BG);
            
            computeInnerPanel.add(clusterPanel, CLUSTER_PANEL_CARD);
            computeInnerPanel.add(spathPanel, SPATH_PANEL_CARD);
            computeInnerPanel.add(centralityPanel, CENTRALITY_PANEL_CARD);
            
            computePanel.add(new JLabel("Compute "), "al right");
            computePanel.add(computeBox, "wrap");
            computePanel.add(computeInnerPanel, "wrap, span 2, al center");
            computePanel.add(computeBtn, "span 2, al center");
            
            JPanel computeWrapperPanel   =   new JPanel(new BorderLayout());
            computeWrapperPanel.add(computePanel);
            
            CardLayout clusterInnerLayout   =   (CardLayout) computeInnerPanel.getLayout();
            clusterInnerLayout.show(computeInnerPanel, CLUSTER_PANEL_CARD);
            
            viewerPanel             =   new JPanel(new MigLayout("fillx"));
            viewerVLabelsCheck      =   new JCheckBox("Vertex labels");
            viewerELabelsCheck      =   new JCheckBox("Edge labels");
            viewerBGBtn             =   new JButton("Choose");
            vertexBGBtn             =   new JButton("Choose");
            edgeBGBtn               =   new JButton("Choose");

            viewerBGBtn.setIcon(new ImageIcon(colourIcon));
            vertexBGBtn.setIcon(new ImageIcon(colourIcon));
            edgeBGBtn.setIcon(new ImageIcon(colourIcon));
            
            viewerBGBtn.addActionListener(this);
            vertexBGBtn.addActionListener(this);
            edgeBGBtn.addActionListener(this);
            viewerVLabelsCheck.addActionListener(this);
            viewerELabelsCheck.addActionListener(this);
            
            
            viewerPanel.setBorder(BorderFactory.createTitledBorder("Viewer controls"));
            viewerPanel.setPreferredSize(new Dimension(500, 200));
            
            viewerPanel.add(viewerVLabelsCheck, "wrap, span 2, al center");
            viewerPanel.add(viewerELabelsCheck, "wrap, span 2, al center");
            viewerPanel.add(new JLabel("Vertex background"), "al right");
            viewerPanel.add(vertexBGBtn, "wrap");
            viewerPanel.add(new JLabel("Edge background"), "al right");
            viewerPanel.add(edgeBGBtn, "wrap");
            viewerPanel.add(new JLabel("Viewer background"), "al right");
            viewerPanel.add(viewerBGBtn, "wrap");
            
            JPanel viewerWrapperPanel   =   new JPanel(new BorderLayout());
            viewerWrapperPanel.add(viewerPanel);
            
            playbackPanel       =   new JPanel(new MigLayout("fillx"));
            activeScriptLabel   =   new JLabel("None");
            recordCtrlsBtn      =   new JButton("Record controls");
            displayCtrlsBtn     =   new JButton("Playback controls");
            recording           =   false;

            recordCtrlsBtn.setIcon(new ImageIcon(recordIcon));
            displayCtrlsBtn.setIcon(new ImageIcon(playIcon));
            
            activeScriptLabel.setFont(new Font("Arial", Font.BOLD, 12));
            playbackPanel.setBorder(BorderFactory.createTitledBorder("Script controls"));
            playbackPanel.add(new JLabel("Active script: "), "al right");
            playbackPanel.add(activeScriptLabel, "wrap");
            playbackPanel.add(recordCtrlsBtn, "span 2, al center, wrap");
            playbackPanel.add(displayCtrlsBtn, "span 2, al center");
            
            recordCtrlsBtn.addActionListener(this);
            displayCtrlsBtn.addActionListener(this);
            
            JPanel pbWrapperPanel  =   new JPanel(new BorderLayout());
            pbWrapperPanel.add(playbackPanel);
            
            menu.exitItem.addActionListener(this);
            menu.miniItem.addActionListener(this);
            menu.maxItem.addActionListener(this);
            menu.importGraphItem.addActionListener(this);
            menu.exportGraphItem.addActionListener(this);
            menu.importLogItem.addActionListener(this);
            menu.exportLogItem.addActionListener(this);
            menu.vLabelsItem.addActionListener(this);
            menu.eLabelsItem.addActionListener(this);
            menu.viewerBGItem.addActionListener(this);
            menu.edgeBGItem.addActionListener(this);
            menu.vertexBGItem.addActionListener(this);
            menu.clearLogItem.addActionListener(this);
            menu.resetGraphItem.addActionListener(this);
            menu.addVertexItem.addActionListener(this);
            menu.editVertexItem.addActionListener(this);
            menu.removeVertexItem.addActionListener(this);
            menu.addEdgeItem.addActionListener(this);
            menu.editEdgeItem.addActionListener(this);
            menu.removeEdgeItem.addActionListener(this);
            menu.aboutItem.addActionListener(this);
            
            
            add(modePanel);
            add(Box.createRigidArea(new Dimension(230, 30)));
            add(simWrapperPanel);
            add(Box.createRigidArea(new Dimension(230, 30)));
            add(ioPanel);
            add(Box.createRigidArea(new Dimension(230, 30)));
            add(editPanel);
            add(Box.createRigidArea(new Dimension(230, 30)));
            add(computeWrapperPanel);
            add(Box.createRigidArea(new Dimension(230, 30)));
            add(viewerWrapperPanel);
            add(Box.createRigidArea(new Dimension(230, 30)));
            add(pbWrapperPanel);
            
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
            nodeFactory.setLastID(0);
            edgeFactory.setLastID(0);
            
            switch(genIndex)
            {
                case 0: showKleinbergSim(); break;
                case 1: showBASim(); break;
            }
            
            if(simTiesCheck.isSelected())
                Network.simulateInterpersonalTies(currentGraph, edgeFactory, (double) simTiesPSpinner.getValue());
                
            screenPanel.graphPanel.reloadGraph();
        }
        
        private void showAbout()
        {
            JLabel nameLabel    =   new JLabel("Kyle Russell 2015", SwingConstants.CENTER);
            JLabel locLabel     =   new JLabel("AUT University");
            JLabel repoLabel    =   new JLabel("https://github.com/denkers/graphi");

            JPanel aboutPanel   =   new JPanel();
            aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
            aboutPanel.add(nameLabel);
            aboutPanel.add(locLabel);
            aboutPanel.add(repoLabel);

            JOptionPane.showMessageDialog(null, aboutPanel, "Graphi - Author", JOptionPane.INFORMATION_MESSAGE);
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
            currentGraph    =   Network.generateKleinberg(latticeSize, clusterExp, nodeFactory, edgeFactory);
        }
        
        private void showBASim()
        {
            int m           =   (int) initialNSpinner.getValue();
            int n           =   (int) addNSpinner.getValue();
            currentGraph    =   Network.generateBerbasiAlbert(nodeFactory, edgeFactory, n, m);
        }
        
        private void showVertexBGChange()
        {
            Color selectedColour    =   JColorChooser.showDialog(null, "Choose vertex colour", Color.BLACK);
            
            if(selectedColour != null)
                screenPanel.graphPanel.setVertexColour(selectedColour, null);
        }
        
        private void showEdgeBGChange()
        {
            Color selectedColour    =   JColorChooser.showDialog(null, "Choose edge colour", Color.BLACK);
            
            if(selectedColour != null)
                screenPanel.graphPanel.setEdgeColour(selectedColour, null);
        }
        
        private void showViewerBGChange()
        {
            Color selectedColour    =   JColorChooser.showDialog(null, "Choose viewer background colour", Color.WHITE);
            
            if(selectedColour != null)
                screenPanel.graphPanel.gViewer.setBackground(selectedColour);
        }
        
        //--------------------------------------
        //  IO PANEL
        //--------------------------------------
        
        private class IOPanel extends JPanel implements ActionListener
        {
            private JButton exportBtn, importBtn;
            private JLabel currentStorageLabel;
            private ButtonGroup storageGroup;
            private JRadioButton storageGraphRadio, storageLogRadio, storageScriptRadio;
            
            public IOPanel()
            {
                setLayout(new GridLayout(3, 1));
                setBorder(BorderFactory.createTitledBorder("I/O Controls"));
                currentStorageLabel     =   new JLabel("None");
                importBtn               =   new JButton("Import");
                exportBtn               =   new JButton("Export");
                storageGroup            =   new ButtonGroup();
                storageGraphRadio       =   new JRadioButton("Graph");
                storageLogRadio         =   new JRadioButton("Log");
                storageScriptRadio      =   new JRadioButton("Script");
                
                importBtn.setIcon(new ImageIcon(openIcon));
                exportBtn.setIcon(new ImageIcon(saveIcon));
                
                storageGroup.add(storageGraphRadio);
                storageGroup.add(storageLogRadio);
                storageGroup.add(storageScriptRadio);
                importBtn.addActionListener(this);
                exportBtn.addActionListener(this);
                storageGraphRadio.addActionListener(this);
                storageLogRadio.addActionListener(this);
                storageScriptRadio.addActionListener(this);
                importBtn.setBackground(Color.WHITE);
                exportBtn.setBackground(Color.WHITE);
                storageGraphRadio.setSelected(true);

                JPanel storageBtnWrapper    =   wrapComponents(null, importBtn, exportBtn);
                JPanel currentGraphWrapper  =   wrapComponents(null, new JLabel("Active: "), currentStorageLabel);
                JPanel storageOptsWrapper   =   wrapComponents(null, storageGraphRadio, storageLogRadio, storageScriptRadio);
                storageBtnWrapper.setBackground(PRESET_BG);
                currentGraphWrapper.setBackground(PRESET_BG);
                storageOptsWrapper.setBackground(PRESET_BG);
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
                    
                    else if(storageLogRadio.isSelected())
                        importLog();
                    
                    else if(storageScriptRadio.isSelected())
                        importScript();
                }

                else if(src == exportBtn)
                {
                    if(storageGraphRadio.isSelected())
                        exportGraph();
                    
                    else if(storageLogRadio.isSelected())
                        exportLog();
                    
                    else if(storageScriptRadio.isSelected())
                        exportScript();
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
        
        private void showSimPanel()
        {
            int selectedIndex   =   genAlgorithmsBox.getSelectedIndex();
            String card;
            
            switch(selectedIndex)
            {
                case 0: card    =   KL_PANEL_CARD; break;
                case 1: card    =   BA_PANEL_CARD; break;
                default: return;
            }
            
            CardLayout gLayout  =   (CardLayout) genPanel.getLayout();
            gLayout.show(genPanel, card);
        }
        
        private void exportGraph()
        {
            File file           =   getFile(false, "Graphi .graph, adjacency matrix .txt, .gml, graphML .xml", "graph", "txt", "gml", "xml");
            String extension    =   getFileExtension(file);
            
            if(file != null && currentGraph != null)
            {
                if(extension.equalsIgnoreCase("graph"))
                    Storage.saveObj(currentGraph, file);
                
                else if(extension.equalsIgnoreCase("txt"))
                    AdjMatrixParser.exportGraph(currentGraph, file, false);
                
                else if(extension.equalsIgnoreCase("gml"))
                    GMLParser.exportGraph(currentGraph, file, false);
            }
        }
        
        private void importGraph()
        {
            File file           =   getFile(true, "Graphi .graph, adjacency matrix .txt, .gml, graphML .xml", "graph", "txt", "gml", "xml");
            String extension    =   getFileExtension(file);   
            
            if(file != null)
            {
                nodeFactory.setLastID(0);
                edgeFactory.setLastID(0);
                
                if(extension.equalsIgnoreCase("graph"))
                    currentGraph    =   (Graph) Storage.openObj(file);
                
                else if(extension.equalsIgnoreCase("txt"))
                {
                    int option      =   JOptionPane.showConfirmDialog(null, "Directed Graph? [Y/N]", "Graph type", JOptionPane.YES_NO_OPTION);
                    currentGraph    =   AdjMatrixParser.importGraph(file, option == JOptionPane.YES_OPTION, nodeFactory, edgeFactory);
                }
                
                else if(extension.equalsIgnoreCase("gml"))
                    currentGraph    =   GMLParser.importGraph(file, nodeFactory, edgeFactory);
                
                else if(extension.equalsIgnoreCase("xml"))
                {
                    currentGraph    =   GraphMLParser.importGraph(file);
                }
                
                
                ioPanel.currentStorageLabel.setText(file.getName());
                
                initCurrentNodes();
                initCurrentEdges();
                
                screenPanel.graphPanel.gLayout.setGraph(currentGraph);
                screenPanel.graphPanel.gViewer.repaint();
                screenPanel.dataPanel.loadNodes(currentGraph);
                screenPanel.dataPanel.loadEdges(currentGraph);
            }
        }
        
        private void importScript()
        {
            File file   =   getFile(true, "Graphi .gscript file", "gscript");
            if(file != null)
            {
                screenPanel.graphPanel.gPlayback    =   (GraphPlayback) Storage.openObj(file);
                ioPanel.currentStorageLabel.setText(file.getName());
                activeScriptLabel.setText(file.getName());
                screenPanel.graphPanel.addPlaybackEntries();
                
            }
        }
        
        private void exportScript()
        {
            File file   =   getFile(false, "Graphi .gscript file", "gscript");
            if(file != null)
                Storage.saveObj(screenPanel.graphPanel.gPlayback, file);
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
            File file   =   getFile(false, "Graphi .log file", "log");
            if(file != null)
                Storage.saveOutputLog(screenPanel.outputPanel.outputArea.getText(), file);
        }
        
        private void importLog()
        {
            File file   =   getFile(true, "Graphi .log file", "log");
            if(file != null)
            {
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
            {
                screenPanel.graphPanel.mouse.setMode(ModalGraphMouse.Mode.EDITING);
                screenPanel.graphPanel.mouse.remove(screenPanel.graphPanel.mouse.getPopupEditingPlugin());
            }
            
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
            
            else if(src == genAlgorithmsBox)
                showSimPanel();
            
            else if(src == vertexBGBtn)
                showVertexBGChange();
            
            else if(src == edgeBGBtn)
                showEdgeBGChange();
            
            else if(src == viewerBGBtn)
                showViewerBGChange();
            
            else if(src == viewerVLabelsCheck)
                screenPanel.graphPanel.showVertexLabels(viewerVLabelsCheck.isSelected());
            
            else if(src == viewerELabelsCheck)
                screenPanel.graphPanel.showEdgeLabels(viewerELabelsCheck.isSelected());
            
            else if(src == menu.aboutItem)
                showAbout();
            
            else if(src == menu.exitItem)
                System.exit(0);
            
            else if(src == menu.miniItem)
                frame.setState(JFrame.ICONIFIED);
            
            else if(src == menu.maxItem)
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            
            else if(src == menu.importGraphItem)
                importGraph();
            
            else if(src == menu.exportGraphItem)
                exportGraph();
            
            else if(src == menu.importLogItem)
                importLog();
            
            else if(src == menu.exportLogItem)
                exportLog();
            
            else if(src == menu.vLabelsItem)
                screenPanel.graphPanel.showVertexLabels(true);
            
            else if(src == menu.eLabelsItem)
                screenPanel.graphPanel.showEdgeLabels(true);
            
            else if(src == menu.viewerBGItem)
                showViewerBGChange();
            
            else if(src == menu.edgeBGItem)
                showEdgeBGChange();
            
            else if(src == menu.vertexBGItem)
                showVertexBGChange();
            
            else if(src == menu.clearLogItem)
                screenPanel.outputPanel.clearLog();
            
            else if(src == menu.resetGraphItem)
                screenPanel.graphPanel.resetGraph();
            
            else if(src == menu.addVertexItem)
                screenPanel.dataPanel.addVertex();
            
            else if(src == menu.editVertexItem)
                screenPanel.dataPanel.editVertex();
            
            else if(src == menu.removeVertexItem)
                screenPanel.dataPanel.removeVertex();
            
            else if(src == menu.addEdgeItem)
                screenPanel.dataPanel.addEdge();
            
            else if(src == menu.editEdgeItem)
                screenPanel.dataPanel.editEdge();
            
            else if(src == menu.removeEdgeItem)
                screenPanel.dataPanel.removeEdge();
            
            else if(src == displayCtrlsBtn)
                screenPanel.graphPanel.changePlaybackPanel(screenPanel.graphPanel.PLAYBACK_CARD);
            
            else if(src == recordCtrlsBtn)
                screenPanel.graphPanel.changePlaybackPanel(screenPanel.graphPanel.RECORD_CARD);
            
            else if(src == simTiesCheck)
            {
                simTiesPSpinner.setVisible(!simTiesPSpinner.isVisible());
                simTiesPLabel.setVisible(!simTiesPLabel.isVisible());
            }
        }
    }
    
    //--------------------------------------
    //  SCREEN PANEL
    //--------------------------------------
    
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
        
        private class DataPanel extends JPanel
        {
            private final JTable vertexTable, edgeTable;
            private final DefaultTableModel vertexDataModel, edgeDataModel;
            private final JTabbedPane dataTabPane;
            private final JScrollPane vertexScroller, edgeScroller;
            
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
            
            private void loadNodes(Graph graph)
            {
                ArrayList<Node> vertices   =   new ArrayList<>(graph.getVertices());
                Collections.sort(vertices, (Node n1, Node n2) -> Integer.compare(n1.getID(), n2.getID()));
                
                nodeFactory.setLastID(0);
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
                        
                        if(vID > nodeFactory.getLastID())
                            nodeFactory.setLastID(vID);
                    }
                });
            }
            
            private void loadEdges(Graph graph)
            {
                ArrayList<Edge> edges  =   new ArrayList<>(graph.getEdges());
                Collections.sort(edges, (Edge e1, Edge e2) -> Integer.compare(e1.getID(), e2.getID())); 
                
                //edgeFactory.setLastID(0);
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
                        
                        if(eID > edgeFactory.getLastID())
                            edgeFactory.setLastID(eID);
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
           
            //--------------------------------------
            //  VERTEX ADD PANEL
            //--------------------------------------
            
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
                    idSpinner.setValue(nodeFactory.getLastID() + 1);
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
                    
                    idSpinner.setValue(edgeFactory.getLastID() + 1);
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
        private class GraphPanel extends JPanel implements ItemListener, GraphMouseListener, ActionListener, ChangeListener
        {
            private final String RECORD_CARD    =   "rec";
            private final String PLAYBACK_CARD  =   "pb";
            private final int INITIAL_DELAY     =   500;
            
            private final VisualizationViewer<Node, Edge> gViewer;
            private AggregateLayout<Node, Edge> gLayout;
            private EditingModalGraphMouse mouse;
            private GraphPlayback gPlayback;
            private JPanel gpControlsWrapper;
            private JPanel gpControlsPanel;
            private JButton gpCtrlsClose;

            private JPanel pbControls;
            private JButton pbToggle;
            private JSlider pbProgress;
            private JSpinner pbProgressSpeed;
            private JLabel pbName, pbDate;
            private boolean pbPlaying;
            
            private JPanel gpRecControls;
            private JButton gpRecSaveBtn;
            private JButton gpRecRemoveBtn;
            private JTextField gpRecEntryName;
            private DateComboBox gpRecDatePicker;
            private JComboBox gpRecEntries;
            
            public GraphPanel()
            {
                setLayout(new BorderLayout());
                gLayout     =   new AggregateLayout(new FRLayout(currentGraph));
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
                
                mouse       =   new EditingModalGraphMouse(gViewer.getRenderContext(), nodeFactory, edgeFactory);
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
            
            private void addPlaybackEntries()
            {
                gpRecEntries.removeAllItems();
                gpRecEntries.addItem("-- New entry --");
                List<PlaybackEntry> entries =   gPlayback.getEntries();
                
                for(PlaybackEntry entry : entries)
                    gpRecEntries.addItem(entry);
                
                gpRecEntries.setSelectedIndex(0);
            }
            
            private void changePlaybackPanel(String card)
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
            
            private final Timer PB_TIMER =   new Timer(INITIAL_DELAY, (ActionEvent e) -> 
            {
                if(gPlayback.hasNext())
                    pbProgress.setValue(pbProgress.getValue() + 1);
                else
                    togglePlayback();
            });
            
            private void startPlayback()
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
            
            private void stopPlayback()
            {
                PB_TIMER.stop();
            }
            
            private void addRecordedGraph()
            {
                PlaybackEntry entry;
                int selectedIndex   =   gpRecEntries.getSelectedIndex();
                if(selectedIndex == 0)
                {
                    Graph<Node, Edge> graph   =   new SparseMultigraph<>();
                    GraphUtilities.copyGraph(currentGraph, graph);

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
                    entry.setGraph(GraphUtilities.copyNewGraph(currentGraph));
                }
            }
            
            private void togglePlayback()
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
            
            private void displayRecordedGraph()
            {
               int selectedIndex    =   gpRecEntries.getSelectedIndex();
               if(selectedIndex != 0)
               {
                   PlaybackEntry entry  =   (PlaybackEntry) gpRecEntries.getSelectedItem();
                   if(entry != null)
                   {
                        gpRecEntryName.setText(entry.getName());
                        gpRecDatePicker.setDate(entry.getDate());
                        currentGraph =   GraphUtilities.copyNewGraph(entry.getGraph());
                        reloadGraph();
                   }
               }
               
               else
               {
                   gpRecEntryName.setText("");
                   gpRecDatePicker.setDate(new Date());
               }
            }
            
            private void removeRecordedGraph()
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
                    dataPanel.loadNodes(currentGraph);
                    dataPanel.loadEdges(currentGraph);
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

                        currentGraph =   GraphUtilities.copyNewGraph(entry.getGraph());
                        reloadGraph();
                    }
                }
            }
            
            
            //--------------------------------------
            //  CENTRALITY TRANSFORMER
            //--------------------------------------
            
            private class CentralityTransformer implements Transformer<Node, Shape>
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
            
            private void setVertexColour(Color colour, Collection<Node> vertices)
            {
                if(vertices == null)
                    vertices   =   currentGraph.getVertices();
                
                for(Node vertex : vertices)
                    vertex.setFill(colour);
                
                gViewer.repaint();
            }
            
            private void setEdgeColour(Color colour, Collection<Edge> edges)
            {
                if(edges == null)
                    edges   =   currentGraph.getEdges();
                
                for(Edge edge : edges)
                    edge.setFill(colour);
                
                gViewer.repaint();
            }
            
            private void showVertexLabels(boolean show)
            {
                gViewer.getRenderContext().setVertexLabelTransformer(new VertexLabelTransformer(show));
                gViewer.repaint();
            }
            
            private void showEdgeLabels(boolean show)
            {
                gViewer.getRenderContext().setEdgeLabelTransformer(new EdgeLabelTransformer(show));
                gViewer.repaint();
            }
            
            
            private void showCluster()
            {
                int numRemoved  =   (int) controlPanel.clusterEdgeRemoveSpinner.getValue();
                boolean group   =   controlPanel.clusterTransformCheck.isSelected();
                GraphUtilities.cluster(gLayout, currentGraph, numRemoved, group);
                gViewer.repaint();
            }
            
            private void showCentrality()
            {
                Map<Node, Double> centrality;
                if(currentGraph.getVertexCount() <= 1) return;
                
                SparseDoubleMatrix2D matrix =   GraphMatrixOperations.graphToSparseMatrix(currentGraph);
                int selectedCentrality      =   controlPanel.centralityTypeBox.getSelectedIndex();
                boolean transform           =   controlPanel.centralityMorphCheck.isSelected();
                String prefix;
                
                switch(selectedCentrality)
                {
                    case 0: 
                        centrality  =   MatrixTools.getScores(MatrixTools.powerIterationFull(matrix), currentGraph);
                        prefix      =   "EigenVector";
                        break;
                        
                    case 1: 
                        centrality  =   MatrixTools.getScores(new ClosenessCentrality(currentGraph, new WeightTransformer()), currentGraph);
                        prefix      =   "Closeness";
                        break;
                        
                    case 2: 
                        centrality  =   MatrixTools.getScores(new BetweennessCentrality(currentGraph, new WeightTransformer()), currentGraph);
                        prefix      =   "Betweenness";
                        break; 
                        
                    default: return;
                }
                
                
                Collection<Node> vertices     =   currentGraph.getVertices();
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
            
            
            private void reloadGraph()
            {
                gViewer.getPickedVertexState().clear();
                gViewer.getPickedEdgeState().clear();
                selectedItems = null;
                
                gLayout.removeAll();
                gLayout.setGraph(currentGraph);
                gViewer.repaint();
                dataPanel.loadNodes(currentGraph);
                dataPanel.loadEdges(currentGraph);
            }
            
            private void resetGraph()
            {
                currentGraph    =   new SparseMultigraph<>();
                reloadGraph();
            }

            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if(controlPanel.selectCheck.isSelected())
                {
                    selectedItems   =   e.getItemSelectable().getSelectedObjects();
                    controlPanel.updateSelectedComponents();
                }
            }
        }
        
        //--------------------------------------
        //  OUTPUT PANEL
        //--------------------------------------
        
        private class OutputPanel extends JPanel
        {
            private JTextArea outputArea;
            public OutputPanel()
            {
                setLayout(new BorderLayout());
                outputArea  =   new JTextArea("");
                outputArea.setBackground(Color.WHITE);
                outputArea.setEditable(false);
                JScrollPane outputScroller  =   new JScrollPane(outputArea);
                outputScroller.setPreferredSize(new Dimension(650, 565));
                outputScroller.setBorder(null);
                
                add(outputScroller);
            }
            
            private void clearLog()
            {
                outputArea.setText("");
            }
        }
    }
}
