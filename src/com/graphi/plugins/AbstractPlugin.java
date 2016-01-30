//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import com.graphi.display.LayoutPanel;

public abstract class AbstractPlugin implements Plugin
{
    private final String name;
    private final String description;
    private final LayoutPanel panel;
    
    public AbstractPlugin(String name, String description, LayoutPanel panel)
    {
        this.name           =   name;
        this.description    =   description;
        this.panel          =   panel;
    }
    
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
    public LayoutPanel getPanel() 
    {
        return panel;
    }
}
