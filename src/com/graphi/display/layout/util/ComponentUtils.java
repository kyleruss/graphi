//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.util;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FilenameUtils;

/**
 * Utility class for various IO/GUI operations in the application
 * Has methods for grabbing files, writing to the output pane & more
 */
public class ComponentUtils 
{
    /**
     * Writes the passed text to the text area passed
     * Typically used to write to the Graphi output pane
     * @param output The text to append
     * @param outputArea The JTextArea to append the output to
     */
    public static void sendToOutput(String output, JTextArea outputArea)
    {
        SimpleDateFormat sdf    =   new SimpleDateFormat("K:MM a dd.MM.yy");
        String date             =   sdf.format(new Date());
        String prefix           =   "\n[" + date + "] ";
        
        SwingUtilities.invokeLater(()->
        {
            outputArea.setText(outputArea.getText() + prefix + output);
        });
    }
    
    /**
     * Opens a dialog for file selection and returns the file chosen
     * Can be used for opening & saving files
     * @param open Pass true for a open/read dialog; false for save/write dialog
     * @param desc The file type description 
     * @param extensions The accepted file types names (e.g txt, jpg, gif)
     * @return 
     */
    public static File getFile(boolean open, String desc, String...extensions)
    {
        JFileChooser jfc                =   new JFileChooser();
        FileNameExtensionFilter filter  =   new FileNameExtensionFilter(desc, extensions);
        jfc.setFileFilter(filter);
        
        if(open)
            jfc.showOpenDialog(null);
        else
            jfc.showSaveDialog(null);
        
        return jfc.getSelectedFile();
    }
    
    /**
     * Gets the extension name from a file
     * @param file The File to get the extension from
     * @return A String of the files extension name
     */
    public static String getFileExtension(File file)
    {
        if(file == null) return "";
        return FilenameUtils.getExtension(file.getPath());
    }

    /**
     * Creates a panel and adds all passed components to it
     * @param border Adds a border to the created panel
     * @param components The components to be added to the panel
     * @return A panel that wraps the passed components and with the border passed
     */
    public static JPanel wrapComponents(Border border, Component... components)
    {
        JPanel panel    =   new JPanel();
        panel.setBorder(border);
        
        for(Component component : components)
            panel.add(component);
        
        return panel;
    }
    
    public static void setTransparentControl(JButton control)
    {
        if(control == null) return;
        
        control.setBorderPainted(false);
        control.setFocusable(false);
        control.setContentAreaFilled(false);
        control.setOpaque(false);
    }
    
    public static String colourToHex(Color colour)
    {
        return String.format("#%02X%02X%02X", colour.getRed(), colour.getGreen(), colour.getBlue());
    }
}
