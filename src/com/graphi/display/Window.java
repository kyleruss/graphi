//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Window
{
    private final String WINDOW_TITLE   =   "Graphi - Kyle Russell 2015";
    
    private final LayoutPanel layout;
    private final MainMenu menu;
    private final JFrame frame;
    
    private Window()
    {
        initLookAndFeel();
        
        menu    =   new MainMenu();
        frame   =   new JFrame(WINDOW_TITLE);
        layout  =   new LayoutPanel(menu, frame);
        
        initFrame();
        initMenu();        
    }
    
    
    private void initMenu()
    {
        frame.setJMenuBar(menu);
    }
    
    private void initLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel");
        } 
        
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
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