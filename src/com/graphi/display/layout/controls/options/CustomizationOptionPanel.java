//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls.options;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CustomizationOptionPanel extends AbstractOptionPanel implements ActionListener, ChangeListener
{
    private final int THEME_CLASSIC     =   0;
    private final int THEME_DARK        =   1;
    private final int EDGE_UNDIRECTED   =   0;
    private final int EDGGE_DIRECTED    =   1;
    
    private final int THEME_INDEX       =   0;
    private final int EDGETYPE_INDEX    =   1;
    private final int CUSTOM_RES_INDEX  =   2;
    private final int WIND_HEIGHT_INDEX =   3;
    private final int WIND_WIDTH_INDEX  =   4;
    private final int DISP_BG_INDEX     =   5;
    private final int NODE_BG_INDEX     =   6;
    private final int EDGE_BG_INDEX     =   7;
    
    private final JCheckBox enableResCheck;
    private final JSpinner customWidthSpinner;
    private final JSpinner customHeightSpinner;
    private final JComboBox themeBox;
    private final JButton displayBGButton;
    private final JButton nodeBGButton;
    private final JButton edgeBGButton;
    private final JComboBox edgeTypeBox;
    private final BackgroundColourPanel displayBGPanel, nodeBGPanel, edgeBGPanel;
    
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
        displayBGPanel      =   new BackgroundColourPanel(Color.WHITE);
        nodeBGPanel         =   new BackgroundColourPanel(Color.BLUE);
        edgeBGPanel         =   new BackgroundColourPanel(Color.BLACK);
        
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
        JPanel displayBGWrapper     =     new JPanel();
        displayBGWrapper.add(displayBGPanel);
        displayBGWrapper.add(displayBGButton);
        
        add(displayBGLabel);
        add(displayBGWrapper, "wrap");
        
        
        //Node background
        JPanel nodeBGWrapper        =   new JPanel();
        nodeBGWrapper.add(nodeBGPanel);
        nodeBGWrapper.add(nodeBGButton);
        
        add(nodeBGLabel);
        add(nodeBGWrapper, "wrap");
        
        //Edge background
        JPanel edgeBGWrapper        =   new JPanel();
        edgeBGWrapper.add(edgeBGPanel);
        edgeBGWrapper.add(edgeBGButton);
        
        add(edgeBGLabel);
        add(edgeBGWrapper);
        
        themeBox.addActionListener(this);
        edgeTypeBox.addActionListener(this);
        displayBGButton.addActionListener(this);
        nodeBGButton.addActionListener(this);
        edgeBGButton.addActionListener(this);
        enableResCheck.addActionListener(this);
        customWidthSpinner.addChangeListener(this);
        customHeightSpinner.addChangeListener(this);
    }
    
    @Override
    protected void loadOptions()
    {
        
    }
    
    @Override
    protected void handleOptionChanged(int optionIndex)
    {
        switch(optionIndex)
        {
            
        }
    }

    public void toggleBackgroundChangeDisplay(int optionIndex)
    {
        BackgroundColourPanel selectedBGPanel;
        
        switch(optionIndex)
        {
            case DISP_BG_INDEX: selectedBGPanel =   displayBGPanel; break;
            case NODE_BG_INDEX: selectedBGPanel =   nodeBGPanel; break;
            case EDGE_BG_INDEX: selectedBGPanel =   edgeBGPanel; break;
            default: return;
        }
        
        Color currentColour     =   selectedBGPanel.getBackgroundColour();
        Color selectedColour    =   JColorChooser.showDialog(null, "Choose background colour", currentColour);
        
        if(!selectedColour.equals(currentColour))
        {
            selectedBGPanel.setBackgroundColour(selectedColour);
            addOptionChanged(optionIndex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src  =   e.getSource();
        
        if(src == displayBGButton)
            toggleBackgroundChangeDisplay(DISP_BG_INDEX);
        
        else if(src == nodeBGButton)
            toggleBackgroundChangeDisplay(NODE_BG_INDEX);
        
        else if(src == edgeBGButton)
            toggleBackgroundChangeDisplay(EDGE_BG_INDEX);
        
        else if(src == themeBox)
            addOptionChanged(THEME_INDEX);
        
        else if(src == edgeTypeBox)
            addOptionChanged(EDGETYPE_INDEX);
        
        else if(src == enableResCheck)
            addOptionChanged(CUSTOM_RES_INDEX);
    }

    @Override
    public void stateChanged(ChangeEvent e) 
    {
        Object src  =   e.getSource();
        
        if(src == customWidthSpinner)
            addOptionChanged(WIND_WIDTH_INDEX);
        
        else if(src == customHeightSpinner)
            addOptionChanged(WIND_HEIGHT_INDEX);
    }
    
    private class BackgroundColourPanel extends JPanel
    {
        private Color backgroundColour;
        
        private BackgroundColourPanel()
        {
            this(Color.BLACK);
        }
        
        private BackgroundColourPanel(Color backgroundColour)
        {
            setPreferredSize(new Dimension(35, 35));
            setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
            
            this.backgroundColour    =   backgroundColour;
        }
        
        public Color getBackgroundColour()
        {
            return backgroundColour;
        }
        
        public void setBackgroundColour(Color backgroundColour)
        {
            this.backgroundColour   =   backgroundColour;
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.setColor(backgroundColour);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
