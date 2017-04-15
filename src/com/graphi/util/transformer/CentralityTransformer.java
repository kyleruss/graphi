//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util.transformer;

import com.graphi.graph.Node;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.List;
import org.apache.commons.collections15.Transformer;

/**
 * A transformer for scaling node size based on their rank in 
 * a vector from a centrality computation (see util.MatrixTools)
 */
public class CentralityTransformer implements Transformer<Node, Shape>
{
    //A list of nodes in order of their rank in the graph
    List<Node> centralNodes;
    
    //The number of ranks to consider scaling size before normalizing the remaining node size
    int numRanks;

    public CentralityTransformer(List<Node> centralNodes, int numRanks)
    {
        this.centralNodes   =   centralNodes;
        this.numRanks       =   numRanks;
    }

    /**
     * Scales a node's size based on it's rank in the vector
     * Scales the size of up to numRanks many nodes
     * @param node The Node's size to scale (should exist in the vector)
     * @return A shape with size scaled appropriate to the passed node's rank in the vector
     */
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
