//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import com.graphi.graph.Node;
import org.apache.commons.collections15.Transformer;

/**
 * A simple transformer to transform Node's to their ID string
 */
public class NodeIDTransformer implements Transformer<Node, String>
{
    /**
     * Transforms a Node into a String representation of it's ID
     * @param v The node to transform
     * @return The String of the passed Node ID
     */
    @Override
    public String transform(Node v) 
    {
        return "" + v.getID();
    }
}
