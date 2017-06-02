//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.app.AppManager;
import com.graphi.display.layout.MainPanel;
import com.graphi.display.layout.ControlPanel;
import com.graphi.plugins.PluginManager;
import java.io.File;

public class ExportTableTask extends AbstractTask
{
    @Override
    public void initTaskDetails() 
    {
        setTaskName("Export table");
    }

    @Override
    public void initDefaultProperties() 
    {
        setProperty("Directory", "data/");
        setProperty("File name", "exampletable");
        setProperty("Table index", "0");
    }

    @Override
    public void performTask() 
    {
        String dir          =   getProperty("Directory");
        String fileName     =   getProperty("File name");
        String extension    =   ".csv";   
        int tableIndex      =   Integer.parseInt(getProperty("Table index"));
        File file           =   new File(dir + fileName + extension);
        int fileIndex       =   2;
        
        while(file.exists())
        {
            file    =   new File(dir + fileName + fileIndex + extension);
            fileIndex++;
        }
        
        ControlPanel.getInstance().getIoPanel().exportTable(tableIndex, file);
    }
}
