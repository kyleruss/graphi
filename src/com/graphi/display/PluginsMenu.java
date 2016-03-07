//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * A sub menu for display.MainMenu
 * Contains options for importing plugins & a plugin list
 * Loaded & active plugin names are maintained for later usage 
 */

public class PluginsMenu extends JMenu
{
    //The menu items of the current loaded plugins
    protected Map<String, JMenuItem> pluginMenuItems;
    
    //The active/current plugin menu item
    protected JMenuItem activePluginItem;
    
    //A tick icon used to identify the active plugin menu item
    protected BufferedImage activeTickIcon;
    
    public PluginsMenu()
    {
        super("Plugins");
        
        pluginMenuItems =   new HashMap<>();
        addPluginMenuItem("defaultPluginItem", new JMenuItem("Default"));
        
        try { activeTickIcon  =   ImageIO.read(new File("resources/images/tick.png")); }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to load resource: " + e.getMessage());
        }
    }
    
    /**
     * Adds plugin menu item to the menu and to the pluginMenuItems map
     * @param name Name of the plugin or key of the plugin menu item in map
     * @param item An item to add to the menu
     */
    public void addPluginMenuItem(String name, JMenuItem item)
    {
        pluginMenuItems.put(name, item);
        add(item);
        revalidate();
    }
    
    /**
     * Returns the plugin menu items map where each entry key is
     * the plugin name & the value is the plugins added JMenuItem
     * @return A map containing plugin menu items
     */
    public Map<String, JMenuItem> getPluginMenuItems()
    {
        return pluginMenuItems;
    }
    
    /**
     * Returns a menu item for the plugin name
     * @param name The plugin name or key in the plugin menu items map used in addPluginMenuItem()
     * @return The JMenuItem for the plugin if found; otherwise null
     */
    public JMenuItem getPluginMenuItem(String name)
    {
        return pluginMenuItems.get(name);
    }
    
    /**
     * Makes the passed item the active/current plguin item
     * The same plugin item cannot be reactivated and displays an error if this happens
     * Sets activePluginItem to the item and changes the active tick icon to item
     * @param item The item to make active - should not already be active
     */
    public void setActivePluginItem(JMenuItem item)
    {
        if(activePluginItem != null)
        {
            if(activePluginItem == item)
            {
                JOptionPane.showMessageDialog(null, "This plugin is already active");
                return;
            }

            activePluginItem.setIcon(null);
        }

        activePluginItem    =   item;
        activePluginItem.setIcon(new ImageIcon(activeTickIcon));
    }
}
