//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.app.AppManager;
import java.util.Date;

public class RecordGraphTask extends AbstractTask
{
    @Override
    public void performTask() 
    {
        Date date           =   (Date) properties.get("Entry date");
        String entryName    =   (String) properties.get("Entry name");
        boolean recordState =   (boolean) properties.get("Record state");
        boolean recordTable =   (boolean) properties.get("Record table");
        
        AppManager.getInstance().getPluginManager().getActivePlugin()
                .getPanel().getScreenPanel().getGraphPanel()
                .addRecordedGraph(entryName, date, recordState, recordTable, true);
    }

    @Override
    public void initDefaultProperties()
    {
        properties.put("Record table", true);
        properties.put("Record state", true);
        properties.put("Entry name", "");
        properties.put("Entry date", new Date());
    }

    @Override
    public void initTaskDetails() 
    {
        name    =   "Record Graph";
    }
}
