//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.sim.generator;

import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Factory;

public interface NetworkGenerator 
{
    public String getGeneratorName();
    
    public String getGeneratorDescription();
    
    public Graph<Node, Edge> generateNetwork(Factory<Node> nodeFactory, Factory<Edge> edgeFactory);
    
    public Graph<Node, Edge> generateNetwork();
}
