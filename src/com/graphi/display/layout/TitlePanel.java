//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.app.Consts;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TitlePanel extends JPanel
{
    private ControlPanel controlPanel;
    private LogoPanel logoPanel;
    
    public TitlePanel()
    {
        setBackground(Color.WHITE);
        controlPanel    =   new ControlPanel();
        logoPanel       =   new LogoPanel();
        
        add(Box.createRigidArea(new Dimension(Consts.WINDOW_WIDTH, 50)));
        add(logoPanel);
        add(Box.createRigidArea(new Dimension(Consts.WINDOW_WIDTH, 50)));
        add(controlPanel);
    }
    
    private class LogoPanel extends JPanel
    {
        private JLabel logoLabel;
       
        private LogoPanel()
        {
            setBackground(Color.WHITE);
            AppResources resources  =   AppResources.getInstance();
            logoLabel   =   new JLabel(new ImageIcon(resources.getResource("logoIcon")));
            
            add(logoLabel);
        }
    }
    
    private class ControlPanel extends JPanel implements ActionListener
    {
        private JButton settingsBtn;
        private JButton newBtn, openBtn;
        private JButton pluginBtn;
        private JButton exitBtn;
        
        private ControlPanel()
        {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
            
            settingsBtn         =   new JButton("Settings");
            newBtn              =   new JButton("New project");
            openBtn             =   new JButton("Open project");
            pluginBtn           =   new JButton("Load plugin");
            exitBtn             =   new JButton("Exit");
            
            GridLayout layout   =   new GridLayout(5, 1, 0, 5);
            JPanel wrapperPanel =   new JPanel(layout);
            wrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            wrapperPanel.setPreferredSize(new Dimension(300, 450));
            wrapperPanel.setBackground(Color.WHITE);
            
            settingsBtn.setFocusable(false);
            newBtn.setFocusable(false);
            openBtn.setFocusable(false);
            pluginBtn.setFocusable(false);
            exitBtn.setFocusable(false);
            
            wrapperPanel.add(newBtn);
            wrapperPanel.add(openBtn);
            wrapperPanel.add(pluginBtn);
            wrapperPanel.add(settingsBtn);
            wrapperPanel.add(exitBtn);
            add(wrapperPanel);
            
            settingsBtn.addActionListener(this);
            newBtn.addActionListener(this);
            openBtn.addActionListener(this);
            pluginBtn.addActionListener(this);
            exitBtn.addActionListener(this);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            Object src  =   e.getSource();
        }
    }
}