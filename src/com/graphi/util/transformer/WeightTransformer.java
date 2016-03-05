//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util;

import org.apache.commons.collections15.Transformer;


public class WeightTransformer implements Transformer<Edge, Double>
{
    @Override
    public Double transform(Edge edge) 
    {
        return 1.0 - (edge.getWeight() / 100.0);
    }
}
