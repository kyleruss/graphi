//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.app.AppManager;
import com.graphi.display.layout.MainPanel;
import com.graphi.plugins.PluginManager;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class ResetSimTask extends AbstractTask
{
    @Override
    public void initTaskDetails()
    {
        setTaskName("Reset network simulation");
    }
    
    @Override
    public void initDefaultProperties() {}

    @Override
    public void performTask() 
    {
        MainPanel mainPanel =   PluginManager.getInstance().getActivePlugin().getPanel();
        mainPanel.getGraphData().setGraph(new SparseMultigraph());
        mainPanel.getScreenPanel().getGraphPanel().reloadGraph();
    }
}
