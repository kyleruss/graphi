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
    
    private AppManager() {}
    
    private void startManagers(String[] startupArgs)
    {
        ErrorManager.createInstance();
        ConfigManager.createInstance();
        Window.createInstance();
        PluginManager.createInstance();
        StartupManager.createInstance(startupArgs).pollListeners();
    }
    
    /**
    * @return Starts Graphi by initializing & displaying the applications frame
    */
    private void start(String[] startupArgs)
    {
        startManagers(startupArgs);
        Window.getInstance().initFrame();
    }

    public static AppManager createInstance()
    {
        if(instance == null) instance = new AppManager();
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
        AppManager manager  =   AppManager.createInstance();
        manager.start(args);
    }
}
