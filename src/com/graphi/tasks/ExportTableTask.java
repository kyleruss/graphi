//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.app.AppManager;
import java.io.File;

public class ExportTableTask extends AbstractTask
{
    @Override
    public void initTaskDetails() 
    {
        name    =   "Export table";
    }

    @Override
    public void initDefaultProperties() 
    {
        properties.put("File name", "data/exampletable.csv");
        properties.put("Table index", "0");
    }

    @Override
    public void performTask() 
    {
        String fileName =   properties.get("File name");
        int tableIndex  =   Integer.parseInt(properties.get("Table index"));
        File file       =   new File(fileName);
        
        AppManager.getInstance().getPluginManager().getActivePlugin()
                .getPanel().getControlPanel().getIoPanel()
                .exportTable(tableIndex, file);
    }
}
