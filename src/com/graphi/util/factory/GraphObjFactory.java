//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util.factory;

import org.apache.commons.collections15.Factory;

public abstract class GraphObjFactory<T> implements Factory<T>
{
    protected int lastID;
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
