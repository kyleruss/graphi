//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainMenu extends JMenuBar
{
    protected final JMenu fileMenu;
    protected final JMenu helpMenu;
    protected final JMenu optionsMenu;
    protected final JMenu graphMenu;
    protected final JMenu loggingMenu;
    protected final JMenu vertexMenu, edgeMenu;
    protected final JMenu viewMenu;
    protected final JMenu pluginMenu;
    protected final JMenu pluginListMenu;
    
    protected Map<String, JMenuItem> menuItems;

    public MainMenu()
    {
        menuItems           =   new HashMap<>();
        fileMenu            =   new JMenu("File");
        optionsMenu         =   new JMenu("Options");
        helpMenu            =   new JMenu("About");
        graphMenu           =   new JMenu("Graph");
        loggingMenu         =   new JMenu("Log");
        vertexMenu          =   new JMenu("Vertex");
        edgeMenu            =   new JMenu("Edge");
        viewMenu            =   new JMenu("View");
        pluginMenu          =   new JMenu("Plugin");
        pluginListMenu      =   new JMenu("Plugins");
        
        addMenuItem("exitItem", new JMenuItem("Exit"), fileMenu);
        addMenuItem("miniItem", new JMenuItem("Minimize"), fileMenu);
        addMenuItem("maxItem", new JMenuItem("Maximize"), fileMenu);
        addMenuItem("aboutItem", new JMenuItem("Author"), helpMenu);
        addMenuItem("importGraphItem", new JMenuItem("Import Graph"), graphMenu);
        addMenuItem("exportGraphItem", new JMenuItem("Export Graph"), graphMenu);
        addMenuItem("importLogItem", new JMenuItem("Import Log"), graphMenu);
        addMenuItem("exportLogItem", new JMenuItem("Export Log"), graphMenu);
        addMenuItem("vLabelsItem", new JMenuItem("Vertex labels"), viewMenu);
        addMenuItem("eLabelsItem", new JMenuItem("Eedge labels"), viewMenu);
        addMenuItem("viewerBGItem", new JMenuItem("Change viewer background"), optionsMenu);
        addMenuItem("edgeBGItem", new JMenuItem("Change edge fill"), optionsMenu);
        addMenuItem("vertexBGItem", new JMenuItem("Change Vertex fill"), optionsMenu);
        addMenuItem("clearLogItem", new JMenuItem("Clear"), loggingMenu);
        addMenuItem("resetGraphItem", new JMenuItem("Reset"), graphMenu);
        addMenuItem("addVertexItem", new JMenuItem("Add"), vertexMenu);
        addMenuItem("editVertexItem", new JMenuItem("Edit"), vertexMenu);
        addMenuItem("removeVertexItem", new JMenuItem("Remove"), vertexMenu);
        addMenuItem("addEdgeItem", new JMenuItem("Add"), edgeMenu);
        addMenuItem("editEdgeItem", new JMenuItem("Edit"), edgeMenu);
        addMenuItem("removeEdgeItem", new JMenuItem("Remove"), edgeMenu);
        addMenuItem("loadPluginItem", new JMenuItem("Load"), pluginMenu);
        addMenuItem("defaultPluginItem", new JMenuItem("Default"), pluginMenu);
        
        graphMenu.add(vertexMenu);
        graphMenu.add(edgeMenu);
        pluginMenu.add(pluginListMenu);
        pluginMenu.addSeparator();
        
        add(fileMenu);
        add(graphMenu);
        add(loggingMenu);
        add(viewMenu);
        add(optionsMenu);
        add(helpMenu);
        add(pluginMenu);
    }
    
    public void addMenuItem(String name, JMenuItem item, JMenu menu)
    {
        menuItems.put(name, item);
        menu.add(item);
    }
    
    public JMenuItem getMenuItem(String name)
    {
        return menuItems.get(name);
    }
    
    public Map<String, JMenuItem> getMenuItems()
    {
        return menuItems;
    }
    
    public void setMenuItemListener(ActionListener listener)
    {
        for(JMenuItem item : menuItems.values())
            item.addActionListener(listener);
    }
}
