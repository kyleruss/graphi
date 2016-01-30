//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import com.graphi.display.Window;
import com.graphi.plugins.def.DefaultPlugin;

public class PluginManager
{
    private AbstractPlugin activePlugin;
    private final Window window;

    public PluginManager(Window window)
    {
        this.window     =   window;
        activePlugin    =   new DefaultPlugin();
        activePlugin.attachPanel(window);
    }
    
    public Window getWindow()
    {
        return window;
    }
    
    public AbstractPlugin getActivePlugin()
    {
        return activePlugin;
    }
    
    public void setActivePlugin(AbstractPlugin plugin)
    {
        this.activePlugin   =   plugin;
    }
}
