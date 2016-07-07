//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.app.AppManager;
import com.graphi.display.layout.MainPanel;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class ResetSimTask extends AbstractTask
{

    @Override
    public void initDefaultProperties() {}

    @Override
    public void performTask() 
    {
        MainPanel mainPanel =   AppManager.getInstance().getPluginManager().getActivePlugin().getPanel();
        mainPanel.getGraphData().setGraph(new SparseMultigraph());
        mainPanel.getScreenPanel().getGraphPanel().reloadGraph();
    }

    @Override
    public void initTaskDetails()
    {
        name    =   "Reset network simulation";
    }
    
}
