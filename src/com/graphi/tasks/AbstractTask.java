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
    protected String description;
    protected String name;
    protected Map properties;
    
    public AbstractTask()
    {
        description =   "";
        name        =   "";
        properties  =   new HashMap();
        initDefaultProperties();
        initTaskDetails();
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
    
    @Override
    public Map getTaskProperties()
    {
        return properties;
    }
    
    public void setProperty(String propertyName, Object propertyValue)
    {
        properties.put(propertyName, propertyValue);
    }
    
    @Override
    public String toString()
    {
        return name;
    }
    
    public abstract void initTaskDetails();
    
    public abstract void initDefaultProperties();

    @Override
    public abstract void performTask();
}
