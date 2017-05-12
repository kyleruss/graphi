//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.config;

import com.graphi.app.Consts;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.awt.Color;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AppConfig implements Config
{
    //-----------------------------------
    //  Advanced Settings
    //-----------------------------------
    private String exportDir;
    private boolean enableLogging;
    private boolean enableUpdating;
    private boolean enableDebugMode;
    //-----------------------------------
    
    
    //-----------------------------------
    //  Viewing Settings
    //-----------------------------------
    private boolean viewNodeLabels;
    private boolean viewEdgeLabels;
    private boolean displayVisuals;
    //-----------------------------------
    
    
    //-----------------------------------
    //  Customization Settings
    //-----------------------------------
    private boolean enableCustomResolution;
    private int customWindowWidth;
    private int customWindowHeight;
    private int themeType;
    private Color displayBackground;
    private Color nodeBackground;
    private Color edgeBackground;
    private EdgeType edgeType;
    
    //-----------------------------------
    
    public AppConfig(Document document)
    {
        parseDocumentConfig(document);
    }
    
    @Override
    public void parseDocumentConfig(Document document)
    {
        displayVisuals          =   ConfigManager.getBooleanConfig(document, "display-visuals");
        exportDir               =   ConfigManager.getStringConfig(document, "export-dir");
        enableLogging           =   ConfigManager.getBooleanConfig(document, "enable-logging");
        enableUpdating          =   ConfigManager.getBooleanConfig(document, "enable-updating");
        enableDebugMode         =   ConfigManager.getBooleanConfig(document, "enable-debug-mode");
        viewNodeLabels          =   ConfigManager.getBooleanConfig(document, "view-node-labels");
        viewEdgeLabels          =   ConfigManager.getBooleanConfig(document, "view-edge-labels");
        enableCustomResolution  =   ConfigManager.getBooleanConfig(document, "enable-custom-res");
        customWindowWidth       =   ConfigManager.getIntegerConfig(document, "custom-width");
        customWindowHeight      =   ConfigManager.getIntegerConfig(document, "custom-height");
        themeType               =   ConfigManager.getIntegerConfig(document, "theme-type");
        displayBackground       =   Color.decode(ConfigManager.getStringConfig(document, "display-background"));
        nodeBackground          =   Color.decode(ConfigManager.getStringConfig(document, "node-background"));
        edgeBackground          =   Color.decode(ConfigManager.getStringConfig(document, "edge-background"));
    }
    
    @Override
    public void saveConfig() 
    {
        try
        {
            Document doc                    =   ConfigManager.generateDocument();
            Element rootElement             =   doc.createElement("app-version");
            Element advancedSettingsElement =   ConfigManager.attachBodyElement(doc, rootElement, "advanced-settings");
            Element viewSettingsElement     =   ConfigManager.attachBodyElement(doc, rootElement, "view-settings");
            Element customSettingsElement   =   ConfigManager.attachBodyElement(doc, rootElement, "customization-settings");

            doc.appendChild(rootElement);
            
            //Advanced settings
            ConfigManager.createConfigTextElement(doc, advancedSettingsElement, "enable-logging", enableLogging);
            ConfigManager.createConfigTextElement(doc, advancedSettingsElement, "enable-updating", enableUpdating);
            ConfigManager.createConfigTextElement(doc, advancedSettingsElement, "enable-debug-mode", enableDebugMode);
            ConfigManager.createConfigTextElement(doc, advancedSettingsElement, "export-dir", exportDir);
            
            //View settings
            ConfigManager.createConfigTextElement(doc, viewSettingsElement, "view-node-labels", viewNodeLabels);
            ConfigManager.createConfigTextElement(doc, viewSettingsElement, "view-edge-labels", viewEdgeLabels);
            ConfigManager.createConfigTextElement(doc, viewSettingsElement, "display-visuals", displayVisuals);
            
            //Customization settings
            ConfigManager.createConfigTextElement(doc, customSettingsElement, "enable-custom-res", enableCustomResolution);
            ConfigManager.createConfigTextElement(doc, customSettingsElement, "custom-width", customWindowWidth);
            ConfigManager.createConfigTextElement(doc, customSettingsElement, "custom-height", customWindowHeight);
            ConfigManager.createConfigTextElement(doc, customSettingsElement, "theme-type", themeType);
            ConfigManager.createConfigTextElement(doc, customSettingsElement, "display-background", displayBackground);
            ConfigManager.createConfigTextElement(doc, customSettingsElement, "node-background", nodeBackground);
            ConfigManager.createConfigTextElement(doc, customSettingsElement, "edge-background", edgeBackground);
            ConfigManager.createConfigTextElement(doc, customSettingsElement, "edge-type", edgeType);
            
            ConfigManager.saveConfig(doc, Consts.APP_CONF_FILE);
        }
        
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Failed to save settings");
        }
    }
    
    @Override
    public String getConfigName()
    {
        return "appConfig";
    }
    
    public String getDefaultExportDir()
    {
        return exportDir;
    }
    
    public boolean isDisplayVisuals()
    {
        return displayVisuals;
    }
    
    public void setDisplayVisuals(boolean displayVisuals)
    {
        this.displayVisuals     =   displayVisuals;
    }

    public String getExportDir() 
    {
        return exportDir;
    }

    public void setExportDir(String exportDir) 
    {
        this.exportDir = exportDir;
    }

    public boolean isEnableLogging() 
    {
        return enableLogging;
    }

    public void setEnableLogging(boolean enableLogging) 
    {
        this.enableLogging = enableLogging;
    }

    public boolean isEnableUpdating() 
    {
        return enableUpdating;
    }

    public void setEnableUpdating(boolean enableUpdating) 
    {
        this.enableUpdating = enableUpdating;
    }

    public boolean isEnableDebugMode() 
    {
        return enableDebugMode;
    }

    public void setEnableDebugMode(boolean enableDebugMode)
    {
        this.enableDebugMode = enableDebugMode;
    }

    public boolean isViewNodeLabels() 
    {
        return viewNodeLabels;
    }

    public void setViewNodeLabels(boolean viewNodeLabels) 
    {
        this.viewNodeLabels = viewNodeLabels;
    }

    public boolean isViewEdgeLabels()
    {
        return viewEdgeLabels;
    }

    public void setViewEdgeLabels(boolean viewEdgeLabels)
    {
        this.viewEdgeLabels = viewEdgeLabels;
    }

    public boolean isEnableCustomResolution()
    {
        return enableCustomResolution;
    }

    public void setEnableCustomResolution(boolean enableCustomResolution)
    {
        this.enableCustomResolution = enableCustomResolution;
    }

    public int getCustomWindowWidth() 
    {
        return customWindowWidth;
    }

    public void setCustomWindowWidth(int customWindowWidth) 
    {
        this.customWindowWidth = customWindowWidth;
    }

    public int getCustomWindowHeight()
    {
        return customWindowHeight;
    }

    public void setCustomWindowHeight(int customWindowHeight)
    {
        this.customWindowHeight = customWindowHeight;
    }

    public int getThemeType()
    {
        return themeType;
    }

    public void setThemeType(int themeType)
    {
        this.themeType = themeType;
    }

    public Color getDisplayBackground()
    {
        return displayBackground;
    }

    public void setDisplayBackground(Color displayBackground) 
    {
        this.displayBackground = displayBackground;
    }

    public Color getNodeBackground()
    {
        return nodeBackground;
    }

    public void setNodeBackground(Color nodeBackground)
    {
        this.nodeBackground = nodeBackground;
    }

    public Color getEdgeBackground() 
    {
        return edgeBackground;
    }

    public void setEdgeBackground(Color edgeBackground) 
    {
        this.edgeBackground = edgeBackground;
    }

    public EdgeType getEdgeType() 
    {
        return edgeType;
    }

    public void setEdgeType(EdgeType edgeType) 
    {
        this.edgeType = edgeType;
    }
}
