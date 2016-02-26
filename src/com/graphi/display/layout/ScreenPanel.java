//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ScreenPanel extends JPanel
{
    protected MainPanel mainPanel;
    protected final DataPanel dataPanel;
    protected final GraphPanel graphPanel;
    protected final OutputPanel outputPanel;
    protected final JTabbedPane tabPane;

    public ScreenPanel(MainPanel mainPanel)
    {            
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 5, 5, 5));
        
        this.mainPanel      =   mainPanel;
        tabPane             =   new JTabbedPane();
        dataPanel           =   new DataPanel(mainPanel);
        graphPanel          =   new GraphPanel(mainPanel);
        outputPanel         =   new OutputPanel();


        tabPane.addTab("", graphPanel);
        tabPane.addTab("", dataPanel);
        tabPane.addTab("", outputPanel);

        JLabel dispLabel    =   new JLabel("Display", JLabel.CENTER);
        JLabel dataLabel    =   new JLabel("Data", JLabel.CENTER);
        JLabel outLabel     =   new JLabel("Output", JLabel.CENTER);

        dispLabel.setIcon(new ImageIcon(mainPanel.graphIcon));
        outLabel.setIcon(new ImageIcon(mainPanel.clipIcon));
        dataLabel.setIcon(new ImageIcon(mainPanel.tableIcon));

        tabPane.setTabComponentAt(0, dispLabel);
        tabPane.setTabComponentAt(1, dataLabel);
        tabPane.setTabComponentAt(2, outLabel);


        add(tabPane);
    }
}
