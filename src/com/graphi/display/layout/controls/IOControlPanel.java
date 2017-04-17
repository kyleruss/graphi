//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.app.Consts;
import com.graphi.display.Window;
import com.graphi.display.AppResources;
import com.graphi.display.layout.DataPanel;
import com.graphi.display.layout.GraphPanel;
import com.graphi.display.layout.MainPanel;
import com.graphi.display.layout.OutputPanel;
import com.graphi.io.AdjMatrixParser;
import com.graphi.io.GMLParser;
import com.graphi.io.GraphMLParser;
import com.graphi.io.Storage;
import com.graphi.io.TableExporter;
import com.graphi.plugins.PluginManager;
import com.graphi.sim.GraphPlayback;
import com.graphi.tasks.TaskManager;
import com.graphi.display.layout.util.ComponentUtils;
import com.graphi.graph.Edge;
import com.graphi.graph.GraphData;
import com.graphi.graph.Node;
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

    public IOControlPanel()
    {
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
        File file               =   ComponentUtils.getFile(true, "Graphi .jar plugin", "jar");
        Window.getInstance().getMenu().getPluginListMenu().loadPluginFile(file);
    }

    public void exportGraph()
    {
        File file           =   ComponentUtils.getFile(false, "Graphi .graph, adjacency matrix .txt, .gml, graphML .xml", "graph", "txt", "gml", "xml");
        String extension    =   ComponentUtils.getFileExtension(file);
        Graph graph         =   MainPanel.getInstance().getData().getGraph();
        
        if(file != null && graph != null)
        {
            if(extension.equalsIgnoreCase("graph"))
                Storage.saveObj(graph, file);

            else if(extension.equalsIgnoreCase("txt"))
                AdjMatrixParser.exportGraph(graph, file, directedCheck.isSelected());

            else if(extension.equalsIgnoreCase("gml"))
                GMLParser.exportGraph(graph, file, directedCheck.isSelected());

            else if(extension.equalsIgnoreCase("xml"))
                GraphMLParser.exportGraph(file, graph);
        }
    }

    public void importGraph()
    {
        File file           =   ComponentUtils.getFile(true, "Graphi .graph, adjacency matrix .txt, .gml, graphML .xml", "graph", "txt", "gml", "xml");
        String extension    =   ComponentUtils.getFileExtension(file);   
        GraphData data      =   MainPanel.getInstance().getData();

        if(file != null)
        {
            data.getNodeFactory().setLastID(0);
            data.getEdgeFactory().setLastID(0);

            if(extension.equalsIgnoreCase("graph"))
                data.setGraph((Graph) Storage.openObj(file, PluginManager.getInstance().getActiveClassLoader()));

            else if(extension.equalsIgnoreCase("txt"))
                data.setGraph(AdjMatrixParser.importGraph(file, directedCheck.isSelected(), data.getNodeFactory(), data.getEdgeFactory()));

            else if(extension.equalsIgnoreCase("gml"))
                data.setGraph(GMLParser.importGraph(file, data.getNodeFactory(), data.getEdgeFactory()));

            else if(extension.equalsIgnoreCase("xml"))
                data.setGraph(GraphMLParser.importGraph(file, data.getNodeFactory(), data.getEdgeFactory()));


            currentStorageLabel.setText(file.getName());

            initCurrentNodes();
            initCurrentEdges();

            GraphPanel graphPanel   =   GraphPanel.getInstance();
            DataPanel dataPanel     =   DataPanel.getInstance();
            
            graphPanel.getGraphLayout().setGraph(data.getGraph());
            graphPanel.getGraphViewer().repaint();
            dataPanel.loadNodes(data.getGraph());
            dataPanel.loadEdges(data.getGraph());
        }
    }

    public void importScript()
    {
        File file   =   ComponentUtils.getFile(true, "Graphi .gscript file", "gscript");
        if(file != null)
        {
            GraphPanel graphPanel   =   GraphPanel.getInstance();
            
            graphPanel.setGraphPlayback(
                    (GraphPlayback) Storage.openObj(file, PluginManager.getInstance().getActiveClassLoader()));
            
            if(graphPanel.getGraphPlayback() == null) return;
            
            graphPanel.getGraphPlayback().prepareIO(false);
            
            currentStorageLabel.setText(file.getName());
            ControlPanel.getInstance().getScriptPanel().getActiveScriptLabel().setText(file.getName());
            graphPanel.addPlaybackEntries();

        }
    }

    public void exportScript()
    {
        File file   =   ComponentUtils.getFile(false, "Graphi .gscript file", "gscript");
        if(file != null)
        {
            GraphPanel.getInstance().getGraphPlayback().prepareIO(true);
            Storage.saveObj(GraphPanel.getInstance().getGraphPlayback(), file);
        }
    }
    
    public void exportTable(int tableIndex, File file)
    {
        if(file != null)
        {
            JTable table        =    null;
            DataPanel dataPanel =   DataPanel.getInstance();
            
            switch (tableIndex) 
            {
                case 0:
                    table   =   dataPanel.getVertexTable();
                    break;
                case 1:
                    table   =   dataPanel.getEdgeTable();
                    break;
                case 2:
                    table   =   dataPanel.getComputeTable();
                    break;
            }
            
            TableExporter.exportTable(table, file);
        }
    }
    
    public void exportTable()
    {
        DataPanel dataPanel =   DataPanel.getInstance();
        int tableIndex      =   dataPanel.getDataTabPane().getSelectedIndex();
        File file           =   ComponentUtils.getFile(false, "CSV, TSV", "csv", "tsv");
        exportTable(tableIndex, file);
    }

    protected void initCurrentNodes()
    {
        GraphData data  =   MainPanel.getInstance().getData();
        if(data.getGraph() == null) return;

        data.getNodes().clear();
        Collection<Node> nodes  =   data.getGraph().getVertices();
        for(Node node : nodes)
            data.getNodes().put(node.getID(), node);
    }

    protected void initCurrentEdges()
    {
        GraphData data  =   MainPanel.getInstance().getData();
        if(data.getGraph() == null) return;

       data.getEdges().clear();
        Collection<Edge> edges  =   data.getGraph().getEdges();

        for(Edge edge : edges)
            data.getEdges().put(edge.getID(), edge);
    }

    public void exportLog()
    {
        File file   =   ComponentUtils.getFile(false, "Graphi .log file", "log");
        if(file != null)
            Storage.saveOutputLog(OutputPanel.getInstance().getOutputArea().getText(), file);
    }

    public void importLog()
    {
        File file   =   ComponentUtils.getFile(true, "Graphi .log file", "log");
        if(file != null)
        {
            currentStorageLabel.setText(file.getName());
            OutputPanel.getInstance().getOutputArea().setText(Storage.openOutputLog(file));
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
            case 4:
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
