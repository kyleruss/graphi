//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.display.layout.controls.ControlPanel;
import com.graphi.app.AppManager;
import com.graphi.app.Consts;
import com.graphi.display.MainMenu;
import com.graphi.error.ErrorManager;
import com.graphi.util.GraphData;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class MainPanel extends JPanel 
{
    protected ControlPanel controlPanel;
    protected ScreenPanel screenPanel;
    protected JSplitPane splitPane;
    protected JScrollPane controlScroll;
    
    public BufferedImage addIcon, removeIcon, colourIcon;
    public BufferedImage clipIcon, openIcon, saveIcon;
    public BufferedImage editBlackIcon, pointerIcon, moveIcon;
    public BufferedImage moveSelectedIcon, editSelectedIcon, pointerSelectedIcon;
    public BufferedImage graphIcon, tableIcon, resetIcon, executeIcon;
    public BufferedImage editIcon, playIcon, stopIcon, recordIcon, closeIcon;
    
    protected GraphData data;
    protected MainMenu menu;
    protected JFrame frame; 
    protected AppManager appManager;
    
    public MainPanel(AppManager appManager)
    {
        setPreferredSize(new Dimension(Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT));
        setLayout(new BorderLayout());        
        initResources();
        
        this.appManager     =   appManager;
        menu                =   appManager.getWindow().getMenu();
        frame               =   appManager.getWindow().getFrame();
        data                =   new GraphData();
        controlPanel        =   new ControlPanel(this);
        screenPanel         =   new ScreenPanel(this);
        splitPane           =   new JSplitPane();
        controlScroll       =   new JScrollPane(controlPanel);

        controlScroll.setBorder(null);
        controlScroll.getVerticalScrollBar().setUnitIncrement(25);
        splitPane.setLeftComponent(screenPanel);
        splitPane.setRightComponent(controlScroll); 
        splitPane.setResizeWeight(Consts.MAIN_SPLIT_WG);
        add(splitPane, BorderLayout.CENTER);
    }
    
    public GraphData getGraphData()
    {
        return data;
    }
    
    public void setGraphData(GraphData data)
    {
        this.data   =   data;
    }
    
    protected void initResources()
    {
        try
        {
            addIcon             =   ImageIO.read(new File(Consts.IMG_DIR + "addSmallIcon.png"));
            removeIcon          =   ImageIO.read(new File(Consts.IMG_DIR + "removeSmallIcon.png"));   
            colourIcon          =   ImageIO.read(new File(Consts.IMG_DIR + "color_icon.png"));   
            clipIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "clipboard.png"));  
            saveIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "new_file.png"));
            openIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "open_icon.png"));
            editBlackIcon       =   ImageIO.read(new File(Consts.IMG_DIR + "editblack.png"));
            pointerIcon         =   ImageIO.read(new File(Consts.IMG_DIR + "pointer.png"));
            moveIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "move.png"));
            moveSelectedIcon    =   ImageIO.read(new File(Consts.IMG_DIR + "move_selected.png"));
            editSelectedIcon    =   ImageIO.read(new File(Consts.IMG_DIR + "editblack_selected.png"));
            pointerSelectedIcon =   ImageIO.read(new File(Consts.IMG_DIR + "pointer_selected.png"));
            graphIcon           =   ImageIO.read(new File(Consts.IMG_DIR + "graph.png"));
            tableIcon           =   ImageIO.read(new File(Consts.IMG_DIR + "table.png"));
            executeIcon         =   ImageIO.read(new File(Consts.IMG_DIR + "execute.png"));
            resetIcon           =   ImageIO.read(new File(Consts.IMG_DIR + "reset.png"));
            editIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "edit.png"));
            playIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "play.png"));
            stopIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "stop.png"));
            recordIcon          =   ImageIO.read(new File(Consts.IMG_DIR + "record.png"));
            closeIcon           =   ImageIO.read(new File(Consts.IMG_DIR + "close.png"));
        }
        
        catch(IOException e)
        {
            ErrorManager.GUIErrorMessage("Failed to load resources", true, e, null);
        }
    }
    
    
    public ControlPanel getControlPanel() 
    {
        return controlPanel;
    }

    public ScreenPanel getScreenPanel() 
    {
        return screenPanel;
    }

    public GraphData getData()
    {
        return data;
    }

    public MainMenu getMenu()
    {
        return menu;
    }

    public AppManager getAppManager() 
    {
        return appManager;
    }
}
