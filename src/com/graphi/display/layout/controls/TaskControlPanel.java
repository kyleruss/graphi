//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.app.Consts;
import com.graphi.display.layout.AppResources;
import com.graphi.display.layout.MainPanel;
import com.graphi.display.layout.util.OptionsManagePanel;
import com.graphi.tasks.Task;
import com.graphi.tasks.TaskManager;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
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
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.miginfocom.swing.MigLayout;

public class TaskControlPanel extends JPanel implements ActionListener
{
   /* protected final String[] OPTIONS    =   
    { 
        "Record graph", 
        "Simulate network", 
        "Reset network simulation",
        "Clear recorded entries"
    }; */
    
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
        JComboBox comboBox      =   setupPanel.optionsBox;
        List taskList           =   setup? setupPanel.getValues(0) : repeatPanel.getValues(0);
        
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < taskList.size(); j++)
            {
                String option   =   taskList.get(j).toString();
                int actionIndex =   ((DefaultComboBoxModel) comboBox.getModel()).getIndexOf(option);
                
                if(actionIndex != -1)
                    handleAction(actionIndex);
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
            case 1: middleMan.getControlPanel().getSimulationPanel().showGeneratorSim(); break;
            case 2: middleMan.getControlPanel().getSimulationPanel().resetSim(); break;
            case 3: middleMan.getScreenPanel().getGraphPanel().removeAllRecordedEntries(); break;
        }
    }
    
    public int getOptionsCount()
    {
        return TaskManager.getInstance().getAvailTaskList().size();//OPTIONS.length;
    }
    
    public void showTasksDialog(boolean isSetup)
    {
        if(isSetup)
            JOptionPane.showMessageDialog(null, setupPanel, "Manage setup tasks", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, repeatPanel, "Manage repeat tasks", JOptionPane.INFORMATION_MESSAGE);
    }
    
    protected void addOption(String name)
    {
        setupPanel.optionsBox.addItem(name);
        repeatPanel.optionsBox.addItem(name);
    }
    
    protected void removeOption(String name)
    {
        setupPanel.optionsBox.removeItem(name);
        repeatPanel.optionsBox.removeItem(name);
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
    
    private class TaskPopupPanel extends OptionsManagePanel implements ActionListener
    {
        private JButton addButton;
        private JComboBox optionsBox;
        
        public TaskPopupPanel()
        {
            addButton       =   new JButton("Add task");
            optionsBox      =   new JComboBox();
            addButton.setIcon(new ImageIcon(AppResources.getInstance().getResource("addIcon")));
            
            JPanel topControlsPanel =   new JPanel(new BorderLayout());
            topControlsPanel.add(optionsBox, BorderLayout.CENTER);
            topControlsPanel.add(addButton, BorderLayout.EAST);
            add(topControlsPanel, BorderLayout.NORTH);
            initOptions();
            
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
    }
}
