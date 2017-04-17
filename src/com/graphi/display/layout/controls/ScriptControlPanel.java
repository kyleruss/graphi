//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.display.AppResources;
import com.graphi.display.layout.GraphPanel;
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
    
    public ScriptControlPanel()
    {
        setLayout(new BorderLayout());
        
        wrapper         =   new JPanel(new MigLayout("fillx"));
        activeScriptLabel   =   new JLabel("None");
        recordCtrlsBtn      =   new JButton("Record controls");
        displayCtrlsBtn     =   new JButton("Playback controls");
        recording           =   false;

        recordCtrlsBtn.setIcon(new ImageIcon(AppResources.getInstance().getResource("recordIcon")));
        displayCtrlsBtn.setIcon(new ImageIcon(AppResources.getInstance().getResource("playIcon")));
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
        GraphPanel grapPanel    =   GraphPanel.getInstance();
        
        if(src == displayCtrlsBtn)
            grapPanel.changePlaybackPanel(grapPanel.PLAYBACK_CARD);

        else if(src == recordCtrlsBtn)
            grapPanel.changePlaybackPanel(grapPanel.RECORD_CARD);
    }

    public boolean isRecording() 
    {
        return recording;
    }

    public JLabel getActiveScriptLabel() 
    {
        return activeScriptLabel;
    }
    
    
}
