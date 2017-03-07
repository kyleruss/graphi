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
    
    //Table containing application images
    //Key: name of the image
    //Value: the image for the corresponding name
    protected Map<String, BufferedImage> resourceMap;
    
    protected AppResources()
    {
        initResources();
    }
    
    /**
     * Initializes all image resources for the application
     */
    private void initResources()
    {
        try
        {
            resourceMap  =   new HashMap<>();
            resourceMap.put("addIcon", getImage("addSmallIcon.png"));
            resourceMap.put("removeIcon", getImage("removeSmallIcon.png"));
            resourceMap.put("colourIcon", getImage("color_icon.png"));
            resourceMap.put("clipIcon", getImage("clipboard.png"));
            resourceMap.put("saveIcon", getImage("new_file.png"));
            resourceMap.put("openIcon", getImage("open_icon.png"));
            resourceMap.put("editBlackIcon", getImage("editblack.png"));
            resourceMap.put("pointerIcon", getImage("pointer.png"));
            resourceMap.put("moveIcon", getImage("move.png"));
            resourceMap.put("moveSelectedIcon", getImage("move_selected.png"));
            resourceMap.put("editSelectedIcon", getImage("editblack_selected.png"));
            resourceMap.put("pointerSelectedIcon", getImage("pointer_selected.png"));
            resourceMap.put("graphIcon", getImage("graph.png"));
            resourceMap.put("tableIcon", getImage("table.png"));
            resourceMap.put("executeIcon", getImage("execute.png"));
            resourceMap.put("resetIcon", getImage("reset.png"));
            resourceMap.put("editIcon", getImage("edit.png"));
            resourceMap.put("playIcon", getImage("play.png"));
            resourceMap.put("stopIcon", getImage("stop.png"));
            resourceMap.put("recordIcon", getImage("record.png"));
            resourceMap.put("closeIcon", getImage("close.png"));
            resourceMap.put("graphIconV2", getImage("graph2.png"));
            resourceMap.put("settingsIcon", getImage("settingsIcon.png"));
            resourceMap.put("helpIcon", getImage("help.png"));
        }
        
        catch(IOException e)
        {
            ErrorManager.GUIErrorMessage("Failed to load resources", true, e, null);
        }
    }
    
    /**
     * Returns an image for the passed image name
     * @param name Name of the image
     * @return The BufferedImage corresponding to the passed image name
     */
    private BufferedImage getImage(String name) throws IOException
    {
        return ImageIO.read(new File(Consts.IMG_DIR + name));
    }
    
    /**
     * @return The image resources in the application
     */
    public Map<String, BufferedImage> getResourceMap()
    {
        return resourceMap;
    }
    
    /**
     * @param resourceName The image name/alias (not path)
     * @return The image for the corresponding resource name
     */
    public BufferedImage getResource(String resourceName)
    {
        return resourceMap.get(resourceName);
    }
    
    /**
     * @param resourceName the name of the image
     * @return True if the image has been initialized; false otherwise
     */
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
