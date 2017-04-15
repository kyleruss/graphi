//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import com.graphi.graph.GraphData;
import java.io.File;
import java.io.Serializable;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ProjectStore implements Serializable
{
    private GraphData graphData;
    private String output;
    private JTable computeTable;
    
    public ProjectStore()
    {
        this(null, null, null);
    }
    
    public ProjectStore(GraphData graphData, String output, JTable computeTable)
    {
        this.graphData          =   graphData;
        this.output             =   output;
        this.computeTable       =   computeTable;
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
        ProjectStore instance   =   (ProjectStore) Storage.openObj(file, null);
        
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
    
    public GraphData getGraphData() 
    {
        return graphData;
    }

    public void setGraphData(GraphData graphData) 
    {
        this.graphData = graphData;
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
