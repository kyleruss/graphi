//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.List;
import org.apache.commons.collections15.Transformer;

public class CentralityTransformer implements Transformer<Node, Shape>
{
    List<Node> centralNodes;
    int numRanks;

    public CentralityTransformer(List<Node> centralNodes, int numRanks)
    {
        this.centralNodes   =   centralNodes;
        this.numRanks       =   numRanks;
    }

    @Override
    public Shape transform(Node node) 
    {

        for(int i = 0; i < numRanks; i++)
        {
            if(node.equals(centralNodes.get(i)))
            {
                int size    =   20 + ((numRanks - i) * 10);
                return new Ellipse2D.Double(-10, -10, size, size);
            }
        }

        return new Ellipse2D.Double(-10, -10, 20, 20);
    }
}
