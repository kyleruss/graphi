//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.util.Iterator;
import org.apache.commons.collections15.Transformer;

public class GMLEdgeTransformer implements Transformer<Edge, String>
{
    private Graph<Node, Edge> graph;
    
    public GMLEdgeTransformer(Graph<Node, Edge> graph)
    {
        this.graph  =   graph;
    }
    
    @Override
    public String transform(Edge edge)
    {
        String output   =   "";
        Node n1, n2;
            
        if(graph.getEdgeType(edge) == EdgeType.DIRECTED)
        {
            n1  =   graph.getSource(edge);
            n2  =   graph.getDest(edge);
        }

        else
        {
            Iterator<Node> iNodeIter =   graph.getIncidentVertices(edge).iterator();
            n1  =   iNodeIter.next();
            n2  =   iNodeIter.next();
        }

        output  +=  "edge [\n";
        output  +=  "source " + n1.getID() + "\n";
        output  +=  "target " + n2.getID() + "\n";
        output  +=  "weight " + edge.getWeight() + "\n]\n";
        
        return output;
    }
}