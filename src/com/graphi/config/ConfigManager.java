//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.config;

import com.graphi.app.Consts;
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
    private static ConfigManager instance;
    private PluginConfig pluginConfig;
    private AppConfig appConfig;
    
    private ConfigManager()
    {
        loadConfig();
    }
    
    /**
    * Imports global config file
    * Initializes all app configs from the global config
    */
    
    public Document getConfigDocument(String documentPath)
    {
        try
        {
            File configFile             =   new File(documentPath);
            DocumentBuilder docBuilder  =   DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document configDoc          =   docBuilder.parse(configFile);
            
            return configDoc;
        }
        
        catch(ParserConfigurationException | SAXException | IOException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to load global config");
            return null;
        }
    }
    
    private void loadConfig()
    {
        Document mainConfig         =   getConfigDocument(Consts.GLOBAL_CONF_FILE);
        pluginConfig                =   new PluginConfig(mainConfig);
        appConfig                   =   new AppConfig(mainConfig);
    }
    
    /**
    * @return The applications plugin config
    * contains default plugin, startup plugins configs etc.
    */
    public PluginConfig getPluginConfig()
    {
        return pluginConfig;
    }
    
    public AppConfig getAppConfig()
    {
        return appConfig;
    }
    
    public static boolean getBooleanConfig(Document doc, String name)
    {
        final String BOOL_NAME   =   "true";
        return doc.getElementsByTagName(name).item(0).getTextContent().equalsIgnoreCase(BOOL_NAME);
    }
    
    public static String getStringConfig(Document doc, String name)
    {
        return doc.getElementsByTagName(name).item(0).getTextContent();
    }
    
    public static int getIntegerConfig(Document doc, String name)
    {
        return Integer.parseInt(doc.getElementsByTagName(name).item(0).getTextContent());
    }
    
    public static ConfigManager createInstance()
    {
        if(instance == null) instance   =   new ConfigManager();
        return instance;
    }
    
    public static ConfigManager getInstance()
    {
        return instance;
    }
}
