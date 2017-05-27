//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.layout.controls.ComputeControlPanel;
import com.graphi.display.layout.controls.GraphObjControlPanel;
import com.graphi.display.layout.controls.IOControlPanel;
import com.graphi.display.layout.controls.SimulationControlPanel;
import com.graphi.display.layout.controls.TaskControlPanel;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class ControlPanel extends JPanel 
{
    protected JMenuItem activePluginItem;
    protected ComputeControlPanel computePanel;
    protected GraphObjControlPanel gObjPanel;
    protected IOControlPanel ioPanel;
    protected SimulationControlPanel simulationPanel;
    protected TaskControlPanel taskPanel;
    private static ControlPanel instance;
    
    public ControlPanel() 
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 0, 3, 8));

        initControls();

        add(simulationPanel);
        add(Box.createRigidArea(new Dimension(230, 30)));
        add(ioPanel);
        add(Box.createRigidArea(new Dimension(230, 30)));
        add(gObjPanel);
        add(Box.createRigidArea(new Dimension(230, 30)));
        add(taskPanel);
        add(Box.createRigidArea(new Dimension(230, 30)));
        add(computePanel);
    }
    
    protected void initControls()
    {
        computePanel    =   new ComputeControlPanel();
        gObjPanel       =   new GraphObjControlPanel();
        ioPanel         =   new IOControlPanel();
        simulationPanel =   new SimulationControlPanel();
        taskPanel       =   new TaskControlPanel();
    }

    public ComputeControlPanel getComputePanel() 
    {
        return computePanel;
    }

    public GraphObjControlPanel getgObjPanel() 
    {
        return gObjPanel;
    }

    public IOControlPanel getIoPanel() 
    {
        return ioPanel;
    }

    public SimulationControlPanel getSimulationPanel() 
    {
        return simulationPanel;
    }

    public TaskControlPanel getTaskPanel()
    {
        return taskPanel;
    }
    
    public static ControlPanel getInstance()
    {
        if(instance == null) instance   =   new ControlPanel();
        return instance;
    }
}