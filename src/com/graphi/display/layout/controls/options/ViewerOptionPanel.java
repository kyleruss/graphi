//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls.options;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

public class ViewerOptionPanel extends AbstractOptionPanel
{
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
    }
}
