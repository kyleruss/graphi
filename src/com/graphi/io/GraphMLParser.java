
package com.graphi.io;

import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.GraphMLReader;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


public class GraphMLParser
{
    public static Graph<Node, Edge> importGraph(File file)
    {
        Graph<Node, Edge> graph     =   new SparseMultigraph<>();
        
        try(BufferedReader reader   =   new BufferedReader(new FileReader(file)))
        {
            GraphMLReader gReader   =   new GraphMLReader();
            gReader.load(reader, graph);
        }
        
        catch(IOException | ParserConfigurationException | SAXException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to read file");
        }
        
        return graph;
    }
    
    public void exportGraph(File file, Graph<Node, Edge> graph)
    {
        try(BufferedWriter writer   =   new BufferedWriter(new FileWriter(file)))
        {
            GraphMLWriter gWriter   =   new GraphMLWriter();
            gWriter.save(graph, writer);
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to read file");
        }
    }
}
