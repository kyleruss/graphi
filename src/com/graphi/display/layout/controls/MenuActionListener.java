//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.display.MainMenu;
import com.graphi.display.Window;
import com.graphi.display.layout.ControlPanel;
import com.graphi.display.layout.DataPanel;
import com.graphi.display.layout.GraphPanel;
import com.graphi.display.layout.OutputPanel;
import com.graphi.display.layout.ViewPort;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

public class MenuActionListener implements ActionListener
{
    
    public MenuActionListener() {}
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JMenuItem src              =   (JMenuItem) e.getSource();

        MainMenu menu           =   MainMenu.getInstance();
        GraphPanel graphPanel   =   GraphPanel.getInstance();
        DataPanel dataPanel     =   DataPanel.getInstance();
        IOControlPanel ioPanel  =   ControlPanel.getInstance().getIoPanel();
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
}
