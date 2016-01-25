
package com.graphi.io;

import com.graphi.util.Edge;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import org.apache.commons.collections15.Transformer;


public class EdgeMetaTransformer implements Transformer<EdgeMetadata, Edge>
{
    @Override
    public Edge transform(EdgeMetadata i)
    {
        Edge edge   =   new Edge(Integer.parseInt(i.getId()), i.isDirected()? EdgeType.DIRECTED : EdgeType.UNDIRECTED);
        return edge;
    }
}
