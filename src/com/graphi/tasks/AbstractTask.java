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
    //A brief description of the task role and actions
    protected String description;
    
    //The name of the task that identifies the task
    protected String name;
    
    //The properties that can be set by the user
    protected Map<String, String> properties;
    
    public AbstractTask()
    {
        description =   "";
        name        =   "";
        properties  =   new HashMap<>();
        initDefaultProperties();
        initTaskDetails();
    }

    /**
     * Should initialize the name and description of the task
     * Called on constructor
     */
    public abstract void initTaskDetails();
    
    /**
     * Should initialize all the possible properties and default values
     * Called on constructor
     */
    public abstract void initDefaultProperties();
    
    /**
     * @return The name of the task
     */
    @Override
    public String toString()
    {
        return name;
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
    
    protected void setTaskName(String name)
    {
        this.name   =   name;
    }
    
    protected void setTaskDescription(String description)
    {
        this.description    =   description;
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
}
