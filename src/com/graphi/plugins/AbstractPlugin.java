//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import java.io.Serializable;
import java.net.URLClassLoader;

/**
 * A basic serializable, Plugin implementation 
 * Maintains the plugin layout and details
 */
public abstract class AbstractPlugin implements Plugin, Serializable
{
    protected String name;
    protected String description;
    protected PluginDisplayHandler displayHandler;
    private URLClassLoader loader;

    public AbstractPlugin()
    {
        initPluginDetails();
    }
    
    public abstract void initPluginDetails();
    
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

    @Override
    public URLClassLoader getLoader() 
    {
        return loader;
    }

    @Override
    public void setLoader(URLClassLoader loader)
    {
        this.loader = loader;
    }
    
    @Override
    public PluginDisplayHandler getDisplayHandler()
    {
        return displayHandler;
    }
    
    public void setDisplayHandler(PluginDisplayHandler displayHandler)
    {
        this.displayHandler =   displayHandler;
    }
    
    public abstract void onPluginLoad();
    
    public abstract void onPluginActivate();
    
    public abstract void onPluginDeactivate();
    
    public void onPluginDisplayShow()
    {
        if(displayHandler != null)
            displayHandler.attachDisplay();
    }
    
    public void onPluginDisplayHide()
    {
        if(displayHandler != null)
            displayHandler.destroyDisplay();
    }
    
    @Override
    public void onEvent(int eventCode)
    {
        switch (eventCode) 
        {
            case Plugin.ONACTIVATE_EVENT:
                onPluginActivate();
                break;
                
            case Plugin.ONDEACTIVATE_PLUGIN_EVENT:
                onPluginDeactivate();
                break;
                
            case Plugin.ONLOAD_EVENT:
                onPluginLoad();
                break;
                
            case Plugin.ONSHOW_DISPLAY_EVENT:
                onPluginDisplayShow();
                break;
                
            case Plugin.ONDESTROY_DISPLAY_EVENT:
                onPluginDisplayHide();
                break;
                
            default: break;
        }
            
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
