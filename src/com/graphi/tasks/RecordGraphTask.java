//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.tasks;

import com.graphi.app.AppManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class RecordGraphTask extends AbstractTask
{
    @Override
    public void performTask() 
    {
        try
        {
            SimpleDateFormat parser =   new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            Date date               =   parser.parse(properties.get("Entry date"));
            String entryName        =   (String) properties.get("Entry name");
            boolean recordState     =   properties.get("Record state").equalsIgnoreCase("true");
            boolean recordTable     =   properties.get("Record table").equalsIgnoreCase("true");

            AppManager.getInstance().getPluginManager().getActivePlugin()
                    .getPanel().getScreenPanel().getGraphPanel()
                    .addRecordedGraph(entryName, date, recordState, recordTable, true);
        }
        
        catch(ParseException e)
        {
            JOptionPane.showMessageDialog(null, "Invalid date");
        }
    }

    @Override
    public void initDefaultProperties()
    {
        properties.put("Record table", "true");
        properties.put("Record state", "true");
        properties.put("Entry name", "");
        properties.put("Entry date", new Date().toString());
    }

    @Override
    public void initTaskDetails() 
    {
        name    =   "Record Graph";
    }
}
