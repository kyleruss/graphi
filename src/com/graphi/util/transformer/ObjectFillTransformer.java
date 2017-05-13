
package com.graphi.util.transformer;

import com.graphi.config.AppConfig;
import com.graphi.config.ConfigManager;
import com.graphi.graph.Edge;
import com.graphi.graph.GraphObject;
import com.graphi.graph.Node;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import java.awt.Color;
import java.awt.Paint;
import org.apache.commons.collections15.Transformer;

/**
 * A Transformer for graph object fill
 * Maintains selected state &  fills accordingly
 * @param <T> 
 */
public class ObjectFillTransformer<T extends GraphObject> implements Transformer<T, Paint>
{
    //Selected/picked state list for the graph objects
    protected final PickedInfo<T> pickedInfo;
    
    //The default fill colour used when a graph object is selected
    private Color selectedColour;
    
    public ObjectFillTransformer(PickedInfo<T> pickedInfo)
    {
        this.pickedInfo =   pickedInfo;
        selectedColour  =   Color.BLUE;   
    }

    /**
     * Transforms the graph object into a paint fill
     * Uses the selected colour fill if the object is selected
     * Otherwise uses the graph objects fill 
     * @param obj
     * @return 
     */
    @Override
    public Paint transform(T obj)
    {
        if(pickedInfo.isPicked(obj))
            return selectedColour;
        else
        {
            Color objFill       =   obj.getFill();
            AppConfig appConfig =   ConfigManager.getInstance().getAppConfig();
            
            if(objFill != null)
                return obj.getFill();
            
            else if(obj instanceof Node)
                return appConfig.getNodeBackground();
            
            else if(obj instanceof Edge)
                return appConfig.getEdgeBackground();
            
            else return Color.BLACK;
        }
    }
    
    /**
     * @param selectedColour The colour to set the selected fill to
     */
    public void setSelectedColour(Color selectedColour)
    {
        this.selectedColour =   selectedColour;
    }
}
