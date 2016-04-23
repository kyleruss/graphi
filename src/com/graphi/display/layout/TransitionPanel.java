package com.graphi.display.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TransitionPanel extends JPanel
{
    protected ImageIcon transitionIcon;
    protected JLabel transitionLabel;
    protected JPanel wrapperPanel;
    
    public TransitionPanel()
    {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        transitionIcon  =   new ImageIcon("data/resources/images/loadspinner.gif");
        transitionLabel =   new JLabel(transitionIcon);
        
        transitionLabel.setBorder(BorderFactory.createEmptyBorder(130, 0, 0, 0));
        wrapperPanel    =   new JPanel();
        wrapperPanel.add(transitionLabel);
        add(wrapperPanel);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getSize().width, getSize().height);
    }
}
