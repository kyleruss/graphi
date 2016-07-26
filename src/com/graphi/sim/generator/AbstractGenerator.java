//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.sim.generator;

import com.graphi.app.AppManager;
import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Factory;

public abstract class AbstractGenerator implements NetworkGenerator
{
    protected String generatorName;
    protected String generatorDescription;
    
    public AbstractGenerator()
    {
        initGeneratorDetails();
    }
    
    protected abstract void initGeneratorDetails();
    
    protected Factory<Node> getDefaultNodeFactory()
    {
        return AppManager.getInstance().getPluginManager().getActivePlugin().getPanel().getGraphData().getNodeFactory();
    }
    
    protected Factory<Edge> getDefaultEdgeFactory()
    {
        return AppManager.getInstance().getPluginManager().getActivePlugin().getPanel().getGraphData().getEdgeFactory();
    }
    
    @Override
    public Graph<Node, Edge> generateNetwork()
    {
        return generateNetwork(getDefaultNodeFactory(), getDefaultEdgeFactory());
    }
    
    @Override
    public String getGeneratorName() 
    {
        return generatorName;
    }

    @Override
    public String getGeneratorDescription() 
    {
        
        return generatorDescription;
    }
}
