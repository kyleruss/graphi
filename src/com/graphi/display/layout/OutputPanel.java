//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.AppResources;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class OutputPanel extends JPanel
{
    protected JTextArea outputArea;
    private OutputControls controls;
    protected MainPanel mainPanel;
    
    public OutputPanel(MainPanel mainPanel)
    {
        setLayout(new BorderLayout());
        this.mainPanel  =   mainPanel;
        outputArea      =   new JTextArea();
        controls        =   new OutputControls();
        outputArea.setBackground(Color.WHITE);
        outputArea.setEditable(false);
        JScrollPane outputScroller  =   new JScrollPane(outputArea);
        outputScroller.setPreferredSize(new Dimension(650, 565));
        outputScroller.setBorder(null);

        add(controls, BorderLayout.SOUTH);
        add(outputScroller, BorderLayout.CENTER);
    }

    public void clearLog()
    {
        outputArea.setText("");
    }
    
    public JTextArea getOutputArea()
    {
        return outputArea;
    }
    
    private class OutputControls extends JPanel implements ActionListener
    {
        private JButton exportBtn, importBtn, clearBtn;
        
        public OutputControls()
        {
            exportBtn   =   new JButton("Export");
            importBtn   =   new JButton("Import");
            clearBtn    =   new JButton("Clear");
            
            AppResources resources  =   AppResources.getInstance();
            exportBtn.setIcon(new ImageIcon(resources.getResource("openIcon")));
            importBtn.setIcon(new ImageIcon(resources.getResource("saveIcon")));
            clearBtn.setIcon(new ImageIcon(resources.getResource("resetIcon")));
            
            add(clearBtn);
            add(importBtn);
            add(exportBtn);
            
            clearBtn.addActionListener(this);
            importBtn.addActionListener(this);
            exportBtn.addActionListener(this);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            Object src  =   e.getSource();
            
            if(src == clearBtn)
                clearLog();
            
            else if(src == importBtn)
                mainPanel.getControlPanel().getIoPanel().importLog();
            
            else if(src == exportBtn)
                mainPanel.getControlPanel().getIoPanel().exportLog();
        }
    }
}