//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.config;

import org.w3c.dom.Document;

public class AppConfig implements Config
{
    private boolean displayVisuals;
    
    public AppConfig(Document document)
    {
        parseDocumentConfig(document);
    }
    
    @Override
    public void parseDocumentConfig(Document document)
    {
        displayVisuals  =   document.getElementsByTagName("displayVisuals").item(0).getTextContent().equalsIgnoreCase("true");
    }
    
    @Override
    public String getConfigName()
    {
        return "appConfig";
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
