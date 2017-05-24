//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.app.Consts;
import com.graphi.display.AppResources;
import com.graphi.display.layout.util.ButtonColumn;
import com.graphi.display.layout.util.ComponentUtils;
import com.graphi.display.layout.util.OptionsManagePanel;
import com.graphi.tasks.Task;
import com.graphi.tasks.TaskManager;
import com.graphi.tasks.TasksBean;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class TaskControlPanel extends JPanel implements ActionListener
{
    private JComboBox repeatBox;
    private TaskPopupPanel taskPopupPanel;
    private JPanel repeatManyPanel;
    private JButton taskButton, runButton;
    private JPopupMenu executePopupMenu;
    private JMenuItem execSetupBtn, execRepeatBtn;
    private JPanel wrapper;
    private JSpinner repeatCountSpinner;
    
    public TaskControlPanel()
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Task controls"));
        
        wrapper             =   new JPanel(new MigLayout("fillx"));
        taskPopupPanel      =   new TaskPopupPanel();
        taskButton          =   new JButton("Tasks");
        executePopupMenu    =   new JPopupMenu();
        runButton           =   ComponentUtils.generateDropdownButton(executePopupMenu, "Run", null);
        repeatManyPanel     =   new JPanel(new MigLayout("fillx"));
        repeatBox           =   new JComboBox();
        execSetupBtn        =   new JMenuItem("Setup tasks");
        execRepeatBtn       =   new JMenuItem("Repeatable tasks");
        repeatCountSpinner  =   new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
       
        executePopupMenu.add(execSetupBtn);
        executePopupMenu.add(execRepeatBtn);
        
        repeatManyPanel.add(new JLabel("Repeat count"));
        repeatManyPanel.add(repeatCountSpinner, "al center");
        repeatManyPanel.setBackground(Consts.PRESET_COL);
        
        repeatBox.addItem("Automatic");
        repeatBox.addItem("Manual");

        wrapper.setBackground(Consts.PRESET_COL);
        wrapper.add(new JLabel("Repeat method"), "al right");
        wrapper.add(repeatBox, "wrap");
        wrapper.add(repeatManyPanel, "al center, span 2, wrap");
        
        wrapper.add(taskButton, "al right");
        wrapper.add(runButton);
        add(wrapper);
        
        taskButton.addActionListener(this);
        repeatBox.addActionListener(this);
        execSetupBtn.addActionListener(this);
        execRepeatBtn.addActionListener(this);
    }
    
    public void initTaskBean(TasksBean bean)
    {
        List<Task> repeatTasks              =   bean.getRepeatableTasks();
        List<Task> setupTasks               =   bean.getSetupTasks();
        OptionsManagePanel setupTaskList    =   taskPopupPanel.taskOptionPanel.setupTaskListPanel;
        OptionsManagePanel repeatTaskList   =   taskPopupPanel.taskOptionPanel.repeatableTaskListPanel;
        
        for(Task task : repeatTasks)
            setupTaskList.addOption(task, "");
        
        for(Task task : setupTasks)
            repeatTaskList.addOption(task, ""); 
    }
    
    public void executeActions(boolean setup, int n)
    {
        OptionsManagePanel setupTaskList    =   taskPopupPanel.taskOptionPanel.setupTaskListPanel;
        OptionsManagePanel repeatTaskList   =   taskPopupPanel.taskOptionPanel.repeatableTaskListPanel;
        List taskList           =   setup? setupTaskList.getValues(0) : repeatTaskList.getValues(0);
        
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < taskList.size(); j++)
            {
                Task task   =   (Task) taskList.get(j);
                task.performTask();
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
    
    
    public int getOptionsCount()
    {
        return TaskManager.getInstance().getAvailTaskList().size();
    }
    
    public void showTasksDialog()
    {
       
        String title    =   "Manage tasks";   
        int option      =   JOptionPane.showConfirmDialog(null, taskPopupPanel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        
        if(option == JOptionPane.OK_OPTION)
            taskPopupPanel.taskPropPanel.saveTaskProperties();
        
        taskPopupPanel.taskPropPanel.resetProperties();
    }
    
    protected void addOption(String name)
    {
        taskPopupPanel.taskOptionPanel.optionsBox.addItem(name);
    }
    
    protected void removeOption(String name)
    {
        taskPopupPanel.taskOptionPanel.optionsBox.removeItem(name);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        if(src == taskButton)
            showTasksDialog();
        
        else if(src == repeatBox)
            repeatManyPanel.setVisible(repeatBox.getSelectedIndex() == 0);
        
        else if(src == execSetupBtn)
            executeActions(true, 1);
        
        else if(src == execRepeatBtn)
            runRepeat();
    }
    
    private class TaskPopupPanel extends JPanel
    {
        private final String T_LIST_CARD    =   "t_list";
        private final String T_PROP_CARD    =   "t_prop";
        
        private TaskOptionPanel taskOptionPanel;
        private TaskPropertiesPanel taskPropPanel;
        
        public TaskPopupPanel()
        {
            setLayout(new CardLayout());
            taskPropPanel        =   new TaskPropertiesPanel();
            taskOptionPanel      =   new TaskOptionPanel();
            
            add(taskOptionPanel, T_LIST_CARD);
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
        
        private class TaskOptionPanel extends JPanel implements ActionListener
        {
            private JButton addButton;
            private JComboBox optionsBox;
            private TaskListPanel setupTaskListPanel, repeatableTaskListPanel;
            private JTabbedPane taskTypeTabPane;

            public TaskOptionPanel()
            {
                setLayout(new BorderLayout());
                
                addButton               =   new JButton("Add task");
                optionsBox              =   new JComboBox();
                taskTypeTabPane         =   new JTabbedPane();
                setupTaskListPanel      =   new TaskListPanel();
                repeatableTaskListPanel =   new TaskListPanel();   
                addButton.setIcon(new ImageIcon(AppResources.getInstance().getResource("addIcon")));
                
                taskTypeTabPane.add("Setup", setupTaskListPanel);
                taskTypeTabPane.add("Repeatables", repeatableTaskListPanel);

                JPanel topControlsPanel =   new JPanel(new BorderLayout());
                topControlsPanel.add(optionsBox, BorderLayout.CENTER);
                topControlsPanel.add(addButton, BorderLayout.EAST);
                add(topControlsPanel, BorderLayout.NORTH);
                initOptions();

                
                taskTypeTabPane.add("Setup", setupTaskListPanel);
                taskTypeTabPane.add("Repeatables", repeatableTaskListPanel);
                add(taskTypeTabPane, BorderLayout.CENTER);
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
                try
                {
                    Task task                       =   (Task) optionsBox.getSelectedItem();
                    Task nTask                      =   task.getClass().newInstance();
                    TaskListPanel selectedTaskPanel =   (TaskListPanel) taskTypeTabPane.getSelectedComponent();
                    
                    selectedTaskPanel.addOption(nTask, "");
                    
                    TasksBean tasks =   TaskManager.getInstance().getTasks();
                    
                    if(taskTypeTabPane.getSelectedIndex() == 0)
                        tasks.addSetupTask(nTask);
                    else
                        tasks.addRepeatableTask(nTask);
                }
                
                catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(null, "Error failed to create new task");
                }
            }
            
            private class TaskListPanel extends OptionsManagePanel
            {
                public TaskListPanel()
                {
                    outerWrapper.setBackground(Color.WHITE);
                    taskTableModel.addColumn("");
                    taskTableModel.addColumn("");
                    taskTable.getColumnModel().getColumn(1).setMaxWidth(60);
                    taskTable.getColumnModel().getColumn(2).setMaxWidth(60);
                    taskTable.getColumnModel().getColumn(3).setMaxWidth(60);

                     ButtonColumn settingsBtnCol    =   new ButtonColumn(taskTable, new SettingsItemListener(), 2, 
                        new ImageIcon(AppResources.getInstance().getResource("settingsIcon")));

                     ButtonColumn helpBtnCol        =   new ButtonColumn(taskTable, new HelpItemListener(), 3, 
                        new ImageIcon(AppResources.getInstance().getResource("helpIcon")));

                    tableWrapper.setBorder(null);
                    attachTable();
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
                
                private class HelpItemListener extends AbstractAction
                {

                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                        int row     =   Integer.valueOf(e.getActionCommand());
                        Task task   =   (Task) taskTable.getValueAt(row, 0);
                        String name =   task.getTaskName();
                        String desc =   task.getTaskDescription();

                        JOptionPane.showMessageDialog(null, desc, "About task: " + name, JOptionPane.INFORMATION_MESSAGE);
                    }
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
                    String propertyValue    =   propertyModel.getValueAt(i, 1).toString();
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
