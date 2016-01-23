//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import javax.swing.JOptionPane;
import org.apache.commons.collections15.Factory;


public class GMLParser 
{
    public static Graph<Node, Edge> importGraph(File file, Factory<Node> nodeFactory, Factory<Edge> edgeFactory)
    {
        Graph<Node, Edge> graph =   new SparseMultigraph<>();
        return graph;
    }
    
    public static void exportGraph(Graph<Node, Edge> graph, File file, boolean directed)
    {
        String output   =   "";
        
        //Graph
        output   +=  "graph [\ndirected " + ((directed)? 1 : 0) + "\n";
        
        //Nodes
        Collection<Node> nodes  =   graph.getVertices();
        for(Node node : nodes)
        {
            output  +=  "node [\n";
            output  +=  "id " + node.getID() + "\n";
            output  +=  "label " + node.getName() + "\n]\n";
        }
        
        //Edges
        Collection<Edge> edges  =   graph.getEdges();
        for(Edge edge : edges)
        {
            output  +=  "edge [\n";
            output  +=  "source " + graph.getSource(edge).getID() + "\n";
            output  +=  "target " + graph.getDest(edge).getID() + "\n";
            output  +=  "weight " + edge.getWeight() + "\n]\n";
        }
        
        //close graph
        output      +=  "]";
        
        try(BufferedWriter writer   =   new BufferedWriter(new FileWriter(file)))
        {
            writer.write(output);
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to write file");
        }
    }
}
