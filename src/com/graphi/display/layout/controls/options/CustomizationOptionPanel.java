//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls.options;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
        nodeBGPanel         =   new BackgroundColourPanel(Color.GREEN);
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
