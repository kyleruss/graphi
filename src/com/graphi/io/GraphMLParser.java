
package com.graphi.io;

import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;


public class GraphMLParser
{
    public Hypergraph<Node, Edge> importGraph(File file)
    {
        Graph<Node, Edge> graph =   null;
        
        try(BufferedReader reader   =   new BufferedReader(new FileReader(file)))
        {
            GraphMLReader2 gReader  =   new GraphMLReader2(reader, new GraphMetaTransformer(), new NodeMetaTransformer(), new EdgeMetaTransformer(), new HyperEdgeMetaTransformer());
            return gReader.readGraph();
        }
        
        catch(IOException | GraphIOException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to read file");
        }
        
        return graph;
    }
}
