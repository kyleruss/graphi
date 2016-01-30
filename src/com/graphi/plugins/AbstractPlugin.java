//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import com.graphi.display.LayoutPanel;
import com.graphi.display.Window;

public abstract class AbstractPlugin implements Plugin
{
    protected final String name;
    protected final String description;
    protected LayoutPanel panel;
    
    public AbstractPlugin(String name, String description)
    {
        this.name           =   name;
        this.description    =   description;
    }
    
    public abstract void attachPanel(Window window);
    
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
