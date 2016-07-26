//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.sim.generator;

public abstract class AbstractGenerator implements NetworkGenerator
{
    protected String generatorName;
    protected String generatorDescription;
    
    public AbstractGenerator()
    {
        initGeneratorDetails();
    }
    
    protected abstract void initGeneratorDetails();
    
    public String getGeneratorName() 
    {
        return generatorName;
    }

    public String getGeneratorDescription() {
        
        return generatorDescription;
    }
}
