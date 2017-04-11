//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.config.ConfigManager;
import com.graphi.config.PluginConfig;
import com.graphi.display.AppResources;
import com.graphi.util.ComponentUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
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
    
    private class PluginTitlePanel extends MenuSceneTemplate.SceneTitlePanel
    {
        private PluginTitlePanel()
        {
            titleLabel.setText("Plugins");
            titleLabel.setIcon(new ImageIcon(AppResources.getInstance().getResource("pluginsTitleIcon")));
        }
    }
    
    private class PluginContentPanel extends MenuSceneTemplate.SceneControlPanel
    {
        private JTable pluginTable;
        private JButton addPluginBtn;
        private JButton defaultPluginBtn;
        private JButton activatePluginBtn;
        private JButton savePluginsBtn;
        private JTextField pluginDirField;
        private DefaultTableModel pluginTableModel; 
        private JScrollPane pluginScrollPane;
        
        private PluginContentPanel()
        {
            setBackground(Color.WHITE);
            
            AppResources resources  =   AppResources.getInstance();
            pluginTableModel        =   new DefaultTableModel();
            pluginTable             =   new JTable(pluginTableModel);
            addPluginBtn            =   new JButton("Load plugin");
            defaultPluginBtn        =   new JButton("Set default");
            activatePluginBtn       =   new JButton("Activate plugin");
            pluginScrollPane        =   new JScrollPane(pluginTable);   
            savePluginsBtn          =   new JButton(new ImageIcon(resources.getResource("saveLargeBtn")));
            pluginDirField          =   new JTextField();
            JLabel pluginDirLabel   =   new JLabel("Plugin Directory");
            JPanel controlWrapper   =   new JPanel(new GridLayout(3, 1));
            JPanel contentWrapper   =   new JPanel(new BorderLayout());
            JPanel pluginDirWrapper =   new JPanel(new BorderLayout());
            JPanel outerWrapper     =   new JPanel(new BorderLayout());
            JPanel btmCtrlWrapper   =   new JPanel();
            
            pluginTableModel.addColumn("Name");
            pluginTableModel.addColumn("Path");
            pluginDirLabel.setIcon(new ImageIcon(resources.getResource("docsIcon")));
            ComponentUtils.setTransparentControl(savePluginsBtn);
            pluginDirLabel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
            outerWrapper.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            pluginDirWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            
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
            
            contentWrapper.add(pluginScrollPane, BorderLayout.CENTER);
            contentWrapper.add(controlWrapper, BorderLayout.EAST);
            contentWrapper.add(pluginDirWrapper, BorderLayout.SOUTH);
            
            outerWrapper.add(contentWrapper, BorderLayout.CENTER);
            outerWrapper.add(btmCtrlWrapper, BorderLayout.SOUTH);
            add(outerWrapper);
        }
        
        public void updateConfig()
        {
            PluginConfig pluginConfig   =   ConfigManager.getInstance().getPluginConfig();
        }
    }
}
