//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.app.Consts;
import com.graphi.display.AppResources;
import com.graphi.display.layout.GraphPanel;
import com.graphi.display.layout.ScreenPanel;
import com.graphi.sim.Network;
import com.graphi.sim.generator.BerbasiGenerator;
import com.graphi.sim.generator.KleinbergGenerator;
import com.graphi.sim.generator.NetworkGenerator;
import com.graphi.sim.generator.RandomNetworkGenerator;
import com.graphi.graph.Edge;
import com.graphi.graph.GraphData;
import com.graphi.graph.GraphDataManager;
import com.graphi.graph.Node;
import edu.uci.ics.jung.graph.Graph;
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
    private JComboBox genAlgorithmsBox;
    private JButton resetGeneratorBtn, executeGeneratorBtn;
    private JPanel genPanel, baGenPanel, klGenPanel, raGenPanel;
    private JSpinner latticeSpinner, clusteringSpinner;
    private JSpinner initialNSpinner, addNSpinner, randProbSpinner, randNumSpinner;
    private JCheckBox simTiesCheck, randDirectedCheck, baDirectedCheck;
    private JSpinner simTiesPSpinner;
    private JLabel simTiesPLabel;
    private JPanel wrapperPanel;
    
    public SimulationControlPanel()
    {
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

        resetGeneratorBtn      =   new JButton("Reset");
        executeGeneratorBtn    =   new JButton("Generate");

        executeGeneratorBtn.addActionListener(this);
        resetGeneratorBtn.addActionListener(this);
        simTiesCheck.addActionListener(this);

        resetGeneratorBtn.setBackground(Color.WHITE);
        executeGeneratorBtn.setBackground(Color.WHITE);

        resetGeneratorBtn.setIcon(new ImageIcon(AppResources.getInstance().getResource("resetIcon")));
        executeGeneratorBtn.setIcon(new ImageIcon(AppResources.getInstance().getResource("executeIcon")));

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
        clusteringSpinner   =   new JSpinner(new SpinnerNumberModel(2.5, 0.0, 10.0, 1.0));
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
    
    public void executeGeneratorSim(final NetworkGenerator generator)
    {
        int genIndex    =   genAlgorithmsBox.getSelectedIndex();
        GraphData data  =   GraphDataManager.getGraphDataInstance();
        data.resetFactoryIDs();
        
        Thread simThread   =   new Thread(()->
        {
            NetworkGenerator gen    =   generator;
            ScreenPanel.getInstance().displayTransition();
            
            if(gen == null)
            {
                switch(genIndex)
                {
                    case 0: gen = getKleinbergSim(); break;
                    case 1: gen = getBASim(); break;
                    case 2: gen = getRASim(); break;
                }
            }
            
            Graph<Node, Edge> generatedGraph    =   gen != null? gen.generateNetwork() : new SparseMultigraph<>();
            
            data.setGraph(generatedGraph);

            if(simTiesCheck.isSelected())
                Network.simulateInterpersonalTies(data.getGraph(), 
                        data.getEdgeFactory(), (double) simTiesPSpinner.getValue());

            GraphPanel.getInstance().reloadGraph();
            ScreenPanel.getInstance().displayGraph();
        });
        
        simThread.start();
    }
    
    protected NetworkGenerator getBASim()
    {
        int m                           =   (int) initialNSpinner.getValue();
        int n                           =   (int) addNSpinner.getValue();
        boolean dir                     =   baDirectedCheck.isSelected();
        
        return new BerbasiGenerator(m, n, dir);
    }

    protected NetworkGenerator getRASim()
    {
        int n               =   (int) randNumSpinner.getValue();
        double p            =   (double) randProbSpinner.getValue();
        boolean directed    =   randDirectedCheck.isSelected();

        return new RandomNetworkGenerator(n, p, directed);
    }
    
    protected NetworkGenerator getKleinbergSim()
    {
        int latticeSize     =   (int) latticeSpinner.getValue();
        double clusterExp   =   (double) clusteringSpinner.getValue();

        return new KleinbergGenerator(latticeSize, clusterExp);
    }
    
    public void resetSim()
    {
        GraphDataManager.getGraphDataInstance().setGraph(new SparseMultigraph());
        GraphPanel.getInstance().reloadGraph();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        
        if(src == genAlgorithmsBox)
            showSimPanel();
         
        else if(src == executeGeneratorBtn)
            executeGeneratorSim(null);

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
