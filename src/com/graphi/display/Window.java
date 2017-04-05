//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display;

import com.graphi.app.Consts;
import com.graphi.config.ConfigManager;
import com.graphi.display.layout.ViewPort;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Manages the Graphi menu and frame
 * Uses singleton pattern: use the static getWindowInstance
 * Initializes the applications look&feel package: LNF_PACKAGE
 */

public final class Window
{
    private final MainMenu menu; //The applications frame menu bar (JMenuBar)
    private final JFrame frame; //The active Graphi frame 
    private static Window instance; //Only Window instance, see getWindowInstance() for initialization
    
    private Window()
    {
        initLookAndFeel();
        
        menu            =   new MainMenu();
        String title    =   "Graphi " + ConfigManager.getInstance().getVersionConfig().getVersion();
        frame           =   new JFrame(title);
       
        initMenu();        
    }
    
    /**
    * @return The active Graphi frame
    */
    public JFrame getFrame()
    {
        return frame;
    }
    
    /**
    * @return The app frame menu bar (JMenuBar)
    */
    public MainMenu getMenu()
    {
        return menu;
    }
    
    /**
    * Sets the frames menu bar to the initialized MainMenu: Window.menu 
    */
    private void initMenu()
    {
        frame.setJMenuBar(menu);
    }
    
    /**
    * Sets the applications look & feel
    * See Consts.LNF_PACKAGE for look & feel package
    */
    private void initLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(Consts.LNF_PACKAGE);
        } 
        
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            System.out.println("[Error] Failed to initialize application look & feel: " + e.getMessage());
        }
    }
    
    private void attachPanel()
    {
        JPanel panel    =   ViewPort.getInstance();
        frame.getContentPane().add(panel);
    }
    
    /**
    * Initializes the application frame and displays it
    * Initial plugin layout is already added to content pane by plugins/PluginManager
    */
    public void initFrame()
    {
        attachPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setIconImage(AppResources.getInstance().getResource("graphIcon"));
        frame.setVisible(true);
    }
    
    /**
    * @return Creates a Window instance if it doesn't exist and returns the instance
    */
    public static Window createInstance()
    {
        if(instance == null)
            instance = new Window();
        
        return instance;
    }
    
    public static Window getInstance()
    {
        return instance;
    }
}