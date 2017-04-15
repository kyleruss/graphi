//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util.transformer;

import com.graphi.graph.Edge;
import java.text.DecimalFormat;
import org.apache.commons.collections15.Transformer;

/**
 * A simple transformer for the purpose of displaying Edge labels (Edge weight)
 */
public class EdgeLabelTransformer implements Transformer<Edge, String>
{
    //A flag to display or hide edge labels
    private boolean showLabels;

    public EdgeLabelTransformer(boolean showLabels)
    {
        this.showLabels   =   showLabels;
    }

    /**
     * Transforms the Edge into it's a formatted edge weight
     * @param edge The Edge to transform
     * @return A formatted String of the passed edge's weight; An empty String if showLabels is false
     */
    @Override
    public String transform(Edge edge)
    {
        if(showLabels)
        {
            DecimalFormat formatter =   new DecimalFormat("#.##");
            return formatter.format(edge.getWeight());
        }
        
        else return "";
    }
    
    /**
     * Sets the showLabels flag to the passed showLabels
     * @param showLabels True then Edge labels are shown; otherwise they are hidden
     */
    public void showLabels(boolean showLabels)
    {
        this.showLabels =   showLabels;
    }
    
    /**
     * @return The showLabels flag. True if the labels are to be shown; false if they are hidden
     */
    public boolean isShowingLabels()
    {
        return showLabels;
    }
}