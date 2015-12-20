
package com.graphi.util;

import edu.uci.ics.jung.visualization.picking.PickedInfo;
import java.awt.Color;
import java.awt.Paint;
import org.apache.commons.collections15.Transformer;


public class ObjectFillTransformer<T extends GraphObject> implements Transformer<T, Paint>
{
    private final PickedInfo<T> pickedInfo;
    private Color selectedColour;
    
    public ObjectFillTransformer(PickedInfo<T> pickedInfo)
    {
        this.pickedInfo =   pickedInfo;
        selectedColour  =   Color.YELLOW;   
    }

    @Override
    public Paint transform(T obj)
    {
        if(pickedInfo.isPicked(obj))
            return selectedColour;
        else
            return obj.getFill();
    }
    
    public void setSelectedColour(Color selectedColour)
    {
        this.selectedColour =   selectedColour;
    }
}
