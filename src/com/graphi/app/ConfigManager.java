//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.app;

import com.graphi.plugins.PluginConfig;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Manages the active configs in the application
 * Handles importing of the app configs
 */

public class ConfigManager 
{
    private PluginConfig pluginConfig;
    
    public ConfigManager()
    {
        loadConfig();
    }
    
    /**
    * Imports global config file
    * Initializes all app configs from the global config
    */
    private void loadConfig()
    {
        try
        {
            File configFile             =   new File(Consts.GLOBAL_CONF_FILE);
            DocumentBuilder docBuilder  =   DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document configDoc          =   docBuilder.parse(configFile);
            
            pluginConfig                =   new PluginConfig(configDoc);
        }
        
        catch(ParserConfigurationException | SAXException | IOException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to load global config");
        }
    }
    
    /**
    * @return The applications plugin config
    * contains default plugin, startup plugins configs etc.
    */
    public PluginConfig getPluginConfig()
    {
        return pluginConfig;
    }
}
