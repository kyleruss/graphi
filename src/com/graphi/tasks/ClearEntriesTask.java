//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.app.AppManager;

public class ClearEntriesTask extends AbstractTask
{

    @Override
    public void initTaskDetails() 
    {
        name = "Clear recorded entries";
    }

    @Override
    public void initDefaultProperties() {}

    @Override
    public void performTask() 
    {
        AppManager.getInstance().getPluginManager().getActivePlugin()
                  .getPanel().getScreenPanel().getGraphPanel().removeAllRecordedEntries();
    }
    
}
