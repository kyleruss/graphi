//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util;

import java.awt.Color;
import java.io.Serializable;


public class Node implements Serializable
{
    private int id;
    private String name;
    private Color color;
    
    public Node()
    {
        this(0, "");
    }
    
    public Node(int id, String name)
    {
        this(id, name, Color.GREEN);
    }
    
    public Node(int id, String name, Color color)
    {
        this.id     =   id;
        this.name   =   name;
        this.color  =   color;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
    
    public Color getColor()
    {
        return color;
    }
    
    public void setColor(Color color)
    {
        this.color  =   color;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name   =   name;
    }
    
    public int getID()
    {
        return id;
    }
    
    public void setID(int id)
    {
        this.id =   id;
    }
    
    @Override
    public int hashCode()
    {
        return Integer.hashCode(id);
    }
    
    @Override
    public boolean equals(Object node)
    {
        if(node != null && node instanceof Node)
        {
            Node n2 =   (Node) node;
            return Integer.compare(this.getID(), n2.getID()) == 0;
        }
        
        else return false;
    }
}
