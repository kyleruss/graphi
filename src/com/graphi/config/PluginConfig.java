//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.config;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class PluginConfig implements Config
{
    //The default plugin index for startup (-1 for default)
    private int defaultIndex;
    
    //List of paths to plugins installed
    private List<String> loadedPluginPaths;
    
    private String pluginsDirectory;
    
    public PluginConfig(Document doc)
    {
        parseDocumentConfig(doc);
    }
    
    public PluginConfig(int defaultIndex, List<String> loadedPluginPaths, String pluginsDirectory)
    {
        this.defaultIndex       =   defaultIndex;
        this.loadedPluginPaths  =   loadedPluginPaths;
        this.pluginsDirectory   =   pluginsDirectory;
    }
    
    @Override
    public void parseDocumentConfig(Document document)
    {
        try
        {
            pluginsDirectory        =   ConfigManager.getStringConfig(document, "plugin-directory");
            defaultIndex            =   ConfigManager.getIntegerConfig(document, "default-plugin-index");
            NodeList loadPluginList =   document.getElementsByTagName("plugin-file");
            List<String> paths      =   new ArrayList<>();
            
            for(int i = 0; i < loadPluginList.getLength(); i++)
                paths.add(loadPluginList.item(i).getTextContent());

            this.loadedPluginPaths  =   paths;
        }
        
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to read plugin config");
        }
    }
    
    @Override
    public String getConfigName()
    {
        return "pluginConfig";
    }
    
    public int getDefaultPluginIndex()
    {
        return defaultIndex;
    }
    
    public String getPluginDirectory()
    {
        return pluginsDirectory;
    }
    
    public void setPluginDirectory(String pluginDirectory)
    {
        this.pluginsDirectory   =   pluginDirectory;
    }
    
    public String getDefaultPluginPath()
    {
        if(defaultIndex < 0 || defaultIndex > loadedPluginPaths.size())
            return null;
        else
            return loadedPluginPaths.get(defaultIndex);
    }
    
    public List<String> getLoadedPluginPaths()
    {
        return loadedPluginPaths;
    }
    
    public void setDefaultPluginIndex(int index)
    {
        this.defaultIndex   =   index;
    }
    
    public void setLoadedPluginPaths(List<String> paths)
    {
        this.loadedPluginPaths  =   paths;
    }
    
}