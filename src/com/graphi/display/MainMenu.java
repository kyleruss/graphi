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
import javax.swing.SwingConstants;

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
    
    private void showAbout()
    {
        JLabel nameLabel    =   new JLabel("Kyle Russell 2015", SwingConstants.CENTER);
        JLabel locLabel     =   new JLabel("AUT University");
        JLabel repoLabel    =   new JLabel("https://github.com/denkers/graphi");
        
        JPanel aboutPanel   =   new JPanel();
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
        aboutPanel.add(nameLabel);
        aboutPanel.add(locLabel);
        aboutPanel.add(repoLabel);
        
        JOptionPane.showMessageDialog(null, aboutPanel, "Graphi - About", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        
        if(src == aboutItem)
            showAbout();
    }
}
