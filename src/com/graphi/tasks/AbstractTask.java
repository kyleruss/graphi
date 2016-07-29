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
    protected Map<String, String> properties;
    
    public AbstractTask()
    {
        description =   "";
        name        =   "";
        properties  =   new HashMap<>();
        initDefaultProperties();
        initTaskDetails();
    }
    
    public abstract void initTaskDetails();
    
    public abstract void initDefaultProperties();

    @Override
    public abstract void performTask();
    
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
    
    @Override
    public void setProperty(String propertyName, String propertyValue)
    {
        properties.put(propertyName, propertyValue);
    }
    
    @Override
    public String getProperty(String propertyName)
    {
        return properties.get(propertyName);
    }
    
    @Override
    public String toString()
    {
        return name;
    }
}
