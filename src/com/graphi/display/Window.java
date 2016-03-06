//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display;

import com.graphi.app.Consts;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public final class Window
{
    private final MainMenu menu;
    private final JFrame frame;
    private static Window instance;
    
    private Window()
    {
        initLookAndFeel();
        
        menu            =   new MainMenu();
        frame           =   new JFrame(Consts.WINDOW_TITLE);
       
        initMenu();        
    }
    
    public JFrame getFrame()
    {
        return frame;
    }
    
    public MainMenu getMenu()
    {
        return menu;
    }
    
    
    private void initMenu()
    {
        frame.setJMenuBar(menu);
    }
    
    private void initLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(Consts.LNF_PACKAGE);
        } 
        
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void initFrame()
    {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public static Window getWindowInstance()
    {
        if(instance == null)
            instance = new Window();
        
        return instance;
    }
}