//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.display.layout.MainPanel;

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
        MainPanel.getInstance().getScreenPanel().getGraphPanel().removeAllRecordedEntries();
    }
    
}
