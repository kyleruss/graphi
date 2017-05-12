//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import org.w3c.dom.Document;

public class UpdaterVersionConfig implements Config
{
    private String buildID;
    private Date buildDate;
    private String version;
    
    public UpdaterVersionConfig(Document document)
    {
        parseDocumentConfig(document);
    }
    
    
    @Override
    public void parseDocumentConfig(Document document) 
    {
        try
        {
            version                 =   ConfigManager.getStringConfig(document, "build-version");
            buildID                 =   ConfigManager.getStringConfig(document, "build-id");
            String buildDateStr     =   ConfigManager.getStringConfig(document, "build-date");
        
            SimpleDateFormat dateFmt    =   new SimpleDateFormat("dd-MM-yyy");
            buildDate                   =   dateFmt.parse(buildDateStr);
        }
        
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to read version config");
        }
    }
    
     @Override
    public void saveConfig() {}

    public String getBuildID()
    {
        return buildID;
    }
    
    public Date getBuildDate()
    {
        return buildDate;
    }
    
    public String getVersion()
    {
        return version;
    }
    
    @Override
    public String toString()
    {
        return "v" + version + " (" + buildID + ")"; 
    }
    
    @Override
    public String getConfigName()
    {
        return "version";
    }
}
