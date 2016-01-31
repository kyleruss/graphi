//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import com.graphi.display.LayoutPanel;
import com.graphi.util.GraphData;

public interface Plugin
{
    public String getPluginName();
    
    public String getPluginDescription();
    
    public LayoutPanel getPanel();
    
    public void passData(GraphData data);
    
    public GraphData getData();
}
