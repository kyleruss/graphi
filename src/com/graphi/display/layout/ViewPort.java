//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.app.Consts;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ViewPort extends JPanel
{
    public static final String TRANSITION_SCENE =   "tr_sc";
    public static final String MAIN_SCENE       =   "main_sc";
    public static final String TITLE_SCENE      =   "title_sc";
    public static final String SETTINGS_SCENE   =   "settings_sc";
    public static final String PLUGINS_SCENE    =   "plugins_sc";
    public static final String ABOUT_SCENE      =   "about_sc";
    
    private static ViewPort instance;
    private TransitionPanel transitionPanel;
    private TitlePanel titlePanel;
    private SettingsPanel settingsPanel;
    private PluginPanel pluginsPanel;
    private AboutPanel aboutPanel;
    private JPanel mainScenePanel;
    
    private ViewPort()
    {
        setLayout(new CardLayout());
        setPreferredSize(new Dimension(Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT));

        initMainScene();
        initTitlePanel();    
        initPluginPanel();
        initSettingsPanel();
        initAboutPanel();
        initTitlePanel();        
        initSettingsPanel();
        initTransitionPanel();
    }
    
    public void attachMainPanel(MainPanel mainPanel)
    {
        mainScenePanel.removeAll();
        mainScenePanel.add(mainPanel);
        mainScenePanel.revalidate();
    }
    
    private void initTitlePanel()
    {
        titlePanel  =   new TitlePanel();
        add(titlePanel, TITLE_SCENE);
    }
    
    private void initSettingsPanel()
    {
        settingsPanel   =   new SettingsPanel();
        add(settingsPanel, SETTINGS_SCENE);
    }
    
    private void initMainScene()
    {
        mainScenePanel  =   new JPanel(new BorderLayout());
        MainPanel.getInstance();
        //mainScenePanel.add(MainPanel.getInstance());
        add(mainScenePanel, MAIN_SCENE);
    }
    
    private void initPluginPanel()
    {
        pluginsPanel =   new PluginPanel();
        add(pluginsPanel, PLUGINS_SCENE);
    }
    
    private void initAboutPanel()
    {
        aboutPanel  =   new AboutPanel();
        add(aboutPanel, ABOUT_SCENE);
    }
    
    private void initTransitionPanel()
    {
        transitionPanel =   new TransitionPanel();
        JLabel transitionLabel  =   transitionPanel.getTransitionLabel();
        transitionLabel.setBorder(BorderFactory.createEmptyBorder(200, 0, 0, 0));
        add(transitionPanel, TRANSITION_SCENE);
    }
    
    public void transitionScene(String sceneName)
    {
        setScene(TRANSITION_SCENE);
        
        SwingUtilities.invokeLater(()->
        {
            Timer timer =   new Timer(1500, (ActionEvent e) -> 
            {
                setScene(sceneName);
            });
            
            timer.setRepeats(false);
            timer.start();
        });
    }
    
    public void setScene(String sceneName)
    {
        CardLayout layout   =   (CardLayout) getLayout();
        layout.show(this, sceneName);
    }
    
    public TitlePanel getTitlePanel()
    {
        return titlePanel;
    }
    
    public SettingsPanel getSettingsPanel()
    {
        return settingsPanel;
    }
    
    public AboutPanel getAboutPanel()
    {
        return aboutPanel;
    }
    
    public PluginPanel getPluginPanel()
    {
        return pluginsPanel;
    }
    
    public static ViewPort getInstance()
    {
        if(instance == null) instance   =   new ViewPort();
        return instance;
    }
}
