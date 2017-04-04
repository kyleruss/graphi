//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.app.Consts;
import com.graphi.config.ConfigManager;
import com.graphi.display.AppResources;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
        private final JLabel userLabel, universityLabel, repositoryLabel;
        private final JLabel licenseLabel, versionLabel;
        private final JLabel userTitleLabel, universityTitleLabel, repositoryTitleLabel;
        private final JLabel licenseTitleLabel, versionTitleLabel;
        
        private AboutContentPanel()
        {
            setBackground(Color.WHITE);
            
            userLabel               =   new JLabel(Consts.ABOUT_NAME);
            universityLabel         =   new JLabel(Consts.ABOUT_UNIVERSITY);
            repositoryLabel         =   new JLabel(Consts.REPO_LINK);
            licenseLabel            =   new JLabel(Consts.ABOUT_LICENSE);
            versionLabel            =   new JLabel(ConfigManager.getInstance().getVersionConfig().toString());
            userTitleLabel          =   new JLabel("Author");
            universityTitleLabel    =   new JLabel("University");
            repositoryTitleLabel    =   new JLabel("Repository");
            licenseTitleLabel       =   new JLabel("License");
            versionTitleLabel       =   new JLabel("Version");
            Font aboutFont  =   new Font("Arial", Font.BOLD, 16);
            
            userLabel.setFont(aboutFont);
            universityLabel.setFont(aboutFont);
            repositoryLabel.setFont(aboutFont);
            licenseLabel.setFont(aboutFont);
            versionLabel.setFont(aboutFont);
            
            userTitleLabel.setFont(aboutFont);
            universityTitleLabel.setFont(aboutFont);
            repositoryTitleLabel.setFont(aboutFont);
            licenseTitleLabel.setFont(aboutFont);
            versionTitleLabel.setFont(aboutFont);
            
            AppResources resources  =   AppResources.getInstance();
            userTitleLabel.setIcon(new ImageIcon(resources.getResource("aboutUserIcon")));
            universityTitleLabel.setIcon(new ImageIcon(resources.getResource("aboutUniversityIcon")));
            licenseTitleLabel.setIcon(new ImageIcon(resources.getResource("aboutLicenseIcon")));
            versionTitleLabel.setIcon(new ImageIcon(resources.getResource("aboutVersionIcon")));
            repositoryTitleLabel.setIcon(new ImageIcon(resources.getResource("aboutGithubIcon")));
            
            JPanel wrapper  =   new JPanel(new GridLayout(5, 2, 1, 50));
            wrapper.setBackground(Color.WHITE);
            wrapper.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
            
            wrapper.add(userTitleLabel);
            wrapper.add(userLabel);
            wrapper.add(universityTitleLabel);
            wrapper.add(universityLabel);
            wrapper.add(licenseTitleLabel);
            wrapper.add(licenseLabel);
            wrapper.add(repositoryTitleLabel);
            wrapper.add(repositoryLabel);
            wrapper.add(versionTitleLabel);
            wrapper.add(versionLabel);
            add(wrapper);
        }
    }
}
