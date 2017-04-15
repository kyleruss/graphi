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
import com.graphi.graph.GraphData;
import com.graphi.plugins.PluginManager;
import edu.uci.ics.jung.graph.Graph;
import java.io.File;
import java.io.Serializable;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class ProjectStore implements Serializable
{
    private Graph graph;
    private String output;
    private JTable computeTable;
    
    public ProjectStore()
    {
        this(null, null, null);
    }
    
    public ProjectStore(Graph graph, String output, JTable computeTable)
    {
        this.graph              =   graph;
        this.output             =   output;
        this.computeTable       =   computeTable;
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
        DataPanel dataPanel     =   mainPanel.getScreenPanel().getDataPanel();
        
        //Initialize system graph
        if(graph != null)
        {
            GraphPanel graphPanel   =   mainPanel.getScreenPanel().getGraphPanel();
            
            graphPanel.getGraphLayout().setGraph(graph);
            graphPanel.getGraphViewer().repaint();
            dataPanel.loadNodes(graph);
            dataPanel.loadEdges(graph);
            mainPanel.getGraphData().setGraph(graph);
        }
        
        //Initialize output
        if(output != null && !output.equals(""))
        {
            OutputPanel outputPanel =   mainPanel.getScreenPanel().getOutputPanel();
            outputPanel.setOutputText(output);
        }
        
        //Initialize computation tables
        if(computeTable != null)
        {
            DefaultTableModel model =   (DefaultTableModel) computeTable.getModel();
            dataPanel.setComputationModel(model);
        }
    }
    
    public static ProjectStore importProject()
    {
        File file               =   getFile(true);
        ProjectStore instance   =   (ProjectStore) Storage.openObj(file, PluginManager.getInstance().getActiveClassLoader());
        
        return instance;
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

    public JTable getComputeTable() 
    {
        return computeTable;
    }

    public void setComputeTable(JTable computeTable) 
    {
        this.computeTable = computeTable;
    }
    
    
    
}
