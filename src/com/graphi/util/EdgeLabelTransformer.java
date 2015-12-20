package com.graphi.util;

import java.text.DecimalFormat;
import org.apache.commons.collections15.Transformer;

public class EdgeLabelTransformer implements Transformer<Edge, String>
{
    private boolean showLabels;

    public EdgeLabelTransformer(boolean showLabels)
    {
        this.showLabels   =   showLabels;
    }

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
    
    public void showLabels(boolean showLabels)
    {
        this.showLabels =   showLabels;
    }
    
    public boolean isShowingLabels()
    {
        return showLabels;
    }
}