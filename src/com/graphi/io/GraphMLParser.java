//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import com.graphi.util.Edge;
import com.graphi.util.EdgeFactory;
import com.graphi.util.Node;
import com.graphi.util.NodeFactory;
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
import java.util.Iterator;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.collections15.Factory;
import org.xml.sax.SAXException;


public class GraphMLParser
{
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
    
    public void exportGraph(File file, Graph<Node, Edge> graph)
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
            JOptionPane.showMessageDialog(null, "[Error] Failed to read file");
        }
    }
}
