//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import com.graphi.display.Window;
import com.graphi.plugins.def.DefaultPlugin;
import java.util.HashMap;
import java.util.Map;

public class PluginManager
{
    private AbstractPlugin activePlugin;
    private final Window window;
    private Map<String, AbstractPlugin> plugins;

    public PluginManager(Window window)
    {
        this.window     =   window;
        
        AbstractPlugin defaultPlugin    =   new DefaultPlugin();
        activatePlugin(defaultPlugin);
        
        plugins =   new HashMap<>();
        plugins.put(activePlugin.getPluginName(), activePlugin);
    }
    
    public Window getWindow()
    {
        return window;
    }
    
    public AbstractPlugin getActivePlugin()
    {
        return activePlugin;
    }
    
    public void activatePlugin(AbstractPlugin plugin)
    {
        this.activePlugin   =   plugin;
        activePlugin.attachPanel(window);
    }
    
    public void activePlugin(String pluginName)
    {
        AbstractPlugin plugin   =   plugins.get(pluginName);
        
        if(plugin != null)
            activatePlugin(plugin);
    }
}
