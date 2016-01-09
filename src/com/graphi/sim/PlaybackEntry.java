//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.sim;

import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import java.util.Date;

public class PlaybackEntry
{
    private final Graph<Node, Edge> graph;
    private Date date;
    private String name;
    
    public PlaybackEntry(Graph<Node, Edge> graph, Date date)
    {
        this(graph, date, date.toString());
    }
    
    public PlaybackEntry(Graph<Node, Edge> graph, Date date, String name)
    {
        this.graph  =   graph;
        this.date   =   date;
        this.name   =   name;
    }
    
    public Graph<Node, Edge> getGraph()
    {
        return graph;
    }
    
    public Date getDate()
    {
        return date;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setDate(Date date)
    {
        this.date   =   date;
    }
    
    public void setName(String name)
    {
        this.name   =   name;
    }
}
