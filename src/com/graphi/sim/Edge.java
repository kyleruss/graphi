
package com.graphi.sim;

import edu.uci.ics.jung.graph.util.EdgeType;
import java.io.Serializable;


public class Edge implements Serializable
{
    private int id;
    private double weight;
    private EdgeType edgeType;
    private Node sourceNode, destNode;
    
    public Edge()
    {
        this(0, 0.0, EdgeType.UNDIRECTED);
    }
    
    public Edge(int id, double weight, EdgeType edgeType)
    {
        this.id         =   id;
        this.weight     =   weight;
        this.edgeType   =   edgeType;
    }
    
    public double getWeight()
    {
        return weight;
    }
    
    public void setWeight(double weight)
    {
        this.weight =   weight;
    }
    
    @Override
    public String toString()
    {
        return "[ID: " + id + ", WEIGHT: " + weight + "]";
    }
    
    public int getID()
    {
        return id;
    }
    
    public void setID(int id)
    {
        this.id =   id;
    }
    
    public EdgeType getEdgeType()
    {
        return edgeType;
    }
    
    public void setEdgeType(EdgeType edgeType)
    {
        this.edgeType   =   edgeType;
    }
    
    public void setSourceNode(Node node)
    {
        sourceNode  =   node;
    }
    
    public Node getSourceNode()
    {
        return sourceNode;
    }
    
    public void setDestNode(Node node)
    {
        destNode    =   node;
    }
    
    public Node getDestNode()
    {
        return destNode;
    }
    
    @Override
    public int hashCode()
    {
        return Integer.hashCode(id);
    }
    
    @Override
    public boolean equals(Object edge)
    {
        if(edge != null && edge instanceof Edge)
        {
            Edge e2 =   (Edge) edge;
            return Integer.compare(this.getID(), e2.getID()) == 0;
        }
        
        else return false;
    }
}
