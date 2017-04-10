//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls.options;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class CustomizationOptionPanel extends AbstractOptionPanel
{
    private final int THEME_CLASSIC     =   0;
    private final int THEME_DARK        =   1;
    private final int EDGE_UNDIRECTED   =   0;
    private final int EDGGE_DIRECTED    =   1;
    
    private final JCheckBox enableResCheck;
    private final JSpinner customWidthSpinner;
    private final JSpinner customHeightSpinner;
    private final JComboBox themeBox;
    private final JButton displayBGButton;
    private final JButton nodeBGButton;
    private final JButton edgeBGButton;
    private final JComboBox edgeTypeBox;
    
    public CustomizationOptionPanel()
    {
        enableResCheck      =   new JCheckBox();
        themeBox            =   new JComboBox();
        edgeTypeBox         =   new JComboBox();
        displayBGButton     =   new JButton("Choose");
        nodeBGButton        =   new JButton("Choose");
        edgeBGButton        =   new JButton("Choose");
        customWidthSpinner  =   new JSpinner(new SpinnerNumberModel(800, 800, 1920, 1));
        customHeightSpinner =   new JSpinner(new SpinnerNumberModel(600, 600, 1080, 1));
        
        JLabel enableResLabel   =   new JLabel("Enable custom resolution");
        JLabel themeLabel       =   new JLabel("Interface theme");
        JLabel edgeLabel        =   new JLabel("Default edge type");
        JLabel displayBGLabel   =   new JLabel("Display background");
        JLabel nodeBGLabel      =   new JLabel("Node background");
        JLabel edgeBGLabel      =   new JLabel("Edge background");
        JLabel widthLabel       =   new JLabel("Window width");
        JLabel heightLabel      =   new JLabel("Window height");
        
        enableResLabel.setFont(titleFont);
        themeLabel.setFont(titleFont);
        edgeLabel.setFont(titleFont);
        displayBGLabel.setFont(titleFont);
        nodeBGLabel.setFont(titleFont);
        edgeBGLabel.setFont(titleFont);
        widthLabel.setFont(titleFont);
        heightLabel.setFont(titleFont);
        
        themeBox.addItem("Classic");
        themeBox.addItem("Dark");
        edgeTypeBox.addItem("Undirected");
        edgeTypeBox.addItem("Directed");
        
        //Theme
        add(themeLabel, "gapx 0 200");
        add(themeBox, "wrap");
        
        //Edge type
        add(edgeLabel);
        add(edgeTypeBox, "wrap");
        
        //Enable custom resolution
        add(enableResLabel);
        add(enableResCheck, "wrap");
        
        //Custom width
        add(widthLabel);
        add(customWidthSpinner, "wrap");
        
        //Custom height
        add(heightLabel);
        add(customHeightSpinner, "wrap");
        
        //Display background
        add(displayBGLabel);
        add(displayBGButton, "wrap");
        
        //Node background
        add(nodeBGLabel);
        add(nodeBGButton, "wrap");
        
        //Edge background
        add(edgeBGLabel);
        add(edgeBGButton);
    }
}
