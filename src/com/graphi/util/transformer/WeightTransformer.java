//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util.transformer;

import com.graphi.graph.Edge;
import org.apache.commons.collections15.Transformer;


/**
 * A simple transformer for transforming Edge's into their edge weight
 */
public class WeightTransformer implements Transformer<Edge, Double>
{
    /**
     * Transforms the passed Edge into it's Double edge weight
     * @param edge The edge to transform
     * @return The transformed Edge weight
     */
    @Override
    public Double transform(Edge edge) 
    {
        return edge.getWeight();
    }
}
