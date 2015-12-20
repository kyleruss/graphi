//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util;

import edu.uci.ics.jung.visualization.picking.PickedInfo;
import java.awt.Color;
import java.awt.Paint;
import org.apache.commons.collections15.Transformer;

public class VertexColourTransformer implements Transformer<Node, Paint>
{
    private final PickedInfo<Node> pickedInfo;
    private Color selectedColour;
    
    public VertexColourTransformer(PickedInfo<Node> pickedInfo)
    {
        this.pickedInfo =   pickedInfo;
        selectedColour  =   Color.YELLOW;   
    }

    @Override
    public Paint transform(Node node)
    {
        if(pickedInfo.isPicked(node))
            return selectedColour;
        else
            return node.getColor();
    }
    
    public void setSelectedColour(Color selectedColour)
    {
        this.selectedColour =   selectedColour;
    }
}
