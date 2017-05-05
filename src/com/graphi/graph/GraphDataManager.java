//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.graph;

public class GraphDataManager 
{
    private static GraphDataManager instance;
    private GraphData graphData;
    
    private GraphDataManager()
    {
        graphData   =   new GraphData();
    }
    
    public GraphData getGraphData()
    {
        return graphData;
    }
    
    public void setGraphData(GraphData graphData)
    {
        this.graphData  =   graphData;
    }
    
    public static GraphDataManager getInstance()
    {
        if(instance == null) instance   =   new GraphDataManager();
        return instance;
    }
    
    public static GraphData getGraphDataInstance()
    {
        return getInstance().getGraphData();
    }
}
