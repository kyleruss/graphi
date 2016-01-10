//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.sim;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GraphPlayback implements Iterator<PlaybackEntry>, Serializable
{
    private List<PlaybackEntry> entries;
    private int index;
    
    public GraphPlayback()
    {
        entries         =   new LinkedList<>();
        index           =   0;
    }
    
    public int getSize()
    {
        return entries.size();
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
    
    public void remove(PlaybackEntry entry)
    {
        entries.remove(entry);
    }
    
    public void remove(int i)
    {
        entries.remove(i);
    }
    
    @Override
    public boolean hasNext()
    {
        return index < entries.size() - 1;
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
        if(entries.isEmpty()) return null;
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
    
    public void setEntries(List<PlaybackEntry>  entries)
    {
        this.entries  =   entries;
    }
    
    public List<PlaybackEntry> getEntries()
    {
        return entries;
    }
    
    public int getIndex()
    {
        return index;
    }
    
    public void setIndex(int index)
    {
        if(index >= 0 && index < entries.size())
            this.index  =   index;
    }
}
