//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.display.MainMenu;
import com.graphi.display.Window;
import com.graphi.display.layout.DataPanel;
import com.graphi.display.layout.GraphPanel;
import com.graphi.display.layout.MainPanel;
import com.graphi.display.layout.OutputPanel;
import com.graphi.display.layout.ViewPort;
import com.graphi.display.layout.util.PluginMenuListener;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class ControlPanel extends JPanel implements ActionListener
{
    protected PluginMenuListener menuListener;
    protected JMenuItem activePluginItem;
    
    protected ComputeControlPanel computePanel;
    protected GraphObjControlPanel gObjPanel;
    protected IOControlPanel ioPanel;
    protected ScriptControlPanel scriptPanel;
    protected SimulationControlPanel simulationPanel;
    protected TaskControlPanel taskPanel;
    private static ControlPanel instance;
    
    public ControlPanel() 
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 0, 3, 8));

        initControls();
        MainMenu.getInstance().setMenuItemListener(this);

        add(simulationPanel);
        add(Box.createRigidArea(new Dimension(230, 30)));
        add(ioPanel);
        add(Box.createRigidArea(new Dimension(230, 30)));
        add(gObjPanel);
        add(Box.createRigidArea(new Dimension(230, 30)));
        add(taskPanel);
        add(Box.createRigidArea(new Dimension(230, 30)));
        add(computePanel);
        add(Box.createRigidArea(new Dimension(230, 30)));
        add(scriptPanel);
    }
    
    protected void initControls()
    {
        computePanel    =   new ComputeControlPanel();
        gObjPanel       =   new GraphObjControlPanel();
        ioPanel         =   new IOControlPanel();
        scriptPanel     =   new ScriptControlPanel();
        simulationPanel =   new SimulationControlPanel();
        taskPanel       =   new TaskControlPanel();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();

        MainMenu menu           =   MainMenu.getInstance();
        MainPanel mainPanel     =   MainPanel.getInstance();   
        GraphPanel graphPanel   =   GraphPanel.getInstance();
        DataPanel dataPanel     =   DataPanel.getInstance();
        JFrame frame            =   Window.getInstance().getFrame();
        
        if(src == menu.getMenuItem("aboutItem"))
            menu.showAbout();

        else if(src == menu.getMenuItem("exitItem"))
            System.exit(0);

        else if(src == menu.getMenuItem("miniItem"))
            frame.setState(JFrame.ICONIFIED);

        else if(src == menu.getMenuItem("maxItem"))
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        else if(src == menu.getMenuItem("importGraphItem"))
            ioPanel.importGraph();

        else if(src == menu.getMenuItem("exportGraphItem"))
            ioPanel.exportGraph();

        else if(src == menu.getMenuItem("importLogItem"))
            ioPanel.importLog();

        else if(src == menu.getMenuItem("exportLogItem"))
            ioPanel.exportLog();

        else if(src == menu.getMenuItem("vLabelsItem"))
            graphPanel.showVertexLabels(true);

        else if(src == menu.getMenuItem("eLabelsItem"))
            graphPanel.showEdgeLabels(true);

        /*else if(src == menu.getMenuItem("viewerBGItem"))
            viewerPanel.showViewerBGChange();

        else if(src == menu.getMenuItem("edgeBGItem"))
            viewerPanel.showEdgeBGChange();

        else if(src == menu.getMenuItem("vertexBGItem"))
            viewerPanel.showVertexBGChange(); */

        else if(src == menu.getMenuItem("clearLogItem"))
            OutputPanel.getInstance().clearLog();

        else if(src == menu.getMenuItem("resetGraphItem"))
            graphPanel.resetGraph();

        else if(src == menu.getMenuItem("addVertexItem"))
            dataPanel.addVertex();

        else if(src == menu.getMenuItem("editVertexItem"))
            dataPanel.editVertex();

        else if(src == menu.getMenuItem("removeVertexItem"))
            dataPanel.removeVertex();

        else if(src == menu.getMenuItem("addEdgeItem"))
            dataPanel.addEdge();

        else if(src == menu.getMenuItem("editEdgeItem"))
            dataPanel.editEdge();

        else if(src == menu.getMenuItem("removeEdgeItem"))
            dataPanel.removeEdge();

        else if(src == menu.getMenuItem("loadPluginItem"))
            ioPanel.importPlugin();
        
        else if(src == menu.getMenuItem("searchObjectItem"))
            graphPanel.searchGraphObject();
        
        else if(src == menu.getMenuItem("mainMenuItem"))
            ViewPort.getInstance().setScene(ViewPort.TITLE_SCENE);

    }

    public PluginMenuListener getMenuListener()
    {
        return menuListener;
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

    public ScriptControlPanel getScriptPanel() 
    {
        return scriptPanel;
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