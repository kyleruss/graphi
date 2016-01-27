
package com.graphi.util;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import java.util.HashMap;
import java.util.Map;


public class GraphData 
{
    private Graph<Node, Edge> graph;
    private final Map<Integer, Node> nodes;
    private final Map<Integer, Edge> edges;
    private final NodeFactory nodeFactory;
    private final EdgeFactory edgeFactory;
    private Object[] selectedItems;
    
    public GraphData()
    {
        graph           =   new SparseMultigraph<>();
        nodes           =   new HashMap<>();
        edges           =   new HashMap<>();
        nodeFactory     =   new NodeFactory();
        edgeFactory     =   new EdgeFactory();
    }
}
