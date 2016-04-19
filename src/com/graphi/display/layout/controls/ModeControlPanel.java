//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ModeControlPanel extends JPanel
{
    protected JRadioButton editCheck, selectCheck, moveCheck;
    protected ButtonGroup modeGroup;
    private final ControlPanel outer;
    
    public ModeControlPanel(ControlPanel outer)
    {
        this.outer  =   outer;
        setBorder(BorderFactory.createTitledBorder("Mode controls"));
        
        modeGroup       =   new ButtonGroup();
        editCheck       =   new JRadioButton("Edit");
        selectCheck     =   new JRadioButton("Select");
        moveCheck       =   new JRadioButton("Move");

        editCheck.setIcon(new ImageIcon(outer.getMainPanel().editBlackIcon));
        selectCheck.setIcon(new ImageIcon(outer.getMainPanel().pointerIcon));
        moveCheck.setIcon(new ImageIcon(outer.getMainPanel().moveIcon));

        moveCheck.setSelectedIcon(new ImageIcon(outer.getMainPanel().moveSelectedIcon));
        editCheck.setSelectedIcon(new ImageIcon(outer.getMainPanel().editSelectedIcon));
        selectCheck.setSelectedIcon(new ImageIcon(outer.getMainPanel().pointerSelectedIcon));
        
        editCheck.addActionListener(this);
        selectCheck.addActionListener(this);
        moveCheck.addActionListener(this);

        modeGroup.add(editCheck);
        modeGroup.add(selectCheck);
        modeGroup.add(moveCheck);
        selectCheck.setSelected(true);
        
        add(editCheck);
        add(selectCheck);
        add(moveCheck);
    }
}
