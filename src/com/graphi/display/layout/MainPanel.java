//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.app.Consts;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

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
        
        JToolBar topControlBar  =   new JToolBar();
        JButton testBtn         =   new JButton("test");
        JButton testBtn2        =   new JButton("test2");
        JButton testBtn3        =   new JButton("test3");
        topControlBar.add(testBtn);
        topControlBar.add(testBtn2);
        topControlBar.add(testBtn3);
        
        add(topControlBar, BorderLayout.NORTH);
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
