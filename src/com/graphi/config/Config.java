//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.config;

import org.w3c.dom.Document;

public interface Config
{
    /**
     * Parses the passed xml document and initializes the config
     * @param doc An XML document to read config from
     */
    public void parseDocumentConfig(Document doc);
    
    /**
     * @return The name of the config document name
     */
    public String getConfigName();
    
    public void saveConfig();
}
