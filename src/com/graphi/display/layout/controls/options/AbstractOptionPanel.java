//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls.options;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public abstract class AbstractOptionPanel extends JPanel
{
    protected Font titleFont;
    protected Font standardFont;
    protected Set<Integer> optionsChanged;
    
    protected AbstractOptionPanel()
    {
        setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 0));
        setLayout(new MigLayout("", "", "50"));
        
        titleFont       =   new Font("Arial", Font.PLAIN, 24);
        standardFont    =   new Font("Arial", Font.PLAIN, 14);   
        optionsChanged  =   new HashSet<>();
    }
    
    public void addOptionChanged(int optionIndex)
    {
        optionsChanged.add(optionIndex);
    }
    
    public void clearOptions()
    {
        optionsChanged.clear();
    }
    
    public Set<Integer> getOptionsChanged()
    {
        return optionsChanged;
    }
    
    public boolean hasUpdates()
    {
        return !optionsChanged.isEmpty();
    }
    
    public void updateOptions()
    {
        for(int optionIndex : optionsChanged)
            handleOptionChanged(optionIndex);
    }
    
    protected abstract void loadOptions();
    
    protected abstract void handleOptionChanged(int optionIndex);
    
    protected void initLayout(int numSettings)
    {
        GridLayout layout   =   new GridLayout(numSettings, 2);
        setLayout(layout);
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
