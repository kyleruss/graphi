//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

public class SimulateNetworkTask extends AbstractTask
{
    @Override
    public void initDefaultProperties()
    {
        properties.put("Generator", "");
        properties.put("Generate directed edges", false);
        properties.put("Directed edge chance", 0.0);
    }

    @Override
    public void performTask() 
    {
        String genAlgorithmStr  =   (String) getProperty("Generator");
        MappedProperty genProp  =   new MappedProperty(genAlgorithmStr);
    }

    @Override
    public void initTaskDetails()
    {
        name    =   "Simulate network";
    }
    
}
