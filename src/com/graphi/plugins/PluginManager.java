//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import com.graphi.display.Window;
import com.graphi.plugins.st.StalkingPlugin;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

public class PluginManager
{
    private AbstractPlugin activePlugin;
    private final Window window;
    private Map<String, AbstractPlugin> plugins;

    public PluginManager(Window window)
    {
        this.window     =   window;
        
        AbstractPlugin defaultPlugin    =   new StalkingPlugin();
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
    
    public Map<String, AbstractPlugin> getPlugins()
    {
        return plugins;
    }
    
    public void setPlugins(Map<String, AbstractPlugin> plugins)
    {
        this.plugins    =   plugins;
    }
    
    public void activatePlugin(AbstractPlugin plugin)
    {
        if(plugin == null) return;

        if(activePlugin != null) 
            plugin.passData(activePlugin.getData());
        
        activePlugin   =   plugin;
        activePlugin.attachPanel(window);
        JFrame frame    =   window.getFrame();
        frame.getContentPane().removeAll();
        frame.add(activePlugin.getPanel());
        frame.revalidate();
    }
    
    public void activePlugin(String pluginName)
    {
        AbstractPlugin plugin   =   plugins.get(pluginName);
        
        if(plugin != null)
            activatePlugin(plugin);
    }
}
