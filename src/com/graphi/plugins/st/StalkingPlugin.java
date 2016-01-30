//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.plugins.st;

import com.graphi.display.Window;
import com.graphi.plugins.AbstractPlugin;


public class StalkingPlugin extends AbstractPlugin
{

    public StalkingPlugin()
    {
        super("Stalking", "Stalking plugin");
    }

    @Override
    public void attachPanel(Window window) 
    {
        panel   =   new StalkingLayout(window.getMenu(), window.getFrame());
    }

}
