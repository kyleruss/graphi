//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.sim.generator;

import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import org.apache.commons.collections15.Factory;

public class KleinbergGenerator extends AbstractGenerator
{
    private int latticeSize;
    private int clusterExp;
    
    
    @Override
    protected void initGeneratorDetails()
    {
        generatorName   =   "Kleinberg";
    }
    
    @Override
    public Graph<Node, Edge> generateNetwork(Factory<Node> nodeFactory, Factory<Edge> edgeFactory)
    {
        if(nodeFactory == null) nodeFactory         =   () -> new Node();
        if(edgeFactory == null) edgeFactory         =   () -> new Edge();
        
        Factory<Graph<Node, Edge>> graphFactory     =   () -> new SparseMultigraph<>();   
        
        KleinbergSmallWorldGenerator gen            =   new KleinbergSmallWorldGenerator(graphFactory, nodeFactory, edgeFactory, latticeSize, clusterExp);
        return gen.create();
    }

    public int getLatticeSize() 
    {
        return latticeSize;
    }

    public void setLatticeSize(int latticeSize) 
    {
        this.latticeSize = latticeSize;
    }

    public int getClusterExp() 
    {
        return clusterExp;
    }

    public void setClusterExp(int clusterExp) 
    {
        this.clusterExp = clusterExp;
    }
    
    
}
