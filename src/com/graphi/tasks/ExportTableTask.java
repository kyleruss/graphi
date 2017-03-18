//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.app.AppManager;
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
        setProperty("File name", "data/exampletable.csv");
        setProperty("Table index", "0");
    }

    @Override
    public void performTask() 
    {
        String fileName =   getProperty("File name");
        int tableIndex  =   Integer.parseInt(getProperty("Table index"));
        File file       =   new File(fileName);
        
        PluginManager.getInstance().getActivePlugin()
                .getPanel().getControlPanel().getIoPanel()
                .exportTable(tableIndex, file);
    }
}
