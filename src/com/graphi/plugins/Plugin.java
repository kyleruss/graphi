//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import com.graphi.display.layout.MainPanel;
import com.graphi.graph.GraphData;
import java.net.URLClassLoader;

/**
 * An abstract definition for a plugin bean
 * Implemenation should preserve name, description and panel fields
 * Also should permit message passing between plugins
 */
public interface Plugin
{
    /**
     * Return the produced or maintained  plugin name
     * The plugin name should be distinguishable and relevant to the function of the plugin
     * @return A String of the Plugin name 
     */
    public String getPluginName();
    
    /**
     * Return the produced or maintained plugin description
     * The plugin description briefly describes the function of the plugin
     * @return A String of the Plugin description
     */
    public String getPluginDescription();
    
    
    /**
     * Set the plugin class loader
     * @param loader The class loader
     */
    public void setLoader(URLClassLoader loader);
    
    /**
     * @return The plugins class loader
     */
    public URLClassLoader getLoader();
}
