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
    protected ControlPanel controlPanel;
    protected ScreenPanel screenPanel;
    protected JSplitPane splitPane;
    protected JScrollPane controlScroll;
    protected GraphData data;
    protected static MainPanel instance;
    
    public MainPanel()
    {
        setPreferredSize(new Dimension(Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT));
        setLayout(new BorderLayout());        
        
        data                =   new GraphData();
        initComponents();

        controlScroll.setBorder(null);
        controlScroll.getVerticalScrollBar().setUnitIncrement(25);
        splitPane.setLeftComponent(screenPanel);
        splitPane.setRightComponent(controlScroll); 
        splitPane.setResizeWeight(Consts.MAIN_SPLIT_WG);
        add(splitPane, BorderLayout.CENTER);
    }
    
    protected void initComponents()
    {
        controlPanel        =   new ControlPanel(this);
        screenPanel         =   new ScreenPanel(this);
        splitPane           =   new JSplitPane();
        controlScroll       =   new JScrollPane(controlPanel);
    }
    
    public void setData(GraphData data)
    {
        this.data   =   data;
    }
    
    public ControlPanel getControlPanel() 
    {
        return controlPanel;
    }

    public ScreenPanel getScreenPanel() 
    {
        return screenPanel;
    }

    public GraphData getData()
    {
        return data;
    }
    
    public static MainPanel getInstance()
    {
        if(instance == null) instance   =   new MainPanel();
        return instance;
    }
}
