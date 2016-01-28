//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import com.graphi.display.LayoutPanel;


public class AbstractPlugin implements Plugin
{
    private String name;
    private String description;
    private LayoutPanel panel;
    
    @Override
    public String getPluginName() 
    {
        return name;
    }

    @Override
    public String getPluginDescription()
    {
        return description;
    }

    @Override
    public LayoutPanel load() 
    {
        return panel;
    }
}
