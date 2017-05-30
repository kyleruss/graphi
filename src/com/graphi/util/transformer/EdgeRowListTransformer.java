//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util.transformer;

import com.graphi.graph.Edge;
import com.graphi.graph.Node;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections15.Transformer;

public class EdgeRowListTransformer implements Transformer<Edge, List>
{
    private Graph<Node, Edge> graph;
    
    public EdgeRowListTransformer()
    {
        graph   =   null;
    }
    
    public EdgeRowListTransformer(Graph<Node, Edge> graph)
    {
        this.graph  =   graph;
    }
    
    @Override
    public List transform(Edge edge)
    {
        List rowList                =   new ArrayList<>();
        
        if(graph != null)
        {
            int eID                     =   edge.getID();
            double weight               =   edge.getWeight();
            Collection<Node> vertices   =   graph.getIncidentVertices(edge);
            String edgeType             =   graph.getEdgeType(edge).toString();
            Node n1, n2;
            int n1_id, n2_id;
            Iterator<Node> iter         =   vertices.iterator();
            n1  =   iter.next();
            n2  =   iter.next();

            if(n1 != null)
                n1_id   =   n1.getID();
            else
                n1_id   =   -1;

            if(n2 != null)
                n2_id   =   n2.getID();
            else
                n2_id   =   -1;


            rowList.add(eID);
            rowList.add(n1_id);
            rowList.add(n2_id);
            rowList.add(weight);
            rowList.add(edgeType);
        }
        
        return rowList;
    }
    
    public void setGraph(Graph<Node, Edge> graph)
    {
        this.graph  =   graph;
    }
}
