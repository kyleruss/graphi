//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;


public class TaskManager 
{
    private static TaskManager instance;
    
    private TaskManager()
    {
        
    }
    
    public static TaskManager getInstance()
    {
        if(instance == null) instance = new TaskManager();
        return instance;
    }
}
