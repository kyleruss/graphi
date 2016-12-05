//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.sim.generator;

import com.graphi.sim.Network;
import com.graphi.util.Edge;
import com.graphi.util.GraphUtilities;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.collections15.Factory;

public class BerbasiGenerator extends AbstractGenerator
{
    private int initialNodeCount;
    private int numAddNodes;
    private boolean directedEdges;
    
    public BerbasiGenerator(int initialNodeCount, int numAddNodes, boolean directedEdges)
    {
        this.initialNodeCount   =   initialNodeCount;
        this.numAddNodes        =   numAddNodes;
        this.directedEdges      =   directedEdges;
    }
    
    @Override
    protected void initGeneratorDetails() 
    {
        generatorName   =   "BerbasiAlbert";
    }

    @Override
    public Graph<Node, Edge> generateNetwork(Factory<Node> nodeFactory, Factory<Edge> edgeFactory) 
    {
        if(nodeFactory == null) nodeFactory         =   () -> new Node();
        if(edgeFactory == null) edgeFactory         =   () -> new Edge();
        int n                                       =   numAddNodes;
        int m                                       =   initialNodeCount;
        Graph<Node, Edge> graph                     =   Network.generateConnectedGraph(nodeFactory, edgeFactory, m);
        
        
        for(int i = 0; i < n; i++)
        {
            Node current    =   nodeFactory.create();
            
            ArrayList<Node> vertices            =   new ArrayList<>(graph.getVertices());
            ArrayList<Node> nextVertices        =   new ArrayList<>();   
            Map<Integer, Integer> ignoreIndexes =   new HashMap<>();   
            Random rGen                         =   new Random();
            
            for(int j = 0; j < vertices.size(); j++)
            {
                if(ignoreIndexes.containsKey(j)) 
                {
                    if(j == vertices.size() -1 && nextVertices.size() < m)
                        j = -1;
                    
                    continue;
                }
                
                Node next       =   vertices.get(j);
                int degree      =   (directedEdges)? graph.inDegree(next) : graph.degree(next);
                int degreeSum   =   GraphUtilities.degreeSum(graph);
                double p        =   degree / (degreeSum * 1.0);
                double r        =   rGen.nextDouble();
                
                if(r <= p)
                {
                    nextVertices.add(next);
                    if(!ignoreIndexes.containsKey(j)) ignoreIndexes.put(j, null);
                }
                
                if(nextVertices.size() == m) break;
                
                else if(j == vertices.size() -1 && nextVertices.size() < m)
                    j = -1;
            }
            
            graph.addVertex(current);
            
            for(Node next : nextVertices)
                graph.addEdge(edgeFactory.create(), current, next,
                directedEdges? EdgeType.DIRECTED : EdgeType.UNDIRECTED);
                       
        }
        
        return graph;
    }

    public int getInitialNodeCount() 
    {
        return initialNodeCount;
    }

    public void setInitialNodeCount(int initialNodeCount) 
    {
        this.initialNodeCount = initialNodeCount;
    }

    public int getNumAddNodes() 
    {
        return numAddNodes;
    }

    public void setNumAddNodes(int numAddNodes)
    {
        this.numAddNodes = numAddNodes;
    }

    public boolean isDirectedEdges()
    {
        return directedEdges;
    }

    public void setDirectedEdges(boolean directedEdges)
    {
        this.directedEdges = directedEdges;
    }
    
    
}
