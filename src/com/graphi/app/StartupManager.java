//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartupManager
{
    private static StartupManager instance;
    private final List<StartupListener> listeners;
    private final Map<String, String> appParams;
    
    private StartupManager(String[] params)
    {
        listeners       =   new ArrayList<>();
        appParams       =   new HashMap<>();
        initAppParams(params);
        initDefaultListeners();
    }
    
    private void initAppParams(String[] params)
    {
        if(params == null || params.length < 2) return;
        
        for(int paramIndex = 0; (paramIndex + 1) < params.length; paramIndex += 2)
        {
            String paramName    =   params[paramIndex];
            String paramValue   =   params[paramIndex + 1];
            appParams.put(paramName, paramValue);
        }
    }
    
    public void pollListeners()
    {
        if(listeners.isEmpty() || appParams.isEmpty()) return;
        
        for(String paramName : appParams.keySet())
        {
            for(StartupListener listener : listeners)
            {
                if(listener.isListening(paramName))
                    listener.setParam(paramName, appParams.get(paramName));
            }
        }
    }
    
    private void initDefaultListeners()
    {
        addListener(new UpdaterStartupListener());
    }
    
    public void addListener(StartupListener listener)
    {
        listeners.add(listener);
    }
    
    public Map<String, String> getAppParams()
    {
        return appParams;
    }
    
    public List<StartupListener> getListeners()
    {
        return listeners;
    }
    
    public static StartupManager createInstance(String[] appParams)
    {
        if(instance == null) instance   =   new StartupManager(appParams);
        return instance;
    }
    
    public static StartupManager getInstance()
    {
        return instance;
    }
}
