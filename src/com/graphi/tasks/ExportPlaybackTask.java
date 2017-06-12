//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.display.layout.ControlPanel;
import com.graphi.display.layout.GraphPanel;
import com.graphi.display.layout.controls.PlaybackControlPanel;
import com.graphi.graph.GraphDataManager;
import com.graphi.io.Storage;
import java.io.File;

public class ExportPlaybackTask extends AbstractTask
{
    @Override
    public void initTaskDetails() 
    {
        setTaskName("Export Playback");
    }

    @Override
    public void initDefaultProperties() 
    {
        setProperty("Directory", "data/");
        setProperty("File name", "exampleplayback");
    }

    @Override
    public void performTask() 
    {
        if(GraphDataManager.getInstance().getGraphData().getGraph().getVertices().size() > 0)
            return;
        
        String dir          =   getProperty("Directory");
        String fileName     =   getProperty("File name");
        String extension    =   ".gscript";   
        File file           =   new File(dir + fileName + extension);
        int fileIndex       =   2;
        
        while(file.exists())
        {
            file    =   new File(dir + fileName + fileIndex + extension);
            fileIndex++;
        }
        
        PlaybackControlPanel pbPanel    =   GraphPanel.getInstance().getPlaybackPanel();
        pbPanel.getGraphPlayback().prepareIO(true);
        Storage.saveObj(pbPanel.getGraphPlayback(), file);
    }
}
