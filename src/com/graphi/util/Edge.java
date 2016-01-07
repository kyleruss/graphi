//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util;

import edu.uci.ics.jung.graph.util.EdgeType;
import java.awt.Color;
import java.io.Serializable;
import java.text.DecimalFormat;


public class Edge implements Serializable, GraphObject
{
    private int id;
    private double weight;
    private EdgeType edgeType;
    private Node sourceNode, destNode;
    private Color fill;
    
    public Edge()
    {
        this(0, 0.0, EdgeType.UNDIRECTED);
    }
    
    public Edge(int id, double weight, EdgeType edgeType)
    {
        this.id         =   id;        
        this.edgeType   =   edgeType;
        fill            =   Color.BLACK;
        setWeight(weight);
    }
    
    public double getWeight()
    {
        return weight;
    }
    
    public void setWeight(double weight)
    {
        DecimalFormat formatter =   new DecimalFormat("#.##");
        this.weight             =   Double.parseDouble(formatter.format(weight));
    }
    
    @Override
    public String toString()
    {
        return "" + weight;
    }
    
    @Override
    public int getID()
    {
        return id;
    }
    
    @Override
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
    public Color getFill()
    {
        return fill;
    }
    
    @Override
    public void setFill(Color fill)
    {
        this.fill =   fill;
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
