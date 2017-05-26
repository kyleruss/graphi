//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.config.ConfigManager;
import com.graphi.config.PluginConfig;
import com.graphi.display.AppResources;
import com.graphi.plugins.Plugin;
import com.graphi.plugins.PluginManager;
import com.graphi.display.layout.util.ComponentUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class PluginPanel extends MenuSceneTemplate
{
    public PluginPanel()
    {
        sceneTitlePanel     =   new PluginTitlePanel();
        sceneControlPanel   =   new PluginContentPanel();
        
        add(sceneTitlePanel, BorderLayout.NORTH);
        add(sceneControlPanel, BorderLayout.CENTER);
    }

    @Override
    public void onSceneLoad()
    {
        activateMenuItem(MenuSceneTemplate.PLUGINS_ITEM);
    }
    
    private class PluginTitlePanel extends MenuSceneTemplate.SceneTitlePanel
    {
        private PluginTitlePanel()
        {
            titleLabel.setText("Plugins");
            titleLabel.setIcon(new ImageIcon(AppResources.getInstance().getResource("pluginsTitleIcon")));
        }
    }
    
    private class PluginContentPanel extends MenuSceneTemplate.SceneControlPanel implements ActionListener
    {
        private final String STATUS_ACTIVE  =   "Active";
        private final String STATUS_DEFAULT =   "Default";
        private final String STATUS_LOADED  =   "Loaded";
        
        private JTable pluginTable;
        private JButton addPluginBtn;
        private JButton defaultPluginBtn;
        private JButton activatePluginBtn;
        private JButton savePluginsBtn;
        private JButton aboutPluginBtn;
        private JTextField pluginDirField;
        private DefaultTableModel pluginTableModel; 
        private JScrollPane pluginScrollPane;
        private int defaultRow;
        private int activeRow;
        
        private PluginContentPanel()
        {
            setBackground(Color.WHITE);
            
            AppResources resources  =   AppResources.getInstance();
            defaultRow              =   0;
            activeRow               =   0;
            pluginTableModel        =   new DefaultTableModel();
            pluginTable             =   new JTable(pluginTableModel);
            addPluginBtn            =   new JButton("");
            defaultPluginBtn        =   new JButton("");
            aboutPluginBtn          =   new JButton("");
            activatePluginBtn       =   new JButton("");
            pluginScrollPane        =   new JScrollPane(pluginTable);   
            savePluginsBtn          =   new JButton(new ImageIcon(resources.getResource("saveLargeBtn")));
            pluginDirField          =   new JTextField();
            JLabel pluginDirLabel   =   new JLabel("Plugin Directory");
            JPanel controlWrapper   =   new JPanel(new GridLayout(4, 1));
            JPanel contentWrapper   =   new JPanel(new BorderLayout());
            JPanel pluginDirWrapper =   new JPanel(new BorderLayout());
            JPanel outerWrapper     =   new JPanel(new BorderLayout());
            JPanel btmCtrlWrapper   =   new JPanel();
            
            addPluginBtn.setIcon(new ImageIcon(resources.getResource("pluginOpenIcon")));
            defaultPluginBtn.setIcon(new ImageIcon(resources.getResource("pluginDefaultIcon")));
            aboutPluginBtn.setIcon(new ImageIcon(resources.getResource("pluginInfoIcon")));
            activatePluginBtn.setIcon(new ImageIcon(resources.getResource("pluginActivateIcon")));
            
            addPluginBtn.setToolTipText("Load plugin");
            defaultPluginBtn.setToolTipText("Set default");
            aboutPluginBtn.setToolTipText("About plugin");
            activatePluginBtn.setToolTipText("Activate plugin");
            
            pluginTableModel.addColumn("Status");
            pluginTableModel.addColumn("Name");
            pluginTableModel.addColumn("Path");
            pluginDirLabel.setIcon(new ImageIcon(resources.getResource("docsIcon")));
            ComponentUtils.setTransparentControl(savePluginsBtn);
            pluginDirLabel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
            outerWrapper.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            pluginDirWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            contentWrapper.setPreferredSize(new Dimension(500, 400));
            
            addPluginBtn.setFocusable(false);
            defaultPluginBtn.setFocusable(false);
            activatePluginBtn.setFocusable(false);
            pluginTable.setFocusable(false);
            controlWrapper.setBackground(Color.WHITE);
            contentWrapper.setBackground(Color.WHITE);
            outerWrapper.setBackground(Color.WHITE);
            btmCtrlWrapper.setBackground(Color.WHITE);
            pluginDirWrapper.setBackground(Color.WHITE);
            
            pluginDirWrapper.add(pluginDirLabel, BorderLayout.WEST);
            pluginDirWrapper.add(pluginDirField, BorderLayout.CENTER);
            
            btmCtrlWrapper.add(savePluginsBtn);
            controlWrapper.add(addPluginBtn);
            controlWrapper.add(activatePluginBtn);
            controlWrapper.add(defaultPluginBtn);
            controlWrapper.add(aboutPluginBtn);
            
            contentWrapper.add(pluginScrollPane, BorderLayout.CENTER);
            contentWrapper.add(controlWrapper, BorderLayout.EAST);
            contentWrapper.add(pluginDirWrapper, BorderLayout.SOUTH);
            
            outerWrapper.add(contentWrapper, BorderLayout.CENTER);
            outerWrapper.add(btmCtrlWrapper, BorderLayout.SOUTH);
            add(outerWrapper);
            
            savePluginsBtn.addActionListener(this);
            addPluginBtn.addActionListener(this);
            activatePluginBtn.addActionListener(this);
            defaultPluginBtn.addActionListener(this);
        }
        
        private void setDefaultPlugin()
        {
            int prevRow     =   defaultRow;
            int selectedRow =   pluginTable.getSelectedRow();
            
            if(prevRow == selectedRow) return;
            
            if(selectedRow != -1)
            {
                if(prevRow == activeRow)
                {
                    pluginTable.setValueAt(STATUS_ACTIVE, prevRow, 0);
                    pluginTable.setValueAt(STATUS_LOADED + ", " + STATUS_DEFAULT, selectedRow, 0);
                }
                
                else
                {
                    pluginTable.setValueAt(STATUS_LOADED, prevRow, 0);
                    
                    if(selectedRow == activeRow)
                        pluginTable.setValueAt(STATUS_ACTIVE + ", " + STATUS_DEFAULT, selectedRow, 0);
                }
                
                defaultRow  =   selectedRow;
            }
            
            else JOptionPane.showMessageDialog(null, "Please select a plugin");
        }
        
        private void setActivePlugin()
        {
            int prevRow     =   activeRow;
            int selectedRow =   pluginTable.getSelectedRow();
            
            if(prevRow == selectedRow) return;
            
            if(selectedRow != -1)
            {
                if(prevRow == defaultRow)
                {
                    pluginTable.setValueAt(STATUS_LOADED + ", " + STATUS_DEFAULT, prevRow, 0);
                    pluginTable.setValueAt(STATUS_ACTIVE, selectedRow, 0);
                }
                
                else 
                {
                    pluginTable.setValueAt(STATUS_LOADED, prevRow, 0);
                    
                    if(selectedRow == defaultRow)
                        pluginTable.setValueAt(STATUS_ACTIVE + ", " + STATUS_DEFAULT, selectedRow, 0);
                }
                
                activeRow           =   selectedRow;
                String pluginName   =   (String) pluginTable.getValueAt(activeRow, 0);
                PluginManager.getInstance().activatePlugin(pluginName);
            }
            
            else JOptionPane.showMessageDialog(null, "Please select a plugin");
        }
        
        private void addPluginToList(String name, String path)
        {
            pluginTableModel.addRow(new String[] { STATUS_LOADED, name, path });
        }
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            Object src  =   e.getSource();
            
            if(src == savePluginsBtn)
                updateConfig();
            
            else if(src == defaultPluginBtn)
                setDefaultPlugin();
            
            else if(src == activatePluginBtn)
                setActivePlugin();
            
            else if(src == addPluginBtn)
                importPlugin();
        }
    }
    
    public void initConfig()
    {
        PluginContentPanel panel    =   (PluginContentPanel) sceneControlPanel;
        PluginConfig pluginConfig   =   ConfigManager.getInstance().getPluginConfig();
        panel.defaultRow            =   pluginConfig.getDefaultPluginIndex();
        panel.pluginDirField.setText(pluginConfig.getDefaultPluginPath());
        List<String> pluginPaths    =   pluginConfig.getLoadedPluginPaths();
        String activeStr            =   panel.STATUS_ACTIVE + ", " + panel.STATUS_DEFAULT;
        panel.pluginTableModel.setRowCount(0);
        panel.pluginTableModel.addRow(new String[] {"", "Base", "N/A"});

        if(!pluginPaths.isEmpty())
        {
            Set<String> pluginNames     =   PluginManager.getInstance().getPlugins().keySet();
            int index                   =   0;

            for(String pluginName : pluginNames)
            {
                String pluginPath  =   pluginPaths.get(index);
                panel.pluginTableModel.addRow(new String[] {index == panel.defaultRow? activeStr : "", pluginName, pluginPath });
                index++;
            }
        }

        else panel.pluginTableModel.setValueAt(activeStr, 0, 0);
    }

    public void updateConfig()
    {
        PluginContentPanel panel    =   (PluginContentPanel) sceneControlPanel;
        PluginConfig pluginConfig   =   ConfigManager.getInstance().getPluginConfig();
        pluginConfig.setPluginDirectory(panel.pluginDirField.getText());

        if(panel.pluginTableModel.getRowCount() > 1)
        {
            List<String> pluginPathList =   new ArrayList<>();
            for(int row = 0; row < panel.pluginTable.getRowCount(); row++)
                pluginPathList.add(panel.pluginTable.getValueAt(row, 1).toString());

            pluginConfig.setLoadedPluginPaths(pluginPathList);
        }


        pluginConfig.setDefaultPluginIndex(panel.defaultRow);
    }
    
    public void importPlugin()
    {
        File file    =   ComponentUtils.getFile(true, "Graphi .jar plugin", "jar");
        if(file == null) return;

        PluginContentPanel panel    =   (PluginContentPanel) sceneControlPanel;
        PluginManager pm            =   PluginManager.getInstance();
        Plugin plugin               =   pm.fetchPlugin(file);
        String defaultPath          =   ConfigManager.getInstance().getPluginConfig().getDefaultPluginPath();
        boolean isDefaultPath       =   file.getPath().equals(defaultPath);

        if(plugin == null)
            JOptionPane.showMessageDialog(null, "Failed to load plugin");

        else
        {
            if(pm.hasPlugin(plugin) && !isDefaultPath)
                JOptionPane.showMessageDialog(null, "Plugin is already loaded");

            else
            {
                panel.addPluginToList(plugin.getPluginName(), file.getPath());

                if(!isDefaultPath)
                    pm.addPlugin(plugin);
            }
        }
    }
}
