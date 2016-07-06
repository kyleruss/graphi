//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTask implements Task
{
    private String description;
    private String name;
    private Map properties;
    
    public AbstractTask()
    {
        description =   "";
        name        =   "";
        properties  =   new HashMap();
    }
    
    @Override
    public String getTaskDescription() 
    {
        return description;
    }

    @Override
    public String getTaskName() 
    {
        return name;
    }
    
    public Map getProperties()
    {
        return properties;
    }
    
    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public abstract void performTask();
}
