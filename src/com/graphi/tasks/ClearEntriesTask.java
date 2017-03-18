//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.plugins.PluginManager;

public class ClearEntriesTask extends AbstractTask
{
    @Override
    public void initTaskDetails() 
    {
        setTaskName("Clear recorded entries");
    }

    @Override
    public void initDefaultProperties() {}

    @Override
    public void performTask() 
    {
        PluginManager.getInstance().getActivePlugin()
                  .getPanel().getScreenPanel().getGraphPanel().removeAllRecordedEntries();
    }
    
}
