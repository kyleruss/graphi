//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import com.graphi.app.Consts;
import com.graphi.error.ErrorManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AppResources 
{
    protected static AppResources instance;
    protected BufferedImage addIcon, removeIcon, colourIcon;
    protected BufferedImage clipIcon, openIcon, saveIcon;
    protected BufferedImage editBlackIcon, pointerIcon, moveIcon;
    protected BufferedImage moveSelectedIcon, editSelectedIcon, pointerSelectedIcon;
    protected BufferedImage graphIcon, tableIcon, resetIcon, executeIcon;
    protected BufferedImage editIcon, playIcon, stopIcon, recordIcon, closeIcon;
    
    protected AppResources()
    {
        initResources();
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
    
    public static AppResources getResources()
    {
        if(instance == null) instance   =   new AppResources();
        return instance;
    }
}
