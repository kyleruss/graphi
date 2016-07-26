//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.sim.generator;

import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.util.Collection;
import java.util.Random;
import org.apache.commons.collections15.Factory;

public class RandomNetworkGenerator extends AbstractGenerator
{
    private int numNodes;
    private double edgeProbability;
    private boolean directed;
            
    @Override
    protected void initGeneratorDetails()
    {
        generatorName   =   "Random";
    }

    @Override
    public Graph<Node, Edge> generateNetwork(Factory<Node> nodeFactory, Factory<Edge> edgeFactory) 
    {
        Graph<Node, Edge> graph =   new SparseMultigraph<>();
        Random rGen             =   new Random();
        
        for(int i = 0; i < numNodes; i++)
            graph.addVertex(nodeFactory.create());
        
        Collection<Node> nodes  =   graph.getVertices();
        for(Node node : nodes)
        {
            for(Node other : nodes)
            {
                if(!other.equals(node))
                {
                    double p    =   rGen.nextDouble() * 100;
                    
                    if(p >= edgeProbability && !(!directed && graph.isNeighbor(other, node)))
                        graph.addEdge(edgeFactory.create(), node, other, directed? EdgeType.DIRECTED : EdgeType.UNDIRECTED);
                }
            }
        }
        
        return graph;
    }

    public int getNumNodes() 
    {
        return numNodes;
    }

    public void setNumNodes(int numNodes)
    {
        this.numNodes = numNodes;
    }

    public double getEdgeProbability() 
    {
        return edgeProbability;
    }

    public void setEdgeProbability(double edgeProbability) 
    {
        this.edgeProbability = edgeProbability;
    }

    public boolean isDirected() 
    {
        return directed;
    }

    public void setDirected(boolean directed)
    {
        this.directed = directed;
    }
    
    
}
