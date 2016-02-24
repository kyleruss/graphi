//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import com.graphi.app.AppManager;
import com.graphi.display.layout.MainPanel;
import com.graphi.util.GraphData;

public interface Plugin
{
    public String getPluginName();
    
    public String getPluginDescription();
    
    public MainPanel getPanel();
    
    public void attachPanel(AppManager appManager);
    
    public void passData(GraphData data);
    
    public GraphData getData();
}
