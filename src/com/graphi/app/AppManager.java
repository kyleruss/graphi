//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.app;

import com.graphi.config.ConfigManager;
import com.graphi.display.Window;
import com.graphi.error.ErrorManager;
import com.graphi.plugins.PluginManager;


/**
* Graphi entry point
* Starts Graphi and initializes it's managers
* Conceptually views Graphi plugins, config and display
*/

public final class AppManager 
{
    private static AppManager instance;
    private final ConfigManager configManager; //Manages stored configs
    private final PluginManager pluginManager; //Manages loading & active plugins
    private final ErrorManager errorManager; // Manages error messages & dumping
    private final StartupManager startupManager;
    private final Window window; //Handles display of the application
    
    private AppManager(String[] args)
    {
        errorManager    =   ErrorManager.createInstance();
        configManager   =   ConfigManager.createInstance();
        window          =   Window.getWindowInstance();
        pluginManager   =   PluginManager.createInstance();
        
        startupManager  =   StartupManager.createInstance(args);
        startupManager.pollListeners();
    }
    
    /**
    * @return The Graphi config manager that handles all stored configs
    */
    public ConfigManager getConfigManager()
    {
        return configManager;
    }
    
    /**
    * @return The Graphi plugin manager for loading plugins & handeling active plugins
    */
    public PluginManager getPluginManager()
    {
        return pluginManager;
    }
    
    /**
    * @return The Graphi layout/display manager
    * Has access to the apps menu and frame
    */
    public Window getWindow()
    {
        return window;
    }
    
    /**
    * @return Starts Graphi by initializing & displaying the applications frame
    */
    private void start()
    {
        window.initFrame();
    }

    public static AppManager createInstance(String[] args)
    {
        if(instance == null) instance = new AppManager(args);
        return instance;
    }
    
    public static AppManager getInstance()
    {
        return instance;
    }
    
    /**
     * @param args Unused
     */
    public static void main(String[] args)
    {
        AppManager manager  =   getInstance();
        manager.start();
    }
}
