//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import com.graphi.util.Node;
import org.apache.commons.collections15.Transformer;

public class NodeIDTransformer implements Transformer<Node, String>
{
    @Override
    public String transform(Node i) 
    {
        return "" + i.getID();
    }
}
