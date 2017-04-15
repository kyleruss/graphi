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
import edu.uci.ics.jung.io.GraphMLReader;
import edu.uci.ics.jung.io.GraphMLWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.collections15.Factory;
import org.xml.sax.SAXException;

/**
 * A utility class for parsing GraphML 
 * @see <a href="http://graphml.graphdrawing.org/">GraphML home</a>
 * Handles importing/exporting GraphML/Graph
 */
public class GraphMLParser
{
    /**
     * Parses a GraphML file and creates & returns a Graph object from the parsed GraphML
     * @param file The GraphML file
     * @param nodeFactory A factory used to create nodes for each node found in the GraphML
     * @param edgeFactory Created edges used for each edge in GraphML
     * @return A Graph created from the parsed GraphML file
     */
    public static Graph<Node, Edge> importGraph(File file, Factory<Node> nodeFactory, Factory<Edge> edgeFactory)
    {
        Graph<Node, Edge> graph     =   new SparseMultigraph<>();
        
        try(BufferedReader reader   =   new BufferedReader(new FileReader(file)))
        {
            GraphMLReader gReader   =   new GraphMLReader(nodeFactory, edgeFactory);
            gReader.load(reader, graph);
        }
        
        catch(IOException | ParserConfigurationException | SAXException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to read file");
        }
        
        return graph;
    }
    
    /**
     * Creates a GraphML document from the passed Graph and exports it to file
     * @param file The file to save the GraphML to
     * @param graph The Graph to parse as GraphML and export 
     */
    public static void exportGraph(File file, Graph<Node, Edge> graph)
    {
        try(BufferedWriter writer   =   new BufferedWriter(new FileWriter(file)))
        {
            GraphMLWriter gWriter   =   new GraphMLWriter();
            gWriter.setEdgeIDs(new EdgeIDTransformer());
            gWriter.setVertexIDs(new NodeIDTransformer());
            
            gWriter.save(graph, writer);
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to write file");
        }
    }
}
