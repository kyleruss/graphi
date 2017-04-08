//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls.options;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public abstract class AbstractOptionPanel extends JPanel
{
    protected Font titleFont;
    protected Font standardFont;
    
    protected AbstractOptionPanel()
    {
        setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 0));
        setLayout(new MigLayout("", "", "50"));
        
        titleFont       =   new Font("Arial", Font.PLAIN, 24);
        standardFont    =   new Font("Arial", Font.PLAIN, 14);   
    }
    
    protected void initLayout(int numSettings)
    {
        GridLayout layout   =   new GridLayout(numSettings, 2);
        setLayout(layout);
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
