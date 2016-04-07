//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class DataPanel extends JPanel implements ActionListener
{

    protected JTable vertexTable, edgeTable, computeTable;
    protected DefaultTableModel vertexDataModel, edgeDataModel, computationModel;
    protected final JTabbedPane dataTabPane;
    protected final JScrollPane vertexScroller, edgeScroller, computeScroller;
    protected JLabel comModelContextLabel;
    protected JButton comContextBtn;
    protected MainPanel mainPanel;

    public DataPanel(MainPanel mainPanel)
    {
        setLayout(new BorderLayout());
        this.mainPanel      =   mainPanel;
        dataTabPane         =   new JTabbedPane();
        initTables();

        vertexScroller      =   new JScrollPane(vertexTable);
        edgeScroller        =   new JScrollPane(edgeTable);
        computeScroller     =   new JScrollPane(computeTable);
        vertexTable.setPreferredScrollableViewportSize(new Dimension(630, 500));

        vertexDataModel.addColumn("NodeID");
        vertexDataModel.addColumn("Name");

        edgeDataModel.addColumn("EdgeID");
        edgeDataModel.addColumn("FromVertex");
        edgeDataModel.addColumn("ToVertex");
        edgeDataModel.addColumn("Weight");
        edgeDataModel.addColumn("EdgeType");

        comModelContextLabel    =   new JLabel("None");
        comContextBtn           =   new JButton("Change");
        
        JPanel compTabWrapper       =   new JPanel(new BorderLayout());
        JPanel compContextWrapper   =   new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel compContextTitle     =   new JLabel("Context: ");
        
        compContextTitle.setFont(new Font("Arial", Font.BOLD, 12));
        compContextWrapper.add(comContextBtn);
        compContextWrapper.add(compContextTitle);
        compContextWrapper.add(comModelContextLabel);
        compTabWrapper.add(compContextWrapper, BorderLayout.NORTH);
        compTabWrapper.add(computeScroller, BorderLayout.CENTER);
        comContextBtn.addActionListener(this);

        dataTabPane.addTab("Vertex table", vertexScroller);
        dataTabPane.addTab("Edge table", edgeScroller);
        dataTabPane.addTab("Computation table", compTabWrapper);
        
        add(dataTabPane);
    }
    
    private void initTables()
    {
        vertexDataModel     =   new DefaultTableModel();
        vertexTable         =   new ImmutableTable(vertexDataModel);

        edgeDataModel       =   new DefaultTableModel();
        edgeTable           =   new ImmutableTable(edgeDataModel);
        
        computationModel    =   new DefaultTableModel();
        computeTable        =   new ImmutableTable(computationModel);
    }
    
    public void clearComputeTable()
    {
        computationModel    =   null;
        setComputationContext(null);
    }
    
    public String getComputationContext()
    {
        return comModelContextLabel.getText();
    }

    protected void loadNodes(Graph graph)
    {
        ArrayList<Node> vertices   =   new ArrayList<>(graph.getVertices());
        Collections.sort(vertices, (Node n1, Node n2) -> Integer.compare(n1.getID(), n2.getID()));

        mainPanel.data.getNodeFactory().setLastID(0);
        mainPanel.data.getNodes().clear();
        SwingUtilities.invokeLater(() -> 
        {
            vertexDataModel.setRowCount(0);
            for(Node vertex : vertices)
            {
                int vID         =   vertex.getID();
                String vName    =   vertex.getName();

                mainPanel.data.getNodes().put(vID, vertex);
                vertexDataModel.addRow(new Object[] { vID, vName });

                if(vID > mainPanel.data.getNodeFactory().getLastID())
                    mainPanel.data.getNodeFactory().setLastID(vID);
                
            }
        });
    }

    protected void loadEdges(Graph graph)
    {
        ArrayList<Edge> edges  =   new ArrayList<>(graph.getEdges());
        Collections.sort(edges, (Edge e1, Edge e2) -> Integer.compare(e1.getID(), e2.getID())); 

        mainPanel.data.getEdges().clear();

        SwingUtilities.invokeLater(() ->
        {
            edgeDataModel.setRowCount(0);
            for(Edge edge : edges)
            {
                int eID                     =   edge.getID();
                double weight               =   edge.getWeight();
                Collection<Node> vertices   =   graph.getIncidentVertices(edge);
                String edgeType             =   graph.getEdgeType(edge).toString();
                Node n1, n2;
                int n1_id, n2_id;
                Iterator<Node> iter         =   vertices.iterator();
                n1  =   iter.next();
                n2  =   iter.next();

                edge.setSourceNode(n1);
                edge.setDestNode(n2);

                if(n1 != null)
                    n1_id   =   n1.getID();
                else
                    n1_id   =   -1;

                if(n2 != null)
                    n2_id   =   n2.getID();
                else
                    n2_id   =   -1;

                mainPanel.data.getEdges().put(eID, edge);
                edgeDataModel.addRow(new Object[] { eID, n1_id, n2_id, weight, edgeType });

                if(eID > mainPanel.data.getEdgeFactory().getLastID())
                    mainPanel.data.getEdgeFactory().setLastID(eID);
            }
        });
    }

    protected void addVertex()
    {
        VertexAddPanel addPanel    =   new VertexAddPanel();

        int option  =   JOptionPane.showConfirmDialog(null, addPanel, "Add vertex", JOptionPane.OK_CANCEL_OPTION);

        if(option == JOptionPane.OK_OPTION)
        {
            int id      =   (int) addPanel.idSpinner.getValue();
            if(mainPanel.data.getNodes().containsKey(id))
            {
                JOptionPane.showMessageDialog(null, "Vertex already exists");
                return;
            }

            String name =   addPanel.nameField.getText();
            Node node   =   new Node(id, name);
            mainPanel.data.getGraph().addVertex(node);
            mainPanel.screenPanel.graphPanel.gViewer.repaint();
            loadNodes(mainPanel.data.getGraph());
            mainPanel.data.getNodes().put(id, node);
        }
    }

    protected void editVertex()
    {
        Node editNode;
        Set<Node> selectedVertices      =   mainPanel.screenPanel.graphPanel.gViewer.getPickedVertexState().getPicked();

        if(selectedVertices.size() == 1)
            editNode = selectedVertices.iterator().next();
        else
        {
            int[] selectedRows  =   vertexTable.getSelectedRows();
            if(selectedRows.length == 1)
            {
                int id      =   (int) vertexDataModel.getValueAt(selectedRows[0], 0);
                editNode    =   mainPanel.data.getNodes().get(id);   
            }

            else
            {
                int id      =   getDialogID("Enter vertex ID to edit", mainPanel.data.getNodes());

                if(id != -1)
                    editNode    =   mainPanel.data.getNodes().get(id);
                else
                    return;
            }
        }

        VertexAddPanel editPanel    =   new VertexAddPanel();
        editPanel.idSpinner.setValue(editNode.getID());
        editPanel.nameField.setText(editNode.getName());
        editPanel.idSpinner.setEnabled(false);
        editPanel.autoCheck.setVisible(false);

        int option  =   JOptionPane.showConfirmDialog(null, editPanel, "Edit vertex", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION)
        {
            editNode.setID((int) editPanel.idSpinner.getValue());
            editNode.setName(editPanel.nameField.getText());
            loadNodes(mainPanel.data.getGraph());
        }
    }

    protected void removeVertices(Set<Node> vertices)
    {
        if(vertices.isEmpty()) return;

        for(Node node : vertices)
        {
            int id  =   node.getID();
            mainPanel.data.getNodes().remove(id);
            mainPanel.data.getGraph().removeVertex(node);
            mainPanel.screenPanel.graphPanel.gViewer.repaint();
            loadNodes(mainPanel.data.getGraph());
        }
    }

    protected void removeVertex()
    {
        Set<Node> pickedNodes    =   mainPanel.screenPanel.graphPanel.gViewer.getPickedVertexState().getPicked();
        if(!pickedNodes.isEmpty())
            removeVertices(pickedNodes);

        else
        {
            int[] selectedRows  =   vertexTable.getSelectedRows();
            if(selectedRows.length > 0)
            {
                Set<Node> selectedNodes  =   new HashSet<>();
                for(int row : selectedRows)
                {
                    int id          =   (int) vertexDataModel.getValueAt(row, 0);
                    Node current    =   mainPanel.data.getNodes().get(id);

                    if(current != null)
                        selectedNodes.add(current);
                }

                removeVertices(selectedNodes);
            }

            else
            {
                int id  =   getDialogID("Enter vertex ID to remove", mainPanel.data.getNodes());
                if(id != -1)
                {
                    Node removedNode    =   mainPanel.data.getNodes().remove(id);
                    mainPanel.data.getGraph().removeVertex(removedNode);
                    loadNodes(mainPanel.data.getGraph());
                    mainPanel.screenPanel.graphPanel.gViewer.repaint();
                }
            }
        }
    }

    protected void addEdge()
    {
        EdgeAddPanel addPanel   =   new EdgeAddPanel();

        int option  =   JOptionPane.showConfirmDialog(null, addPanel, "Add edge", JOptionPane.OK_CANCEL_OPTION);

        if(option == JOptionPane.OK_OPTION)
        {
            int id  =   (int) addPanel.idSpinner.getValue();
            if(mainPanel.data.getEdges().containsKey(id))
            {
                JOptionPane.showMessageDialog(null, "Edge ID already exists");
                return;
            }

            int fromID          =   (int) addPanel.fromSpinner.getValue();
            int toID            =   (int) addPanel.toSpinner.getValue();
            double weight       =   (double) addPanel.weightSpinner.getValue();
            int eType           =   addPanel.edgeTypeBox.getSelectedIndex();
            EdgeType edgeType   =   (eType == 0)? EdgeType.UNDIRECTED : EdgeType.DIRECTED;

            if(mainPanel.data.getNodes().containsKey(fromID) && mainPanel.data.getNodes().containsKey(toID))
            {
                Edge edge       =   new Edge(id, weight, edgeType);
                Node n1         =   mainPanel.data.getNodes().get(fromID);
                Node n2         =   mainPanel.data.getNodes().get(toID);
                edge.setSourceNode(n1);
                edge.setDestNode(n2);

                mainPanel.data.getEdges().put(id, edge);
                mainPanel.data.getGraph().addEdge(edge, n1, n2, edgeType);
                loadEdges(mainPanel.data.getGraph());
                mainPanel.screenPanel.graphPanel.gViewer.repaint();
            }

            else JOptionPane.showMessageDialog(null, "Vertex ID does not exist");
        }
    }

    protected void editEdge()
    {
        Edge editEdge;
        Set<Edge> selectedEdges =   mainPanel.screenPanel.graphPanel.gViewer.getPickedEdgeState().getPicked();
        if(selectedEdges.size() == 1)
            editEdge    =   selectedEdges.iterator().next();
        else
        {
            int[] selectedRows  =   edgeTable.getSelectedRows();
            if(selectedRows.length == 1)
            {
                int id      =   (int) edgeDataModel.getValueAt(selectedRows[0], 0);
                editEdge    =   mainPanel.data.getEdges().get(id);
            }

            else
            {
                int id  =   getDialogID("Enter edge ID to edit", mainPanel.data.getEdges());

                if(id != -1)
                    editEdge    =   mainPanel.data.getEdges().get(id);
                else
                    return;
            }
        }

        EdgeAddPanel editPanel  =   new EdgeAddPanel();
        editPanel.idSpinner.setValue(editEdge.getID());
        editPanel.fromSpinner.setValue(editEdge.getSourceNode().getID());
        editPanel.toSpinner.setValue(editEdge.getDestNode().getID());
        editPanel.weightSpinner.setValue(editEdge.getWeight());
        editPanel.edgeTypeBox.setSelectedIndex(editEdge.getEdgeType() == EdgeType.UNDIRECTED? 0 : 1);

        editPanel.idSpinner.setEnabled(false);
        editPanel.autoCheck.setVisible(false);

        int option  =   JOptionPane.showConfirmDialog(null, editPanel, "Edit edge", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION)
        {
            editEdge.setWeight((double) editPanel.weightSpinner.getValue());
            editEdge.setEdgeType(editPanel.edgeTypeBox.getSelectedIndex() == 0? EdgeType.UNDIRECTED : EdgeType.DIRECTED);

            Node from   =   mainPanel.data.getNodes().get(Integer.parseInt(editPanel.fromSpinner.getValue().toString()));
            Node to     =   mainPanel.data.getNodes().get(Integer.parseInt(editPanel.toSpinner.getValue().toString()));
            editEdge.setSourceNode(from);
            editEdge.setDestNode(to);

            mainPanel.data.getGraph().removeEdge(editEdge);
            mainPanel.data.getGraph().addEdge(editEdge, from, to, editEdge.getEdgeType());

            loadEdges(mainPanel.data.getGraph());
            mainPanel.screenPanel.graphPanel.gViewer.repaint();
        }
    }

    protected void removeEdge()
    {
        Set<Edge> selectedEdges =   mainPanel.screenPanel.graphPanel.gViewer.getPickedEdgeState().getPicked();

        if(selectedEdges.isEmpty())
        {
            int[] selectedRows  =   edgeTable.getSelectedRows();
            if(selectedRows.length > 0)
            {
                for(int row : selectedRows)
                {
                    int id          =   (int) edgeDataModel.getValueAt(row, 0);
                    Edge current    =   mainPanel.data.getEdges().remove(id);
                    mainPanel.data.getGraph().removeEdge(current);
                }
            }

            else
            {
                int id  =   getDialogID("Enter edge ID to remove", mainPanel.data.getEdges());

                if(id != -1)
                {
                    Edge removeEdge =   mainPanel.data.getEdges().remove(id);
                    mainPanel.data.getGraph().removeEdge(removeEdge);
                }

                else return;
            }
        }

        else
        {
            for(Edge edge : selectedEdges)
            {
                mainPanel.data.getEdges().remove(edge.getID());
                mainPanel.data.getGraph().removeEdge(edge);
            }
        }

        loadEdges(mainPanel.data.getGraph());
        mainPanel.screenPanel.graphPanel.gViewer.repaint();
    }

    protected int getDialogID(String message, Map collection)
    {
        String idStr = JOptionPane.showInputDialog(message);
        int id = -1;
        while(idStr != null && id == -1)
        {
            try
            {
                id  =   Integer.parseInt(idStr);
                if(collection.containsKey(id))
                    break;
                else
                {
                    id      =   -1;
                    JOptionPane.showMessageDialog(null, "ID was not found");
                    idStr   =   JOptionPane.showInputDialog(message);
                }
            }

            catch(NumberFormatException e)
            {
                JOptionPane.showMessageDialog(null, "Invalid input found");
                idStr = JOptionPane.showInputDialog(message);
            }
        }

        return id;
    }

    public JTable getComputeTable() 
    {
        return computeTable;
    }

    public void setComputeTable(JTable computeTable)
    {
        this.computeTable = computeTable;
    }

    public DefaultTableModel getComputationModel()
    {
        return computationModel;
    }

    public void setComputationModel(DefaultTableModel computationModel) 
    {
        this.computationModel = computationModel;
        computeTable.setModel(computationModel);
        computeTable.tableChanged(new TableModelEvent(computationModel));
    }

    public MainPanel getMainPanel()
    {
        return mainPanel;
    }

    public void setMainPanel(MainPanel mainPanel) 
    {
        this.mainPanel = mainPanel;
    }
    
    public void setComputationContext(String context)
    {
        if(context == null) context = "None";
        comModelContextLabel.setText(context);
    }
    
    public void changeComputationContext()
    {
        String context  =   JOptionPane.showInputDialog(null, "Enter the context of this table", "Table context", JOptionPane.OK_CANCEL_OPTION);
        setComputationContext(context);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src = e.getSource();
        
        if(src == comContextBtn)
            changeComputationContext();
    }
    
    
    
    private class ImmutableTable extends JTable
    {
        public ImmutableTable(DefaultTableModel model)
        {
            super(model);
        }
        
        @Override
        public boolean isCellEditable(int row, int col)
        {
            return false;
        }
    }
    
     //--------------------------------------
    //  VERTEX ADD PANEL
    //--------------------------------------
            
    protected class VertexAddPanel extends JPanel implements ActionListener
    {
        protected JSpinner idSpinner;
        protected JCheckBox autoCheck;
        protected JTextField nameField;

        public VertexAddPanel()
        {
            setLayout(new MigLayout());
            idSpinner      =   new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));   
            autoCheck      =   new JCheckBox("Auto");
            nameField      =   new JTextField();

            autoCheck.setSelected(true);
            idSpinner.setValue(mainPanel.data.getNodeFactory().getLastID() + 1);
            idSpinner.setEnabled(false);

            add(new JLabel("ID "));
            add(idSpinner, "width :40:");
            add(autoCheck, "wrap");
            add(new JLabel("Name: "));
            add(nameField, "span 3, width :100:");
            autoCheck.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            Object src  =   e.getSource();
            if(src == autoCheck)
                idSpinner.setEnabled(!autoCheck.isSelected());
        }
    }

    //--------------------------------------
    //  EDGE ADD PANEL
    //--------------------------------------

    protected class EdgeAddPanel extends JPanel implements ActionListener
    {
        protected JSpinner idSpinner, fromSpinner, toSpinner, weightSpinner;
        protected JCheckBox autoCheck;
        protected JComboBox edgeTypeBox;

        public EdgeAddPanel()
        {
            setLayout(new MigLayout());
            idSpinner       =   new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
            fromSpinner     =   new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
            toSpinner       =   new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
            weightSpinner   =   new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10000.0, 0.1));
            autoCheck       =   new JCheckBox("Auto");
            edgeTypeBox     =   new JComboBox();

            edgeTypeBox.addItem("Undirected");
            edgeTypeBox.addItem("Directed");

            idSpinner.setValue(mainPanel.data.getEdgeFactory().getLastID() + 1);
            idSpinner.setEnabled(false);
            autoCheck.setSelected(true);
            autoCheck.addActionListener(this);

            add(new JLabel("ID "));
            add(idSpinner, "width :40:");
            add(autoCheck, "wrap");
            add(new JLabel("From vertex ID"));
            add(fromSpinner, "span 2, width :40:, wrap");
            add(new JLabel("To vertex ID"));
            add(toSpinner, "span 2, width :40:, wrap");
            add(new JLabel("Weight"));
            add(weightSpinner, "span 2, width :70:, wrap");
            add(new JLabel("Type"));
            add(edgeTypeBox, "span 2");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object src  =   e.getSource();
            if(src == autoCheck)
                idSpinner.setEnabled(!autoCheck.isSelected());
        }
    }
    
    public JTable getVertexTable() 
    {
        return vertexTable;
    }

    public JTable getEdgeTable() {
        return edgeTable;
    }

    public DefaultTableModel getVertexDataModel() 
    {
        return vertexDataModel;
    }

    public DefaultTableModel getEdgeDataModel() 
    {
        return edgeDataModel;
    }
}