//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import com.graphi.display.LayoutPanel;
import com.graphi.display.Window;
import com.graphi.util.GraphData;

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
    
    @Override
    public void passData(GraphData data)
    {
        panel.setGraphData(data);
    }
    
    @Override
    public boolean equals(Object other)
    {
        if(other instanceof AbstractPlugin)
        {
            AbstractPlugin otherPlugin   =   (AbstractPlugin) other;
            return this.getPluginName().equalsIgnoreCase(otherPlugin.getPluginName());
        }
        
        else return false;
    }
    
    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
}
