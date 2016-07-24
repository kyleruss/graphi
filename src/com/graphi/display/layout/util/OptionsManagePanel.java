/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.graphi.display.layout.util;

import com.graphi.app.Consts;
import com.graphi.display.layout.AppResources;
import com.graphi.display.layout.controls.TaskControlPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public abstract class OptionsManagePanel extends JPanel 
{
    protected JTable taskTable;
    protected DefaultTableModel taskTableModel;
    protected JPanel outerWrapper, tableWrapper;
    
    public OptionsManagePanel()
    {
        setLayout(new BorderLayout());
        setBackground(Consts.PRESET_COL);
        setPreferredSize(new Dimension(300, 270));

        taskTableModel  =   new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int col)
            {
                return col != 0;
            }
        };

        taskTable       =   new JTable(taskTableModel);
        taskTableModel.addColumn("");
        taskTableModel.addColumn("");
        taskTable.getColumnModel().getColumn(0).setCellRenderer(new TaskLabelCellRenderer());
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        taskTable.setBackground(Consts.PRESET_COL);

        tableWrapper =   new JPanel(new BorderLayout());
        outerWrapper =   new JPanel(new BorderLayout());
        tableWrapper.setBorder(BorderFactory.createTitledBorder("Items"));
    }
    
    protected void attachTable() 
    {
        ButtonColumn btnColumn    =   new ButtonColumn(taskTable, new RemoveItemListener(), 1, 
                new ImageIcon(AppResources.getInstance().getResource("removeIcon")));
        
        tableWrapper.add(taskTable);
        outerWrapper.add(tableWrapper);

        add(outerWrapper, BorderLayout.CENTER);
    }
    
    protected abstract ImageIcon getItemIcon();
 
   
    public List getValues(int column)
    {
        int rowCount    =   taskTableModel.getRowCount();
        List items      =   new ArrayList();
        
        for(int row = 0; row < rowCount; row++)
            items.add(taskTableModel.getValueAt(row, column));
        
        return items;
    }

    public void addOption(Object... obj)
    {
        taskTableModel.addRow(obj);
    }
    
    private class RemoveItemListener extends AbstractAction
    {

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            int row =   Integer.valueOf(e.getActionCommand());
            taskTableModel.removeRow(row);
        }
    }
    

    private class TaskLabelCellRenderer implements TableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            JLabel taskLabel    =   new JLabel("" + value);
            taskLabel.setIcon(getItemIcon());

            return taskLabel;
        }
    }
}