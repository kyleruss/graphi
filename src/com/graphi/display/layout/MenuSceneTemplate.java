//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.AppResources;
import com.graphi.util.ComponentUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
        private JButton homeBtn;
        
        protected SceneTitlePanel()
        {
            setBackground(Color.WHITE);
            setLayout(new BorderLayout());
            
            AppResources resources  =   AppResources.getInstance();
            homeBtn     =   new JButton();
            homeBtn.setIcon(new ImageIcon(resources.getResource("homeIcon")));
            homeBtn.addActionListener(new TitleControlListener());
            ComponentUtils.setTransparentControl(homeBtn);
            
            JPanel homeBtnWrapper   =   new JPanel();
            homeBtnWrapper.setBackground(Color.WHITE);
            homeBtnWrapper.add(homeBtn);
            
            titleLabel  =   new JLabel();
            titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
            titleLabel.setForeground(Color.DARK_GRAY);
            titleLabel.setIcon(new ImageIcon(resources.getResource("settingsTitleIcon")));
            titleLabel.setIconTextGap(20);
            JPanel titleWrapper =   new JPanel();
            titleWrapper.setBackground(Color.WHITE);
            titleWrapper.add(titleLabel);
            
            add(Box.createRigidArea(new Dimension(1, 50)), BorderLayout.NORTH);
            add(homeBtnWrapper, BorderLayout.WEST);
            add(titleWrapper, BorderLayout.CENTER);
        }
        
        private class TitleControlListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Object src  =   e.getSource();
                
                if(src == homeBtn)
                    ViewPort.getInstance().setScene(ViewPort.TITLE_SCENE);
            }
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
