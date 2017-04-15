//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.graph;

import com.graphi.app.Consts;
import java.awt.Color;
import java.io.Serializable;
import java.text.DecimalFormat;


public class Edge implements Serializable, GraphObject<Edge>
{
    protected int id;
    protected double weight;
    protected Color fill;
    
    public Edge()
    {
        this(0, 0.0);
    }
    
    public Edge(int id)
    {
        this(id, 0.0);
    }
    
    public Edge(int id, double weight)
    {
        this.id         =   id;        
        fill            =   Consts.DEFAULT_EDGE_COLOUR;
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

    @Override
    public Edge copyGraphObject() 
    {
        Edge edge   =   new Edge(id, weight);
        edge.setFill(fill);
        
        return edge;
    }
}
