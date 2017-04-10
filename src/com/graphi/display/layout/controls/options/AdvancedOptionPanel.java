//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls.options;

import java.awt.Dimension;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AdvancedOptionPanel extends AbstractOptionPanel
{
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
    }
    
    @Override
    protected void loadOptions()
    {
        
    }
    
    @Override
    protected void handleOptionChanged(int optionIndex)
    {
        
    }
}
