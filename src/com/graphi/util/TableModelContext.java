
package com.graphi.util;

import java.io.Serializable;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class TableModelContext implements Serializable
{
    private String description;
    private transient DefaultTableModel model;
    private Vector modelData;
    private Vector columns;
    
    public TableModelContext(DefaultTableModel model)
    {
        this(model, "None");
    }
    
    public TableModelContext(DefaultTableModel model, String description)
    {
        this.model          =   model;
        this.description    =   description;
    }
    
    public DefaultTableModel getModel()
    {
        return model;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void prepareExport()
    {
        if(model == null) return;
        
        columns     =   new Vector();
        modelData   =   model.getDataVector();
        
        for(int i = 0; i < model.getColumnCount(); i++)
            columns.add(model.getColumnName(i)); 
    }
    
    public void prepareImport()
    {
        model   =   new DefaultTableModel();
        for(int i = 0; i < columns.size(); i++)
            model.addColumn(columns.get(i));
        
        for(int i = 0; i < modelData.size(); i++)
        {
            Vector otherVector  =   (Vector) modelData.get(i);
            model.addRow(otherVector);
        }

        modelData   =   null;
        columns     =   null;
    }
}
