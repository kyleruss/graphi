//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins;

import com.graphi.app.AppManager;
import com.graphi.display.layout.MainPanel;

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

    /**
     * Sets the default plugin panel to the base layout: display/layout/MainPanel
     * @param appManager The parent AppManager reference
     */
    @Override
    public void attachPanel(AppManager appManager)
    {
        panel  =   new MainPanel(appManager);
    }
}
