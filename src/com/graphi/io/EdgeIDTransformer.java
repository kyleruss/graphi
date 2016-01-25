
package com.graphi.io;

import com.graphi.util.Edge;
import org.apache.commons.collections15.Transformer;


public class EdgeIDTransformer implements Transformer<Edge, String>
{
    @Override
    public String transform(Edge i) 
    {
        return "" + i.getID();
    }
}
