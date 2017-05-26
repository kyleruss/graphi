//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.util;

import com.graphi.plugins.PluginManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

public class PluginMenuListener implements ActionListener
{
    public PluginMenuListener() {}
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JMenuItem item      =   (JMenuItem) e.getSource();
        String pluginName   =   item.getText();
        PluginManager.getInstance().activatePlugin(pluginName);
    }
}