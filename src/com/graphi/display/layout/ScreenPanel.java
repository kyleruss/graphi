//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.AppResources;
import com.graphi.app.Consts;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ScreenPanel extends JPanel
{
    protected JTabbedPane tabPane;
    protected TransitionPanel transitionPanel;
    protected JPanel displayPanel;
    private static ScreenPanel instance;

    public ScreenPanel()
    {            
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 5, 5, 5));
        
        displayPanel                    =   new JPanel(new CardLayout());
        tabPane                         =   new JTabbedPane();
        transitionPanel                 =   new TransitionPanel();
        DataPanel dataPanel             =   DataPanel.getInstance();
        GraphPanel graphPanel           =   GraphPanel.getInstance();
        OutputPanel outputPanel         =   OutputPanel.getInstance();
        
        displayPanel.add(transitionPanel, Consts.DISPLAY_TRANSIT_CARD);
        displayPanel.add(graphPanel, Consts.DISPLAY_GRAPH_CARD);

        tabPane.addTab("", displayPanel);
        tabPane.addTab("", dataPanel);
        tabPane.addTab("", outputPanel);

        JLabel dispLabel    =   new JLabel("Display", JLabel.CENTER);
        JLabel dataLabel    =   new JLabel("Data", JLabel.CENTER);
        JLabel outLabel     =   new JLabel("Output", JLabel.CENTER);

        dispLabel.setIcon(new ImageIcon(AppResources.getInstance().getResource("graphIconV2")));
        outLabel.setIcon(new ImageIcon(AppResources.getInstance().getResource("clipIcon")));
        dataLabel.setIcon(new ImageIcon(AppResources.getInstance().getResource("tableIcon")));

        tabPane.setTabComponentAt(0, dispLabel);
        tabPane.setTabComponentAt(1, dataLabel);
        tabPane.setTabComponentAt(2, outLabel);

        add(tabPane);
        
        SwingUtilities.invokeLater(()->
        {
            Timer timer =   new Timer(2500, (ActionEvent e) -> 
            {
                changeDisplayCard(Consts.DISPLAY_GRAPH_CARD);
            });
            
            timer.setRepeats(false);
            timer.start();
        });
    }
    
    public void displayTransition()
    {
        changeDisplayCard(Consts.DISPLAY_TRANSIT_CARD);
    }
    
    public void displayGraph()
    {
        changeDisplayCard(Consts.DISPLAY_GRAPH_CARD);
    }
    
    public void changeDisplayCard(String cardName)
    {
        CardLayout cLayout  =   (CardLayout) displayPanel.getLayout();
        cLayout.show(displayPanel, cardName);
    }
    
    public JPanel getDisplayPanel()
    {
        return displayPanel;
    }
    
    public static ScreenPanel getInstance()
    {
        if(instance == null) instance   =   new ScreenPanel();
        return instance;
    }
}
