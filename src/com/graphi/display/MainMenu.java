package com.graphi.display;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainMenu extends JMenuBar implements ActionListener
{
    private final JMenu fileMenu;
    private final JMenu helpMenu;
    private final JMenu optionsMenu;
    
    private final JMenuItem saveItem, loadItem;
    private final JMenuItem exitItem;
    private final JMenuItem settingsItem;
    private final JMenuItem aboutItem;
    
    public MainMenu()
    {
        fileMenu        =   new JMenu("File");
        optionsMenu     =   new JMenu("Options");
        helpMenu        =   new JMenu("Help");
        
        saveItem        =   new JMenuItem("Save");
        loadItem        =   new JMenuItem("Open");
        exitItem        =   new JMenuItem("Exit");
        settingsItem    =   new JMenuItem("Settings");
        aboutItem       =   new JMenuItem("About");
        
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(exitItem);
        optionsMenu.add(settingsItem);
        helpMenu.add(aboutItem);
        
        add(fileMenu);
        add(optionsMenu);
        add(helpMenu);
        
        saveItem.addActionListener(this);
        loadItem.addActionListener(this);
        exitItem.addActionListener(this);
        settingsItem.addActionListener(this);
        aboutItem.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
    }
}
