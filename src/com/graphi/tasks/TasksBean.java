//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TasksBean implements Serializable
{
    private List<Task> setupTasks;
    private List<Task> repeatableTasks;

    public TasksBean()
    {
        setupTasks      =   new ArrayList<>();
        repeatableTasks =   new ArrayList<>();   
    }

    public List<Task> getSetupTasks()
    {
        return setupTasks;
    }

    public List<Task> getRepeatableTasks() 
    {
        return repeatableTasks;
    }
    
    public void addSetupTask(Task task)
    {
        setupTasks.add(task);
    }
    
    public void addRepeatableTask(Task task)
    {
        repeatableTasks.add(task);
    }
}
