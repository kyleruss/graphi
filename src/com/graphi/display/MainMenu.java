package com.graphi.display;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainMenu extends JMenuBar
{
    protected final JMenu fileMenu;
    protected final JMenu helpMenu;
    protected final JMenu optionsMenu;
    
    protected final JMenuItem saveItem, loadItem;
    protected final JMenuItem exitItem;
    protected final JMenuItem settingsItem;
    protected final JMenuItem aboutItem;
    
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
        
    }
}
