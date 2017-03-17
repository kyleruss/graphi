//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.app;

import java.util.HashMap;
import java.util.Map;

public abstract class StartupListener 
{
    protected Map<String, String> params;
    
    public StartupListener()
    {
        params  =   new HashMap<>();
    }
    
    protected abstract void initParamNames();
    
    protected void setInitialParamName(String paramName)
    {
        params.put(paramName, null);
    }
    
    public Map<String, String> getParams()
    {
        return params;
    }
    
    public boolean hasParam(String paramName)
    {
        return params.containsKey(paramName);
    }
    
    public String getParam(String paramName)
    {
        return params.get(paramName);
    }
    
    public void setParam(String paramName, String paramValue)
    {
        params.put(paramName, paramValue);
    }
}
