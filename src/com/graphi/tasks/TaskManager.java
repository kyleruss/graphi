//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.app.AppManager;
import com.graphi.io.Storage;
import java.io.File;


public class TaskManager 
{
    private static TaskManager instance;
    private TasksBean tasks;
    
    private TaskManager()
    {
        tasks   =   new TasksBean();
    }
    
    public TasksBean getTasks()
    {
        return tasks;
    }
    
    public void importTasks(File file)
    {
        tasks   =   (TasksBean) Storage.openObj(file, AppManager.getInstance().getPluginManager().getActiveClassLoader());
    }
    
    public void exportTasks(File file)
    {
        
    }
    
    public static TaskManager getInstance()
    {
        if(instance == null) instance = new TaskManager();
        return instance;
    }
}
