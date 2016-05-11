//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.app.Consts;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ScreenPanel extends JPanel
{
    protected MainPanel mainPanel;
    protected DataPanel dataPanel;
    protected GraphPanel graphPanel;
    protected OutputPanel outputPanel;
    protected JTabbedPane tabPane;
    protected TransitionPanel transitionPanel;
    protected JPanel displayPanel;

    public ScreenPanel(MainPanel mainPanel)
    {            
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 5, 5, 5));
        
        this.mainPanel      =   mainPanel;
        displayPanel        =   new JPanel(new CardLayout());
        tabPane             =   new JTabbedPane();
        transitionPanel     =   new TransitionPanel();
        dataPanel           =   new DataPanel(mainPanel);
        graphPanel          =   new GraphPanel(mainPanel);
        outputPanel         =   new OutputPanel();
        
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
    
    public void changeDisplayCard(String cardName)
    {
        CardLayout cLayout  =   (CardLayout) displayPanel.getLayout();
        cLayout.show(displayPanel, cardName);
    }
    
    public JPanel getDisplayPanel()
    {
        return displayPanel;
    }
    
    public MainPanel getMainPanel() 
    {
        return mainPanel;
    }

    public DataPanel getDataPanel() 
    {
        return dataPanel;
    }

    public GraphPanel getGraphPanel() 
    {
        return graphPanel;
    }

    public OutputPanel getOutputPanel() 
    {
        return outputPanel;
    }
}
