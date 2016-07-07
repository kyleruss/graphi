//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import java.util.Date;

public class RecordGraphTask extends AbstractTask
{
    @Override
    public void performTask() 
    {
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
