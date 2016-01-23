//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import java.io.File;
import org.apache.commons.collections15.Factory;


public class GMLParser 
{
    public static Graph<Node, Edge> importGraph(File file, Factory<Node> nodeFactory, Factory<Edge> edgeFactory)
    {
        Graph<Node, Edge> graph =   new SparseMultigraph<>();
        return graph;
    }
    
    public static void exportGraph(Graph<Node, Edge> graph, File file, boolean directed)
    {
        
    }
}
