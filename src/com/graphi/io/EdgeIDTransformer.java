//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import com.graphi.util.Edge;
import org.apache.commons.collections15.Transformer;

/**
 * A simple transformer for Edge's to String
 * Transform's the Edge into it's ID string
 */
public class EdgeIDTransformer implements Transformer<Edge, String>
{
    /**
     * Transforms an Edge into a String of it's ID
     * @param e The Edge to transformed
     * @return A String representation of the passed Edge ID
     */
    @Override
    public String transform(Edge e) 
    {
        return "" + e.getID();
    }
}
