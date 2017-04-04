//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.AppResources;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;

public class AboutPanel extends MenuSceneTemplate
{
    public AboutPanel()
    {
        sceneTitlePanel     =   new AboutTitlePanel();
        sceneControlPanel   =   new SceneControlPanel();

        add(sceneTitlePanel, BorderLayout.NORTH);
        add(sceneControlPanel, BorderLayout.CENTER);
    }
    
    private class AboutTitlePanel extends MenuSceneTemplate.SceneTitlePanel
    {
        private AboutTitlePanel()
        {
            titleLabel.setText("About");
            titleLabel.setIcon(new ImageIcon(AppResources.getInstance().getResource("aboutTitleIcon")));
        }
    }
}
