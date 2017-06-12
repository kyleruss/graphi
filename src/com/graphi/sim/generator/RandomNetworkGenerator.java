//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.sim.generator;

import com.graphi.graph.Edge;
import com.graphi.graph.Node;
import com.graphi.util.factory.GraphFactory;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.collections15.Factory;

public class RandomNetworkGenerator extends AbstractGenerator
{
    private int numNodes;
    private double edgeProbability;
    private boolean directed;
    
    public RandomNetworkGenerator()
    {
        this(10, 0.5, false);
    }
    
    public RandomNetworkGenerator(int numNodes, double edgeProbability, boolean directed)
    {
        this.numNodes           =   numNodes;
        this.edgeProbability    =   edgeProbability;
        this.directed           =   directed;
    }
            
    @Override
    protected void initGeneratorDetails()
    {
        generatorName   =   "Random";
    }

    @Override
    public Graph<Node, Edge> generateNetwork(Factory<Node> nodeFactory, Factory<Edge> edgeFactory) 
    {
        /*ErdosRenyiGenerator gen =   new ErdosRenyiGenerator(new GraphFactory(), nodeFactory, edgeFactory, numNodes, edgeProbability);
        return gen.create(); */
        
        Graph<Node, Edge> graph     =   new SparseMultigraph<>();
        Random rGen                 =   new Random();
        Map<Integer, Node> nodeMap  =   new HashMap<>();   
        
        for(int i = 0; i < numNodes; i++)
        {
            Node nextNode   =   nodeFactory.create();
            graph.addVertex(nextNode);
            nodeMap.put(i, nextNode);
        }
        
        for(int i = 0; i < numNodes; i++)
        {
            Node currentNode    =   nodeMap.get(i);   
            int randomID;
            Node connectNode;
        
            do 
            { 
                randomID    =   rGen.nextInt(numNodes); 
                connectNode =   nodeMap.get(randomID);
            }
            
            while(currentNode.equals(connectNode) || graph.isNeighbor(currentNode, connectNode));
        
            graph.addEdge(edgeFactory.create(), currentNode, connectNode, EdgeType.UNDIRECTED);
        }
        
        Collection<Node> nodes  =   nodeMap.values();
        
        for(Node node : nodes)
        {
            for(Node other : nodes)
            {
                if(!other.equals(node))
                {
                    double p    =   rGen.nextDouble();
                    
                    if(p <= edgeProbability && !graph.isNeighbor(node, other))
                        graph.addEdge(edgeFactory.create(), node, other, EdgeType.UNDIRECTED);
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
