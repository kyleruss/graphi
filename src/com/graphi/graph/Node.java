//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.graph;

import com.graphi.app.Consts;
import java.awt.Color;
import java.io.Serializable;


public class Node implements Serializable, GraphObject<Node>
{
    protected int id;
    protected String name;
    protected Color fill;
    
    public Node()
    {
        this(0, "");
    }
    
    public Node(int id)
    {
        this(id, "");
    }
    
    public Node(int id, String name)
    {
        this(id, name, Consts.DEFAULT_NODE_COLOUR);
    }
    
    public Node(int id, String name, Color fill)
    {
        this.id     =   id;
        this.name   =   name;
        this.fill   =   fill;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
    
    @Override
    public Color getFill()
    {
        return fill;
    }
    
    @Override
    public void setFill(Color fill)
    {
        this.fill  =   fill;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name   =   name;
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

    @Override
    public Node copyGraphObject()
    {
        Node node   =   new Node(id, name);
        node.setFill(fill);
        
        return node;
    }
}
