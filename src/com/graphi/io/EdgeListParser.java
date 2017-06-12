//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import com.graphi.graph.Edge;
import com.graphi.graph.Node;
import com.graphi.util.factory.NodeFactory;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.apache.commons.collections15.Factory;

public class EdgeListParser 
{
    public static Graph<Node, Edge> importGraph(File file, boolean directed, Factory<Node> nodeFactory, Factory<Edge>  edgeFactory)
    {
        Graph<Node, Edge> graph     =   new SparseMultigraph<>();
        EdgeType edgeType           =   (directed)? EdgeType.DIRECTED : EdgeType.UNDIRECTED;
        Map<Integer, Node> nodeMap  =   new HashMap<>();
        int maxID                   =   0;
        
        try(BufferedReader reader   =   new BufferedReader(new FileReader(file)))
        {
            String line;
            while((line =   reader.readLine()) != null)
            {
                String[] nodes          =   line.split("\\s+");
                int nodeAID             =   Integer.parseInt(nodes[0]);
                int nodeBID             =   Integer.parseInt(nodes[1]);
                Node nodeA, nodeB;

                if(!nodeMap.containsKey(nodeAID))
                {
                    nodeA   =   nodeFactory.create();
                    nodeA.setID(nodeAID);
                    nodeMap.put(nodeAID, nodeA);

                    if(nodeAID > maxID) maxID   =   nodeAID;
                }

                else nodeA  =   nodeMap.get(nodeAID);

                if(!nodeMap.containsKey(nodeBID))
                {
                    nodeB   =   nodeFactory.create();
                    nodeB.setID(nodeBID);
                    nodeMap.put(nodeBID, nodeB);

                    if(nodeBID > maxID) maxID   =   nodeBID;
                }

                else nodeB  =   nodeMap.get(nodeBID);

                graph.addEdge(edgeFactory.create(), nodeA, nodeB, edgeType);
            }
            
            ((NodeFactory) nodeFactory).setLastID(maxID);
        } 
        
        catch (IOException ex) 
        {
            JOptionPane.showMessageDialog(null, "[Error] Could not import graph: " + ex.getMessage());
        }
        
        return graph;
    }
}
