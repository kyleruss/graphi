
package com.graphi.io;

import com.graphi.util.Edge;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import org.apache.commons.collections15.Transformer;


public class HyperEdgeMetaTransformer implements Transformer<HyperEdgeMetadata, Edge>
{
    @Override
    public Edge transform(HyperEdgeMetadata i)
    {
        Edge edge   =   new Edge(Integer.parseInt(i.getId()), EdgeType.UNDIRECTED);
        return edge;
    }
}
