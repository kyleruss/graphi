//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util.factory;

import org.apache.commons.collections15.Factory;

/**
 * An abstract factory for creating related graph objects (Edge, Node)
 * Maintains a lastID pointer for incremental ID initialization
 * Maintains a incAmount for the ID increase amount 
 * @param <T> The graph object type
 */
public abstract class GraphObjFactory<T> implements Factory<T>
{
    //Pointer for incremental ID initialization
    protected int lastID;
    
    //The ID increase amount 
    protected int incAmount;
    
    public GraphObjFactory()
    {
        this(0);
    }
    
    public GraphObjFactory(int lastID)
    {
        this(lastID, 1);
    }
    
    public GraphObjFactory(int lastID, int incAmount)
    {
        this.lastID     =   lastID;
        this.incAmount  =   incAmount;   
    }
    
    @Override
    public abstract T create();
    
    public int getLastID()
    {
        return lastID;
    }
    
    public void setLastID(int lastID)
    {
        this.lastID =   lastID;
    }

    public int getIncreaseAmount()
    {
        return incAmount;
    }
    
    public void setIncreaseAmount(int incAmount)
    {
        this.incAmount  =   incAmount;
    }
}
