//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls.options;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class ViewerOptionPanel extends JPanel
{
    public ViewerOptionPanel() {}
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
