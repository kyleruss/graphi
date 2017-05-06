//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import java.util.List;

public interface PluginDisplayHandler 
{
    public void initPluginDisplay();
    
    public void attachDisplay();
    
    public void destroyDisplay();
    
    public List<String> getPluginResources();
}
