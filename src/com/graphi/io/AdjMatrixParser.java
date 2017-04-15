//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import com.graphi.graph.Edge;
import com.graphi.graph.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.commons.collections15.Factory;

/**
 * Utility class for importing Graph objects from files containing adjacency matrix
 * Can also export a Graph object's adjacency matrix to file 
 */
public class AdjMatrixParser
{
    /**
     * Reads an adjacency matrix from file and creates and returns its Graph object
     * Files must only contain the adjacency matrix with simple formatting
     * @param file The file to read the graph adjacency matrix from
     * @param directed A boolean for whether the imported graph has directed edges; false for undirected
     * @param nodeFactory A factory to create vertices/nodes from. Files do not contain any additional info e.g id's/names of nodes
     * @param edgeFactory A factory for creating edges 
     * @return The Graph object created from the adjacency matrix read
     */
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
                    
                    if(edges[col].equals("1") && !(graph.isNeighbor(source, dest) && !directed))
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
    
    /**
     * Creates an adjacency matrix from a Graph object and exports it to a file
     * @param graph The Graph object that will transformed to a adjacency matrix and exported
     * @param file The file to export to
     * @param directed A boolean as to whether the edges in the adjacency matrix are directed
     */
    public static void exportGraph(Graph<Node, Edge> graph, File file, boolean directed)
    {
        String output       =   "";
        List<Node> nodes    =   new ArrayList<>(graph.getVertices());
        int n               =   nodes.size();
        
        for(int row = 0; row < n; row++)
        {
            Node source                 =   nodes.get(row);
            Collection<Node> neighbours =   graph.getNeighbors(source);
            
            for(int col = 0; col < n; col++)
            {
                Node dest       =   nodes.get(col);
                if(neighbours.contains(dest))
                    output      +=  "1";
                else
                    output      +=  "0";
                
                output          +=  (col < n - 1)? " " : "";
            }
            
            output += "\n";
        }
        
        try(BufferedWriter writer   =   new BufferedWriter(new FileWriter(file)))
        {
            writer.write(output);
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to write file");
        }
    }
}
