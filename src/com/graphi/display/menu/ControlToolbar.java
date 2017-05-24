//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.menu;

import com.graphi.display.AppResources;
import com.graphi.display.layout.ControlPanel;
import com.graphi.display.layout.GraphPanel;
import com.graphi.display.layout.OutputPanel;
import com.graphi.display.layout.util.ComponentUtils;
import com.graphi.io.ProjectStore;
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
    
    private JPopupMenu clearPopupMenu, executePopupMenu, tasksPopupMenu;
    private JMenuItem clearLogItem, clearDisplayItem;
    private JMenuItem execGeneratorItem, execCompItem;
    private JMenuItem execSetupTasksItem, execRepeatTasksItem, showTasksItem;
    
    public ControlToolbar()
    {
        AppResources resources  =   AppResources.getInstance();
        clearPopupMenu          =   new JPopupMenu();
        executePopupMenu        =   new JPopupMenu();
        tasksPopupMenu          =   new JPopupMenu();
        saveBtn                 =   new JButton(new ImageIcon(resources.getResource("toolbarSaveIcon")));
        newBtn                  =   new JButton(new ImageIcon(resources.getResource("toolbarNewIcon")));
        openBtn                 =   new JButton(new ImageIcon(resources.getResource("toolbarOpenIcon")));
        clearBtn                =   ComponentUtils.generateDropdownButton(clearPopupMenu, null, new ImageIcon(resources.getResource("toolbarClearIcon")));
        searchBtn               =   new JButton(new ImageIcon(resources.getResource("toolbarSearchIcon")));
        executeBtn              =   ComponentUtils.generateDropdownButton(executePopupMenu, null, new ImageIcon(resources.getResource("toolbarExecuteIcon")));
        recordBtn               =   new JButton(new ImageIcon(resources.getResource("toolbarRecordIcon")));
        playBtn                 =   new JButton(new ImageIcon(resources.getResource("toolbarPlayIcon")));
        tasksBtn                =   ComponentUtils.generateDropdownButton(tasksPopupMenu, null, new ImageIcon(resources.getResource("toolbarTasksIcon")));
        
        clearLogItem            =   new JMenuItem("Clear Log");
        clearDisplayItem        =   new JMenuItem("Clear Display");
        execGeneratorItem       =   new JMenuItem("Run Network Generator");
        execCompItem            =   new JMenuItem("Run Computations");
        execSetupTasksItem      =   new JMenuItem("Run Setup Tasks");
        execRepeatTasksItem     =   new JMenuItem("Run Repeatable Tasks");
        showTasksItem           =   new JMenuItem("Manage Tasks");
        
        clearPopupMenu.add(clearDisplayItem);
        clearPopupMenu.add(clearLogItem);
        executePopupMenu.add(execGeneratorItem);
        executePopupMenu.add(execCompItem);
        tasksPopupMenu.add(showTasksItem);
        tasksPopupMenu.add(execSetupTasksItem);
        tasksPopupMenu.add(execRepeatTasksItem);
        
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
        execGeneratorItem.addActionListener(this);
        execCompItem.addActionListener(this);
        execSetupTasksItem.addActionListener(this);
        execRepeatTasksItem.addActionListener(this);
        showTasksItem.addActionListener(this);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src                  =   e.getSource();
        ControlPanel controlPanel   =   ControlPanel.getInstance();  
        GraphPanel graphPanel       =   GraphPanel.getInstance();
        
        if(src == newBtn)
            ProjectStore.newProject();
        
        else if(src == saveBtn)
            ProjectStore.saveProject();
        
        else if(src == openBtn)
            ProjectStore.loadProject();
        
        else if(src == clearLogItem)
            OutputPanel.getInstance().clearLog();
        
        else if(src == clearDisplayItem)
            controlPanel.getSimulationPanel().resetSim();
        
        else if(src == execGeneratorItem)
            controlPanel.getSimulationPanel().executeGeneratorSim(null);
        
        else if(src == searchBtn)
            graphPanel.searchGraphObject();
        
        else if(src == showTasksItem)
            controlPanel.getTaskPanel().showTasksDialog();
        
        else if(src == execSetupTasksItem)
            controlPanel.getTaskPanel().executeActions(true, 1);
        
        else if(src == execRepeatTasksItem)
            controlPanel.getTaskPanel().runRepeat();
        
        else if(src == recordBtn)
        {
            graphPanel.changePlaybackPanel(GraphPanel.RECORD_CARD);
            graphPanel.addRecordedGraph();
        }
        
        else if(src == playBtn)
        {
            graphPanel.changePlaybackPanel(GraphPanel.PLAYBACK_CARD);
            graphPanel.togglePlayback();
        }
    }
}
