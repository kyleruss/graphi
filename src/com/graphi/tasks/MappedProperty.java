package com.graphi.tasks;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MappedProperty 
{
    private String name;
    private Map<String, String> params;

    public MappedProperty()
    {
        name    =   "";
        params  =   new LinkedHashMap<>();
    }
    
    public MappedProperty(String propertyStr)
    {
        this();
        initParams(propertyStr);
    }

    private void initParams(String propertyStr)
    {
        Matcher matcher         =   Pattern.compile("(@\\w+\\s?=\\s?-?\\w+.?(\\w+)?)").matcher(propertyStr);
        Matcher nameMatcher     =   Pattern.compile("(\\w+)").matcher(propertyStr);
        name                    =   nameMatcher.find()? nameMatcher.group() : "";
        
        while(matcher.find())
        {
            String paramGroup       =   matcher.group();
            Matcher pNameMatcher    =   Pattern.compile("@(\\w+)").matcher(paramGroup);
            if(pNameMatcher.find())
            {
                
                String paramName        =   pNameMatcher.group(1);
                Matcher pValueMatcher   =   Pattern.compile("=\\s?(-?\\w+.?(\\w+)?)").matcher(paramGroup);
                if(pValueMatcher.find())
                {
                    String paramValue      =   pValueMatcher.group(1).replaceAll("[\\),]", "");
                    params.put(paramName, paramValue);
                }
            }
            
        }
    }
    
    public Map<String, String> getParams()
    {
        return params;
    }
    
    public String getParamValue(String paramName)
    {
        return params.get(paramName);
    }
    
    public int getIntParamValue(String paramName)
    {
        return Integer.parseInt(params.get(paramName));
    }

    public double getDoubleParamValue(String paramName)
    {
        return Double.parseDouble(params.get(paramName));
    }
    
    public boolean getBoolParamValue(String paramName)
    {
        if(!params.containsKey(paramName)) return false;
        else return params.get(paramName).equalsIgnoreCase("true");
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name   =   name;
    }
    
    public void setParamValue(String name, String value)
    {
        params.put(name, value);
    }
        
    @Override
    public String toString()
    {
        String propertyStr  =   "";
        String paramStr     =   "";
        propertyStr += name + "(";
        
        for(Entry<String, String> param : params.entrySet())
            paramStr += "@" + param.getKey() + "=" + param.getValue() + ", ";
        
        if(paramStr.length() > 1) paramStr = paramStr.substring(0, paramStr.length() - 2);
        propertyStr += paramStr + ")";
        
        return propertyStr;
    }
}
