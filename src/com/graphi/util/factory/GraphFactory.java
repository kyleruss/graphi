//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util.factory;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import org.apache.commons.collections15.Factory;

public class GraphFactory implements Factory<Graph>
{
    @Override
    public Graph create()
    {
        return new UndirectedSparseMultigraph<>();
    }
}
