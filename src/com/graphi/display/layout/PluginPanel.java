//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.AppResources;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;

public class PluginPanel extends MenuSceneTemplate
{
    public PluginPanel()
    {
        sceneTitlePanel     =   new PluginTitlePanel();
        sceneControlPanel   =   new SceneControlPanel();
        
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
}
