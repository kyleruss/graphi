
package com.graphi.display.layout.controls;



import com.graphi.app.Consts;
import com.graphi.display.layout.MainPanel;
import com.graphi.display.layout.util.ButtonColumn;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import net.miginfocom.swing.MigLayout;

public class TaskControlPanel extends JPanel implements ActionListener
{
    private final String[] OPTIONS    =   
    { 
        "Record graph", 
        "Simulate network", 
        "Reset network simulation",
    };
    
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
        DefaultTableModel model =   setup? setupPanel.taskTableModel : repeatPanel.taskTableModel;
        int rowCount            =   model.getRowCount();
        JComboBox comboBox      =   setupPanel.optionsBox;
        
        for(int i = 0; i < n; i++)
        {
            for(int row = 0; row < rowCount; row++)
            {
                String option   =   model.getValueAt(row, 0).toString();
                int actionIndex =   ((DefaultComboBoxModel) comboBox.getModel()).getIndexOf(option);

                if(actionIndex != -1) handleAction(actionIndex);
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
    
    
    private void handleAction(int actionIndex)
    {
        MainPanel middleMan  =   controlPanel.getMainPanel();
        
        switch(actionIndex)
        {
            case 0: middleMan.getScreenPanel().getGraphPanel().addRecordedGraph(); break;
            case 1: middleMan.getControlPanel().getSimulationPanel().showGeneratorSim(); break;
            case 3: middleMan.getControlPanel().getSimulationPanel().resetSim(); break;
        }
    }
    
    public void showTasksDialog(boolean isSetup)
    {
        if(isSetup)
            JOptionPane.showMessageDialog(null, setupPanel, "Manage setup tasks", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, repeatPanel, "Manage repeat tasks", JOptionPane.INFORMATION_MESSAGE);
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
    
    private class TaskPopupPanel extends JPanel implements ActionListener
    {
        private JButton addButton;
        private JComboBox optionsBox;
        private JTable taskTable;
        private DefaultTableModel taskTableModel;
        
        public TaskPopupPanel()
        {
            setLayout(new BorderLayout());
            setBackground(Consts.PRESET_COL);
            setPreferredSize(new Dimension(300, 270));
            
            addButton       =   new JButton("Add task");
            optionsBox      =   new JComboBox();
            taskTableModel  =   new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int col)
                {
                    return col != 0;
                }
            };
            
            taskTable       =   new JTable(taskTableModel);
            addButton.setIcon(new ImageIcon(controlPanel.getMainPanel().addIcon));
            
            taskTableModel.addColumn("");
            taskTableModel.addColumn("");
            taskTable.getColumnModel().getColumn(0).setCellRenderer(new TaskLabelCellRenderer());
            
            ButtonColumn btnColumn  =   new ButtonColumn(taskTable, new TaskItemListener(), 1, new ImageIcon(controlPanel.getMainPanel().removeIcon));
            
            taskTable.getColumnModel().getColumn(0).setCellRenderer(new TaskLabelCellRenderer());
            taskTable.getColumnModel().getColumn(0).setPreferredWidth(120);
            taskTable.getColumnModel().getColumn(1).setPreferredWidth(5);
            taskTable.setBackground(Consts.PRESET_COL);
            
            JPanel tableWrapper =   new JPanel(new BorderLayout());
            JPanel outerWrapper =   new JPanel(new BorderLayout());
            tableWrapper.setBorder(BorderFactory.createTitledBorder("Tasks"));
            
            tableWrapper.add(taskTable);
            outerWrapper.add(tableWrapper);
            
            JPanel topControlsPanel =   new JPanel(new BorderLayout());
            topControlsPanel.add(optionsBox, BorderLayout.CENTER);
            topControlsPanel.add(addButton, BorderLayout.EAST);
            
            initOptions();
            addButton.addActionListener(this);
            
            add(outerWrapper, BorderLayout.CENTER);
            add(topControlsPanel, BorderLayout.NORTH);
        }
        
        private void initOptions()
        {
            for(String option : OPTIONS)
                optionsBox.addItem(option);
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            taskTableModel.addRow(new Object[] { optionsBox.getSelectedItem(), "" });
        }
        
        private class TaskItemListener extends AbstractAction
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
                taskLabel.setIcon(new ImageIcon(controlPanel.getMainPanel().executeIcon));
                
                return taskLabel;
            }
        }
    }
    
}
