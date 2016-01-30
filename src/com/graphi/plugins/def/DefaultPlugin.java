//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins.def;

import com.graphi.display.Window;
import com.graphi.plugins.AbstractPlugin;


public class DefaultPlugin extends AbstractPlugin
{
    public DefaultPlugin() 
    {
        super("Default", "Default plugin");
    }

    @Override
    public void attachPanel(Window window) 
    {
        panel  =   new DefaultLayout(window.getMenu(), window.getFrame());
    }
}
