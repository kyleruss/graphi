//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.app.Consts;
import com.graphi.util.ComponentUtils;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.miginfocom.swing.MigLayout;

public class ComputeControlPanel extends JPanel implements ActionListener
{
    protected JPanel computeInnerPanel;
    protected JPanel clusterPanel, centralityPanel;
    protected JSpinner clusterEdgeRemoveSpinner;
    protected JCheckBox clusterTransformCheck;
    protected JComboBox computeBox;
    protected JButton computeBtn;
    protected JComboBox centralityTypeBox;
    protected ButtonGroup centralityOptions;
    protected JCheckBox centralityMorphCheck;
    protected JPanel wrapper;
    private final ControlPanel outer;
    
    public ComputeControlPanel(ControlPanel outer)
    {
        this.outer  =   outer;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Computation controls"));
        
        computeInnerPanel   =   new JPanel(new CardLayout());
        clusterPanel        =   new JPanel();
        wrapper             =   new JPanel(new MigLayout("fillx"));
        centralityPanel     =   new JPanel(new MigLayout());
        computeBox          =   new JComboBox();
        computeBtn          =   new JButton("Execute");
        computeBtn.setBackground(Color.WHITE);
        computeBtn.addActionListener(this);
        computeBtn.setIcon(new ImageIcon(outer.getMainPanel().executeIcon));

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
        computeInnerPanel.add(centralityPanel, Consts.CENTRALITY_PANEL_CARD);

        wrapper.setBackground(Consts.PRESET_COL);
        wrapper.add(new JLabel("Compute "), "al right");
        wrapper.add(computeBox, "wrap");
        wrapper.add(computeInnerPanel, "wrap, span 2, al center");
        wrapper.add(computeBtn, "span 2, al center");
        add(wrapper);
        
        CardLayout clusterInnerLayout   =   (CardLayout) computeInnerPanel.getLayout();
        clusterInnerLayout.show(computeInnerPanel, Consts.CLUSTER_PANEL_CARD);

    }
    
    protected void computeExecute()
    {
        int selectedIndex   =   computeBox.getSelectedIndex();
        outer.getMainPanel().getScreenPanel().getDataPanel().clearComputeTable();

        switch(selectedIndex)
        {
            case 0: outer.getMainPanel().getScreenPanel().getGraphPanel().showCluster();
            case 1: outer.getMainPanel().getScreenPanel().getGraphPanel().showCentrality();
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

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src  =   e.getSource();
        
        if(src == computeBtn)
            computeExecute();
        
        else if(src == computeBox)
            showCurrentComputePanel();
    }

    public JSpinner getClusterEdgeRemoveSpinner() 
    {
        return clusterEdgeRemoveSpinner;
    }

    public JCheckBox getClusterTransformCheck()
    {
        return clusterTransformCheck;
    }

    public JComboBox getComputeBox() 
    {
        return computeBox;
    }

    public JComboBox getCentralityTypeBox()
    {
        return centralityTypeBox;
    }

    public JCheckBox getCentralityMorphCheck() 
    {
        return centralityMorphCheck;
    }
    
    
}
