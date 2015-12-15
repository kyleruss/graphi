package com.graphi.display;

import com.graphi.test.GraphTest;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

public class Layout extends JPanel
{
    private final ControlPanel controlPanel;
    private final ScreenPanel screenPanel;
    private final JSplitPane splitPane;
    
    public static final Color TRANSPARENT   =   new Color(255, 255, 255, 0);
    
    public Layout()
    {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 650));
        
        controlPanel    =   new ControlPanel();
        screenPanel     =   new ScreenPanel();
        splitPane       =   new JSplitPane();
        
        splitPane.setLeftComponent(screenPanel);
        splitPane.setRightComponent(controlPanel); 
       splitPane.setDividerLocation(670);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private class ControlPanel extends JPanel
    {
        private JPanel modePanel;
        private JPanel simPanel;
        private JRadioButton editCheck, selectCheck, moveCheck;
        private ButtonGroup modeGroup;
        private JComboBox genAlgorithmsBox;
        private JButton resetGeneratorBtn, executeGeneratorBtn;
        
        public ControlPanel()
        {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEmptyBorder(15, 0, 3, 8));
            setMinimumSize(new Dimension(230, 650));
            
            modePanel   =   new JPanel();
            modePanel.setPreferredSize(new Dimension(230, 100));
            simPanel    =   new JPanel();
            modePanel.setBorder(BorderFactory.createTitledBorder("Mode controls"));
            simPanel.setBorder(BorderFactory.createTitledBorder("Simulation controls"));
  
            modeGroup               =   new ButtonGroup();
            editCheck               =   new JRadioButton("Edit");
            selectCheck             =   new JRadioButton("Select");
            moveCheck               =   new JRadioButton("Move");
            
            modeGroup.add(editCheck);
            modeGroup.add(selectCheck);
            modeGroup.add(moveCheck);
            modePanel.add(editCheck);
            modePanel.add(selectCheck);
            modePanel.add(moveCheck);
            
            genAlgorithmsBox        =   new JComboBox();
            genAlgorithmsBox.addItem("Kleinberg");
            genAlgorithmsBox.addItem("Barabasi-Albert");
            JPanel generatorPanel   =   new JPanel();
            generatorPanel.add(new JLabel("Generators"));
            generatorPanel.add(genAlgorithmsBox);
            generatorPanel.setBackground(TRANSPARENT);
            
            JPanel generatorControls    =   new JPanel();
            resetGeneratorBtn           =   new JButton("Reset");
            executeGeneratorBtn         =   new JButton("Generate");
            generatorControls.add(resetGeneratorBtn);
            generatorControls.add(executeGeneratorBtn);
            generatorControls.setBackground(simPanel.getBackground());
            
            simPanel.add(generatorPanel);
            simPanel.add(generatorControls);
            
            add(modePanel);
            add(simPanel);
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
            setBorder(BorderFactory.createEmptyBorder(15, 5, 5, 5));
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
                ScalingControl scaler   =   new CrossoverScalingControl();
                scaler.scale(visViewer, 0.7f, visViewer.getCenter());
                visViewer.scaleToLayout(scaler);
                visViewer.setBackground(Color.WHITE);
                add(visViewer, "Test");
                CardLayout cLayout  =   (CardLayout) getLayout();
                cLayout.show(this, "Test");
            }
        }
    }
}
