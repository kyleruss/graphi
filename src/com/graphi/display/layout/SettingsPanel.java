//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.AppResources;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;

public class SettingsPanel extends MenuSceneTemplate
{
    public SettingsPanel()
    {
        sceneTitlePanel     =   new SettingsTitlePanel();
        sceneControlPanel   =   new SceneControlPanel();
        
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
}
