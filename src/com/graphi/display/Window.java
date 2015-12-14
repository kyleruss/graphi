
package com.graphi.display;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Window
{
    private final String WINDOW_TITLE   =   "Graphi - Kyle Russell 2015";
    
    private final Layout layout;
    private final Menu menu;
    private final JFrame frame;
    
    private Window()
    {
        layout  =   new Layout();
        menu    =   new Menu();
        frame   =   new JFrame(WINDOW_TITLE);
        
        initFrame();
    }
    
    
    
    private void initFrame()
    {
        frame.getContentPane().add(layout);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public static void main(String[] args)
    {
        Window window   =   new Window();
    }
}