//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.display.layout.AppResources;
import com.graphi.display.layout.util.GUIUtils;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ModeControlPanel extends JPanel implements ActionListener
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

        AppResources resources  =   AppResources.getInstance();
        editCheck.setIcon(new ImageIcon(resources.getResource("editBlackIcon")));
        selectCheck.setIcon(new ImageIcon(resources.getResource("pointerIcon")));
        moveCheck.setIcon(new ImageIcon(resources.getResource("moveIcon")));
        GUIUtils.showTooltip(editCheck, "Hey!");

        moveCheck.setSelectedIcon(new ImageIcon(resources.getResource("moveSelectedIcon")));
        editCheck.setSelectedIcon(new ImageIcon(resources.getResource("editSelectedIcon")));
        selectCheck.setSelectedIcon(new ImageIcon(resources.getResource("pointerSelectedIcon")));
        
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

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        EditingModalGraphMouse mouse    =   outer.getMainPanel().getScreenPanel().getGraphPanel().getMouse();
        
        if(src == editCheck)
        {
            mouse.setMode(ModalGraphMouse.Mode.EDITING);
            mouse.remove(mouse.getPopupEditingPlugin());
        }

        else if(src == moveCheck)
            mouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

        else if(src == selectCheck)
            mouse.setMode(ModalGraphMouse.Mode.PICKING);
    }
    
    

    public JRadioButton getEditCheck() 
    {
        return editCheck;
    }

    public JRadioButton getSelectCheck() 
    {
        return selectCheck;
    }

    public JRadioButton getMoveCheck()
    {
        return moveCheck;
    }
    
    
}
