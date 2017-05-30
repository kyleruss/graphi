//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

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
        
        transitionIcon  =   new ImageIcon("data/resources/images/loadspinner.gif");
        transitionLabel =   new JLabel(transitionIcon);
        
        transitionLabel.setBorder(BorderFactory.createEmptyBorder(130, 0, 0, 0));
        wrapperPanel    =   new JPanel();
        wrapperPanel.add(transitionLabel);
        wrapperPanel.setBackground(Color.WHITE);
        add(wrapperPanel);
    }
    
    public JLabel getTransitionLabel()
    {
        return transitionLabel;
    }
    
    public JPanel getWrapperPanel()
    {
        return wrapperPanel;
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getSize().width, getSize().height);
    }
}
