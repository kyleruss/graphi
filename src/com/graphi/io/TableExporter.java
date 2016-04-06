
package com.graphi.io;

import com.graphi.error.ErrorManager;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.io.FilenameUtils;

public class TableExporter 
{
    public static void exportTable(JTable table, File file)
    {
        if(file == null) return;
        
        String extension    =   FilenameUtils.getExtension(file.getName());
        if(extension.equalsIgnoreCase("csv"))
            exportToCSV(table, file);
        
        else if(extension.equalsIgnoreCase("tsv"))
            exportToTSV(table, file);
    }
    
    public static void exportToCSV(JTable table, File file)
    {
        exportToDelimitedFormat(table, ",", file);
    }
    
    public static void exportToTSV(JTable table, File file)
    {
        exportToDelimitedFormat(table, "\t", file);
    }
    
    public static void exportToDelimitedFormat(JTable table, String delimitor, File file)
    {
        String output               =   "";
        DefaultTableModel tModel    =   (DefaultTableModel) table.getModel();
        int rowSize                 =   tModel.getRowCount();
        int colSize                 =   tModel.getColumnCount();
        
        for(int col = 0; col < colSize; col++)
            output += tModel.getColumnName(col) + (col < colSize - 1? delimitor : "");
        
        output  +=  "\n";
        for(int row = 0; row < rowSize; row++)
        {
            for(int col = 0; col < colSize; col++)
                output  +=   tModel.getValueAt(row, col) + (col < colSize - 1? delimitor : "");
            
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
