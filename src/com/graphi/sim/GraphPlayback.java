//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.sim;

import com.graphi.util.Edge;
import com.graphi.util.GraphUtilities;
import com.graphi.util.Node;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GraphPlayback implements Iterator<PlaybackEntry>, Serializable
{
    private List<PlaybackEntry> entries;
    private VisualizationViewer<Node, Edge> viewer;
    private AggregateLayout<Node, Edge> layout;
    private int index;
    
    
    public GraphPlayback(VisualizationViewer<Node, Edge> viewer, AggregateLayout<Node, Edge> layout)
    {
        this.viewer     =   viewer;
        this.layout     =   layout;
        entries         =   new LinkedList<>();
        index           =   0;
    }
    
    public void add(PlaybackEntry entry)
    {
        entries.add(entry);
    }
    
    public void add(PlaybackEntry entry, int i)
    {
        entries.add(i, entry);
    }
    
    @Override
    public void remove()
    {
        entries.remove(index);
    }
    
    public void remove(int i)
    {
        entries.remove(i);
    }
    
    @Override
    public boolean hasNext()
    {
        return index < entries.size();
    }
    
    public boolean hasPrevious()
    {
        return index > 0;
    }
    
    public PlaybackEntry start()
    {
        index = 0;
        return entries.get(0);
    }
    
    public PlaybackEntry end()
    {
        index = entries.size() - 1;
        
        if(index >= 0)
            return entries.get(index);
        else
            return null;
    }
    
    public PlaybackEntry current()
    {
        return entries.get(index);
    }
    
    @Override
    public PlaybackEntry next()
    {
        if(!hasNext()) return null;
        return entries.get(index++);
    }
    
    public PlaybackEntry previous()
    {
        if(!hasPrevious()) return null;
        return entries.get(index--);
    }
    
    public void display()
    {
        PlaybackEntry current   =   entries.get(index);
        
        if(current != null)
        {
            layout.setGraph(current.getGraph());
            viewer.repaint();
        }
    }
    
    public void display(int i)
    {
        index = i;
        display();
    }
    
    public void setViewer(VisualizationViewer<Node, Edge> viewer)
    {
        this.viewer =   viewer;
    }
    
    public void setGraphData(List<PlaybackEntry>  entries)
    {
        this.entries  =   entries;
    }
    
    public List<PlaybackEntry> getGraphData()
    {
        return entries;
    }
    
    public VisualizationViewer<Node, Edge> getViewer()
    {
        return viewer;
    }
    
    public AggregateLayout<Node, Edge> getLayout()
    {
        return layout;
    }
    
    public void setLayout(AggregateLayout<Node, Edge> layout)
    {
        this.layout =   layout;
    }
}
