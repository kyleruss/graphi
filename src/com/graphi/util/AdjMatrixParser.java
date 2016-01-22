//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.commons.collections15.Factory;


public class AdjMatrixParser
{
    public static Graph<Node, Edge> importGraph(File file, boolean directed, Factory<Node> nodeFactory, Factory<Edge>  edgeFactory)
    {
        Graph<Node, Edge> graph =   new SparseMultigraph<>();
        EdgeType edgeType       =   (directed)? EdgeType.DIRECTED : EdgeType.UNDIRECTED;
        
        try(BufferedReader reader   =   new BufferedReader(new FileReader(file)))
        {
            String line             =   reader.readLine();
            int numNodes            =   line.split("\\s+").length;
            int row                 =   0;
            
            for(int i = 0; i < numNodes; i++)
                graph.addVertex(nodeFactory.create());
            
            List<Node> nodes  =   new ArrayList<>(graph.getVertices());
            
            while(line != null)
            {
                String[] edges          =   line.split("\\s+");
                Node source             =   nodes.get(row);
                
                if(edges.length < numNodes || edges.length > numNodes)
                    continue;
                
                for(int col = 0; col < numNodes; col++)
                {
                    Node dest   =   nodes.get(col);
                    
                    if(edges[col].equals("1") || edges[col].equalsIgnoreCase("true"))
                    {
                        Edge edge   =   edgeFactory.create();
                        graph.addEdge(edge, source, dest, edgeType);
                    }
                }
                
                line    =   reader.readLine();
                row++;
            }
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to read file");
        }
        
        return graph;
    }
    
    public static void exportGraph(Graph<Node, Edge> graph, File file, boolean directed)
    {
        
    }
}
