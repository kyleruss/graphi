//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import javax.swing.JPanel;

public class ViewPort extends JPanel
{
    private static ViewPort instance;
    
    private ViewPort() {} 
    
    public static ViewPort getInstance()
    {
        if(instance == null) instance   =   new ViewPort();
        return instance;
    }
}
