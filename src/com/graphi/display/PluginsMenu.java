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

public class PluginsMenu extends JMenu
{
    protected Map<String, JMenuItem> pluginMenuItems;
    protected JMenuItem activePluginItem;
    protected BufferedImage activeTickIcon;
    
    public PluginsMenu()
    {
        super("Plugins");
        
        pluginMenuItems =   new HashMap<>();
        addPluginMenuItem("defaultPluginItem", new JMenuItem("Default"));
        
        try
        {
            activeTickIcon  =   ImageIO.read(new File("resources/images/tick.png"));
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to load resource: " + e.getMessage());
        }
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
