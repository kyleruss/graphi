//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.AppResources;
import com.graphi.display.layout.controls.options.ViewerOptionPanel;
import com.graphi.util.ComponentUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
        
        private final AdvancedSettingsPanel advancedPanel;
        private final CustomizationSettingsPanel customizationPanel;
        private final ViewerOptionPanel viewingPanel;
        private final SettingsControlPanel controlPanel;
        private final JTabbedPane settingsTabPane;
        
        private SettingsContentPanel()
        {
            setBackground(Color.WHITE);
            setLayout(new BorderLayout());
            
            advancedPanel       =   new AdvancedSettingsPanel();
            customizationPanel  =   new CustomizationSettingsPanel();
            viewingPanel        =   new ViewerOptionPanel();
            controlPanel        =   new SettingsControlPanel();
            settingsTabPane     =   new JTabbedPane();
            
            
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
            }

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Object src  =   e.getSource();
                
            }
        }
        
        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
        private class AdvancedSettingsPanel extends JPanel
        {
            private AdvancedSettingsPanel()
            {
            }
            
            @Override
            protected void paintComponent(Graphics g)
            {
                SettingsContentPanel.this.paintComponent(g);
            }
        }
        
        private class CustomizationSettingsPanel extends JPanel
        {
            private CustomizationSettingsPanel()
            {
            }
            
            @Override
            protected void paintComponent(Graphics g)
            {
                SettingsContentPanel.this.paintComponent(g);
            }
        }
        
    }
}
