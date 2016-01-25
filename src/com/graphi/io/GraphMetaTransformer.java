
package com.graphi.io;

import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import org.apache.commons.collections15.Transformer;


public class GraphMetaTransformer implements Transformer<GraphMetadata, Graph<Node, Edge>>
{
    @Override
    public Graph<Node, Edge> transform(GraphMetadata i)
    {
        return new SparseMultigraph<>();
    }
}
