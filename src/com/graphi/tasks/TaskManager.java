//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.display.layout.MainPanel;
import com.graphi.display.layout.controls.ControlPanel;
import com.graphi.io.Storage;
import com.graphi.plugins.PluginManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class TaskManager 
{
    private static TaskManager instance;
    private TasksBean tasks;
    private List<Task> availTaskList;
    
    private TaskManager()
    {
        tasks   =   new TasksBean();
        initDefaultTaskList();
    }
    
    private void initDefaultTaskList()
    {
        availTaskList    =   new ArrayList<>();
        availTaskList.add(new RecordGraphTask());
        availTaskList.add(new SimulateNetworkTask());
        availTaskList.add(new ClearEntriesTask());
        availTaskList.add(new ResetSimTask());
        availTaskList.add(new ExportTableTask());
    }
    
    public List<Task> getAvailTaskList()
    {
        return availTaskList;
    }
    
    public void registerTaskList(List<Task> otherTasks)
    {
        availTaskList.addAll(otherTasks);
    }
    
    public TasksBean getTasks()
    {
        return tasks;
    }
    
    public void registerTask(Task task)
    {
        availTaskList.add(task);
    }
    
    public void importTasks(File file)
    {
        PluginManager pluginManager =   PluginManager.getInstance();
        tasks   =   (TasksBean) Storage.openObj(file, pluginManager.getActiveClassLoader());
        
        ControlPanel.getInstance().getTaskPanel().initTaskBean(tasks);
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
