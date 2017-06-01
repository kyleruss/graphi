//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.display.layout.ControlPanel;
import com.graphi.sim.generator.BerbasiGenerator;
import com.graphi.sim.generator.KleinbergGenerator;
import com.graphi.sim.generator.NetworkGenerator;
import com.graphi.sim.generator.RandomNetworkGenerator;
import javax.swing.JOptionPane;

public class SimulateNetworkTask extends AbstractTask
{
    @Override
    public void initTaskDetails()
    {
        setTaskName("Simulate network");
    }
    
    @Override
    public void initDefaultProperties()
    {
        MappedProperty prop =   new MappedProperty();
        prop.setName("kleinberg");
        prop.setParamValue("latSize", "15");
        prop.setParamValue("exp", "2");
        setProperty("Generator", prop.toString());
        setProperty("Generate directed edges", "false");
        setProperty("Directed edge chance", "0.0");
    }

    @Override
    public void performTask() 
    {
        String genAlgorithmStr  =   (String) getProperty("Generator");
        MappedProperty genProp  =   new MappedProperty(genAlgorithmStr);
        String genName          =   genProp.getName();
        NetworkGenerator gen;
        
        switch(genName)
        {
            case "berbasi": gen     =   getBASim(genProp); break;
            case "kleinberg": gen   =   getKleinbergSim(genProp); break;
            case "random": gen      =   getRASim(genProp); break;
            default: gen = null;
        }
        
        if(gen == null) 
            JOptionPane.showMessageDialog(null, "Invalid generator algorithm");
        else
        {           
            ControlPanel.getInstance().getSimulationPanel()
            .executeGeneratorSim(gen);
        }
    }
    
    protected NetworkGenerator getBASim(MappedProperty properties)
    {
        int m           =   Integer.parseInt(properties.getParamValue("i"));
        int n           =   Integer.parseInt(properties.getParamValue("n"));
        Object dirParam =   properties.getParamValue("dir");
        boolean dir     =   dirParam != null && ((String) dirParam).equals("true");
        
       return new BerbasiGenerator(m, n, dir);
    }

    protected NetworkGenerator getRASim(MappedProperty properties)
    {
        int n               =   Integer.parseInt(properties.getParamValue("n"));
        double p            =   properties.getDoubleParamValue("p");
        System.out.println("P: " + p + " p actual: " + properties.getParamValue("p"));
        Object dirParam     =   properties.getParamValue("dir");
        boolean dir         =   dirParam != null && ((String) dirParam).equals("true");

        return new RandomNetworkGenerator(n, p, dir);
    }
    
    protected NetworkGenerator getKleinbergSim(MappedProperty properties)
    {
        int latticeSize =   Integer.parseInt(properties.getParamValue("latSize"));
        int clusterExp  =   Integer.parseInt(properties.getParamValue("exp"));

        return new KleinbergGenerator(latticeSize, clusterExp);
    }
}
