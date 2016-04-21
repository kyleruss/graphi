//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.display.MainMenu;
import com.graphi.display.layout.MainPanel;
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
    
    private ComputeControlPanel computePanel;
    private GraphObjControlPanel gObjPanel;
    private IOControlPanel ioPanel;
    private ModeControlPanel modePanel;
    private ScriptControlPanel scriptPanel;
    private SimulationControlPanel simulationPanel;
    private TaskControlPanel taskPanel;
    private ViewerControlPanel viewerPanel;
    private final MainPanel mainPanel;
    
    public ControlPanel(MainPanel mainPanel) 
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 0, 3, 8));

        this.mainPanel  =   mainPanel;
        computePanel    =   new ComputeControlPanel(this);
        gObjPanel       =   new GraphObjControlPanel(this);
        ioPanel         =   new IOControlPanel(this);
        modePanel       =   new ModeControlPanel(this);
        scriptPanel     =   new ScriptControlPanel(this);
        simulationPanel =   new SimulationControlPanel(this);
        viewerPanel     =   new ViewerControlPanel(this);
        taskPanel       =   new TaskControlPanel(this);

        mainPanel.getMenu().setMenuItemListener(this);

        add(modePanel);
        add(Box.createRigidArea(new Dimension(230, 30)));
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
        add(viewerPanel);
        add(Box.createRigidArea(new Dimension(230, 30)));
        add(scriptPanel);

    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();

        MainMenu menu   =   mainPanel.getMenu();
        
        if(src == mainPanel.getMenu().getMenuItem("aboutItem"))
            mainPanel.getMenu().showAbout();

        else if(src == mainPanel.getMenu().getMenuItem("exitItem"))
            System.exit(0);

        else if(src == menu.getMenuItem("miniItem"))
            mainPanel.getFrame().setState(JFrame.ICONIFIED);

        else if(src == menu.getMenuItem("maxItem"))
            mainPanel.getFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);

        else if(src == menu.getMenuItem("importGraphItem"))
            ioPanel.importGraph();

        else if(src == menu.getMenuItem("exportGraphItem"))
            ioPanel.exportGraph();

        else if(src == menu.getMenuItem("importLogItem"))
            ioPanel.importLog();

        else if(src == menu.getMenuItem("exportLogItem"))
            ioPanel.exportLog();

        else if(src == menu.getMenuItem("vLabelsItem"))
            mainPanel.getScreenPanel().getGraphPanel().showVertexLabels(true);

        else if(src == menu.getMenuItem("eLabelsItem"))
            mainPanel.getScreenPanel().getGraphPanel().showEdgeLabels(true);

        else if(src == menu.getMenuItem("viewerBGItem"))
            viewerPanel.showViewerBGChange();

        else if(src == menu.getMenuItem("edgeBGItem"))
            viewerPanel.showEdgeBGChange();

        else if(src == menu.getMenuItem("vertexBGItem"))
            viewerPanel.showVertexBGChange();

        else if(src == menu.getMenuItem("clearLogItem"))
            mainPanel.getScreenPanel().getOutputPanel().clearLog();

        else if(src == menu.getMenuItem("resetGraphItem"))
            mainPanel.getScreenPanel().getGraphPanel().resetGraph();

        else if(src == menu.getMenuItem("addVertexItem"))
            mainPanel.getScreenPanel().getDataPanel().addVertex();

        else if(src == menu.getMenuItem("editVertexItem"))
            mainPanel.getScreenPanel().getDataPanel().editVertex();

        else if(src == menu.getMenuItem("removeVertexItem"))
            mainPanel.getScreenPanel().getDataPanel().removeVertex();

        else if(src == menu.getMenuItem("addEdgeItem"))
            mainPanel.getScreenPanel().getDataPanel().addEdge();

        else if(src == menu.getMenuItem("editEdgeItem"))
            mainPanel.getScreenPanel().getDataPanel().editEdge();

        else if(src == menu.getMenuItem("removeEdgeItem"))
            mainPanel.getScreenPanel().getDataPanel().removeEdge();

        else if(src == menu.getMenuItem("loadPluginItem"))
            ioPanel.importPlugin();
        
        else if(src == menu.getMenuItem("searchObjectItem"))
            mainPanel.getScreenPanel().getGraphPanel().searchGraphObject();

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

    public ModeControlPanel getModePanel() 
    {
        return modePanel;
    }

    public ScriptControlPanel getScriptPanel() 
    {
        return scriptPanel;
    }

    public SimulationControlPanel getSimulationPanel() 
    {
        return simulationPanel;
    }

    public ViewerControlPanel getViewerPanel()
    {
        return viewerPanel;
    }
    
    public TaskControlPanel getTaskPanel()
    {
        return taskPanel;
    }
    
    public MainPanel getMainPanel()
    {
        return mainPanel;
    }
}