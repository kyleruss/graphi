
package com.graphi.util;

import java.io.Serializable;
import javax.swing.table.DefaultTableModel;

public class TableModelContext implements Serializable
{
    private String description;
    private DefaultTableModel model;
    
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
}
