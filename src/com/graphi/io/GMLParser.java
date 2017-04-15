//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import com.graphi.graph.Edge;
import com.graphi.util.factory.GraphObjFactory;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 * A Graph Modelling Language (GML) parser
 * @see <a href="https://en.wikipedia.org/wiki/Graph_Modelling_Language">GML Wiki</a>
 * Handles importing/exporting of Graph objects from GML documents
 */
public class GMLParser 
{
    /**
     * Takes a GML document & returns the inner object under objName header
     * @param doc The document to filter
     * @param objName The inner object header name
     * @return The filtered inner document 
     */
    private static Matcher getDocumentObj(String doc, String objName)
    {
        Pattern p = Pattern.compile(objName + " \\[\n([^\\]]*)\\]", Pattern.MULTILINE);
        Matcher m = p.matcher(doc);
        
        return m;
    }
    
    /**
     * Extracts a property from a GML document
     * @param docObj The GML document which contains the desired property
     * @param property The property name
     * @return The property string value; returns the first property if more than one value found
     */
    private static String getDocumentObjProperty(String docObj, String property)
    {
        Pattern p = Pattern.compile(property + "\\s+(\\w+)");
        Matcher m = p.matcher(docObj);
        
        if(m.find())
        {
            String[] group    =   m.group().split("\\s+");
            
            if(group.length > 1) return group[1];
            else return null;
        }
        
        else return null;
    }
    
    /**
     * Creates & returns a Graph object a parsed GML document
     * Note: Only ID properties are preserved in standard GML 
     * @param file The GML file to import
     * @param nodeFactory A factory to create nodes from GML formatted nodes
     * @param edgeFactory A factory to create edges from GML formatted edges
     * @return 
     */
    public static Graph<Node, Edge> importGraph(File file, GraphObjFactory<Node> nodeFactory, GraphObjFactory<Edge> edgeFactory)
    {
        Graph<Node, Edge> graph =   new SparseMultigraph<>();
        String document         =   "";
        
        try(BufferedReader reader   =   new BufferedReader(new FileReader(file)))
        {
            String line;
            while((line     =   reader.readLine()) != null)
                document    +=  line + "\n";
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to read file");
            return graph;
        }
        
        Matcher graphObj =   getDocumentObj(document, "graph");
        
        if(graphObj.find())
        {
            String graphGroup   =   graphObj.group();
            String dirStr       =   getDocumentObjProperty(graphGroup, "directed");
            EdgeType edgeType   =   (dirStr != null && dirStr.equals("1"))? EdgeType.DIRECTED : EdgeType.UNDIRECTED;
            
            Matcher nodes               =   getDocumentObj(document, "node");
            Map<Integer, Node> nodeMap  =   new HashMap<>();   
            
            while(nodes.find())
            {
                String nodeGroup        =   nodes.group();
                
                try
                {
                    int nodeID              =   Integer.parseInt(getDocumentObjProperty(nodeGroup, "id"));
                    String name             =   getDocumentObjProperty(nodeGroup, "label");

                    Node node               =   nodeFactory.create();
                    node.setID(nodeID);
                    node.setName(name);

                    if(nodeFactory.getLastID() < nodeID)
                        nodeFactory.setLastID(nodeID);

                    graph.addVertex(node);
                    nodeMap.put(nodeID, node);
                }
                
                catch(NumberFormatException e)
                {
                    System.out.println(nodeGroup);
                }
            }
            
            Matcher edges       =   getDocumentObj(document, "edge");
            while(edges.find())
            {
                String edgeGroup    =   edges.group();
                
                try
                {
                    String idProperty   =   getDocumentObjProperty(edgeGroup, "id");
                    String wProperty    =   getDocumentObjProperty(edgeGroup, "weight");
                    int edgeID          =   (idProperty != null)? Integer.parseInt(idProperty) : 0;
                    int sourceID        =   Integer.parseInt(getDocumentObjProperty(edgeGroup, "source"));
                    int targetID        =   Integer.parseInt(getDocumentObjProperty(edgeGroup, "target"));
                    double weight       =   (wProperty != null)? Double.parseDouble(getDocumentObjProperty(edgeGroup, "weight")) : 0.0;

                    Node sourceNode     =   nodeMap.get(sourceID);
                    Node targetNode     =   nodeMap.get(targetID);

                    if(sourceNode != null && targetNode != null)
                    {
                        Edge edge       =   edgeFactory.create();
                        edge.setWeight(weight);

                        if(edgeFactory.getLastID() < edgeID)
                        {
                            edgeFactory.setLastID(edgeID);
                            edge.setID(edgeID);
                        }

                        graph.addEdge(edge, sourceNode, targetNode, edgeType);
                    }
                }
                
                catch(NumberFormatException e)
                {
                    System.out.println(edgeGroup);
                }
            }
        }
        
        return graph;
    }
    
    /**
     * Creates a GML document from a Graph object to export to file
     * @param graph The Graph to export
     * @param file The File to export to
     * @param directed A boolean for whether the exported Graph is directed (adds a GML property)
     */
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
            Node n1, n2;
            
            if(graph.getEdgeType(edge) == EdgeType.DIRECTED)
            {
                n1  =   graph.getSource(edge);
                n2  =   graph.getDest(edge);
            }
            
            else
            {
                Iterator<Node> iNodeIter =   graph.getIncidentVertices(edge).iterator();
                n1  =   iNodeIter.next();
                n2  =   iNodeIter.next();
            }
            
            output  +=  "edge [\n";
            output  +=  "source " + n1.getID() + "\n";
            output  +=  "target " + n2.getID() + "\n";
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
