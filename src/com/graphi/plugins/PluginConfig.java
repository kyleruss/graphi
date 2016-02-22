
package com.graphi.plugins;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class PluginConfig 
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
    
    public static PluginConfig parseDocumentConfig(Document doc)
    {
        try
        {
            int defaultIndex        =   Integer.parseInt(doc.getElementsByTagName("defaultPlugin").item(0).getTextContent());
            NodeList loadPluginList =   doc.getElementsByTagName("pluginFile");
            List<String> paths      =   new ArrayList<>();
            
            for(int i = 0; i < loadPluginList.getLength(); i++)
                paths.add(loadPluginList.item(i).getTextContent());
            
            return new PluginConfig(defaultIndex, paths);
        }
        
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to read plugin config");
            return null;
        }
    }
}
