
package com.graphi.display.layout.controls;

import com.graphi.display.AppResources;
import com.graphi.display.layout.GraphPanel;
import static com.graphi.display.layout.GraphPanel.SELECT_MODE;
import com.graphi.display.layout.util.ComponentUtils;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

public class DisplayNavigationPanel extends JPanel implements ActionListener
{
    private JButton moveBtn;
    private JButton selectBtn;
    private JButton editBtn;
    private BufferedImage currentBG;
    private int mode;

    private DisplayNavigationPanel()
    {
        setPreferredSize(new Dimension(120, 150));
        setLayout(new BorderLayout());

        moveBtn                 =   new JButton();
        selectBtn               =   new JButton();
        editBtn                 =   new JButton();
        currentBG               =   AppResources.getInstance().getResource("displayNavSelect");
        mode                    =   SELECT_MODE;

        ComponentUtils.setTransparentControl(moveBtn);
        ComponentUtils.setTransparentControl(selectBtn);
        ComponentUtils.setTransparentControl(editBtn);

        editBtn.setPreferredSize(new Dimension(40, 60));
        moveBtn.setPreferredSize(new Dimension(60, 40));
        selectBtn.setPreferredSize(new Dimension(60, 40));
        Component botPadding    =   Box.createRigidArea(new Dimension(1, 20));

        add(editBtn, BorderLayout.NORTH);
        add(moveBtn, BorderLayout.WEST);
        add(selectBtn, BorderLayout.EAST);
        add(botPadding, BorderLayout.SOUTH);

        editBtn.addActionListener(this);
        moveBtn.addActionListener(this);
        selectBtn.addActionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(currentBG, 0, 0, null);
    }

    private void toggleSelect()
    {
        GraphPanel.getInstance().getMouse().setMode(ModalGraphMouse.Mode.PICKING);
        currentBG   =   AppResources.getInstance().getResource("displayNavSelect");
        repaint();

    }

    private void toggleMove()
    {
        GraphPanel.getInstance().getMouse().setMode(ModalGraphMouse.Mode.TRANSFORMING);
        currentBG   =   AppResources.getInstance().getResource("displayNavMove");   
        repaint();

    }

    private void toggleEdit()
    {
        EditingModalGraphMouse mouse    =   GraphPanel.getInstance().getMouse();
        mouse.setMode(ModalGraphMouse.Mode.EDITING);
        mouse.remove(mouse.getPopupEditingPlugin());
        currentBG   =   AppResources.getInstance().getResource("displayNavEdit");
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src  =   e.getSource();

        if(src == editBtn)
            toggleEdit();

        else if(src == moveBtn)
            toggleMove();

        else if(src == selectBtn)
            toggleSelect();

    }
}
