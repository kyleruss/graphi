//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.sim.generator;

import static com.graphi.sim.Network.generateConnectedGraph;
import com.graphi.util.Edge;
import com.graphi.util.GraphUtilities;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import org.apache.commons.collections15.Factory;

public class BerbasiGenerator extends AbstractGenerator
{
    private int initialNodeCount;
    private int numAddNodes;
    private boolean directedEdges;
    
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
        
        Graph<Node, Edge> graph                     =   generateConnectedGraph(nodeFactory, edgeFactory, m);
        
        
        for(int i = 0; i < n; i++)
        {
            Node current    =   nodeFactory.create();
            
            PriorityQueue<AbstractMap.SimpleEntry<Node, Double>> nextVerticies =   new PriorityQueue<>
                ((AbstractMap.SimpleEntry<Node, Double> a1, AbstractMap.SimpleEntry<Node, Double> a2) 
                -> Double.compare(a1.getValue(), a2.getValue()));
            
            ArrayList<Node> vertices    =   new ArrayList<>(graph.getVertices());
            Random rGen                 =   new Random();
            
            while(!vertices.isEmpty())
            {
                int index   =   rGen.nextInt(vertices.size());
                Node next   =   vertices.get(index);
                int degree      =   (directedEdges)? graph.inDegree(next) : graph.degree(next);
                int degreeSum   =   GraphUtilities.degreeSum(graph);
                double p        =   degree / (degreeSum * 1.0);

                if(nextVerticies.size() < m)
                    nextVerticies.add(new AbstractMap.SimpleEntry<> (next, p));
                
                else if(p > nextVerticies.peek().getValue())
                {
                    nextVerticies.poll();
                    nextVerticies.add(new AbstractMap.SimpleEntry<> (next, p));
                }
                
                vertices.remove(index);
            }
            
            graph.addVertex(current);
            
            while(!nextVerticies.isEmpty())
                graph.addEdge(edgeFactory.create(), current, nextVerticies.poll().getKey(), directedEdges? EdgeType.DIRECTED : EdgeType.UNDIRECTED);
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
