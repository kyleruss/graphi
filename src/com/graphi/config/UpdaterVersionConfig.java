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
    
    public UpdaterVersionConfig(Document document)
    {
        parseDocumentConfig(document);
    }
    
    @Override
    public void parseDocumentConfig(Document document) 
    {
        try
        {
            buildID                 =   ConfigManager.getStringConfig(document, "build-id");
            String buildDateStr     =   ConfigManager.getStringConfig(document, "build-date");
        
            SimpleDateFormat dateFmt    =   new SimpleDateFormat("dd-MM-yyy");
            buildDate                   =   dateFmt.parse(buildDateStr);
            System.out.println(buildID);
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "[Error] Failed to read version config");
        }
    }

    public String getBuildID()
    {
        return buildID;
    }
    
    public Date getBuildDate()
    {
        return buildDate;
    }
    
    @Override
    public String getConfigName()
    {
        return "version";
    }
}
