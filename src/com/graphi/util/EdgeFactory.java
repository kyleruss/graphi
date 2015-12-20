//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util;

import edu.uci.ics.jung.graph.util.EdgeType;
import java.util.Random;

public class EdgeFactory extends GraphObjFactory<Edge>
{
    private final Random RGEN =   new Random();
    
    public EdgeFactory()
    {
        super();
    }
    
    public EdgeFactory(int lastID)
    {
        super(lastID);
    }
    
    public EdgeFactory(int lastID, int incAmount)
    {
        super(lastID, incAmount);
    }
    
    @Override
    public Edge create() 
    {
        lastID          +=  incAmount;
        double weight   =   RGEN.nextDouble() * 100.0;
        return new Edge(lastID, weight, EdgeType.UNDIRECTED);
    }
}
