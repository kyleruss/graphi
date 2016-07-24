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
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class AppResources 
{
    protected static AppResources instance;
    protected Map<String, BufferedImage> resourceMap;
    
    protected AppResources()
    {
        initResources();
    }
    
    protected void initResources()
    {
        try
        {
            resourceMap  =   new HashMap<>();
            resourceMap.put("addIcon", ImageIO.read(new File(Consts.IMG_DIR + "addSmallIcon.png")));
            resourceMap.put("removeIcon", ImageIO.read(new File(Consts.IMG_DIR + "removeSmallIcon.png")));
            resourceMap.put("colourIcon", ImageIO.read(new File(Consts.IMG_DIR + "color_icon.png")));
            resourceMap.put("clipIcon", ImageIO.read(new File(Consts.IMG_DIR + "clipboard.png")));
            resourceMap.put("saveIcon", ImageIO.read(new File(Consts.IMG_DIR + "new_file.png")));
            resourceMap.put("openIcon", ImageIO.read(new File(Consts.IMG_DIR + "open_icon.png")));
            resourceMap.put("editBlackIcon", ImageIO.read(new File(Consts.IMG_DIR + "editblack.png")));
            resourceMap.put("pointerIcon", ImageIO.read(new File(Consts.IMG_DIR + "pointer.png")));
            resourceMap.put("moveIcon", ImageIO.read(new File(Consts.IMG_DIR + "move.png")));
            resourceMap.put("moveSelectedIcon", ImageIO.read(new File(Consts.IMG_DIR + "move_selected.png")));
            resourceMap.put("editSelectedIcon", ImageIO.read(new File(Consts.IMG_DIR + "editblack_selected.png")));
            resourceMap.put("pointerSelectedIcon", ImageIO.read(new File(Consts.IMG_DIR + "pointer_selected.png")));
            resourceMap.put("graphIcon", ImageIO.read(new File(Consts.IMG_DIR + "graph.png")));
            resourceMap.put("tableIcon", ImageIO.read(new File(Consts.IMG_DIR + "table.png")));
            resourceMap.put("executeIcon", ImageIO.read(new File(Consts.IMG_DIR + "execute.png")));
            resourceMap.put("resetIcon", ImageIO.read(new File(Consts.IMG_DIR + "reset.png")));
            resourceMap.put("editIcon", ImageIO.read(new File(Consts.IMG_DIR + "edit.png")));
            resourceMap.put("playIcon", ImageIO.read(new File(Consts.IMG_DIR + "play.png")));
            resourceMap.put("stopIcon", ImageIO.read(new File(Consts.IMG_DIR + "stop.png")));
            resourceMap.put("recordIcon", ImageIO.read(new File(Consts.IMG_DIR + "record.png")));
            resourceMap.put("closeIcon", ImageIO.read(new File(Consts.IMG_DIR + "close.png")));
            resourceMap.put("graphIconV2", ImageIO.read(new File(Consts.IMG_DIR + "graph2.png")));
            resourceMap.put("settingsIcon", ImageIO.read(new File(Consts.IMG_DIR + "settingsIcon.png")));
        }
        
        catch(IOException e)
        {
            ErrorManager.GUIErrorMessage("Failed to load resources", true, e, null);
        }
    }
    
    public Map<String, BufferedImage> getResourceMap()
    {
        return resourceMap;
    }
    
    public BufferedImage getResource(String resourceName)
    {
        return resourceMap.get(resourceName);
    }
    
    public boolean hasResource(String resourceName)
    {
        return resourceMap.containsKey(resourceName);
    }
    
    public static AppResources getInstance()
    {
        if(instance == null) instance   =   new AppResources();
        return instance;
    }
}
