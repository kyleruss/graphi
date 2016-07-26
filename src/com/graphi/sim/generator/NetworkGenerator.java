//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.sim.generator;

import edu.uci.ics.jung.graph.Graph;

public interface NetworkGenerator 
{
    public String getGeneratorName();
    
    public String getGeneratorDescription();;
    
    public Graph generateNetwork();
}
