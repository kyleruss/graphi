//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.display.layout.GraphPanel;
import com.graphi.graph.GraphDataManager;
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
        GraphDataManager.getGraphDataInstance().setGraph(new SparseMultigraph());
        GraphPanel.getInstance().reloadGraph();
    }
}
