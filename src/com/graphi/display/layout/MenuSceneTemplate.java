//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.AppResources;
import com.graphi.display.layout.util.ComponentUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public abstract class MenuSceneTemplate extends JPanel
{
    public static final int OPTIONS_ITEM    =   0;
    public static final int PLUGINS_ITEM    =   1;
    public static final int ABOUT_ITEM      =   2;
    
    protected SceneTitlePanel sceneTitlePanel;
    protected SceneControlPanel sceneControlPanel;
    
    public MenuSceneTemplate()
    {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
    }
    
    public abstract void onSceneLoad();
    
    public void activateMenuItem(int itemIndex)
    {
        switch (itemIndex) 
        {
            case OPTIONS_ITEM:
                sceneTitlePanel.setActiveItem(sceneTitlePanel.optionsItem);
                break;
            case PLUGINS_ITEM:
                sceneTitlePanel.setActiveItem(sceneTitlePanel.pluginItem);
                break;
            case ABOUT_ITEM:
                sceneTitlePanel.setActiveItem(sceneTitlePanel.aboutItem);
                break;
            default:
                break;
        }
    }
    
    protected class SceneTitlePanel extends JPanel
    {
        protected JLabel titleLabel;
        protected JButton menuButton;
        protected TitleControlListener controlListener;
        protected JPopupMenu popupMenu;
        protected JMenuItem mainMenuItem, graphControlsItem, optionsItem, pluginItem, aboutItem;
        protected JMenuItem activeMenuItem;
        
        protected SceneTitlePanel()
        {
            setBackground(Color.WHITE);
            setLayout(new BorderLayout());
            
            AppResources resources  =   AppResources.getInstance();
            controlListener =   new TitleControlListener();   
            menuButton      =   new JButton();
            menuButton.setIcon(new ImageIcon(resources.getResource("menuIcon")));
            menuButton.addMouseListener(controlListener);
            ComponentUtils.setTransparentControl(menuButton);
            
            JPanel homeBtnWrapper   =   new JPanel();
            homeBtnWrapper.setBackground(Color.WHITE);
            homeBtnWrapper.add(menuButton);
            
            popupMenu           =   new JPopupMenu();
            mainMenuItem        =   new JMenuItem("Menu");
            graphControlsItem   =   new JMenuItem("Controls");
            optionsItem         =   new JMenuItem("Options");
            pluginItem          =   new JMenuItem("Plugins");
            aboutItem           =   new JMenuItem("About");
            
            popupMenu.add(mainMenuItem);
            popupMenu.add(graphControlsItem);
            popupMenu.add(optionsItem);
            popupMenu.add(pluginItem);
            popupMenu.add(aboutItem);
            
            mainMenuItem.addActionListener(controlListener);
            graphControlsItem.addActionListener(controlListener);
            optionsItem.addActionListener(controlListener);
            pluginItem.addActionListener(controlListener);
            aboutItem.addActionListener(controlListener);
            
            titleLabel  =   new JLabel();
            titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
            titleLabel.setForeground(Color.DARK_GRAY);
            titleLabel.setIconTextGap(20);
            JPanel titleWrapper =   new JPanel(new FlowLayout(FlowLayout.LEFT));
            titleWrapper.setBackground(Color.WHITE);
            titleWrapper.add(Box.createRigidArea(new Dimension(100, 0)));
            titleWrapper.add(titleLabel);
            
            add(Box.createRigidArea(new Dimension(1, 50)), BorderLayout.NORTH);
            add(homeBtnWrapper, BorderLayout.WEST);
            add(titleWrapper, BorderLayout.CENTER);
        }
        
        private void setActiveItem(JMenuItem menuItem)
        {
            if(activeMenuItem != null)
                activeMenuItem.setEnabled(true);
            
            activeMenuItem  =   menuItem;
            activeMenuItem.setEnabled(false);
        }

        private class TitleControlListener extends MouseAdapter implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Object src          =   e.getSource();
                ViewPort viewPort   =   ViewPort.getInstance();   
                
                if(src == mainMenuItem)
                    viewPort.setScene(ViewPort.TITLE_SCENE);
                
                else if(src == graphControlsItem)
                    viewPort.setScene(ViewPort.MAIN_SCENE);
                
                else if(src == optionsItem)
                    viewPort.setScene(ViewPort.SETTINGS_SCENE);
                
                else if(src == pluginItem)
                    viewPort.setScene(ViewPort.PLUGINS_SCENE);
                
                else if(src == aboutItem)
                    viewPort.setScene(ViewPort.ABOUT_SCENE);
            }
            
            @Override
            public void mousePressed(MouseEvent e)
            {
                Object src  =   e.getSource();
                
                if(src == menuButton)
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
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
