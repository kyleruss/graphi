//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class OutputPanel extends JPanel
{
    protected JTextArea outputArea;
    
    public OutputPanel()
    {
        setLayout(new BorderLayout());
        outputArea  =   new JTextArea("");
        outputArea.setBackground(Color.WHITE);
        outputArea.setEditable(false);
        JScrollPane outputScroller  =   new JScrollPane(outputArea);
        outputScroller.setPreferredSize(new Dimension(650, 565));
        outputScroller.setBorder(null);

        add(outputScroller);
    }

    public void clearLog()
    {
        outputArea.setText("");
    }
    
    public JTextArea getOutputArea()
    {
        return outputArea;
    }
}