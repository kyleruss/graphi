
package com.graphi.sim;

import edu.uci.ics.jung.graph.util.EdgeType;


public class Edge
{
    private int id;
    private double weight;
    private EdgeType edgeType;
    
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
}
