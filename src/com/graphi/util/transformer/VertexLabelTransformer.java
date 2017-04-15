//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util.transformer;

import com.graphi.graph.Node;
import org.apache.commons.collections15.Transformer;

/**
 * A simple transformer for transforming Node's into their name labels
 */
public class VertexLabelTransformer implements Transformer<Node, String>
{
    //A flag indicating the display of Node labels
    private boolean showLabels;
    
    public VertexLabelTransformer(boolean showLabels)
    {
        this.showLabels =   showLabels;
    }

    /**
     * Transforms a node into a String that is it's name
     * @param node The Node to transform
     * @return if the showLabels flag is true then returns the passed Node's name; otherwise empty String
     */
    @Override
    public String transform(Node node)
    {
        if(showLabels)
            return "" + node.getID();
        else
            return "";
    }
    
    /**
     * @return The showLabels flag indiciating if the vertex labels are visible
     */
    public boolean isShowingLabels()
    {
        return showLabels;
    }
    
    /**
     * @param showLabels Pass true to show labels; false to hide
     */
    public void showLabels(boolean showLabels)
    {
        this.showLabels =   showLabels;
    }
}
