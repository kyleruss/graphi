//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import com.graphi.app.AppManager;
import com.graphi.display.PluginsMenu;
import com.graphi.util.GraphData;
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
import javax.swing.JFrame;
import javax.swing.JMenuItem;

public final class PluginManager
{
    private Plugin activePlugin;
    private final AppManager appManager;
    private Map<String, Plugin> plugins;

    public PluginManager(AppManager appManager)
    {
        this.appManager     =   appManager;
        plugins             =   new HashMap<>();
        initDefaultPlugin();
    }
    
    private void initDefaultPlugin()
    {
        AbstractPlugin basePlugin       =   new DefaultPlugin();
        addPlugin(basePlugin);
        
        Plugin defaultPlugin;
        PluginConfig config             =   appManager.getConfigManager().getPluginConfig();
        int defaultPluginIndex          =   config.getDefaultPluginIndex();
        
        if(defaultPluginIndex == -1)
        {
            defaultPlugin = basePlugin;
        }
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
        
        activatePlugin(defaultPlugin);
        defaultPlugin.getPanel().initConfigPlugins(this);
    }
    
    public AppManager getManager()
    {
        return appManager;
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
        
        if(activePlugin != null) 
            data        =   activePlugin.getData();
        
        activePlugin    =   plugin;
        activePlugin.attachPanel(appManager);
        
        if(data != null) 
            activePlugin.passData(data);
        
        
        JFrame frame    =   appManager.getWindow().getFrame();
        frame.getContentPane().removeAll();
        frame.add(activePlugin.getPanel());
        frame.revalidate();
        
        PluginsMenu pluginsMenu      =   appManager.getWindow().getMenu().getPluginListMenu();
        String itemName              =   plugin.getPluginName().equals("Default")? "defaultPluginItem" : plugin.getPluginName();
        JMenuItem item              =    pluginsMenu.getPluginMenuItem(itemName);
        
        activePlugin.getPanel().getControlPanel().initPluginMenuListener(this, item);
        pluginsMenu.setActivePluginItem(item);
    }
    
    public void activePlugin(String pluginName)
    {
        Plugin plugin   =   plugins.get(pluginName);
        
        if(plugin != null && !activePlugin.equals(plugin))
            activatePlugin(plugin);
    }
}
