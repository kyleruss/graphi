package com.graphi.display;

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
    
    protected final JMenuItem exitItem, miniItem, maxItem;
    protected final JMenuItem aboutItem;
    protected final JMenuItem importGraphItem, exportGraphItem;
    protected final JMenuItem importLogItem, exportLogItem;
    protected final JMenuItem vLabelsItem, eLabelsItem;
    protected final JMenuItem clearLogItem, resetGraphItem;
    protected final JMenuItem viewerBGItem, edgeBGItem, vertexBGItem;
    protected final JMenuItem addVertexItem, editVertexItem, removeVertexItem;
    protected final JMenuItem addEdgeItem, editEdgeItem, removeEdgeItem;
    

    public MainMenu()
    {
        fileMenu            =   new JMenu("File");
        optionsMenu         =   new JMenu("Options");
        helpMenu            =   new JMenu("Help");
        graphMenu           =   new JMenu("Graph");
        loggingMenu         =   new JMenu("Log");
        vertexMenu          =   new JMenu("Vertex");
        edgeMenu            =   new JMenu("Edge");
        viewMenu            =   new JMenu("View");
        
        exitItem            =   new JMenuItem("Exit");
        miniItem            =   new JMenuItem("Minimize");
        maxItem             =   new JMenuItem("Maximize");
        aboutItem           =   new JMenuItem("About");
        importGraphItem     =   new JMenuItem("Import Graph");
        exportGraphItem     =   new JMenuItem("Export Graph");
        importLogItem       =   new JMenuItem("Import Log");
        exportLogItem       =   new JMenuItem("Export Log");
        vLabelsItem         =   new JMenuItem("Vertex labels");
        eLabelsItem         =   new JMenuItem("Eedge labels");
        viewerBGItem        =   new JMenuItem("Change viewer background");
        edgeBGItem          =   new JMenuItem("Change edge fill");
        vertexBGItem        =   new JMenuItem("Change Vertex fill");
        clearLogItem        =   new JMenuItem("Clear");
        resetGraphItem      =   new JMenuItem("Reset");
        addVertexItem       =   new JMenuItem("Add");
        editVertexItem      =   new JMenuItem("Edit");
        removeVertexItem    =   new JMenuItem("Remove");
        addEdgeItem         =   new JMenuItem("Add");
        editEdgeItem        =   new JMenuItem("Edit");
        removeEdgeItem      =   new JMenuItem("Remove");
        
        
        fileMenu.add(exitItem);
        fileMenu.add(miniItem);
        fileMenu.add(maxItem);
        
        optionsMenu.add(viewerBGItem);
        optionsMenu.add(edgeBGItem);
        optionsMenu.add(vertexBGItem);
        
        vertexMenu.add(addVertexItem);
        vertexMenu.add(editVertexItem);
        vertexMenu.add(removeVertexItem);
        
        edgeMenu.add(addEdgeItem);
        edgeMenu.add(editEdgeItem);
        edgeMenu.add(removeEdgeItem);
        
        graphMenu.add(vertexMenu);
        graphMenu.add(edgeMenu);
        graphMenu.add(resetGraphItem);
        graphMenu.add(importGraphItem);
        graphMenu.add(exportGraphItem);
        
        loggingMenu.add(clearLogItem);
        loggingMenu.add(importLogItem);
        loggingMenu.add(exportLogItem);
        
        viewMenu.add(vLabelsItem);
        viewMenu.add(eLabelsItem);
        
        helpMenu.add(aboutItem);
        
        add(fileMenu);
        add(graphMenu);
        add(loggingMenu);
        add(viewMenu);
        add(optionsMenu);
        add(helpMenu);
        
    }
}
