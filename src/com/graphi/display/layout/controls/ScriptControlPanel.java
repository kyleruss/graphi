//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class ScriptControlPanel extends JPanel implements ActionListener
{
    protected JButton recordCtrlsBtn;
    protected boolean recording;
    protected JButton displayCtrlsBtn;
    protected JLabel activeScriptLabel;
    protected JPanel wrapper;
    private final ControlPanel outer;
    
    public ScriptControlPanel(ControlPanel outer)
    {
        this.outer      =   outer;
        setLayout(new BorderLayout());
        
        wrapper         =   new JPanel(new MigLayout("fillx"));
        activeScriptLabel   =   new JLabel("None");
        recordCtrlsBtn      =   new JButton("Record controls");
        displayCtrlsBtn     =   new JButton("Playback controls");
        recording           =   false;

        recordCtrlsBtn.setIcon(new ImageIcon(outer.getMainPanel().recordIcon));
        displayCtrlsBtn.setIcon(new ImageIcon(outer.getMainPanel().playIcon));
        activeScriptLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        wrapper.setBorder(BorderFactory.createTitledBorder("Script controls"));
        wrapper.add(new JLabel("Active script: "), "al right");
        wrapper.add(activeScriptLabel, "wrap");
        wrapper.add(recordCtrlsBtn, "span 2, al center, wrap");
        wrapper.add(displayCtrlsBtn, "span 2, al center");
        add(wrapper);

        recordCtrlsBtn.addActionListener(this);
        displayCtrlsBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src  =   e.getSource();
        
        if(src == displayCtrlsBtn)
            outer.getMainPanel().getScreenPanel().getGraphPanel().changePlaybackPanel(outer.getMainPanel().getScreenPanel().getGraphPanel().PLAYBACK_CARD);

        else if(src == recordCtrlsBtn)
            outer.getMainPanel().getScreenPanel().getGraphPanel().changePlaybackPanel(outer.getMainPanel().getScreenPanel().getGraphPanel().RECORD_CARD);
    }
}
