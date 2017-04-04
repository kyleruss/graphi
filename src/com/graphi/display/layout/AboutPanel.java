//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.AppResources;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class AboutPanel extends MenuSceneTemplate
{
    public AboutPanel()
    {
        sceneTitlePanel     =   new AboutTitlePanel();
        sceneControlPanel   =   new AboutContentPanel();

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
    
    private class AboutContentPanel extends MenuSceneTemplate.SceneControlPanel
    {
        private JLabel userLabel, universityLabel, repositoryLabel;
        private JLabel licenseLabel, versionLabel;
        
        private AboutContentPanel()
        {
            
        }
    }
}
