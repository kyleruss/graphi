//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.app.AppManager;

public class SimulateNetworkTask extends AbstractTask
{
    @Override
    public void initDefaultProperties()
    {
        properties.put("Generator name", "");
        properties.put("Generate directed edges", false);
        properties.put("Directed edge chance", 0.0);
    }

    @Override
    public void performTask() 
    {
        AppManager.getInstance().getPluginManager().getActivePlugin()
                  .getPanel().getControlPanel().getSimulationPanel()
                  .showGeneratorSim();;
    }

    @Override
    public void initTaskDetails()
    {
        name    =   "Simulate network";
    }
    
}
