//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.app;

public class AppManager 
{
    private final ConfigManager config;
    
    public AppManager()
    {
        config  =   new ConfigManager();
    }
    
    public ConfigManager getConfigManager()
    {
        return config;
    }
}
