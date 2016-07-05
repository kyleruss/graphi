//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.config;

import org.w3c.dom.Document;

public interface Config
{
    public void parseDocumentConfig(Document doc);
    
    public String getConfigName();
}
