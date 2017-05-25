//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.app.AppManager;
import com.graphi.display.layout.GraphPanel;
import com.graphi.display.layout.MainPanel;
import com.graphi.display.layout.controls.PlaybackControlPanel;
import com.graphi.plugins.PluginManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class RecordGraphTask extends AbstractTask
{

    @Override
    public void initTaskDetails() 
    {
        setTaskName("Record Graph");
    }
    
    @Override
    public void performTask() 
    {
        try
        {
            SimpleDateFormat parser         =   new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            Date date                       =   parser.parse(getProperty("Entry date"));
            String entryName                =   (String) getProperty("Entry name");
            boolean recordState             =   getProperty("Record state").equalsIgnoreCase("true");
            boolean recordTable             =   getProperty("Record table").equalsIgnoreCase("true");
            PlaybackControlPanel pbPanel    =   GraphPanel.getInstance().getPlaybackPanel();
            pbPanel.addRecordedGraph(entryName, date, recordState, recordTable, true);
        }
        
        catch(ParseException e)
        {
            JOptionPane.showMessageDialog(null, "Invalid date");
        }
    }

    @Override
    public void initDefaultProperties()
    {
        setProperty("Record table", "true");
        setProperty("Record state", "true");
        setProperty("Entry name", "");
        setProperty("Entry date", new Date().toString());
    }
}
