//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.app;

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
    private final ConfigManager configManager; //Manages stored configs
    private final PluginManager pluginManager; //Manages loading & active plugins
    private final ErrorManager errorManager; // Manages error messages & dumping
    private final Window window; //Handles display of the application
    
    private AppManager()
    {
        errorManager    =   new ErrorManager();
        configManager   =   new ConfigManager();
        window          =   Window.getWindowInstance();
        pluginManager   =   new PluginManager(this);   
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
    
    /**
     * @param args Unused
     */
    public static void main(String[] args)
    {
        AppManager manager  =   new AppManager();
        manager.start();
    }
}
