//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.sim;

import com.graphi.util.Edge;
import com.graphi.util.Node;
import com.graphi.util.TableModelContext;
import edu.uci.ics.jung.graph.Graph;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaybackEntry implements Comparable<PlaybackEntry>, Serializable
{
    private Graph<Node, Edge> graph;
    private Date date;
    private String name;
    private TableModelContext computationModel;
    
    public PlaybackEntry(Graph<Node, Edge> graph, Date date)
    {
        this(graph, date, null);
    }
    
    public PlaybackEntry(Graph<Node, Edge> graph, Date date, String name)
    {
        this.graph  =   graph;
        this.date   =   date;
        
        if(name != null)
            this.name   =   name;
        else
            this.name   =   getDateFormatted();
    }
    
    public Graph<Node, Edge> getGraph()
    {
        return graph;
    }
    
    public Date getDate()
    {
        return date;
    }
    
    public String getDateFormatted()
    {
        SimpleDateFormat formatter =   new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setGraph(Graph<Node, Edge> graph)
    {
        this.graph  =   graph;
    }
    
    public void setDate(Date date)
    {
        this.date   =   date;
    }
    
    public void setName(String name)
    {
        this.name   =   name;
    }
    
    public TableModelContext getComputationModel()
    {
        return computationModel;
    }
    
    
    public void setComputationModel(TableModelContext computationModel)
    {
        this.computationModel   =   computationModel;
    }
    
    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public int compareTo(PlaybackEntry other)
    {
        return this.date.compareTo(other.getDate());
    }
    
    @Override
    public boolean equals(Object other)
    {
        if(other instanceof PlaybackEntry)
        {
            PlaybackEntry entry =   (PlaybackEntry) other;
            return this.name.equalsIgnoreCase(entry.getName()) && this.getGraph().equals(entry.getGraph());
        }
        
        else return false;
    }
    
    @Override
    public int hashCode()
    {
        return name.hashCode() + graph.hashCode();
    }
}
