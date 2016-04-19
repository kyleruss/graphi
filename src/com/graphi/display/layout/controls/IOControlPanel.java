//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.app.Consts;
import com.graphi.util.ComponentUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class IOControlPanel extends JPanel implements ActionListener
{
    protected JButton exportBtn;
    protected JButton importBtn;
    protected JLabel currentStorageLabel;
    protected JCheckBox directedCheck;
    protected JComboBox ioTypeBox;
    protected JPanel directedCheckWrapper;
    private final ControlPanel outer;

    public IOControlPanel(ControlPanel outer)
    {
        this.outer = outer;
        setLayout(new GridLayout(4, 1));
        setBorder(BorderFactory.createTitledBorder("I/O Controls"));
        
        currentStorageLabel = new JLabel("None");
        importBtn = new JButton("Import");
        exportBtn = new JButton("Export");
        ioTypeBox = new JComboBox();
        directedCheck = new JCheckBox("Directed");
        importBtn.setIcon(new ImageIcon(outer.getMainPanel().openIcon));
        exportBtn.setIcon(new ImageIcon(outer.getMainPanel().saveIcon));
        ioTypeBox.addItem("Graph");
        ioTypeBox.addItem("Log");
        ioTypeBox.addItem("Script");
        ioTypeBox.addItem("Table");
        ioTypeBox.setPreferredSize(new Dimension(150, 30));
        ioTypeBox.addActionListener(this);
        importBtn.addActionListener(this);
        exportBtn.addActionListener(this);
        importBtn.setBackground(Color.WHITE);
        exportBtn.setBackground(Color.WHITE);
        JPanel storageBtnWrapper = ComponentUtils.wrapComponents(null, importBtn, exportBtn);
        JPanel currentGraphWrapper = ComponentUtils.wrapComponents(null, new JLabel("Active: "), currentStorageLabel);
        JPanel ioTypeWrapper = ComponentUtils.wrapComponents(null, ioTypeBox);
        directedCheckWrapper = ComponentUtils.wrapComponents(null, directedCheck);
        storageBtnWrapper.setBackground(Consts.PRESET_COL);
        currentGraphWrapper.setBackground(Consts.PRESET_COL);
        directedCheckWrapper.setBackground(Consts.PRESET_COL);
        ioTypeWrapper.setBackground(Consts.PRESET_COL);
        add(currentGraphWrapper);
        add(ioTypeWrapper);
        add(directedCheckWrapper);
        add(storageBtnWrapper);
        currentStorageLabel.setFont(new Font("Arial", Font.BOLD, 12));
    }

    private void performImport() {
        int typeIndex = ioTypeBox.getSelectedIndex();
        switch (typeIndex) {
            case 0:
                outer.importGraph();
                break;
            case 1:
                outer.importLog();
                break;
            case 2:
                outer.importScript();
                break;
            default:
                break;
        }
    }

    private void performExport() {
        int typeIndex = ioTypeBox.getSelectedIndex();
        switch (typeIndex) {
            case 0:
                outer.exportGraph();
                break;
            case 1:
                outer.exportLog();
                break;
            case 2:
                outer.exportScript();
                break;
            case 3:
                outer.exportTable();
                break;
            default:
                break;
        }
    }

    private void ioTypeChange() {
        int typeIndex = ioTypeBox.getSelectedIndex();
        importBtn.setEnabled(typeIndex != 3);
        directedCheckWrapper.setVisible(typeIndex == 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == importBtn) {
            performImport();
        } else if (src == exportBtn) {
            performExport();
        } else if (src == ioTypeBox) {
            ioTypeChange();
        }
    }
    
}
