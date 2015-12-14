package com.graphi.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

public class Layout extends JPanel
{
    private final ControlPanel controlPanel;
    private final ScreenPanel screenPanel;
    
    public Layout()
    {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(850, 650));
        
        controlPanel    =   new ControlPanel();
        screenPanel     =   new ScreenPanel();
        
        add(screenPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
    }
    
    private class ControlPanel extends JPanel
    {
        public ControlPanel()
        {
            setPreferredSize(new Dimension(250, 1));
            setBackground(Color.LIGHT_GRAY);
        }
    }
    
    private class ScreenPanel extends JPanel
    {
        public ScreenPanel()
        {
            
        }
    }
}
