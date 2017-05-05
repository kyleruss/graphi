//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import com.graphi.display.layout.DataPanel;
import com.graphi.display.layout.GraphPanel;
import com.graphi.display.layout.MainPanel;
import com.graphi.display.layout.OutputPanel;
import com.graphi.display.layout.ViewPort;
import com.graphi.graph.GraphDataManager;
import com.graphi.plugins.PluginManager;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import java.io.File;
import java.io.Serializable;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ProjectStore implements Serializable
{
    private Graph graph;
    private String output;
    private TableModel computeTableModel;
    
    public ProjectStore()
    {
        this(new SparseMultigraph<>(), "", new DefaultTableModel());
    }
    
    public ProjectStore(Graph graph, String output, TableModel computeTableModel)
    {
        this.graph                  =   graph;
        this.output                 =   output;
        this.computeTableModel      =   computeTableModel;
    }
    
    public void exportProject()
    {
        File file   =   getFile(false);
        
        if(file != null)
            Storage.saveObj(this, file);
    }
    
    public void initInstance()
    {
        MainPanel mainPanel     =   MainPanel.getInstance();
        DataPanel dataPanel     =   DataPanel.getInstance();
        
        //Initialize system graph
        if(graph != null)
            initGraph();
        
        //Initialize output
        if(output != null && !output.equals(""))
            initOutput();
            
        //Initialize computation tables
        if(computeTableModel != null)
            initTableModel();
    }
    
    public void initGraph()
    {
        MainPanel mainPanel     =   MainPanel.getInstance();
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
        dataPanel.setComputationModel((DefaultTableModel) computeTableModel);
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
    
    public static void newProject()
    {
        new ProjectStore().initInstance();
        ViewPort.getInstance().transitionScene(ViewPort.MAIN_SCENE);
    }
    
    private static File getFile(boolean open)
    {
        JFileChooser fileChooser        =   new JFileChooser();
        FileNameExtensionFilter filter  =   new FileNameExtensionFilter("Graphi project file", "gproject");    
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

    public TableModel getComputeTableModel() 
    {
        return computeTableModel;
    }

    public void setComputeTableModel(TableModel computeTableModel) 
    {
        this.computeTableModel = computeTableModel;
    }
    
    
    
}
