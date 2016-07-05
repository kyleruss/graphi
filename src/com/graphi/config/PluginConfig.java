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
    private int defaultIndex;
    private List<String> loadedPluginPaths;
    
    public PluginConfig(Document doc)
    {
        parseDocumentConfig(doc);
    }
    
    public PluginConfig(int defaultIndex, List<String> loadedPluginPaths)
    {
        this.defaultIndex       =   defaultIndex;
        this.loadedPluginPaths  =   loadedPluginPaths;
    }
    
    @Override
    public void parseDocumentConfig(Document doc)
    {
        try
        {
            int pluginIndex         =   Integer.parseInt(doc.getElementsByTagName("defaultPluginIndex").item(0).getTextContent());
            NodeList loadPluginList =   doc.getElementsByTagName("pluginFile");
            List<String> paths      =   new ArrayList<>();
            
            for(int i = 0; i < loadPluginList.getLength(); i++)
                paths.add(loadPluginList.item(i).getTextContent());
            
            this.defaultIndex       =   pluginIndex;
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