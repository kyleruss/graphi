//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.display.AppResources;
import com.graphi.display.layout.controls.ControlPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class ViewerControlPanel extends JPanel implements ActionListener
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

        ImageIcon clrIcon   =   new ImageIcon(AppResources.getInstance().getResource("colourIcon"));
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
    
    public void showVertexBGChange()
    {
        Color selectedColour    =   JColorChooser.showDialog(null, "Choose vertex colour", Color.BLACK);

        if(selectedColour != null)
            outer.getMainPanel().getScreenPanel().getGraphPanel().setVertexColour(selectedColour, null);
    }

    public void showEdgeBGChange()
    {
        Color selectedColour    =   JColorChooser.showDialog(null, "Choose edge colour", Color.BLACK);

        if(selectedColour != null)
            outer.getMainPanel().getScreenPanel().getGraphPanel().setEdgeColour(selectedColour, null);
    }

    public void showViewerBGChange()
    {
        Color selectedColour    =   JColorChooser.showDialog(null, "Choose viewer background colour", Color.WHITE);

        if(selectedColour != null)
            outer.getMainPanel().getScreenPanel().getGraphPanel().getGraphViewer().setBackground(selectedColour);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        
        if(src == vertexBGBtn)
            showVertexBGChange();

        else if(src == edgeBGBtn)
            showEdgeBGChange();

        else if(src == viewerBGBtn)
            showViewerBGChange();

        else if(src == viewerVLabelsCheck)
            outer.getMainPanel().getScreenPanel().getGraphPanel().showVertexLabels(viewerVLabelsCheck.isSelected());

        else if(src == viewerELabelsCheck)
            outer.getMainPanel().getScreenPanel().getGraphPanel().showEdgeLabels(viewerELabelsCheck.isSelected());
    }

    public JCheckBox getViewerVLabelsCheck() 
    {
        return viewerVLabelsCheck;
    }

    public JCheckBox getViewerELabelsCheck() 
    {
        return viewerELabelsCheck;
    }
    
    
}
