
package com.graphi.sim;

import com.graphi.test.Edge;
import com.graphi.test.Node;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import org.apache.commons.collections15.Factory;


public class Network
{
    public Graph<Node, Edge> generateKleinberg(int latticeSize, int clusterExp)
    {
        Factory<Node> nodeFactory                   =   () -> new Node();
        Factory<Edge> edgeFactory                   =   () -> new Edge();
        Factory<Graph<Node, Edge>> graphFactory     =   () -> new SparseMultigraph<>();   
        
        KleinbergSmallWorldGenerator gen            =   new KleinbergSmallWorldGenerator(graphFactory, nodeFactory, edgeFactory, latticeSize, clusterExp);
        return gen.create();
    }
}
