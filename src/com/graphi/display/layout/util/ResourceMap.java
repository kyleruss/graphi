//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.util;

import com.graphi.app.Consts;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;

public abstract class ResourceMap 
{
    //Table containing application images
    //Key: name of the image
    //Value: the image for the corresponding name
    protected Map<String, BufferedImage> resourceMap;
    
    protected ResourceMap()
    {
        initResources();
    }
    
    protected abstract void initResources();
    
    /**
     * Returns an image for the passed image name
     * @param name Name of the image
     * @return The BufferedImage corresponding to the passed image name
     * @throws java.io.IOException
     */
    public BufferedImage getImage(String name) throws IOException
    {
        return ImageIO.read(new File(Consts.IMG_DIR + name));
    }
    
      /**
     * Returns an image for the passed image name
     * @param name Name of the image
     * @param dir Directory of the image
     * @return The BufferedImage corresponding to the passed image name
     * @throws java.io.IOException
     */
    public BufferedImage getImage(String name, String dir) throws IOException
    {
        return ImageIO.read(new File(dir + name));
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
    
    public void addImageResource(String resourceName, String fileName)
    throws IOException
    {
        addImageResourceFromDir(resourceName, Consts.IMG_DIR, fileName);
    }
    
    public void addImageResourceFromDir(String resourceName, String directory, String fileName)
    throws IOException
    {
        String path =   directory + fileName;
        resourceMap.put(resourceName, getImage(path));
    }
    
    public void removeResource(String resourceName)
    {
        resourceMap.remove(resourceName);
    }
}
