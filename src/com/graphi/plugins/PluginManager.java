//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import com.graphi.config.PluginConfig;
import com.graphi.app.AppManager;
import com.graphi.config.ConfigManager;
import com.graphi.display.menu.MainMenu;
import com.graphi.display.menu.PluginsMenu;
import com.graphi.display.Window;
import com.graphi.display.layout.ViewPort;
import com.graphi.graph.GraphData;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.JMenuItem;

public final class PluginManager
{
    private static PluginManager instance;
    
    //Current plugin in use
    private Plugin activePlugin;
    
    //Table of plugins
    //Key: plugin name
    //Value: plugin instance
    private Map<String, Plugin> plugins;

    private PluginManager()
    {
        plugins             =   new HashMap<>();
        MainMenu.getInstance().getPluginListMenu().initPluginMenuListener(this);
        initDefaultPlugin();
    }
    
    //Initializes the default in the application
    //Uses the user specified default or the base plugin
    private void initDefaultPlugin()
    {
        AbstractPlugin basePlugin       =   new DefaultPlugin();
        addPlugin(basePlugin);
        MainMenu.getInstance().getPluginListMenu().addPluginMenuItem("defaultPluginItem", new JMenuItem("Default"));
        
        Plugin defaultPlugin;
        PluginConfig config             =   ConfigManager.getInstance().getPluginConfig();
        int defaultPluginIndex          =   config.getDefaultPluginIndex();
        
        //No user specified default, use base
        if(defaultPluginIndex == -1)
            defaultPlugin = basePlugin;
        
        //Use user specified default plugin
        else
        {
            List<String> pluginPaths    =   config.getLoadedPluginPaths();
            String defaultPluginPath    =   pluginPaths.get(defaultPluginIndex);
            defaultPlugin               =   fetchPlugin(new File(defaultPluginPath));
            
            if(defaultPlugin == null)
                defaultPlugin = basePlugin;
            
            else
                addPlugin(defaultPlugin);
        }

        ViewPort.getInstance().getPluginPanel().initConfig();
        //Window.getInstance().getMenu().getPluginListMenu().loadConfigPlugins(this, appManager);
        activatePlugin(defaultPlugin);
    }
    
    public Plugin fetchPlugin(File file)
    {
        if(file == null) return null;
        
        try
        {
            URL url                         =   file.toURI().toURL();
            URLClassLoader loader           =   new URLClassLoader(new URL[] { url }, this.getClass().getClassLoader());
            JarFile jar                     =   new JarFile(url.getFile());
            Enumeration<JarEntry> entries   =   jar.entries();

            while(entries.hasMoreElements())
            {
                JarEntry entry  =   entries.nextElement();
                String name = entry.getName();

                if(name.endsWith(".class"))
                {
                    name                        =   name.replaceAll("/", ".").replace(".class", "");
                    Class<?> loadedClass        =   loader.loadClass(name);
                    Class<?>[] loadedInterfaces =   loadedClass.getInterfaces();
                    
                    if(loadedInterfaces.length == 0)
                    {
                        Class<?> loadedSuper    =   loadedClass.getSuperclass();
                        
                        if(loadedSuper != null && loadedSuper.getName().equals(AbstractPlugin.class.getName()))
                        {
                            Plugin plugin   =   (Plugin) loadedClass.newInstance();
                            plugin.setLoader(loader);
                            return plugin;
                        }
                    }
                    
                    else
                    {
                        for(Class<?> loadedInterface : loadedInterfaces)
                        {
                            if(loadedInterface.getName().equals(Plugin.class.getName()))
                            {
                                Plugin plugin   =   (Plugin) loadedClass.newInstance();
                                plugin.setLoader(loader);
                                return plugin;
                            }
                        }
                    }
                }
            }
            
            return null;
        }

        catch(IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            System.out.println("[Error] " + e.getMessage());
            return null;
        }
    }
    
    public void activatePlugin(Plugin plugin)
    {
        if(plugin == null) return;

        GraphData data  =   null;
        
       /* if(activePlugin != null) 
            data        =   activePlugin.getData(); */
        
        activePlugin    =   plugin;
     /*   activePlugin.attachPanel();
        
        if(data != null) 
            activePlugin.passData(data); */
        
        ViewPort viewPort   =   ViewPort.getInstance();
    //    viewPort.attachMainPanel(activePlugin.getPanel());
        
        PluginsMenu pluginsMenu     =   MainMenu.getInstance().getPluginListMenu();
        String itemName             =   plugin.getPluginName().equals("Default")? "defaultPluginItem" : plugin.getPluginName();
        
        JMenuItem item              =    pluginsMenu.getPluginMenuItem(itemName);
        pluginsMenu.setActivePluginItem(item);
    }
    
    public void activePlugin(String pluginName)
    {
        Plugin plugin   =   plugins.get(pluginName);
        
        if(plugin != null && !activePlugin.equals(plugin))
            activatePlugin(plugin);
    }
    
    public Plugin getActivePlugin()
    {
        return activePlugin;
    }
    
    public Map<String, Plugin> getPlugins()
    {
        return plugins;
    }
    
    public void addPlugin(Plugin plugin)
    {
        if(plugin != null)
            plugins.put(plugin.getPluginName(), plugin);
    }
    
    public boolean hasPlugin(Plugin plugin)
    {
        if(plugin == null) return false;
        else return plugins.containsKey(plugin.getPluginName());
    }
    
    public void setPlugins(Map<String, Plugin> plugins)
    {
        this.plugins    =   plugins;
    }
    
    public URLClassLoader getActiveClassLoader()
    {
        return activePlugin.getLoader();
    }
    
    public static PluginManager createInstance()
    {
        if(instance == null) instance   =   new PluginManager();
        return instance;
    }
    
    public static PluginManager getInstance()
    {
        return instance;
    }
}
