//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.config;

import org.w3c.dom.Document;

public class AppConfig implements Config
{
    //enable or disable graph visuals on main panel
    private boolean displayVisuals;
    
    //default export path
    private String exportDir;
    
    public AppConfig(Document document)
    {
        parseDocumentConfig(document);
    }
    
    @Override
    public void parseDocumentConfig(Document document)
    {
        displayVisuals  =   document.getElementsByTagName("displayVisuals").item(0).getTextContent().equalsIgnoreCase("true");
        exportDir       =   document.getElementsByTagName("exportDir").item(0).getTextContent();
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
}
