//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.AppResources;
import com.graphi.display.layout.controls.options.AbstractOptionPanel;
import com.graphi.display.layout.controls.options.AdvancedOptionPanel;
import com.graphi.display.layout.controls.options.CustomizationOptionPanel;
import com.graphi.display.layout.controls.options.ViewerOptionPanel;
import com.graphi.display.layout.util.ComponentUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class SettingsPanel extends MenuSceneTemplate
{
    public SettingsPanel()
    {
        sceneTitlePanel     =   new SettingsTitlePanel();
        sceneControlPanel   =   new SettingsContentPanel();
        
        add(sceneTitlePanel, BorderLayout.NORTH);
        add(sceneControlPanel, BorderLayout.CENTER);
    }
    
    private class SettingsTitlePanel extends MenuSceneTemplate.SceneTitlePanel
    {
        private SettingsTitlePanel()
        {
            titleLabel.setText("Options");
            titleLabel.setIcon(new ImageIcon(AppResources.getInstance().getResource("settingsTitleIcon")));
        }
    }
    
    private class SettingsContentPanel extends MenuSceneTemplate.SceneControlPanel
    {
        private final int SETTINGS_WIDTH    =   700;
        private final int SETTINGS_HEIGHT   =   500;
        
        private final AdvancedOptionPanel advancedPanel;
        private final CustomizationOptionPanel customizationPanel;
        private final ViewerOptionPanel viewingPanel;
        private final SettingsControlPanel controlPanel;
        private final List<AbstractOptionPanel> optionPanels;
        private final JTabbedPane settingsTabPane;
        
        private SettingsContentPanel()
        {
            setBackground(Color.WHITE);
            setLayout(new BorderLayout());
            
            advancedPanel       =   new AdvancedOptionPanel();
            customizationPanel  =   new CustomizationOptionPanel();
            viewingPanel        =   new ViewerOptionPanel();
            controlPanel        =   new SettingsControlPanel();
            settingsTabPane     =   new JTabbedPane();
            optionPanels        =   new ArrayList<>();
            
            optionPanels.add(customizationPanel);
            optionPanels.add(viewingPanel);
            optionPanels.add(advancedPanel);
            
            AppResources resources  =   AppResources.getInstance();
            JLabel customTabLabel   =   new JLabel("Customization", JLabel.CENTER);
            JLabel viewTabLabel     =   new JLabel("Display", JLabel.CENTER);
            JLabel advancedTabLabel =   new JLabel("Advanced", JLabel.CENTER);
            customTabLabel.setIcon(new ImageIcon(resources.getResource("customizationTabIcon")));
            viewTabLabel.setIcon(new ImageIcon(resources.getResource("viewTabIcon")));
            advancedTabLabel.setIcon(new ImageIcon(resources.getResource("advancedTabIcon")));
            
            settingsTabPane.addTab("", customizationPanel);
            settingsTabPane.addTab("", viewingPanel);
            settingsTabPane.addTab("", advancedPanel);
            
            settingsTabPane.setTabComponentAt(0, customTabLabel);
            settingsTabPane.setTabComponentAt(1, viewTabLabel);
            settingsTabPane.setTabComponentAt(2, advancedTabLabel);
            
            settingsTabPane.setFont(new Font("Arial", Font.PLAIN, 14));
            settingsTabPane.setPreferredSize(new Dimension(SETTINGS_WIDTH, SETTINGS_HEIGHT));
            
            JPanel settingsContentWrapper   =   new JPanel();
            settingsContentWrapper.setBackground(Color.WHITE);
            settingsContentWrapper.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            settingsContentWrapper.add(settingsTabPane);
            settingsContentWrapper.add(settingsTabPane);
            
            add(settingsContentWrapper, BorderLayout.CENTER);
            add(controlPanel, BorderLayout.SOUTH);
        }
        
        private class SettingsControlPanel extends JPanel implements ActionListener
        {
            private final JButton saveSettingsBtn;
            
            private SettingsControlPanel()
            {
                setBackground(Color.WHITE);
                saveSettingsBtn =   new JButton();
                saveSettingsBtn.setIcon(new ImageIcon(AppResources.getInstance().getResource("saveLargeBtn")));
                ComponentUtils.setTransparentControl(saveSettingsBtn);
                
                add(saveSettingsBtn);
                saveSettingsBtn.addActionListener(this);
            }
            
            private void saveSettings()
            {
                for(AbstractOptionPanel optionPanel : optionPanels)
                {
                    optionPanel.updateOptions();
                    optionPanel.clearOptions();
                }
            }

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Object src  =   e.getSource();
                
                if(src == saveSettingsBtn)
                    saveSettings();
            }
        }
    }
}
