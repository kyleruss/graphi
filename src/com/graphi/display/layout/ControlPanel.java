//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.app.Consts;
import com.graphi.display.layout.util.PluginMenuListener;
import com.graphi.io.AdjMatrixParser;
import com.graphi.io.GMLParser;
import com.graphi.io.GraphMLParser;
import com.graphi.io.Storage;
import com.graphi.io.TableExporter;
import com.graphi.plugins.Plugin;
import com.graphi.plugins.PluginConfig;
import com.graphi.plugins.PluginManager;
import com.graphi.sim.GraphPlayback;
import com.graphi.sim.Network;
import com.graphi.util.ComponentUtils;
import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;


public class ControlPanel extends JPanel implements ActionListener
{
    protected PluginMenuListener menuListener;
    protected JMenuItem activePluginItem;

    protected JPanel dataControlPanel, outputControlPanel, displayControlPanel;
    protected JPanel modePanel;
    protected JPanel simPanel;
    protected JRadioButton editCheck, selectCheck, moveCheck;
    protected ButtonGroup modeGroup;
    protected JComboBox genAlgorithmsBox;
    protected JButton resetGeneratorBtn, executeGeneratorBtn;
    protected JPanel genPanel, baGenPanel, klGenPanel, raGenPanel;
    protected JSpinner latticeSpinner, clusteringSpinner;
    protected JSpinner initialNSpinner, addNSpinner, randProbSpinner, randNumSpinner;
    protected JCheckBox simTiesCheck, randDirectedCheck, baDirectedCheck;
    protected JSpinner simTiesPSpinner;
    protected JLabel simTiesPLabel;

    protected IOPanel ioPanel;
    protected JPanel editPanel;
    protected JLabel selectedLabel;
    protected JButton gObjAddBtn, gObjEditBtn, gObjRemoveBtn;

    protected JPanel computePanel;
    protected JPanel computeInnerPanel;
    protected JPanel clusterPanel, spathPanel;
    protected JSpinner clusterEdgeRemoveSpinner;
    protected JCheckBox clusterTransformCheck;
    protected JComboBox computeBox;
    protected JTextField spathFromField, spathToField;
    protected JButton computeBtn;
    protected JPanel centralityPanel;
    protected JComboBox centralityTypeBox;
    protected ButtonGroup centralityOptions;
    protected JCheckBox centralityMorphCheck;
    protected ButtonGroup editObjGroup;
    protected JRadioButton editVertexRadio, editEdgeRadio;

    protected JPanel viewerPanel;
    protected JCheckBox viewerVLabelsCheck;
    protected JCheckBox viewerELabelsCheck;
    protected JButton viewerBGBtn, vertexBGBtn, edgeBGBtn;

    protected JPanel playbackPanel;
    protected JButton recordCtrlsBtn;
    protected boolean recording;
    protected JButton displayCtrlsBtn;
    protected JLabel activeScriptLabel;
    private MainPanel mainPanel;

    public ControlPanel(MainPanel mainPanel) 
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 0, 3, 8));
        setPreferredSize(new Dimension(230, 1650));

        this.mainPanel  =   mainPanel;
        menuListener    =   new PluginMenuListener(mainPanel.menu.getPluginListMenu(), mainPanel.appManager.getPluginManager());
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

        editCheck.setIcon(new ImageIcon(mainPanel.editBlackIcon));
        selectCheck.setIcon(new ImageIcon(mainPanel.pointerIcon));
        moveCheck.setIcon(new ImageIcon(mainPanel.moveIcon));

        moveCheck.setSelectedIcon(new ImageIcon(mainPanel.moveSelectedIcon));
        editCheck.setSelectedIcon(new ImageIcon(mainPanel.editSelectedIcon));
        selectCheck.setSelectedIcon(new ImageIcon(mainPanel.pointerSelectedIcon));

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
        genAlgorithmsBox.addItem("Random");
        genAlgorithmsBox.addActionListener(this);

        resetGeneratorBtn           =   new JButton("Reset");
        executeGeneratorBtn         =   new JButton("Generate");

        executeGeneratorBtn.addActionListener(this);
        resetGeneratorBtn.addActionListener(this);
        simTiesCheck.addActionListener(this);

        resetGeneratorBtn.setBackground(Color.WHITE);
        executeGeneratorBtn.setBackground(Color.WHITE);

        resetGeneratorBtn.setIcon(new ImageIcon(mainPanel.resetIcon));
        executeGeneratorBtn.setIcon(new ImageIcon(mainPanel.executeIcon));

        genPanel    =   new JPanel(new CardLayout());
        baGenPanel  =   new JPanel(new MigLayout());
        klGenPanel  =   new JPanel(new MigLayout());
        raGenPanel  =   new JPanel(new MigLayout());

        genPanel.add(klGenPanel, Consts.KL_PANEL_CARD);
        genPanel.add(baGenPanel, Consts.BA_PANEL_CARD);
        genPanel.add(raGenPanel, Consts.RA_PANEL_CARD);

        genPanel.setBackground(Consts.TRANSPARENT_COL);
        klGenPanel.setBackground(Consts.TRANSPARENT_COL);
        baGenPanel.setOpaque(false);
        raGenPanel.setOpaque(false);

        latticeSpinner      =   new JSpinner(new SpinnerNumberModel(15, 0, 100, 1));
        clusteringSpinner   =   new JSpinner(new SpinnerNumberModel(2, 0, 10, 1));
        randProbSpinner     =   new JSpinner(new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1));
        randNumSpinner      =   new JSpinner(new SpinnerNumberModel(5, 1, 1000, 1));

        latticeSpinner.setPreferredSize(new Dimension(50, 20));
        clusteringSpinner.setPreferredSize(new Dimension(50, 20));
        randProbSpinner.setPreferredSize(new Dimension(50, 20));
        randNumSpinner.setPreferredSize(new Dimension(50, 20));
        latticeSpinner.setOpaque(true);
        clusteringSpinner.setOpaque(true);
        randNumSpinner.setOpaque(true);
        randProbSpinner.setOpaque(true);

        randDirectedCheck   =   new JCheckBox("Directed");
        baDirectedCheck     =   new JCheckBox("Directed");

        randDirectedCheck.setOpaque(true);
        randDirectedCheck.setBackground(Consts.PRESET_COL);
        baDirectedCheck.setOpaque(true);
        baDirectedCheck.setBackground(Consts.PRESET_COL);

        initialNSpinner     =   new JSpinner(new SpinnerNumberModel(2, 0, 1000, 1));
        addNSpinner         =   new JSpinner(new SpinnerNumberModel(100, 0, 1000, 1));
        initialNSpinner.setOpaque(true);
        addNSpinner.setOpaque(true);

        baGenPanel.add(new JLabel("Initial nodes"));
        baGenPanel.add(initialNSpinner, "wrap");
        baGenPanel.add(new JLabel("Generated nodes"));
        baGenPanel.add(addNSpinner, "wrap");
        baGenPanel.add(baDirectedCheck, "al center, span 2");

        klGenPanel.add(new JLabel("Lattice size"));
        klGenPanel.add(latticeSpinner, "wrap");
        klGenPanel.add(new JLabel("Clustering exp."));
        klGenPanel.add(clusteringSpinner);

        raGenPanel.add(new JLabel("Edge probability"));
        raGenPanel.add(randProbSpinner, "wrap");
        raGenPanel.add(new JLabel("No. vertices"));
        raGenPanel.add(randNumSpinner, "wrap"); 
        raGenPanel.add(randDirectedCheck, "al center, span 2");

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
        editPanel.setBackground(Consts.TRANSPARENT_COL);

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
        gObjAddBtn.setIcon(new ImageIcon(mainPanel.addIcon));
        gObjRemoveBtn.setIcon(new ImageIcon(mainPanel.removeIcon));
        gObjEditBtn.setIcon(new ImageIcon(mainPanel.editIcon));

        selectedLabel.setFont(new Font("Arial", Font.BOLD, 12));

        gObjAddBtn.addActionListener(this);
        gObjEditBtn.addActionListener(this);
        gObjRemoveBtn.addActionListener(this);

        editObjGroup.add(editVertexRadio);
        editObjGroup.add(editEdgeRadio);
        editVertexRadio.setSelected(true);

        JPanel selectedPanel        =   ComponentUtils.wrapComponents(null, new JLabel("Selected: "), selectedLabel);
        JPanel editObjPanel         =   ComponentUtils.wrapComponents(null, editVertexRadio, editEdgeRadio);
        JPanel gObjOptsPanel        =   ComponentUtils.wrapComponents(null, gObjAddBtn, gObjEditBtn, gObjRemoveBtn);
        selectedPanel.setBackground(Consts.PRESET_COL);
        gObjOptsPanel.setBackground(Consts.PRESET_COL);
        editObjPanel.setBackground(Consts.PRESET_COL);

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
        spathPanel.setBackground(Consts.TRANSPARENT_COL);
        computeBtn.setBackground(Color.WHITE);
        computeBtn.addActionListener(this);
        computeBtn.setIcon(new ImageIcon(mainPanel.executeIcon));

        computeBox.addItem("Clusters");
        computeBox.addItem("Centrality");
        computeBox.addActionListener(this);

        clusterEdgeRemoveSpinner    =   new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        clusterTransformCheck       =   new JCheckBox("Transform graph");
        clusterEdgeRemoveSpinner.setPreferredSize(new Dimension(50, 25));

        JPanel clusterEdgesPanel        =   ComponentUtils.wrapComponents(null, new JLabel("Delete edges"), clusterEdgeRemoveSpinner);
        clusterPanel.setLayout(new MigLayout());
        clusterPanel.add(clusterEdgesPanel, "wrap");
        clusterPanel.add(clusterTransformCheck);
        clusterEdgesPanel.setBackground(Consts.PRESET_COL);
        clusterPanel.setBackground(Consts.PRESET_COL);

        spathFromField  =   new JTextField();
        spathToField    =   new JTextField();
        spathFromField.setPreferredSize(new Dimension(50, 20));
        spathToField.setPreferredSize(new Dimension(50, 20));

        JLabel tLabel           =   new JLabel("To ID");
        JPanel spathFromPanel   =   ComponentUtils.wrapComponents(null, new JLabel("From ID"), spathFromField);
        JPanel spathToPanel     =   ComponentUtils.wrapComponents(null, tLabel, spathToField);
        JPanel spathWrapper     =   new JPanel(new MigLayout()); 
        spathWrapper.add(spathFromPanel, "wrap");
        spathWrapper.add(spathToPanel);
        spathWrapper.setBackground(Consts.TRANSPARENT_COL);
        spathFromPanel.setBackground(Consts.TRANSPARENT_COL);
        spathToPanel.setBackground(Consts.TRANSPARENT_COL);
        spathPanel.add(spathWrapper); 

        centralityTypeBox       =   new JComboBox();
        centralityOptions       =   new ButtonGroup();
        centralityMorphCheck    =   new JCheckBox("Transform graph");
        centralityTypeBox.addItem("Eigenvector");
        centralityTypeBox.addItem("Closeness");
        centralityTypeBox.addItem("Betweenness");
        centralityTypeBox.addActionListener(this);

        JPanel cenTypePanel     =   ComponentUtils.wrapComponents(null, new JLabel("Type"), centralityTypeBox);
        centralityPanel.add(cenTypePanel, "wrap");
        centralityPanel.add(centralityMorphCheck);
        cenTypePanel.setBackground(Consts.PRESET_COL);
        centralityPanel.setBackground(Consts.PRESET_COL);

        computeInnerPanel.add(clusterPanel, Consts.CLUSTER_PANEL_CARD);
        computeInnerPanel.add(spathPanel, Consts.SPATH_PANEL_CARD);
        computeInnerPanel.add(centralityPanel, Consts.CENTRALITY_PANEL_CARD);

        computePanel.add(new JLabel("Compute "), "al right");
        computePanel.add(computeBox, "wrap");
        computePanel.add(computeInnerPanel, "wrap, span 2, al center");
        computePanel.add(computeBtn, "span 2, al center");

        JPanel computeWrapperPanel   =   new JPanel(new BorderLayout());
        computeWrapperPanel.add(computePanel);

        CardLayout clusterInnerLayout   =   (CardLayout) computeInnerPanel.getLayout();
        clusterInnerLayout.show(computeInnerPanel, Consts.CLUSTER_PANEL_CARD);

        viewerPanel             =   new JPanel(new MigLayout("fillx"));
        viewerVLabelsCheck      =   new JCheckBox("Vertex labels");
        viewerELabelsCheck      =   new JCheckBox("Edge labels");
        viewerBGBtn             =   new JButton("Choose");
        vertexBGBtn             =   new JButton("Choose");
        edgeBGBtn               =   new JButton("Choose");

        ImageIcon clrIcon   =   new ImageIcon(mainPanel.colourIcon);
        viewerBGBtn.setIcon(clrIcon);
        vertexBGBtn.setIcon(clrIcon);
        edgeBGBtn.setIcon(clrIcon);

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

        recordCtrlsBtn.setIcon(new ImageIcon(mainPanel.recordIcon));
        displayCtrlsBtn.setIcon(new ImageIcon(mainPanel.playIcon));

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

        mainPanel.menu.setMenuItemListener(this);
        mainPanel.menu.getPluginListMenu().setActivePluginItem(mainPanel.menu.getPluginListMenu().getPluginMenuItem("defaultPluginItem"));

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

    protected void updateSelectedComponents()
    {
        if(mainPanel.data.getSelectedItems() == null || mainPanel.data.getSelectedItems().length == 0)
            selectedLabel.setText("None");
        else
        {
            if(mainPanel.data.getSelectedItems().length > 1)
                selectedLabel.setText(mainPanel.data.getSelectedItems().length + " objects");
            else
            {
                Object selectedObj  =   mainPanel.data.getSelectedItems()[0];
                if(selectedObj instanceof Node)
                    selectedLabel.setText("Node (ID=" + ((Node) selectedObj).getID() + ")");


                else if(selectedObj instanceof Edge)
                    selectedLabel.setText("Edge (ID=" + ((Edge) selectedObj).getID() + ")");
            }
        }
    }

    protected void computeExecute()
    {
        int selectedIndex   =   computeBox.getSelectedIndex();

        switch(selectedIndex)
        {
            case 0: mainPanel.screenPanel.graphPanel.showCluster();
            case 1: mainPanel.screenPanel.graphPanel.showCentrality();
        }
    }

    protected void showGeneratorSim()
    {
        int genIndex    =   genAlgorithmsBox.getSelectedIndex();
        mainPanel.data.getNodeFactory().setLastID(0);
        mainPanel.data.getEdgeFactory().setLastID(0);

        switch(genIndex)
        {
            case 0: showKleinbergSim(); break;
            case 1: showBASim(); break;
            case 2: showRASim(); break;
        }

        if(simTiesCheck.isSelected())
            Network.simulateInterpersonalTies(mainPanel.data.getGraph(), mainPanel.data.getEdgeFactory(), (double) simTiesPSpinner.getValue());

        mainPanel.screenPanel.graphPanel.reloadGraph();
    }

    protected void showAbout()
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

    protected void resetSim()
    {
        mainPanel.data.setGraph(new SparseMultigraph());
        mainPanel.screenPanel.graphPanel.reloadGraph();
    }

    protected void showKleinbergSim()
    {
        int latticeSize =   (int) latticeSpinner.getValue();
        int clusterExp  =   (int) clusteringSpinner.getValue();

        mainPanel.data.setGraph(Network.generateKleinberg(latticeSize, clusterExp, mainPanel.data.getNodeFactory(), mainPanel.data.getEdgeFactory()));
    }

    protected void showBASim()
    {
        int m           =   (int) initialNSpinner.getValue();
        int n           =   (int) addNSpinner.getValue();

        mainPanel.data.setGraph(Network.generateBerbasiAlbert(mainPanel.data.getNodeFactory(), mainPanel.data.getEdgeFactory(), n, m, baDirectedCheck.isSelected()));
    }

    protected void showRASim()
    {
        int n               =   (int) randNumSpinner.getValue();
        double p            =   (double) randProbSpinner.getValue();
        boolean directed    =   randDirectedCheck.isSelected();

        mainPanel.data.setGraph(Network.generateRandomGraph(mainPanel.data.getNodeFactory(), mainPanel.data.getEdgeFactory(), n, p, directed));
    }

    protected void showVertexBGChange()
    {
        Color selectedColour    =   JColorChooser.showDialog(null, "Choose vertex colour", Color.BLACK);

        if(selectedColour != null)
            mainPanel.screenPanel.graphPanel.setVertexColour(selectedColour, null);
    }

    protected void showEdgeBGChange()
    {
        Color selectedColour    =   JColorChooser.showDialog(null, "Choose edge colour", Color.BLACK);

        if(selectedColour != null)
            mainPanel.screenPanel.graphPanel.setEdgeColour(selectedColour, null);
    }

    protected void showViewerBGChange()
    {
        Color selectedColour    =   JColorChooser.showDialog(null, "Choose viewer background colour", Color.WHITE);

        if(selectedColour != null)
            mainPanel.screenPanel.graphPanel.gViewer.setBackground(selectedColour);
    }

    //--------------------------------------
    //  IO PANEL
    //--------------------------------------

    protected class IOPanel extends JPanel implements ActionListener
    {
        protected JButton exportBtn, importBtn;
        protected JLabel currentStorageLabel;
        protected JCheckBox directedCheck;
        protected JComboBox ioTypeBox;
        protected JPanel directedCheckWrapper;

        public IOPanel()
        {
            setLayout(new GridLayout(4, 1));
            setBorder(BorderFactory.createTitledBorder("I/O Controls"));
            currentStorageLabel     =   new JLabel("None");
            importBtn               =   new JButton("Import");
            exportBtn               =   new JButton("Export");
            ioTypeBox               =   new JComboBox();
            directedCheck           =   new JCheckBox("Directed");

            importBtn.setIcon(new ImageIcon(mainPanel.openIcon));
            exportBtn.setIcon(new ImageIcon(mainPanel.saveIcon));

            ioTypeBox.addItem("Graph");
            ioTypeBox.addItem("Log");
            ioTypeBox.addItem("Script");
            ioTypeBox.addItem("Table");
            ioTypeBox.setPreferredSize(new Dimension(150, 30));

            ioTypeBox.addActionListener(this);
            importBtn.addActionListener(this);
            exportBtn.addActionListener(this);

            importBtn.setBackground(Color.WHITE);
            exportBtn.setBackground(Color.WHITE);

            JPanel storageBtnWrapper    =   ComponentUtils.wrapComponents(null, importBtn, exportBtn);
            JPanel currentGraphWrapper  =   ComponentUtils.wrapComponents(null, new JLabel("Active: "), currentStorageLabel);
            JPanel ioTypeWrapper        =   ComponentUtils.wrapComponents(null, ioTypeBox);
            directedCheckWrapper        =   ComponentUtils.wrapComponents(null, directedCheck);

            storageBtnWrapper.setBackground(Consts.PRESET_COL);
            currentGraphWrapper.setBackground(Consts.PRESET_COL);
            directedCheckWrapper.setBackground(Consts.PRESET_COL);
            ioTypeWrapper.setBackground(Consts.PRESET_COL);

            add(currentGraphWrapper);
            add(ioTypeWrapper);
            add(directedCheckWrapper);
            add(storageBtnWrapper);
            currentStorageLabel.setFont(new Font("Arial", Font.BOLD, 12));
        }
        
        private void performImport()
        {
            int typeIndex   =   ioTypeBox.getSelectedIndex();
            
            switch (typeIndex) 
            {
                case 0:
                    importGraph();
                    break;
                    
                case 1:
                    importLog();
                    break;
                    
                case 2:
                    importScript();
                    break;
                    
                default:
                    break;
            }
        }
        
        private void performExport()
        {
            int typeIndex = ioTypeBox.getSelectedIndex();
            
            switch(typeIndex)
            {
                case 0:
                    exportGraph();
                    break;
                    
                case 1:
                    exportLog();
                    break;
                    
                case 2:
                    exportScript();
                    break;
                    
                case 3:
                    exportTable();
                    break;
                    
                default:
                    break;
            }
        }
        
        private void ioTypeChange()
        {
            int typeIndex   =   ioTypeBox.getSelectedIndex();
            
            importBtn.setEnabled(typeIndex != 3);
            directedCheckWrapper.setVisible(typeIndex == 0);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object src  =   e.getSource();

            if(src == importBtn)
                performImport();

            else if(src == exportBtn)
                performExport();
            
            else if(src == ioTypeBox)
                ioTypeChange();
        }
    }

    protected void showCurrentComputePanel()
    {
        int selectedIndex   =   computeBox.getSelectedIndex();
        String card;

        switch(selectedIndex)
        {
            case 0: card = Consts.CLUSTER_PANEL_CARD; break;
            case 1: card = Consts.CENTRALITY_PANEL_CARD; break;
            case 2: card = Consts.SPATH_PANEL_CARD; break;
            default: return;
        }

        CardLayout clusterInnerLayout   =   (CardLayout) computeInnerPanel.getLayout();
        clusterInnerLayout.show(computeInnerPanel, card);
    }

    protected void showSimPanel()
    {
        int selectedIndex   =   genAlgorithmsBox.getSelectedIndex();
        String card;

        switch(selectedIndex)
        {
            case 0: card    =   Consts.KL_PANEL_CARD; break;
            case 1: card    =   Consts.BA_PANEL_CARD; break;
            case 2: card    =   Consts.RA_PANEL_CARD; break;
            default: return;
        }

        CardLayout gLayout  =   (CardLayout) genPanel.getLayout();
        gLayout.show(genPanel, card);
    }

    protected void loadConfigPlugins(PluginManager pm)
    {
        PluginConfig config =   mainPanel.appManager.getConfigManager().getPluginConfig();
        List<String> paths  =   config.getLoadedPluginPaths();

        for(int i = 0; i < paths.size(); i++)
            loadPluginFile(new File(paths.get(i)), pm);
    }

    protected void loadPluginFile(File file, PluginManager pm)
    {
        if(file == null) return;

        if(pm == null) pm       =   mainPanel.appManager.getPluginManager();

        Plugin plugin           =   pm.fetchPlugin(file);
        String defaultPath      =   mainPanel.appManager.getConfigManager().getPluginConfig().getDefaultPluginPath();
        boolean isDefaultPath   =   file.getPath().equals(defaultPath);

        if(plugin == null)
            JOptionPane.showMessageDialog(null, "Failed to load plugin");

        else
        {
            if(pm.hasPlugin(plugin) && !isDefaultPath)
                JOptionPane.showMessageDialog(null, "Plugin is already loaded");

            else
            {
                JMenuItem item  =   new JMenuItem(plugin.getPluginName());
                item.addActionListener(menuListener);
                mainPanel.menu.getPluginListMenu().addPluginMenuItem(plugin.getPluginName(), item);

                if(!isDefaultPath)
                    pm.addPlugin(plugin);
                else
                    mainPanel.menu.getPluginListMenu().setActivePluginItem(item);
            }
        }
    }

    protected void importPlugin()
    {
        File file           =   ComponentUtils.getFile(true, "Graphi .jar plugin", "jar");
        loadPluginFile(file, null);
    }

    protected void exportGraph()
    {
        File file           =   ComponentUtils.getFile(false, "Graphi .graph, adjacency matrix .txt, .gml, graphML .xml", "graph", "txt", "gml", "xml");
        String extension    =   ComponentUtils.getFileExtension(file);

        if(file != null && mainPanel.data.getGraph() != null)
        {
            if(extension.equalsIgnoreCase("graph"))
                Storage.saveObj(mainPanel.data.getGraph(), file);

            else if(extension.equalsIgnoreCase("txt"))
                AdjMatrixParser.exportGraph(mainPanel.data.getGraph(), file, ioPanel.directedCheck.isSelected());

            else if(extension.equalsIgnoreCase("gml"))
                GMLParser.exportGraph(mainPanel.data.getGraph(), file, ioPanel.directedCheck.isSelected());

            else if(extension.equalsIgnoreCase("xml"))
                GraphMLParser.exportGraph(file, mainPanel.data.getGraph());
        }
    }

    protected void importGraph()
    {
        File file           =   ComponentUtils.getFile(true, "Graphi .graph, adjacency matrix .txt, .gml, graphML .xml", "graph", "txt", "gml", "xml");
        String extension    =   ComponentUtils.getFileExtension(file);   

        if(file != null)
        {
            mainPanel.data.getNodeFactory().setLastID(0);
            mainPanel.data.getEdgeFactory().setLastID(0);

            if(extension.equalsIgnoreCase("graph"))
                mainPanel.data.setGraph((Graph) Storage.openObj(file));

            else if(extension.equalsIgnoreCase("txt"))
                mainPanel.data.setGraph(AdjMatrixParser.importGraph(file, ioPanel.directedCheck.isSelected(), mainPanel.data.getNodeFactory(), mainPanel.data.getEdgeFactory()));

            else if(extension.equalsIgnoreCase("gml"))
                mainPanel.data.setGraph(GMLParser.importGraph(file, mainPanel.data.getNodeFactory(), mainPanel.data.getEdgeFactory()));

            else if(extension.equalsIgnoreCase("xml"))
                mainPanel.data.setGraph(GraphMLParser.importGraph(file, mainPanel.data.getNodeFactory(), mainPanel.data.getEdgeFactory()));


            ioPanel.currentStorageLabel.setText(file.getName());

            initCurrentNodes();
            initCurrentEdges();

            mainPanel.screenPanel.graphPanel.gLayout.setGraph(mainPanel.data.getGraph());
            mainPanel.screenPanel.graphPanel.gViewer.repaint();
            mainPanel.screenPanel.dataPanel.loadNodes(mainPanel.data.getGraph());
            mainPanel.screenPanel.dataPanel.loadEdges(mainPanel.data.getGraph());
        }
    }

    protected void importScript()
    {
        File file   =   ComponentUtils.getFile(true, "Graphi .gscript file", "gscript");
        if(file != null)
        {
            mainPanel.screenPanel.graphPanel.gPlayback    =   (GraphPlayback) Storage.openObj(file);
            ioPanel.currentStorageLabel.setText(file.getName());
            activeScriptLabel.setText(file.getName());
            mainPanel.screenPanel.graphPanel.addPlaybackEntries();

        }
    }

    protected void exportScript()
    {
        File file   =   ComponentUtils.getFile(false, "Graphi .gscript file", "gscript");
        if(file != null)
            Storage.saveObj(mainPanel.screenPanel.graphPanel.gPlayback, file);
    }
    
    protected void exportTable()
    {
        File file   =   ComponentUtils.getFile(false, "CSV, TSV", "csv", "tsv");
        if(file != null)
        {
            JTable table        =    null;
            DataPanel dataPanel =   mainPanel.screenPanel.dataPanel;
            int tableIndex      =   dataPanel.dataTabPane.getSelectedIndex();
            
            switch (tableIndex) 
            {
                case 0:
                    table   =   dataPanel.vertexTable;
                    break;
                case 1:
                    table   =   dataPanel.edgeTable;
                    break;
                case 2:
                    table   =   dataPanel.computeTable;
                    break;
            }
            
            TableExporter.exportTable(table, file);
        }
    }

    protected void initCurrentNodes()
    {
        if(mainPanel.data.getGraph() == null) return;

        mainPanel.data.getNodes().clear();
        Collection<Node> nodes  =   mainPanel.data.getGraph().getVertices();
        for(Node node : nodes)
            mainPanel.data.getNodes().put(node.getID(), node);
    }

    protected void initCurrentEdges()
    {
        if(mainPanel.data.getGraph() == null) return;

        mainPanel.data.getEdges().clear();
        Collection<Edge> edges  =   mainPanel.data.getGraph().getEdges();

        for(Edge edge : edges)
            mainPanel.data.getEdges().put(edge.getID(), edge);
    }

    protected void exportLog()
    {
        File file   =   ComponentUtils.getFile(false, "Graphi .log file", "log");
        if(file != null)
            Storage.saveOutputLog(mainPanel.screenPanel.outputPanel.outputArea.getText(), file);
    }

    protected void importLog()
    {
        File file   =   ComponentUtils.getFile(true, "Graphi .log file", "log");
        if(file != null)
        {
            ioPanel.currentStorageLabel.setText(file.getName());
            mainPanel.screenPanel.outputPanel.outputArea.setText(Storage.openOutputLog(file));
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
                mainPanel.screenPanel.dataPanel.addVertex();
            else
                mainPanel.screenPanel.dataPanel.addEdge();
        }

        else if(src == gObjEditBtn)
        {
            if(editVertexRadio.isSelected())
                mainPanel.screenPanel.dataPanel.editVertex();
            else
                mainPanel.screenPanel.dataPanel.editEdge();
        }

        else if(src == gObjRemoveBtn)
        {
            if(editVertexRadio.isSelected())
                mainPanel.screenPanel.dataPanel.removeVertex();
            else
                mainPanel.screenPanel.dataPanel.removeEdge();
        }

        else if(src == editCheck)
        {
            mainPanel.screenPanel.graphPanel.mouse.setMode(ModalGraphMouse.Mode.EDITING);
            mainPanel.screenPanel.graphPanel.mouse.remove(mainPanel.screenPanel.graphPanel.mouse.getPopupEditingPlugin());
        }

        else if(src == moveCheck)
            mainPanel.screenPanel.graphPanel.mouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

        else if(src == selectCheck)
            mainPanel.screenPanel.graphPanel.mouse.setMode(ModalGraphMouse.Mode.PICKING);

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
            mainPanel.screenPanel.graphPanel.showVertexLabels(viewerVLabelsCheck.isSelected());

        else if(src == viewerELabelsCheck)
            mainPanel.screenPanel.graphPanel.showEdgeLabels(viewerELabelsCheck.isSelected());

        else if(src == mainPanel.menu.getMenuItem("aboutItem"))
            showAbout();

        else if(src == mainPanel.menu.getMenuItem("exitItem"))
            System.exit(0);

        else if(src == mainPanel.menu.getMenuItem("miniItem"))
            mainPanel.frame.setState(JFrame.ICONIFIED);

        else if(src == mainPanel.menu.getMenuItem("maxItem"))
            mainPanel.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        else if(src == mainPanel.menu.getMenuItem("importGraphItem"))
            importGraph();

        else if(src == mainPanel.menu.getMenuItem("exportGraphItem"))
            exportGraph();

        else if(src == mainPanel.menu.getMenuItem("importLogItem"))
            importLog();

        else if(src == mainPanel.menu.getMenuItem("exportLogItem"))
            exportLog();

        else if(src == mainPanel.menu.getMenuItem("vLabelsItem"))
            mainPanel.screenPanel.graphPanel.showVertexLabels(true);

        else if(src == mainPanel.menu.getMenuItem("eLabelsItem"))
            mainPanel.screenPanel.graphPanel.showEdgeLabels(true);

        else if(src == mainPanel.menu.getMenuItem("viewerBGItem"))
            showViewerBGChange();

        else if(src == mainPanel.menu.getMenuItem("edgeBGItem"))
            showEdgeBGChange();

        else if(src == mainPanel.menu.getMenuItem("vertexBGItem"))
            showVertexBGChange();

        else if(src == mainPanel.menu.getMenuItem("clearLogItem"))
            mainPanel.screenPanel.outputPanel.clearLog();

        else if(src == mainPanel.menu.getMenuItem("resetGraphItem"))
            mainPanel.screenPanel.graphPanel.resetGraph();

        else if(src == mainPanel.menu.getMenuItem("addVertexItem"))
            mainPanel.screenPanel.dataPanel.addVertex();

        else if(src == mainPanel.menu.getMenuItem("editVertexItem"))
            mainPanel.screenPanel.dataPanel.editVertex();

        else if(src == mainPanel.menu.getMenuItem("removeVertexItem"))
            mainPanel.screenPanel.dataPanel.removeVertex();

        else if(src == mainPanel.menu.getMenuItem("addEdgeItem"))
            mainPanel.screenPanel.dataPanel.addEdge();

        else if(src == mainPanel.menu.getMenuItem("editEdgeItem"))
            mainPanel.screenPanel.dataPanel.editEdge();

        else if(src == mainPanel.menu.getMenuItem("removeEdgeItem"))
            mainPanel.screenPanel.dataPanel.removeEdge();

        else if(src == mainPanel.menu.getMenuItem("loadPluginItem"))
            importPlugin();

        else if(src == displayCtrlsBtn)
            mainPanel.screenPanel.graphPanel.changePlaybackPanel(mainPanel.screenPanel.graphPanel.PLAYBACK_CARD);

        else if(src == recordCtrlsBtn)
            mainPanel.screenPanel.graphPanel.changePlaybackPanel(mainPanel.screenPanel.graphPanel.RECORD_CARD);

        else if(src == simTiesCheck)
        {
            simTiesPSpinner.setVisible(!simTiesPSpinner.isVisible());
            simTiesPLabel.setVisible(!simTiesPLabel.isVisible());
        }
    }
}