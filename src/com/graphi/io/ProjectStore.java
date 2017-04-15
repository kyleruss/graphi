//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import com.graphi.graph.GraphData;
import javax.swing.JTable;

public class ProjectStore 
{
    private GraphData graphData;
    private String output;
    private JTable outputTable;

    public GraphData getGraphData() 
    {
        return graphData;
    }

    public void setGraphData(GraphData graphData) 
    {
        this.graphData = graphData;
    }

    public String getOutput() 
    {
        return output;
    }

    public void setOutput(String output)
    {
        this.output = output;
    }

    public JTable getOutputTable() 
    {
        return outputTable;
    }

    public void setOutputTable(JTable outputTable) 
    {
        this.outputTable = outputTable;
    }
    
    
    
}
