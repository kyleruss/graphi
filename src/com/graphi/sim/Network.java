
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
import java.util.Set;
import org.apache.commons.collections15.Factory;


public class Network
{
    public static Graph<Node, Edge> generateKleinberg(int latticeSize, int clusterExp)
    {
        Factory<Node> nodeFactory                   =   () -> new Node();
        Factory<Edge> edgeFactory                   =   () -> new Edge();
        Factory<Graph<Node, Edge>> graphFactory     =   () -> new SparseMultigraph<>();   
        
        KleinbergSmallWorldGenerator gen            =   new KleinbergSmallWorldGenerator(graphFactory, nodeFactory, edgeFactory, latticeSize, clusterExp);
        return gen.create();
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
}
