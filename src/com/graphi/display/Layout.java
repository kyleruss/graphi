package com.graphi.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class Layout extends JPanel
{
    private final ControlPanel controlPanel;
    private final ScreenPanel screenPanel;
    private final JSplitPane splitPane;
    
    public Layout()
    {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(850, 650));
        
        controlPanel    =   new ControlPanel();
        screenPanel     =   new ScreenPanel();
        splitPane       =   new JSplitPane();
        
        splitPane.setLeftComponent(screenPanel);
        splitPane.setRightComponent(controlPanel);
        add(splitPane, BorderLayout.CENTER);
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
            add(new JButton("Butt"));
        }
    }
}
