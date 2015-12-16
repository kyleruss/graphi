
package com.graphi.sim;

import java.awt.Color;


public class Node
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
}
