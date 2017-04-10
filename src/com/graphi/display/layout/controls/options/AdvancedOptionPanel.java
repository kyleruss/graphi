//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls.options;

import com.graphi.config.AppConfig;
import com.graphi.config.ConfigManager;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AdvancedOptionPanel extends AbstractOptionPanel implements ActionListener
{
    private final int ENABLE_LOG_INDEX      =   0;
    private final int ENABLE_DEBUG_INDEX    =   1;
    private final int ENABLE_UPDATE_INDEX   =   2;
    private final int EXPORT_DIR_INDEX      =   3;
    
    private final JCheckBox enableLogCheck;
    private final JCheckBox enableUpdateCheck;
    private final JCheckBox enableDebugCheck;
    private final JTextField exportDirField;
    
    public AdvancedOptionPanel()
    {
        enableLogCheck      =   new JCheckBox();
        enableUpdateCheck   =   new JCheckBox();
        enableDebugCheck    =   new JCheckBox();
        exportDirField      =   new JTextField();
        
        JLabel logLabel     =   new JLabel("Enable Logging");
        JLabel updateLabel  =   new JLabel("Check for updates");
        JLabel debugLabel   =   new JLabel("Enable Debug Mode");
        JLabel exportLabel  =   new JLabel("Export Directory");
        
        logLabel.setFont(titleFont);
        updateLabel.setFont(titleFont);
        debugLabel.setFont(titleFont);
        exportLabel.setFont(titleFont);
        exportDirField.setPreferredSize(new Dimension(200, 20));
        
        add(logLabel, "gapx 0 100");
        add(enableLogCheck, "wrap");
        add(debugLabel);
        add(enableDebugCheck, "wrap");
        add(updateLabel);
        add(enableUpdateCheck, "wrap");
        add(exportLabel);
        add(exportDirField);
        
        enableLogCheck.addActionListener(this);
        enableUpdateCheck.addActionListener(this);
        enableDebugCheck.addActionListener(this);
    }
    
    @Override
    protected void loadOptions()
    {
        AppConfig appConfig =   ConfigManager.getInstance().getAppConfig();
        
        enableLogCheck.setSelected(appConfig.isEnableLogging());
        enableUpdateCheck.setSelected(appConfig.isEnableUpdating());
        enableDebugCheck.setSelected(appConfig.isEnableDebugMode());
        exportDirField.setText(appConfig.getExportDir());
    }
    
    @Override
    protected void handleOptionChanged(int optionIndex)
    {
        AppConfig appConfig =   ConfigManager.getInstance().getAppConfig();
        
        switch(optionIndex)
        {
            case ENABLE_LOG_INDEX: appConfig.setEnableLogging(enableLogCheck.isSelected()); break;
            case ENABLE_DEBUG_INDEX: appConfig.setEnableDebugMode(enableDebugCheck.isSelected()); break;
            case ENABLE_UPDATE_INDEX: appConfig.setEnableUpdating(enableUpdateCheck.isSelected()); break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src  =   e.getSource();
        
        if(src == enableLogCheck)
            addOptionChanged(ENABLE_LOG_INDEX);
        
        else if(src == enableDebugCheck)
            addOptionChanged(ENABLE_DEBUG_INDEX);
        
        else if(src == enableUpdateCheck)
            addOptionChanged(ENABLE_UPDATE_INDEX);
    }
}
