//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.AppResources;
import com.graphi.display.layout.controls.ControlPanel;
import com.graphi.display.layout.controls.IOControlPanel;
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
    private static OutputPanel instance;
    
    public OutputPanel()
    {
        setLayout(new BorderLayout());
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
    
    public void setOutputText(String text)
    {
        outputArea.setText(text);
    }
    
    public void extendOutputText(String text)
    {
        String originalText =   outputArea.getText();
        text                =   originalText + text;
        outputArea.setText(text);
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
            Object src              =   e.getSource();
            IOControlPanel ioPanel  =   ControlPanel.getInstance().getIoPanel();
            
            if(src == clearBtn)
                clearLog();
            
            else if(src == importBtn)
                ioPanel.importLog();
            
            else if(src == exportBtn)
                ioPanel.exportLog();
        }
    }
    
    public static OutputPanel getInstance()
    {
        if(instance == null) instance   =   new OutputPanel();
        return instance;
    }
}