//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import com.graphi.util.Node;
import org.apache.commons.collections15.Transformer;

/**
 * A tranformer for transforming a Node to a GML formatted node String
 */
public class GMLNodeTransformer implements Transformer<Node, String>
{
    /**
     * Transform the node to a GML formatted node String
     * @param node The Node to transform
     * @return A GML String of the passed Node
     * @see <a href="https://en.wikipedia.org/wiki/Graph_Modelling_Language">GML Wiki</a>
     */
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
