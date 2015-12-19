
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
        Graph<Node, Edge> graph =   generateConnectedGraph(nodeFactory, edgeFactory, m);
       /* Node n1 =   nodeFactory.create();
        Node n2 =   nodeFactory.create();
        graph.addVertex(n1);
        graph.addVertex(n2);
        graph.addEdge(edgeFactory.create(), n1, n2); */
        
        
        
        for(int i = 0; i < n; i++)
        {
            Node current    =   nodeFactory.create();
            //graph.addVertex(current);
            
            ArrayList<Node> vertices    =   new ArrayList<>(graph.getVertices());
            Random rGen                 =   new Random();
            Node next1 = null;
            Node next2 = null;
            double n1p  =   0.0;
            double n2p  =   0.0;
            
            while((next1 == null || next2 == null) && !vertices.isEmpty())
            {
                int index   =   rGen.nextInt(vertices.size());
                Node next   =   vertices.get(index);
                
                int degree      =   graph.degree(next);
                int degreeSum   =   degreeSum(graph);
                double p        =   degree / (degreeSum * 1.0);

                if(next1 == null || p > n1p)
                {
                    next1   =   next;
                    n1p     =   p;
                }

                else if(next2 ==  null || p > n2p)
                {
                    next2   =   next;
                    n2p     =   p;
                }
                
                vertices.remove(index);
            }
            
            System.out.println("Add vertex");
            graph.addVertex(current);
            graph.addEdge(edgeFactory.create(), current, next1);
            graph.addEdge(edgeFactory.create(), current, next2);
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
