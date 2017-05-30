//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util.transformer;

import com.graphi.graph.Node;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections15.Transformer;

public class NodeRowListTransformer implements Transformer<Node, List>
{
    @Override
    public List transform(Node node) 
    {
        int vID         =   node.getID();
        String vName    =   node.getName();
        List rowList    =   new ArrayList<>();
        
        rowList.add(vID);
        rowList.add(vName);
        return rowList;
    }
}
