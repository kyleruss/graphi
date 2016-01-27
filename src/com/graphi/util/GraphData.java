
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
    
    public Graph<Node, Edge> getGraph()
    {
        return graph;
    }
    
    public Map<Integer, Node> getNodes()
    {
        return nodes;
    }
    
    public Map<Integer, Edge> getEdges()
    {
        return edges;
    }
    
    public NodeFactory getNodeFactory()
    {
        return nodeFactory;
    }
    
    public EdgeFactory getEdgeFactory()
    {
        return edgeFactory;
    }
    
    public Object[] getSelectedItems()
    {
        return selectedItems;
    }
 
    public void setSelectedItems(Object[] selectedItems)
    {
        this.selectedItems  =   selectedItems;
    }
    
    public void setGraph(Graph<Node, Edge> graph)
    {
        this.graph  =   graph;
    }
}
