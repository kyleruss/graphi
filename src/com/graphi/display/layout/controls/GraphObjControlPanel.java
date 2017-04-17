//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.app.Consts;
import com.graphi.display.AppResources;
import com.graphi.display.layout.DataPanel;
import com.graphi.display.layout.util.ComponentUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class GraphObjControlPanel extends JPanel implements ActionListener
{
    protected ButtonGroup editObjGroup;
    protected JRadioButton editVertexRadio, editEdgeRadio;
    protected JLabel selectedLabel;
    protected JButton gObjAddBtn, gObjEditBtn, gObjRemoveBtn;
    
    public GraphObjControlPanel()
    {
        setLayout(new GridLayout(3, 1));
        setBorder(BorderFactory.createTitledBorder("Graph object Controls"));
        setBackground(Consts.TRANSPARENT_COL);
        
        editObjGroup    =   new ButtonGroup();
        editVertexRadio =   new JRadioButton("Vertex");
        editEdgeRadio   =   new JRadioButton("Edge");
        gObjAddBtn      =   new JButton("Add");
        gObjEditBtn     =   new JButton("Edit");
        gObjRemoveBtn   =   new JButton("Delete");
        selectedLabel   =   new JLabel("None");
        gObjAddBtn.setBackground(Color.WHITE);
        gObjEditBtn.setBackground(Color.WHITE);
        gObjRemoveBtn.setBackground(Color.WHITE);
        gObjAddBtn.setIcon(new ImageIcon(AppResources.getInstance().getResource("addIcon")));
        gObjRemoveBtn.setIcon(new ImageIcon(AppResources.getInstance().getResource("removeIcon")));
        gObjEditBtn.setIcon(new ImageIcon(AppResources.getInstance().getResource("editIcon")));

        selectedLabel.setFont(new Font("Arial", Font.BOLD, 12));

        gObjAddBtn.addActionListener(this);
        gObjEditBtn.addActionListener(this);
        gObjRemoveBtn.addActionListener(this);

        editObjGroup.add(editVertexRadio);
        editObjGroup.add(editEdgeRadio);
        editVertexRadio.setSelected(true);
        
        JPanel selectedPanel        =   ComponentUtils.wrapComponents(null, new JLabel("Selected: "), selectedLabel);
        JPanel editObjPanel         =   ComponentUtils.wrapComponents(null, editVertexRadio, editEdgeRadio);
        JPanel gObjOptsPanel        =   ComponentUtils.wrapComponents(null, gObjAddBtn, gObjEditBtn, gObjRemoveBtn);
        selectedPanel.setBackground(Consts.PRESET_COL);
        gObjOptsPanel.setBackground(Consts.PRESET_COL);
        editObjPanel.setBackground(Consts.PRESET_COL);

        add(selectedPanel);
        add(editObjPanel);
        add(gObjOptsPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        DataPanel dataPanel =   DataPanel.getInstance();
        
        if(src == gObjAddBtn)
        {
            if(editVertexRadio.isSelected())
                dataPanel.addVertex();
            else
                dataPanel.addEdge();
        }
        
        else if(src == gObjEditBtn)
        {
            if(editVertexRadio.isSelected())
                dataPanel.editVertex();
            else
                dataPanel.editEdge();
        }

        else if(src == gObjRemoveBtn)
        {
            if(editVertexRadio.isSelected())
                dataPanel.removeVertex();
            else
                dataPanel.removeEdge();
        }
    }

    public JRadioButton getEditVertexRadio()
    {
        return editVertexRadio;
    }

    public JRadioButton getEditEdgeRadio()
    {
        return editEdgeRadio;
    }

    public JLabel getSelectedLabel()
    {
        return selectedLabel;
    }
    
    
}
