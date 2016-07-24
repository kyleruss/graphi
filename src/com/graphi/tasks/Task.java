//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import java.io.Serializable;
import java.util.Map;

public interface Task extends Serializable
{
    public String getTaskDescription();
    
    public String getTaskName();
    
    public void performTask();
    
    public Map getTaskProperties();
    
    public void setProperty(String propertyName, Object value);
    
    public Object getProperty(String propertyName);
}
