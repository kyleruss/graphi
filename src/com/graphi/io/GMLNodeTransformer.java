
package com.graphi.io;

import com.graphi.util.Node;
import org.apache.commons.collections15.Transformer;


public class GMLNodeTransformer implements Transformer<Node, String>
{
    @Override
    public String transform(Node node)
    {
        String output   =   "";
        output  +=  "node [\n";
        output  +=  "id " + node.getID() + "\n";
        output  +=  "label " + node.getName() + "\n]\n";
        
        return output;
    }
    
}
