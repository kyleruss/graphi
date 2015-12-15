package com.graphi.display;

import com.graphi.test.GraphTest;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

public class Layout extends JPanel
{
    private final ControlPanel controlPanel;
    private final ScreenPanel screenPanel;
    private final JSplitPane splitPane;
    
    public Layout()
    {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 650));
        
        controlPanel    =   new ControlPanel();
        screenPanel     =   new ScreenPanel();
        splitPane       =   new JSplitPane();
        
        splitPane.setLeftComponent(screenPanel);
        splitPane.setRightComponent(controlPanel); 
        splitPane.setResizeWeight(0.7);
        controlPanel.setMinimumSize(new Dimension(230, 650));
        add(splitPane, BorderLayout.CENTER);
    }
    
    private class ControlPanel extends JPanel
    {
        public ControlPanel()
        {
            setBackground(Color.WHITE);
        }
    }
    
    private class ScreenPanel extends JPanel
    {
        private final DataPanel dataPanel;
        private final GraphPanel graphPanel;
        private final JTabbedPane tabPane;
        
        public ScreenPanel()
        {            
            setLayout(new BorderLayout());
            tabPane     =   new JTabbedPane();
            dataPanel   =   new DataPanel();
            graphPanel  =   new GraphPanel();
            
            
            tabPane.addTab("Display", graphPanel);
            tabPane.addTab("Data", dataPanel);
            add(tabPane);
        }
        
        private class DataPanel extends JPanel
        {
            public DataPanel()
            {
                
            }
        }
        
        private class GraphPanel extends JPanel
        {
            private VisualizationViewer visViewer;
            public GraphPanel()
            {
                setLayout(new CardLayout());
                visViewer   =   GraphTest.getGraph();
                visViewer.setBackground(Color.WHITE);
                add(visViewer, "Test");
                CardLayout cLayout  =   (CardLayout) getLayout();
                cLayout.show(this, "Test");
            }
        }
    }
}
