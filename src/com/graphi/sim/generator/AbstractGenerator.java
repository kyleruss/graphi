//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.sim.generator;

import com.graphi.graph.Edge;
import com.graphi.graph.GraphDataManager;
import com.graphi.graph.Node;
import edu.uci.ics.jung.graph.Graph;
import java.util.Map;
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
        return GraphDataManager.getGraphDataInstance().getNodeFactory();
    }
    
    protected Factory<Edge> getDefaultEdgeFactory()
    {
        return GraphDataManager.getGraphDataInstance().getEdgeFactory();
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
