//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class MenuSceneTemplate extends JPanel
{
    protected SceneTitlePanel sceneTitlePanel;
    protected SceneControlPanel sceneControlPanel;
    
    public MenuSceneTemplate()
    {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
    }
    
    protected class SceneTitlePanel extends JPanel
    {
        protected JLabel titleLabel;
        
        protected SceneTitlePanel()
        {
            setBackground(Color.WHITE);
            
            titleLabel  =   new JLabel("Options");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
            titleLabel.setForeground(Color.DARK_GRAY);
            titleLabel.setIcon(new ImageIcon(AppResources.getInstance().getResource("settingsTitleIcon")));
            titleLabel.setIconTextGap(20);
            
            add(Box.createRigidArea(new Dimension(100, 200)));
            add(titleLabel);
        }
    }
    
    protected class SceneControlPanel extends JPanel
    {
        protected SceneControlPanel()
        {
            setBackground(Color.WHITE);
        }
    }
}
