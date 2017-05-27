//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.util;

import com.graphi.display.AppResources;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class StatusPanel extends JPanel
{
    private JLabel statusLabel;
    private String successText, failText;
    private int delay;
    
    public StatusPanel()
    {
        setBackground(Color.WHITE);
        statusLabel =   new JLabel();
        delay       =   1500;
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setVisible(false);
        
        add(statusLabel);
    }
    
    public void showStatusLabel(boolean success)
    {
        AppResources resources  =   AppResources.getInstance();
        String resourceName     =   success? "statusTickMedium" : "statusXMedium";

        statusLabel.setIcon(new ImageIcon(resources.getResource(resourceName)));
        statusLabel.setText(success? successText : failText);
        statusLabel.setVisible(true);

        SwingUtilities.invokeLater(()->
        {
            Timer timer =   new Timer(delay, (ActionEvent e) -> 
            {
                statusLabel.setVisible(false);
            });

            timer.setRepeats(false);
            timer.start();
        });
    }

    public JLabel getStatusLabel()
    {
        return statusLabel;
    }

    public void setStatusLabel(JLabel statusLabel) 
    {
        this.statusLabel = statusLabel;
    }

    public String getSuccessText()
    {
        return successText;
    }

    public void setSuccessText(String successText) 
    {
        this.successText = successText;
    }

    public String getFailText() 
    {
        return failText;
    }

    public void setFailText(String failText)
    {
        this.failText = failText;
    }

    public void setDelay(int delay) 
    {
        this.delay = delay;
    }
}
