
package com.graphi.display.layout.util;

import com.graphi.display.PluginsMenu;
import com.graphi.plugins.PluginManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

public class PluginMenuListener implements ActionListener
{
    private final PluginsMenu pluginMenu;
    private final PluginManager pluginManager;

    public PluginMenuListener(PluginsMenu pluginMenu, PluginManager pluginManager)
    {
        this.pluginMenu     =   pluginMenu;
        this.pluginManager  =   pluginManager;
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JMenuItem item      =   (JMenuItem) e.getSource();
        String pluginName   =   item.getText();
        pluginManager.activePlugin(pluginName);
    }
    
    public PluginsMenu getPluginMenu() 
    {
        return pluginMenu;
    }

    public PluginManager getPluginManager() 
    {
        return pluginManager;
    }
}