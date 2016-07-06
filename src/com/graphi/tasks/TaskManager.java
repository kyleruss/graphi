//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.app.AppManager;
import com.graphi.io.Storage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class TaskManager 
{
    private static TaskManager instance;
    private TasksBean tasks;
    private List<Task> taskList;
    
    private TaskManager()
    {
        tasks   =   new TasksBean();
    }
    
    private void initDefaultTaskList()
    {
        taskList    =   new ArrayList<>();
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
        if(tasks != null)
            Storage.saveObj(tasks, file);
    }
    
    public static TaskManager getInstance()
    {
        if(instance == null) instance = new TaskManager();
        return instance;
    }
}
