//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

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
    /**
     * Exports a passed javax.swing.JTable to a specified path
     * Supports csv & tsv
     * @param table a non-null javax.swing.JTable to export
     * @param file the path file to export the table to
     */
    public static void exportTable(JTable table, File file)
    {
        if(file == null || table == null) return;
        
        String extension    =   FilenameUtils.getExtension(file.getName());
        if(extension.equalsIgnoreCase("csv"))
            exportToCSV(table, file);
        
        else if(extension.equalsIgnoreCase("tsv"))
            exportToTSV(table, file);
    }
    
    /**
     * Exports a passed jtable to a CSV file
     * @param table a non-null javax.swing.JTable to export
     * @param file the path file to export the table to
     */
    public static void exportToCSV(JTable table, File file)
    {
        exportToDelimitedFormat(table, ",", file);
    }
    
    /**
     * Exports a passed jtable to a TSV file
     * @param table a non-null javax.swing.JTable to export
     * @param file the path file to export the table to
     */
    public static void exportToTSV(JTable table, File file)
    {
        exportToDelimitedFormat(table, "\t", file);
    }
    
    /**
     * Exports a passed jtable to a file with specified delimitor
     * @param table a non-null javax.swing.JTable to export
     * @param file the path file to export the table to
     * @param delimitor The file delimitor type e.g. "," for CSV
     */
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
