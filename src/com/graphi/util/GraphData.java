
package com.graphi.util;

import com.graphi.util.factory.EdgeFactory;
import com.graphi.util.factory.NodeFactory;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import java.util.HashMap;
import java.util.Map;


public class GraphData 
{
    private Graph<Node, Edge> graph;
    private final Map<Integer, Node> nodes;
    private final Map<Integer, Edge> edges;
    private NodeFactory nodeFactory;
    private EdgeFactory edgeFactory;
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

    public void setNodeFactory(NodeFactory nodeFactory) 
    {
        this.nodeFactory = nodeFactory;
    }

    public void setEdgeFactory(EdgeFactory edgeFactory) 
    {
        this.edgeFactory = edgeFactory;
    }
}
