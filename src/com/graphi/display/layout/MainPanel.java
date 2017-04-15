//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.layout.controls.ControlPanel;
import com.graphi.app.AppManager;
import com.graphi.app.Consts;
import com.graphi.display.MainMenu;
import com.graphi.display.Window;
import com.graphi.plugins.PluginManager;
import com.graphi.graph.GraphData;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
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
    protected MainMenu menu;
    protected JFrame frame; 
    
    public MainPanel(AppManager appManager)
    {
        setPreferredSize(new Dimension(Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT));
        setLayout(new BorderLayout());        
        
        menu                =   Window.getInstance().getMenu();
        frame               =   Window.getInstance().getFrame();
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
    
    public GraphData getGraphData()
    {
        return data;
    }
    
    public void setGraphData(GraphData data)
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

    public MainMenu getMenu()
    {
        return menu;
    }
    
    public JFrame getFrame()
    {
        return frame;
    }
    
    public static MainPanel getInstance()
    {
        return PluginManager.getInstance().getActivePlugin().getPanel();
    }
}
