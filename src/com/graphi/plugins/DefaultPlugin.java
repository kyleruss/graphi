//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;


/**
 * The Graphi default plugin creates a basic plugin with the base layout
 * DefaultPlugin is loaded on startup if no other plugin exists or is the default choice
 * Uses display/layout/MainPanel as layout 
 */
public class DefaultPlugin extends AbstractPlugin
{
    public DefaultPlugin() 
    {
        super("Default", "Default plugin");
    }

    @Override
    public void onEvent(int eventCode) {}
}
