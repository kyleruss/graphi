//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.layout.controls.ControlPanel;
import com.graphi.app.Consts;
import com.graphi.graph.GraphData;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class MainPanel extends JPanel 
{
    protected JSplitPane splitPane;
    protected JScrollPane controlScroll;
    protected static MainPanel instance;
    
    public MainPanel()
    {
        setPreferredSize(new Dimension(Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT));
        setLayout(new BorderLayout());        
        
        initComponents();

        controlScroll.setBorder(null);
        controlScroll.getVerticalScrollBar().setUnitIncrement(25);
        splitPane.setRightComponent(controlScroll); 
        splitPane.setResizeWeight(Consts.MAIN_SPLIT_WG);
        add(splitPane, BorderLayout.CENTER);
    }
    
    protected void initComponents()
    {
        ControlPanel controlPanel   =   ControlPanel.getInstance();
        ScreenPanel screenPanel     =   ScreenPanel.getInstance();
        splitPane                   =   new JSplitPane();
        controlScroll               =   new JScrollPane(controlPanel);
        splitPane.setLeftComponent(screenPanel);
    }
    
    public static MainPanel getInstance()
    {
        if(instance == null) instance   =   new MainPanel();
        return instance;
    }
}
