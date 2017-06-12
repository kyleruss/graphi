//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls.options;

import com.graphi.config.AppConfig;
import com.graphi.config.ConfigManager;
import com.graphi.display.layout.GraphPanel;
import com.graphi.display.layout.MainPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

public class ViewerOptionPanel extends AbstractOptionPanel implements ActionListener
{
    private final int ENABLE_VISUALS_INDEX  =   0;
    private final int NODE_LABEL_INDEX      =   1;
    private final int EDGE_LABEL_INDEX      =   2;
    
    private final JCheckBox nodeLabelsCheck;
    private final JCheckBox edgeLabelsCheck;
    private final JCheckBox enableVisualsCheck;
    
    public ViewerOptionPanel() 
    {
        nodeLabelsCheck     =   new JCheckBox();
        edgeLabelsCheck     =   new JCheckBox();
        enableVisualsCheck  =   new JCheckBox();   
        
        JLabel nodeLabel    =   new JLabel("Display node labels");
        JLabel edgeLabel    =   new JLabel("Display edge labels");
        JLabel visualsLabel =   new JLabel("Enable visuals");
        
        nodeLabel.setFont(titleFont);
        edgeLabel.setFont(titleFont);
        visualsLabel.setFont(titleFont);
        
        add(visualsLabel);
        add(enableVisualsCheck, "wrap");
        add(nodeLabel, "gapx 0 100");
        add(nodeLabelsCheck, "wrap");
        add(edgeLabel);
        add(edgeLabelsCheck, "wrap");
        
        enableVisualsCheck.addActionListener(this);
        nodeLabelsCheck.addActionListener(this);
        edgeLabelsCheck.addActionListener(this);
        
        loadOptions();
    }
    
    @Override
    protected void loadOptions()
    {
        AppConfig appConfig =   ConfigManager.getInstance().getAppConfig();
        
        nodeLabelsCheck.setSelected(appConfig.isViewNodeLabels());
        edgeLabelsCheck.setSelected(appConfig.isViewEdgeLabels());
        enableVisualsCheck.setSelected(appConfig.isDisplayVisuals());
        
        toggleNodeLabels();
        toggleEdgeLabels();
        toggleDisplayVisuals();
    }
    
    public void toggleNodeLabels()
    {
        AppConfig appConfig =   ConfigManager.getInstance().getAppConfig();
        boolean showLabels  =   nodeLabelsCheck.isSelected();
        
        GraphPanel.getInstance().showObjectLabels(showLabels, true);
        appConfig.setViewNodeLabels(showLabels);
    }
    
    public void toggleEdgeLabels()
    {
        AppConfig appConfig =   ConfigManager.getInstance().getAppConfig();
        boolean showLabels  =   edgeLabelsCheck.isSelected();
        
        GraphPanel.getInstance().showObjectLabels(showLabels, false);
        appConfig.setViewEdgeLabels(showLabels);
    }
    
    public void toggleDisplayVisuals()
    {
        AppConfig appConfig =   ConfigManager.getInstance().getAppConfig();
        appConfig.setDisplayVisuals(enableVisualsCheck.isSelected());
    }
    
    @Override
    protected void handleOptionChanged(int optionIndex)
    {
        switch(optionIndex)
        {
            case ENABLE_VISUALS_INDEX: toggleDisplayVisuals(); break;
            case NODE_LABEL_INDEX: toggleNodeLabels(); break;
            case EDGE_LABEL_INDEX: toggleEdgeLabels(); break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src  =   e.getSource();
        
        if(src == enableVisualsCheck)
            addOptionChanged(ENABLE_VISUALS_INDEX);
        
        else if(src == nodeLabelsCheck)
            addOptionChanged(NODE_LABEL_INDEX);
        
        else if(src == edgeLabelsCheck)
            addOptionChanged(EDGE_LABEL_INDEX);
    }
}
