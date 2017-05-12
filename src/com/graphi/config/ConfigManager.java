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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
    private UpdaterVersionConfig versionConfig;
    
    private ConfigManager()
    {
        loadConfigs();
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
    
    private void loadConfigs()
    {
        Document appConfigDoc       =   getConfigDocument(Consts.APP_CONF_FILE);
        appConfig                   =   new AppConfig(appConfigDoc);
        
        Document pluginConfigDoc    =   getConfigDocument(Consts.PLUGIN_CONF_FILE);
        pluginConfig                =   new PluginConfig(pluginConfigDoc);
        
        String versionPath          =   Consts.UPDATER_DIR + "data" + Consts.SEPARATOR + "conf" + Consts.SEPARATOR + "version.xml";
        Document versionConfDoc     =   getConfigDocument(versionPath);
        versionConfig               =   new UpdaterVersionConfig(versionConfDoc);
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
    
    public UpdaterVersionConfig getVersionConfig()
    {
        return versionConfig;
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
    
    public static Element createConfigTextElement(Document doc, Element parentElement, String nodeName, Object content)
    {
        Element element     =   doc.createElement(nodeName);
        String textContent  =   "";
        
        if(content != null)
        {
            if(content instanceof Boolean)
            {
                boolean boolContent =   (Boolean) content;
                textContent         =   boolContent? "true" : "false";
            }
            
            else textContent        =   content.toString();
        }
        
        element.appendChild(doc.createTextNode(textContent));
        parentElement.appendChild(element);
        
        return element;
    }
    
    public static void saveConfig(Document doc, String path) throws TransformerConfigurationException, TransformerException
    {
        TransformerFactory tFactory     =   TransformerFactory.newInstance();
        Transformer transformer         =   tFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource src                   =   new DOMSource(doc);
        StreamResult res                =   new StreamResult(new File(path));
        transformer.transform(src, res);
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
