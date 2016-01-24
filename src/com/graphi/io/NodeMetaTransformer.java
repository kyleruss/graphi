
package com.graphi.io;

import com.graphi.util.Node;
import edu.uci.ics.jung.io.graphml.NodeMetadata;
import org.apache.commons.collections15.Transformer;


public class NodeMetaTransformer implements Transformer<NodeMetadata, Node>
{

    @Override
    public Node transform(NodeMetadata i)
    {
        Node node   =   new Node(Integer.parseInt(i.getId()));
        return node;
    }
}
