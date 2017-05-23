//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.menu;

import com.graphi.display.AppResources;
import com.graphi.display.layout.util.ComponentUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

public class ControlToolbar extends JToolBar implements ActionListener
{
    private JButton saveBtn;
    private JButton newBtn;
    private JButton openBtn;
    private JButton clearBtn;
    private JButton searchBtn;
    private JButton executeBtn;
    private JButton recordBtn;
    private JButton playBtn;
    private JButton tasksBtn;
    
    private JPopupMenu clearPopupMenu, executePopupMenu;
    private JMenuItem clearLogItem, clearDisplayItem;
    private JMenuItem execTasksItem, execGeneratorItem, execCompItem;
    
    public ControlToolbar()
    {
        AppResources resources  =   AppResources.getInstance();
        clearPopupMenu          =   new JPopupMenu();
        executePopupMenu        =   new JPopupMenu();
        saveBtn                 =   new JButton(new ImageIcon(resources.getResource("toolbarSaveIcon")));
        newBtn                  =   new JButton(new ImageIcon(resources.getResource("toolbarNewIcon")));
        openBtn                 =   new JButton(new ImageIcon(resources.getResource("toolbarOpenIcon")));
        clearBtn                =   ComponentUtils.generateDropdownButton(clearPopupMenu, null, new ImageIcon(resources.getResource("toolbarClearIcon")));
        searchBtn               =   new JButton(new ImageIcon(resources.getResource("toolbarSearchIcon")));
        executeBtn              =   ComponentUtils.generateDropdownButton(executePopupMenu, null, new ImageIcon(resources.getResource("toolbarExecuteIcon")));
        recordBtn               =   new JButton(new ImageIcon(resources.getResource("toolbarRecordIcon")));
        playBtn                 =   new JButton(new ImageIcon(resources.getResource("toolbarPlayIcon")));
        tasksBtn                =   new JButton(new ImageIcon(resources.getResource("toolbarTasksIcon")));
        
        clearLogItem            =   new JMenuItem("Clear Log");
        clearDisplayItem        =   new JMenuItem("Clear Display");
        execTasksItem           =   new JMenuItem("Run Tasks");
        execGeneratorItem       =   new JMenuItem("Run Network Generator");
        execCompItem            =   new JMenuItem("Run Computations");
        
        clearPopupMenu.add(clearDisplayItem);
        clearPopupMenu.add(clearLogItem);
        executePopupMenu.add(execTasksItem);
        executePopupMenu.add(execGeneratorItem);
        executePopupMenu.add(execCompItem);
        
        newBtn.setToolTipText("New Project");
        saveBtn.setToolTipText("Save Project");
        openBtn.setToolTipText("Open Project");
        clearBtn.setToolTipText("Clear");
        searchBtn.setToolTipText("Search");
        executeBtn.setToolTipText("Execute");
        recordBtn.setToolTipText("Record Graph");
        playBtn.setToolTipText("Playback recording");
        tasksBtn.setToolTipText("Tasks");
        
        saveBtn.setFocusable(false);
        newBtn.setFocusable(false);
        openBtn.setFocusable(false);
        clearBtn.setFocusable(false);
        searchBtn.setFocusable(false);
        executeBtn.setFocusable(false);
        recordBtn.setFocusable(false);
        playBtn.setFocusable(false);
        tasksBtn.setFocusable(false);
        
        add(newBtn);
        add(openBtn);
        add(saveBtn);
        addSeparator();
        add(clearBtn);
        add(searchBtn);
        add(executeBtn);
        addSeparator();
        add(tasksBtn);
        add(recordBtn);
        add(playBtn);
        
        newBtn.addActionListener(this);
        openBtn.addActionListener(this);
        saveBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        searchBtn.addActionListener(this);
        executeBtn.addActionListener(this);
        tasksBtn.addActionListener(this);
        recordBtn.addActionListener(this);
        playBtn.addActionListener(this);
        clearLogItem.addActionListener(this);
        clearDisplayItem.addActionListener(this);
        execTasksItem.addActionListener(this);
        execGeneratorItem.addActionListener(this);
        execCompItem.addActionListener(this);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
    }
}
