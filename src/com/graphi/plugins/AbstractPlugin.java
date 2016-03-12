//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import com.graphi.app.AppManager;
import com.graphi.display.layout.MainPanel;
import com.graphi.util.GraphData;
import java.io.Serializable;

/**
 * A basic serializable, Plugin implementation 
 * Maintains the plugin layout and details
 */
public abstract class AbstractPlugin implements Plugin, Serializable
{
    protected final String name;
    protected final String description;
    protected MainPanel panel;
    
    public AbstractPlugin(String name, String description)
    {
        this.name           =   name;
        this.description    =   description;
    }
    
    /**
     * Implementation should initialize the panel
     * Plugins not using the default MainPanel should override & initialize their own panel
     * @param appManager The parent AppManager that is passed to the MainPanel constructor for attachment
     */
    @Override
    public void attachPanel(AppManager appManager)
    {
        panel   =   new MainPanel(appManager);
    }
    
    /**
     * @return The String name of the plugin
     */
    @Override
    public String getPluginName() 
    {
        return name;
    }

    /**
     * @return The String description of the plugin 
     */
    @Override
    public String getPluginDescription()
    {
        return description;
    }

    /**
     * @return The plugins layout panel; Can be null if bad attachPanel() implementation
     */
    @Override
    public MainPanel getPanel() 
    {
        return panel;
    }
    
    /**
     * Sets the plugin's layout panel active GraphData
     * Useful when switching plugins to pass data among plugins
     * @param data 
     */
    @Override
    public void passData(GraphData data)
    {
        panel.setGraphData(data);
    }
    
    /**
     * @return The plugin's active GraphData
     */
    @Override
    public GraphData getData()
    {
        return panel.getGraphData();
    }
    
    /**
     * Tests if two plugins have the same name
     * Plugin name's are not unique and can be the same for different vendors
     * @param other The other plugin to comapre to
     * @return True if the Plugin's are equal; false otherwise
     */
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
    
    /**
     * @return The plugin name's String hashcode
     */
    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
}
