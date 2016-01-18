//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.sim;

import com.graphi.util.Edge;
import com.graphi.util.GraphUtilities;
import com.graphi.util.Node;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Random;
import org.apache.commons.collections15.Factory;


public class Network
{
    public static Graph<Node, Edge> generateKleinberg(int latticeSize, int clusterExp, Factory<Node> nodeFactory, Factory<Edge> edgeFactory)
    {
        if(nodeFactory == null) nodeFactory         =   () -> new Node();
        if(edgeFactory == null) edgeFactory         =   () -> new Edge();
        
        Factory<Graph<Node, Edge>> graphFactory     =   () -> new SparseMultigraph<>();   
        
        KleinbergSmallWorldGenerator gen            =   new KleinbergSmallWorldGenerator(graphFactory, nodeFactory, edgeFactory, latticeSize, clusterExp);
        return gen.create();
    }
    
    public static Graph<Node, Edge> generateBerbasiAlbert(Factory<Node> nodeFactory, Factory<Edge> edgeFactory, int n, int m)
    {
        Graph<Node, Edge> graph                 =   generateConnectedGraph(nodeFactory, edgeFactory, m);
        
        
        for(int i = 0; i < n; i++)
        {
            Node current    =   nodeFactory.create();
            
            PriorityQueue<SimpleEntry<Node, Double>> nextVerticies =   new PriorityQueue<>
                ((SimpleEntry<Node, Double> a1, SimpleEntry<Node, Double> a2) 
                -> Double.compare(a1.getValue(), a2.getValue()));
            
            ArrayList<Node> vertices    =   new ArrayList<>(graph.getVertices());
            Random rGen                 =   new Random();
            
            while(!vertices.isEmpty())
            {
                int index   =   rGen.nextInt(vertices.size());
                Node next   =   vertices.get(index);
                int degree      =   graph.degree(next);
                int degreeSum   =   GraphUtilities.degreeSum(graph);
                double p        =   degree / (degreeSum * 1.0);

                if(nextVerticies.size() < m)
                    nextVerticies.add(new SimpleEntry<> (next, p));
                
                else if(p > nextVerticies.peek().getValue())
                {
                    nextVerticies.poll();
                    nextVerticies.add(new SimpleEntry<> (next, p));
                }
                
                vertices.remove(index);
            }
            
            graph.addVertex(current);
            
            while(!nextVerticies.isEmpty())
                graph.addEdge(edgeFactory.create(), current, nextVerticies.poll().getKey());
        }
        
        return graph;
    }
    
    public static Graph<Node, Edge> generateConnectedGraph(Factory<Node> nodeFactory, Factory<Edge> edgeFactory, int n)
    {
        Graph<Node, Edge> graph =   new SparseMultigraph<>();
        for(int i = 0; i < n; i++)
            graph.addVertex(nodeFactory.create());
        
        ArrayList<Node> vertices    =   new ArrayList<>(graph.getVertices());
        for(int i = 1; i < n; i++)
            graph.addEdge(edgeFactory.create(), vertices.get(i - 1), vertices.get(i));
        
        return graph;
    }
    
    public static double getITProbability(Graph<Node, Edge> graph, Node from, Node to)
    {
        if(!graph.isNeighbor(from, to)) return 0.0;
        
        int n                       =   0;
        int degree                  =   graph.degree(from);
        Collection<Node> neighbours =   graph.getNeighbors(from);
        
        for(Node neighbour : neighbours)
        {
            if(!neighbour.equals(to) && graph.isNeighbor(neighbour, to))
                n++;
        }
        
        return n / (degree * 1.0);
    }
    
    public static void simulateInterpersonalTies(Graph<Node, Edge> graph, Factory<Edge> edgeFactory)
    {
        Collection<Node> nodes  =   graph.getVertices();
        final double P          =   0.5;
        
        for(Node node: nodes)
        {
            Collection<Node> neighbours = graph.getNeighbors(node);
            
            for(Node neighbour : neighbours)
            {
                double p    =   getITProbability(graph, node, neighbour);
                if(p >= P)
                {
                    Edge edge   =   edgeFactory.create();
                    graph.addEdge(edge, node, neighbour, EdgeType.DIRECTED);
                }
            }
        }
    }
}
