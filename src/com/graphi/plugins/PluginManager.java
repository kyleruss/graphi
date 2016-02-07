//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import com.graphi.display.Window;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.JFrame;

public class PluginManager
{
    private Plugin activePlugin;
    private final Window window;
    private Map<String, Plugin> plugins;

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
    
    public Plugin fetchPlugin(File file)
    {
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
                            return (Plugin) loadedClass.newInstance();

                    }
                    
                    else
                    {
                        for(Class<?> loadedInterface : loadedInterfaces)
                        {
                            if(loadedInterface.getName().equals(Plugin.class.getName()))
                                return (Plugin) loadedClass.newInstance();
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
        Plugin plugin   =   plugins.get(pluginName);
        
        if(plugin != null)
            activatePlugin(plugin);
    }
}
