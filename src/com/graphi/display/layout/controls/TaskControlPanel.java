//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.app.Consts;
import com.graphi.display.layout.AppResources;
import com.graphi.display.layout.MainPanel;
import com.graphi.display.layout.util.ButtonColumn;
import com.graphi.display.layout.util.OptionsManagePanel;
import com.graphi.tasks.Task;
import com.graphi.tasks.TaskManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class TaskControlPanel extends JPanel implements ActionListener
{
    private ControlPanel controlPanel;
    private JComboBox repeatBox;
    private TaskPopupPanel setupPanel, repeatPanel;
    private JPanel repeatManyPanel;
    private JRadioButton setupRadio, repeatablesRadio;
    private ButtonGroup taskBtnGroup;
    private JButton taskButton, runButton;
    private JPanel wrapper;
    private JSpinner repeatCountSpinner;
    
    public TaskControlPanel(ControlPanel controlPanel)
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Task controls"));
        
        this.controlPanel   =   controlPanel;
        wrapper             =   new JPanel(new MigLayout("fillx"));
        setupPanel          =   new TaskPopupPanel();
        repeatPanel         =   new TaskPopupPanel();
        taskButton          =   new JButton("Tasks");
        runButton           =   new JButton("Run");
        repeatManyPanel     =   new JPanel(new MigLayout("fillx"));
        repeatBox           =   new JComboBox();
        repeatCountSpinner  =   new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        setupRadio          =   new JRadioButton("Setup");
        repeatablesRadio    =   new JRadioButton("Repeatables");
        taskBtnGroup        =   new ButtonGroup();
        
        taskBtnGroup.add(setupRadio);
        taskBtnGroup.add(repeatablesRadio);
        setupRadio.setSelected(true);
        
        repeatManyPanel.add(new JLabel("Repeat count"));
        repeatManyPanel.add(repeatCountSpinner, "al center");
        repeatManyPanel.setBackground(Consts.PRESET_COL);
        
        repeatBox.addItem("Automatic");
        repeatBox.addItem("Manual");

        wrapper.setBackground(Consts.PRESET_COL);
        wrapper.add(new JLabel("Repeat method"), "al right");
        wrapper.add(repeatBox, "wrap");
        wrapper.add(repeatManyPanel, "al center, span 2, wrap");
        
        JPanel radioWrapper =   new JPanel();
        radioWrapper.setBackground(Consts.PRESET_COL);
        radioWrapper.add(setupRadio);
        radioWrapper.add(repeatablesRadio);
        
        wrapper.add(radioWrapper, "al center, span 2, wrap");
        wrapper.add(taskButton, "al right");
        wrapper.add(runButton);
        add(wrapper);
        
        taskButton.addActionListener(this);
        runButton.addActionListener(this);
        repeatBox.addActionListener(this);
    }
    
    public void executeActions(boolean setup, int n)
    {
        JComboBox comboBox      =   setupPanel.taskListPanel.optionsBox;
        List taskList           =   setup? setupPanel.taskListPanel.getValues(0) : repeatPanel.taskListPanel.getValues(0);
        
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < taskList.size(); j++)
            {
                Task task   =   (Task) taskList.get(j);
                task.performTask();
                /*int actionIndex =   ((DefaultComboBoxModel) comboBox.getModel()).getIndexOf(option);
                
                if(actionIndex != -1)
                    handleAction(actionIndex); */
            }
        }
    }
    
    public void runRepeat()
    {
        boolean repeatMany  =   repeatBox.getSelectedIndex() == 0;
        
        if(repeatMany) 
            executeActions(false, (int) repeatCountSpinner.getValue());
        else 
            executeActions(false, 1);
    }
    
    
    protected void handleAction(int actionIndex)
    {
        MainPanel middleMan  =   controlPanel.getMainPanel();
        
        switch(actionIndex)
        {
            case 0: middleMan.getScreenPanel().getGraphPanel().addRecordedGraph(); break;
            case 1: middleMan.getControlPanel().getSimulationPanel().showGeneratorSim(null); break;
            case 2: middleMan.getControlPanel().getSimulationPanel().resetSim(); break;
            case 3: middleMan.getScreenPanel().getGraphPanel().removeAllRecordedEntries(); break;
        }
    }
    
    public int getOptionsCount()
    {
        return TaskManager.getInstance().getAvailTaskList().size();
    }
    
    public void showTasksDialog(boolean isSetup)
    {
        TaskPopupPanel panel;
        String title;
        
        if(isSetup)
        {
            panel   =   setupPanel;
            title   =   "Manage setup tasks";
        }
        
        else
        {
            panel   =   repeatPanel;
            title   =   "Manage repeat tasks";
        }
        
        int option  =   JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        
        if(option == JOptionPane.OK_OPTION)
            panel.taskPropPanel.saveTaskProperties();
        
        panel.taskPropPanel.resetProperties();
    }
    
    protected void addOption(String name)
    {
        setupPanel.taskListPanel.optionsBox.addItem(name);
        repeatPanel.taskListPanel.optionsBox.addItem(name);
    }
    
    protected void removeOption(String name)
    {
        setupPanel.taskListPanel.optionsBox.removeItem(name);
        repeatPanel.taskListPanel.optionsBox.removeItem(name);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        if(src == taskButton)
            showTasksDialog(setupRadio.isSelected());
        
        else if(src == repeatBox)
            repeatManyPanel.setVisible(repeatBox.getSelectedIndex() == 0);
        
        else if(src == runButton)
        {
            if(setupRadio.isSelected())
                executeActions(true, 1);
            
            else runRepeat();
        }
    }
    
    private class TaskPopupPanel extends JPanel
    {
        private final String T_LIST_CARD    =   "t_list";
        private final String T_PROP_CARD    =   "t_prop";
        
        private TaskOptionPanel taskListPanel;
        private TaskPropertiesPanel taskPropPanel;
        
        public TaskPopupPanel()
        {
            setLayout(new CardLayout());
            taskListPanel   =   new TaskOptionPanel();
            taskPropPanel   =   new TaskPropertiesPanel();
            
            add(taskListPanel, T_LIST_CARD);
            add(taskPropPanel, T_PROP_CARD);
        }
        
        public void showTaskList()
        {
            changePopupPanel(T_LIST_CARD);
        }
        
        public void showTaskProperties()
        {
            changePopupPanel(T_PROP_CARD);
        }
        
        private void changePopupPanel(String cardName)
        {
            CardLayout cLayout  =   (CardLayout) getLayout();
            cLayout.show(this, cardName);
        }
        
        private class TaskOptionPanel extends OptionsManagePanel implements ActionListener
        {
            private JButton addButton;
            private JComboBox optionsBox;

            public TaskOptionPanel()
            {
                addButton       =   new JButton("Add task");
                optionsBox      =   new JComboBox();
                addButton.setIcon(new ImageIcon(AppResources.getInstance().getResource("addIcon")));

                JPanel topControlsPanel =   new JPanel(new BorderLayout());
                topControlsPanel.add(optionsBox, BorderLayout.CENTER);
                topControlsPanel.add(addButton, BorderLayout.EAST);
                add(topControlsPanel, BorderLayout.NORTH);
                initOptions();

                taskTableModel.addColumn("");
                taskTable.getColumnModel().getColumn(1).setMaxWidth(60);
                taskTable.getColumnModel().getColumn(2).setMaxWidth(60);
                 ButtonColumn otherBtnCol  =   new ButtonColumn(taskTable, new SettingsItemListener(), 2, 
                    new ImageIcon(AppResources.getInstance().getResource("settingsIcon")));

                attachTable();
                addButton.addActionListener(this);
            }

            protected void initOptions()
            {
                for(Task taskOption : TaskManager.getInstance().getAvailTaskList())
                    optionsBox.addItem(taskOption);
            }

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                addOption(optionsBox.getSelectedItem(), "");
            }

            @Override
            protected ImageIcon getItemIcon() 
            {
                return new ImageIcon(AppResources.getInstance().getResource("executeIcon"));
            }

            private class SettingsItemListener extends AbstractAction
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    int row     =   Integer.valueOf(e.getActionCommand());
                    Task task   =   (Task) taskTable.getValueAt(row, 0);
                    taskPropPanel.setTaskNameTitle(task.getTaskName());
                    taskPropPanel.setActiveTask(task);
                    showTaskProperties();
                }
            }
        }
        
        private class TaskPropertiesPanel extends JPanel implements ActionListener
        {
            private JTable propertyTable;
            private JLabel taskNameLabel;
            private DefaultTableModel propertyModel;
            private JScrollPane propertyTableScroller;
            private JButton backBtn;
            private Task activeTask;

            public TaskPropertiesPanel()
            {
                setLayout(new BorderLayout());
                taskNameLabel           =   new JLabel();
                backBtn                 =   new JButton(new ImageIcon(AppResources.getInstance().getResource("resetIcon")));
                propertyModel           =   new DefaultTableModel();
                propertyTable           =   new JTable(propertyModel);
                propertyTableScroller   =   new JScrollPane(propertyTable);   
                
                propertyModel.addColumn("Property");
                propertyModel.addColumn("Value");
                propertyTableScroller.setPreferredSize(new Dimension(400, 300));
                
                JPanel titleWrapper     =    new JPanel();
                titleWrapper.add(backBtn);
                titleWrapper.add(taskNameLabel);
                
                add(titleWrapper, BorderLayout.NORTH);
                add(propertyTableScroller, BorderLayout.CENTER);
                backBtn.addActionListener(this);
            }
            
            public void resetProperties()
            {
                activeTask  =   null;
                showTaskList();
            }
            
            public void setActiveTask(Task activeTask)
            {
                this.activeTask =   activeTask;
                initTaskProperties();
            }
            
            public Task getActiveTask()
            {
                return activeTask;
            }
            
            public void initTaskProperties()
            {
                propertyModel.setRowCount(0);
                
                if(activeTask == null) return;
                
                Map properties  =   activeTask.getTaskProperties();
                
                if(properties != null)
                {
                    for(Object entry : properties.entrySet())
                    {
                        Entry<String, Object> property  =   (Entry<String, Object>) entry;
                        String propertyName             =   property.getKey();
                        Object propertyValue            =   property.getValue();
                        propertyModel.addRow(new Object[] { propertyName, propertyValue });
                    }
                }
            }
            
            public void saveTaskProperties()
            {
                if(activeTask == null) return;
             
                int n   =   propertyModel.getRowCount();
                for(int i = 0; i < n; i++)
                {
                    String propertyName     =   (String) propertyModel.getValueAt(i, 0);
                    Object propertyValue    =   propertyModel.getValueAt(i, 1);
                    activeTask.setProperty(propertyName, propertyValue);
                }
            }
            
            public void setTaskNameTitle(String taskName)
            {
                taskNameLabel.setText(taskName + " properties");
            }

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                saveTaskProperties();
                showTaskList();
            }
        }
    }
}
