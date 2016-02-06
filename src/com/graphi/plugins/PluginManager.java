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
    
    public Map<String, AbstractPlugin> getPlugins()
    {
        return plugins;
    }
    
    public void setPlugins(Map<String, AbstractPlugin> plugins)
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
                    
                    for(Class<?> loadedInterface : loadedInterfaces)
                    {
                        if(loadedInterface.getSimpleName().equalsIgnoreCase("Plugin"))
                            return (Plugin) loadedClass.newInstance();
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
