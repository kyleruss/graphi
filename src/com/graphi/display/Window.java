
package com.graphi.display;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Window
{
    private final String WINDOW_TITLE   =   "Graphi - Kyle Russell 2015";
    
    private final Layout layout;
    private final MainMenu menu;
    private final JFrame frame;
    
    private Window()
    {
        layout  =   new Layout();
        menu    =   new MainMenu();
        frame   =   new JFrame(WINDOW_TITLE);
        
        initFrame();
        initMenu();
    }
    
    
    private void initMenu()
    {
        frame.setMenuBar(menu);
    }
    
    private void initLayout()
    {
        
    }
    
    private void initFrame()
    {
        frame.getContentPane().add(layout);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
    
    private void display()
    {
        frame.setVisible(true);
    }
    
    public static void main(String[] args)
    {
        Window window   =   new Window();
        window.display();
    }
}