
package com.graphi.sim;

import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
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
                int degreeSum   =   degreeSum(graph);
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
    
    public static void groupCluster(AggregateLayout layout, Set<Node> cluster)
    {
        Point2D center              =   layout.transform(cluster.iterator().next());
        Graph<Node, Edge> sGraph    =   SparseMultigraph.<Node, Edge>getFactory().create();

        for(Node node : cluster)
            sGraph.addVertex(node);

        Layout subLayout =   new CircleLayout(sGraph);
        subLayout.setSize(new Dimension(50, 50));
        layout.put(subLayout, center);
    }
    
    public static void cluster(AggregateLayout layout, Graph graph, int numRemoved, boolean group)
    {
        EdgeBetweennessClusterer clusterer = new EdgeBetweennessClusterer(numRemoved);
       Set<Set<Node>> clusterSet  = clusterer.transform(graph); 
       System.out.println("cluster size: " + clusterSet.size());

         Color[] colors = { Color.GREEN, Color.RED, Color.YELLOW, Color.BLACK, Color.MAGENTA, Color.BLUE, Color.GRAY };
         int colorIndex =   0;
        
        layout.reset();
        
        if(clusterSet.size() > 1)
        {
            for(Set<Node> set : clusterSet)
            {
                if(group) groupCluster((AggregateLayout) layout, set);
                
                Color setColor = colors[++colorIndex % colors.length];

                for(Node node : set)
                    node.setColor(setColor);
            } 
        } 
    }
    
    public static int degreeSum(Graph<Node, Edge> graph)
    {
        Collection<Node> vertices   =   graph.getVertices();
        int sum =  0;
        
        for(Node node : vertices)
            sum += graph.degree(node);
        
        return sum;
    }
}
