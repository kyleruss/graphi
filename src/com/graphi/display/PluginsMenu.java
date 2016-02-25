//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class PluginsMenu extends JMenu
{
    protected Map<String, JMenuItem> pluginMenuItems;
    
    public PluginsMenu()
    {
        super("Plugins");
        
        pluginMenuItems =   new HashMap<>();
    }
    
    public void addPluginMenuItem(String name, JMenuItem item)
    {
        pluginMenuItems.put(name, item);
        add(item);
        revalidate();
    }
    
    public Map<String, JMenuItem> getPluginMenuItems()
    {
        return pluginMenuItems;
    }
    
    public JMenuItem getPluginMenuItem(String name)
    {
        return pluginMenuItems.get(name);
    }
}
