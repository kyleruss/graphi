
package com.graphi.io;

import com.graphi.error.ErrorManager;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TableExporter 
{
    public static void exportToDelimitedFormat(JTable table, String delimitor, File file)
    {
        String output               =   "";
        DefaultTableModel tModel    =   (DefaultTableModel) table.getModel();
        
        for(int col = 0; col < tModel.getColumnCount(); col++)
            output += tModel.getColumnName(col) + delimitor;
        
        output  +=  "\n";
        for(int row = 0; row < tModel.getRowCount(); row++)
        {
            for(int col = 0; col < tModel.getColumnCount(); col++)
                output  +=   tModel.getValueAt(row, col) + delimitor;
            
            output  +=  "\n";
        }
        
        try(BufferedWriter writer   =   new BufferedWriter(new FileWriter(file)))
        {
            writer.write(output);
        }
        
        catch(IOException e)
        {
            ErrorManager.GUIErrorMessage("Failed to export table", false, e, null);
        }
    }
}
