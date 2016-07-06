//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.app.Consts;
import com.graphi.display.layout.AppResources;
import com.graphi.display.layout.DataPanel;
import com.graphi.io.AdjMatrixParser;
import com.graphi.io.GMLParser;
import com.graphi.io.GraphMLParser;
import com.graphi.io.Storage;
import com.graphi.io.TableExporter;
import com.graphi.sim.GraphPlayback;
import com.graphi.tasks.TaskManager;
import com.graphi.util.ComponentUtils;
import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class IOControlPanel extends JPanel implements ActionListener
{
    protected JButton exportBtn;
    protected JButton importBtn;
    protected JLabel currentStorageLabel;
    protected JCheckBox directedCheck;
    protected JComboBox ioTypeBox;
    protected JPanel directedCheckWrapper;
    private final ControlPanel outer;

    public IOControlPanel(ControlPanel outer)
    {
        this.outer = outer;
        setLayout(new GridLayout(4, 1));
        setBorder(BorderFactory.createTitledBorder("I/O Controls"));
        
        currentStorageLabel = new JLabel("None");
        importBtn = new JButton("Import");
        exportBtn = new JButton("Export");
        ioTypeBox = new JComboBox();
        directedCheck = new JCheckBox("Directed");
        importBtn.setIcon(new ImageIcon(AppResources.getInstance().getResource("openIcon")));
        exportBtn.setIcon(new ImageIcon(AppResources.getInstance().getResource("saveIcon")));
        ioTypeBox.addItem("Graph");
        ioTypeBox.addItem("Log");
        ioTypeBox.addItem("Script");
        ioTypeBox.addItem("Table");
        ioTypeBox.addItem("Tasks");
        ioTypeBox.setPreferredSize(new Dimension(150, 30));
        ioTypeBox.addActionListener(this);
        importBtn.addActionListener(this);
        exportBtn.addActionListener(this);
        importBtn.setBackground(Color.WHITE);
        exportBtn.setBackground(Color.WHITE);
        JPanel storageBtnWrapper = ComponentUtils.wrapComponents(null, importBtn, exportBtn);
        JPanel currentGraphWrapper = ComponentUtils.wrapComponents(null, new JLabel("Active: "), currentStorageLabel);
        JPanel ioTypeWrapper = ComponentUtils.wrapComponents(null, ioTypeBox);
        directedCheckWrapper = ComponentUtils.wrapComponents(null, directedCheck);
        storageBtnWrapper.setBackground(Consts.PRESET_COL);
        currentGraphWrapper.setBackground(Consts.PRESET_COL);
        directedCheckWrapper.setBackground(Consts.PRESET_COL);
        ioTypeWrapper.setBackground(Consts.PRESET_COL);
        add(currentGraphWrapper);
        add(ioTypeWrapper);
        add(directedCheckWrapper);
        add(storageBtnWrapper);
        currentStorageLabel.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    public void importPlugin()
    {
        File file           =   ComponentUtils.getFile(true, "Graphi .jar plugin", "jar");
        outer.getMainPanel().getAppManager().getWindow().getMenu().getPluginListMenu().loadPluginFile(file, outer.getMainPanel().getAppManager().getPluginManager(), outer.getMainPanel().getAppManager());
    }

    public void exportGraph()
    {
        File file           =   ComponentUtils.getFile(false, "Graphi .graph, adjacency matrix .txt, .gml, graphML .xml", "graph", "txt", "gml", "xml");
        String extension    =   ComponentUtils.getFileExtension(file);

        if(file != null && outer.getMainPanel().getGraphData().getGraph() != null)
        {
            if(extension.equalsIgnoreCase("graph"))
                Storage.saveObj(outer.getMainPanel().getGraphData().getGraph(), file);

            else if(extension.equalsIgnoreCase("txt"))
                AdjMatrixParser.exportGraph(outer.getMainPanel().getGraphData().getGraph(), file, directedCheck.isSelected());

            else if(extension.equalsIgnoreCase("gml"))
                GMLParser.exportGraph(outer.getMainPanel().getGraphData().getGraph(), file, directedCheck.isSelected());

            else if(extension.equalsIgnoreCase("xml"))
                GraphMLParser.exportGraph(file, outer.getMainPanel().getGraphData().getGraph());
        }
    }

    public void importGraph()
    {
        File file           =   ComponentUtils.getFile(true, "Graphi .graph, adjacency matrix .txt, .gml, graphML .xml", "graph", "txt", "gml", "xml");
        String extension    =   ComponentUtils.getFileExtension(file);   

        if(file != null)
        {
            outer.getMainPanel().getGraphData().getNodeFactory().setLastID(0);
            outer.getMainPanel().getGraphData().getEdgeFactory().setLastID(0);

            if(extension.equalsIgnoreCase("graph"))
                outer.getMainPanel().getGraphData().setGraph((Graph) Storage.openObj(file, outer.getMainPanel().getAppManager().getPluginManager().getActiveClassLoader()));

            else if(extension.equalsIgnoreCase("txt"))
                outer.getMainPanel().getGraphData().setGraph(AdjMatrixParser.importGraph(file, directedCheck.isSelected(), outer.getMainPanel().getGraphData().getNodeFactory(), outer.getMainPanel().getGraphData().getEdgeFactory()));

            else if(extension.equalsIgnoreCase("gml"))
                outer.getMainPanel().getGraphData().setGraph(GMLParser.importGraph(file, outer.getMainPanel().getGraphData().getNodeFactory(), outer.getMainPanel().getGraphData().getEdgeFactory()));

            else if(extension.equalsIgnoreCase("xml"))
                outer.getMainPanel().getGraphData().setGraph(GraphMLParser.importGraph(file, outer.getMainPanel().getGraphData().getNodeFactory(), outer.getMainPanel().getGraphData().getEdgeFactory()));


            currentStorageLabel.setText(file.getName());

            initCurrentNodes();
            initCurrentEdges();

            outer.getMainPanel().getScreenPanel().getGraphPanel().getGraphLayout().setGraph(outer.getMainPanel().getGraphData().getGraph());
            outer.getMainPanel().getScreenPanel().getGraphPanel().getGraphViewer().repaint();
            outer.getMainPanel().getScreenPanel().getDataPanel().loadNodes(outer.getMainPanel().getGraphData().getGraph());
            outer.getMainPanel().getScreenPanel().getDataPanel().loadEdges(outer.getMainPanel().getGraphData().getGraph());
        }
    }

    public void importScript()
    {
        File file   =   ComponentUtils.getFile(true, "Graphi .gscript file", "gscript");
        if(file != null)
        {
            outer.getMainPanel().getScreenPanel().getGraphPanel().setGraphPlayback(
                    (GraphPlayback) Storage.openObj(file, outer.getMainPanel().getAppManager().getPluginManager().getActiveClassLoader()));
            
            if(outer.getMainPanel().getScreenPanel().getGraphPanel().getGraphPlayback() == null) return;
            
            outer.getMainPanel().getScreenPanel().getGraphPanel().getGraphPlayback().prepareIO(false);
            
            currentStorageLabel.setText(file.getName());
            outer.getScriptPanel().getActiveScriptLabel().setText(file.getName());
            outer.getMainPanel().getScreenPanel().getGraphPanel().addPlaybackEntries();

        }
    }

    public void exportScript()
    {
        File file   =   ComponentUtils.getFile(false, "Graphi .gscript file", "gscript");
        if(file != null)
        {
            outer.getMainPanel().getScreenPanel().getGraphPanel().getGraphPlayback().prepareIO(true);
            Storage.saveObj(outer.getMainPanel().getScreenPanel().getGraphPanel().getGraphPlayback(), file);
        }
    }
    
    protected void exportTable()
    {
        File file   =   ComponentUtils.getFile(false, "CSV, TSV", "csv", "tsv");
        if(file != null)
        {
            JTable table        =    null;
            DataPanel dataPanel =   outer.getMainPanel().getScreenPanel().getDataPanel();
            int tableIndex      =   dataPanel.getDataTabPane().getSelectedIndex();
            
            switch (tableIndex) 
            {
                case 0:
                    table   =   outer.getMainPanel().getScreenPanel().getDataPanel().getVertexTable();
                    break;
                case 1:
                    table   =   outer.getMainPanel().getScreenPanel().getDataPanel().getEdgeTable();
                    break;
                case 2:
                    table   =   outer.getMainPanel().getScreenPanel().getDataPanel().getComputeTable();
                    break;
            }
            
            TableExporter.exportTable(table, file);
        }
    }

    protected void initCurrentNodes()
    {
        if(outer.getMainPanel().getGraphData().getGraph() == null) return;

        outer.getMainPanel().getGraphData().getNodes().clear();
        Collection<Node> nodes  =   outer.getMainPanel().getGraphData().getGraph().getVertices();
        for(Node node : nodes)
            outer.getMainPanel().getGraphData().getNodes().put(node.getID(), node);
    }

    protected void initCurrentEdges()
    {
        if(outer.getMainPanel().getGraphData().getGraph() == null) return;

        outer.getMainPanel().getGraphData().getEdges().clear();
        Collection<Edge> edges  =   outer.getMainPanel().getGraphData().getGraph().getEdges();

        for(Edge edge : edges)
            outer.getMainPanel().getGraphData().getEdges().put(edge.getID(), edge);
    }

    public void exportLog()
    {
        File file   =   ComponentUtils.getFile(false, "Graphi .log file", "log");
        if(file != null)
            Storage.saveOutputLog(outer.getMainPanel().getScreenPanel().getOutputPanel().getOutputArea().getText(), file);
    }

    public void importLog()
    {
        File file   =   ComponentUtils.getFile(true, "Graphi .log file", "log");
        if(file != null)
        {
            currentStorageLabel.setText(file.getName());
            outer.getMainPanel().getScreenPanel().getOutputPanel().getOutputArea().setText(Storage.openOutputLog(file));
        }
    }
    
    public void storeTasks(boolean importTasks)
    {
        File file   =   ComponentUtils.getFile(importTasks, "Graphi .tasks file", "tasks");
        if(file != null)
        {
            if(importTasks)
                TaskManager.getInstance().importTasks(file);
            else
                TaskManager.getInstance().exportTasks(file);
        }
    }
    

    private void performImport()
    {
        int typeIndex = ioTypeBox.getSelectedIndex();
        
        switch (typeIndex) 
        {
            case 0:
                importGraph();
                break;
            case 1:
                importLog();
                break;
            case 2:
                importScript();
                break;
            case 3:
                storeTasks(true);
                break;
            default:
                break;
        }
    }

    private void performExport() 
    {
        int typeIndex = ioTypeBox.getSelectedIndex();
        
        switch (typeIndex) 
        {
            case 0:
                exportGraph();
                break;
            case 1:
                exportLog();
                break;
            case 2:
                exportScript();
                break;
            case 3:
                exportTable();
                break;
            case 4:
                storeTasks(false);
                break;
            default:
                break;
        }
    }

    private void ioTypeChange() 
    {
        int typeIndex   =   ioTypeBox.getSelectedIndex();
        importBtn.setEnabled(typeIndex != 3);
        directedCheckWrapper.setVisible(typeIndex == 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src = e.getSource();
        
        if (src == importBtn) 
            performImport();
        
         else if (src == exportBtn) 
            performExport();
         
        else if (src == ioTypeBox) 
            ioTypeChange();
    }

    public JLabel getCurrentStorageLabel()
    {
        return currentStorageLabel;
    }

    public JCheckBox getDirectedCheck() 
    {
        return directedCheck;
    }

    public JComboBox getIoTypeBox()
    {
        return ioTypeBox;
    }
    
    
}
