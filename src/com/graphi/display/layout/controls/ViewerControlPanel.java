//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class ViewerControlPanel extends JPanel
{
    protected JCheckBox viewerVLabelsCheck;
    protected JCheckBox viewerELabelsCheck;
    protected JButton viewerBGBtn, vertexBGBtn, edgeBGBtn;
    protected JPanel wrapper;
    private ControlPanel outer;
    
    public ViewerControlPanel(ControlPanel outer)
    {
        this.outer  =       outer;
        setLayout(new BorderLayout());
        
        
        wrapper                 =   new JPanel(new MigLayout("fillx"));
        viewerVLabelsCheck      =   new JCheckBox("Vertex labels");
        viewerELabelsCheck      =   new JCheckBox("Edge labels");
        viewerBGBtn             =   new JButton("Choose");
        vertexBGBtn             =   new JButton("Choose");
        edgeBGBtn               =   new JButton("Choose");

        ImageIcon clrIcon   =   new ImageIcon(outer.getMainPanel().colourIcon);
        viewerBGBtn.setIcon(clrIcon);
        vertexBGBtn.setIcon(clrIcon);
        edgeBGBtn.setIcon(clrIcon);

        viewerBGBtn.addActionListener(this);
        vertexBGBtn.addActionListener(this);
        edgeBGBtn.addActionListener(this);
        viewerVLabelsCheck.addActionListener(this);
        viewerELabelsCheck.addActionListener(this);

        wrapper.setBorder(BorderFactory.createTitledBorder("Viewer controls"));
        wrapper.add(viewerVLabelsCheck, "wrap, span 2, al center");
        wrapper.add(viewerELabelsCheck, "wrap, span 2, al center");
        wrapper.add(new JLabel("Vertex background"), "al right");
        wrapper.add(vertexBGBtn, "wrap");
        wrapper.add(new JLabel("Edge background"), "al right");
        wrapper.add(edgeBGBtn, "wrap");
        wrapper.add(new JLabel("Viewer background"), "al right");
        wrapper.add(viewerBGBtn, "wrap");
        add(wrapper);
    }
}
