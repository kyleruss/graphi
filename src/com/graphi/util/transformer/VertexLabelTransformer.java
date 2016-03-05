//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util.transformer;

import com.graphi.util.Node;
import org.apache.commons.collections15.Transformer;

public class VertexLabelTransformer implements Transformer<Node, String>
{
    private boolean showLabels;
    
    public VertexLabelTransformer(boolean showLabels)
    {
        this.showLabels =   showLabels;
    }

    @Override
    public String transform(Node node)
    {
        if(showLabels)
            return node.getName();
        else
            return "";
    }
    
    public boolean isShowingLabels()
    {
        return showLabels;
    }
    
    public void showLabels(boolean showLabels)
    {
        this.showLabels =   showLabels;
    }
}
