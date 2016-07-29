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
    /**
     * @return A brief description of the task actions
     * As of Graphi 1.7.1 description is not display
     */
    public String getTaskDescription();
    
    /**
     * @return The name of the task that identifies the task
     * and is displayed to the user in the GUI
     */
    public String getTaskName();
    
    /**
     * Should run the operations of the task
     * Called when ever the user runs setup/repeatable tasks
     */
    public void performTask();
    
    /**
     * @return A map of the current task properties
     * Property values are string types for user input and should be parsed
     * by the handler to appropriate types when necessary
     */
    public Map<String, String> getTaskProperties();
    
    /**
     * Adds a property with @param propertyName key and @param value
     * @param propertyName The key name of the property
     * @param value The property value that corresponds to the @param propertyName
     */
    public void setProperty(String propertyName, String value);
    
    /**
     * @param propertyName The name and key of the property
     * @return The property value for the property @propertyName; null if it does not exist
     */
    public String getProperty(String propertyName);
}
