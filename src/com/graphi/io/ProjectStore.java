//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import com.graphi.display.layout.DataPanel;
import com.graphi.display.layout.GraphPanel;
import com.graphi.display.layout.OutputPanel;
import com.graphi.display.ViewPort;
import com.graphi.graph.GraphDataManager;
import com.graphi.graph.TableModelBean;
import com.graphi.plugins.PluginManager;
import com.graphi.tasks.TaskManager;
import com.graphi.tasks.TasksBean;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import java.io.File;
import java.io.Serializable;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ProjectStore implements Serializable
{
    private Graph graph;
    private String output;
    private TableModelBean computeTableBean;
    private TasksBean tasksBean;
    
    public ProjectStore()
    {
        this(new SparseMultigraph<>(), "", new TableModelBean(), new TasksBean());
    }
    
    public ProjectStore(Graph graph, String output, TableModelBean computeTableBean, TasksBean tasksBean)
    {
        this.graph                  =   graph;
        this.output                 =   output;
        this.computeTableBean       =   computeTableBean;
        this.tasksBean              =   tasksBean;
    }
    
    public void initInstance()
    {
        //Initialize system graph
        if(graph != null)
            initGraph();
        
        //Initialize output
        if(output != null && !output.equals(""))
            initOutput();
            
        //Initialize computation tables
        if(computeTableBean != null)
            initTableModel();
        
        //Initialize task bean
        if(tasksBean != null)
            initTasksBean();
    }
    
    public void initGraph()
    {
        DataPanel dataPanel     =   DataPanel.getInstance();
        GraphPanel graphPanel   =   GraphPanel.getInstance();
            
        graphPanel.getGraphLayout().setGraph(graph);
        graphPanel.getGraphViewer().repaint();
        dataPanel.loadNodes(graph);
        dataPanel.loadEdges(graph);
        GraphDataManager.getGraphDataInstance().setGraph(graph);
    }
    
    public void initOutput()
    {
        OutputPanel outputPanel =   OutputPanel.getInstance();
        outputPanel.setOutputText(output);
    }
    
    public void initTableModel()
    {
        DataPanel dataPanel     =   DataPanel.getInstance();
        computeTableBean.prepareImport();
        dataPanel.setComputationModel(computeTableBean.getModel());
    }
    
    public void initTasksBean()
    {
        TaskManager taskManager =   TaskManager.getInstance();
        taskManager.importTasksFromBean(tasksBean);
    }
    
    public void exportProject()
    {
        File file   =   getFile(false);
        
        if(file != null)
            Storage.saveObj(this, file);
    }
    
    public static ProjectStore importProject()
    {
        File file               =   getFile(true);
        ProjectStore instance   =   (ProjectStore) Storage.openObj(file, PluginManager.getInstance().getActiveClassLoader());
        
        return instance;
    }
    
    public static ProjectStore loadProject()
    {
        ProjectStore project    =   importProject();
        
        if(project != null)
        {
            project.initInstance();
            ViewPort.getInstance().transitionScene(ViewPort.MAIN_SCENE);
        }
        
        return project;
    }
    
    public static void saveProject()
    {
        Graph graph                     =   GraphDataManager.getGraphDataInstance().getGraph();
        String output                   =   OutputPanel.getInstance().getOutput();
        TableModelBean tableBean        =   new TableModelBean(DataPanel.getInstance().getComputationModel());
        tableBean.prepareExport();
        
        TasksBean tasksBean             =   TaskManager.getInstance().getTasks();
        ProjectStore project            =   new ProjectStore(graph, output, tableBean, tasksBean);
        
        project.exportProject();
    }
    
    public static void newProject()
    {
        new ProjectStore().initInstance();
        ViewPort.getInstance().transitionScene(ViewPort.MAIN_SCENE);
    }
    
    private static File getFile(boolean open)
    {
        JFileChooser fileChooser        =   new JFileChooser();
        FileNameExtensionFilter filter  =   new FileNameExtensionFilter("Graphi gproj file", "gproj");    
        fileChooser.setFileFilter(filter);
        
        int option                      =   open? fileChooser.showOpenDialog(null) : fileChooser.showSaveDialog(null);
        
        if(option == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile();
        else 
            return null;
    }
    
    public Graph getGraph() 
    {
        return graph;
    }

    public void setGraph(Graph graph) 
    {
        this.graph = graph;
    }

    public String getOutput() 
    {
        return output;
    }

    public void setOutput(String output)
    {
        this.output = output;
    }

    public TableModelBean getComputeTableBean() 
    {
        return computeTableBean;
    }

    public void setComputeTableBean(TableModelBean computeTableBean) 
    {
        this.computeTableBean = computeTableBean;
    }

    public TasksBean getTasksBean() 
    {
        return tasksBean;
    }

    public void setTasksBean(TasksBean tasksBean) 
    {
        this.tasksBean = tasksBean;
    }
}
