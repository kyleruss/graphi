//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.app.Consts;
import com.graphi.sim.Network;
import edu.uci.ics.jung.graph.SparseMultigraph;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.miginfocom.swing.MigLayout;

public class SimulationControlPanel extends JPanel implements ActionListener
{
    protected JComboBox genAlgorithmsBox;
    protected JButton resetGeneratorBtn, executeGeneratorBtn;
    protected JPanel genPanel, baGenPanel, klGenPanel, raGenPanel;
    protected JSpinner latticeSpinner, clusteringSpinner;
    protected JSpinner initialNSpinner, addNSpinner, randProbSpinner, randNumSpinner;
    protected JCheckBox simTiesCheck, randDirectedCheck, baDirectedCheck;
    protected JSpinner simTiesPSpinner;
    protected JLabel simTiesPLabel;
    protected JPanel wrapperPanel;
    private final ControlPanel outer;
    
    public SimulationControlPanel(ControlPanel outer)
    {
        this.outer  =   outer;
        setLayout(new BorderLayout());
        
        genAlgorithmsBox        =   new JComboBox();
        simTiesCheck            =   new JCheckBox("Interpersonal ties");
        simTiesPLabel           =   new JLabel("P");
        wrapperPanel            =   new JPanel(new MigLayout("fillx"));
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

        resetGeneratorBtn.setIcon(new ImageIcon(outer.getMainPanel().resetIcon));
        executeGeneratorBtn.setIcon(new ImageIcon(outer.getMainPanel().executeIcon));

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

        wrapperPanel.setBorder(BorderFactory.createTitledBorder("Simulation controls"));
        wrapperPanel.add(new JLabel("Generator"), "al right");
        wrapperPanel.add(genAlgorithmsBox, "wrap");
        wrapperPanel.add(genPanel, "wrap, span 2, al center");
        wrapperPanel.add(simTiesCheck, "al center, span 2, wrap");
        wrapperPanel.add(simTiesPLabel, "al right");
        wrapperPanel.add(simTiesPSpinner, "wrap");
        wrapperPanel.add(resetGeneratorBtn, "al right");
        wrapperPanel.add(executeGeneratorBtn, "");
        add(wrapperPanel);
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
    
    public void showGeneratorSim()
    {
        int genIndex    =   genAlgorithmsBox.getSelectedIndex();
        outer.getMainPanel().getGraphData().getNodeFactory().setLastID(0);
        outer.getMainPanel().getGraphData().getEdgeFactory().setLastID(0);

        switch(genIndex)
        {
            case 0: showKleinbergSim(); break;
            case 1: showBASim(); break;
            case 2: showRASim(); break;
        }

        if(simTiesCheck.isSelected())
            Network.simulateInterpersonalTies(outer.getMainPanel().getGraphData().getGraph(), 
                    outer.getMainPanel().getGraphData().getEdgeFactory(), (double) simTiesPSpinner.getValue());

        outer.getMainPanel().getScreenPanel().getGraphPanel().reloadGraph();
    }
    
    protected void showBASim()
    {
        int m           =   (int) initialNSpinner.getValue();
        int n           =   (int) addNSpinner.getValue();

        outer.getMainPanel().getGraphData().setGraph(Network.generateBerbasiAlbert(outer.getMainPanel().getGraphData().getNodeFactory(), 
                outer.getMainPanel().getGraphData().getEdgeFactory(), n, m, baDirectedCheck.isSelected()));
    }

    protected void showRASim()
    {
        int n               =   (int) randNumSpinner.getValue();
        double p            =   (double) randProbSpinner.getValue();
        boolean directed    =   randDirectedCheck.isSelected();

        outer.getMainPanel().getGraphData().setGraph(Network.generateRandomGraph(outer.getMainPanel().getGraphData().getNodeFactory(), 
                outer.getMainPanel().getGraphData().getEdgeFactory(), n, p, directed));
    }
    
    protected void showKleinbergSim()
    {
        int latticeSize =   (int) latticeSpinner.getValue();
        int clusterExp  =   (int) clusteringSpinner.getValue();

        outer.getMainPanel().getGraphData().setGraph(Network.generateKleinberg(latticeSize, 
                clusterExp, outer.getMainPanel().getGraphData().getNodeFactory(), 
                outer.getMainPanel().getGraphData().getEdgeFactory()));
    }
    
    public void resetSim()
    {
        outer.getMainPanel().getGraphData().setGraph(new SparseMultigraph());
        outer.getMainPanel().getScreenPanel().getGraphPanel().reloadGraph();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        
        if(src == genAlgorithmsBox)
            showSimPanel();
         
        else if(src == executeGeneratorBtn)
            showGeneratorSim();

        else if(src == resetGeneratorBtn)
            resetSim();
         
        else if(src == simTiesCheck)
        {
            simTiesPSpinner.setVisible(!simTiesPSpinner.isVisible());
            simTiesPLabel.setVisible(!simTiesPLabel.isVisible());
        }
    }

    public JComboBox getGenAlgorithmsBox() 
    {
        return genAlgorithmsBox;
    }

    public JSpinner getLatticeSpinner()
    {
        return latticeSpinner;
    }

    public JSpinner getClusteringSpinner() 
    {
        return clusteringSpinner;
    }

    public JSpinner getInitialNSpinner()
    {
        return initialNSpinner;
    }

    public JSpinner getAddNSpinner()
    {
        return addNSpinner;
    }

    public JSpinner getRandProbSpinner() 
    {
        return randProbSpinner;
    }

    public JSpinner getRandNumSpinner()
    {
        return randNumSpinner;
    }

    public JCheckBox getSimTiesCheck()
    {
        return simTiesCheck;
    }

    public JCheckBox getBaDirectedCheck() 
    {
        return baDirectedCheck;
    }

    public JSpinner getSimTiesPSpinner()
    {
        return simTiesPSpinner;
    }
    
    
}
