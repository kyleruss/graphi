//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util;

import java.awt.Component;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FilenameUtils;

public class ComponentUtils 
{
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
    
    public static String getFileExtension(File file)
    {
        if(file == null) return "";
        return FilenameUtils.getExtension(file.getPath());
    }

    public static JPanel wrapComponents(Border border, Component... components)
    {
        JPanel panel    =   new JPanel();
        panel.setBorder(border);
        
        for(Component component : components)
            panel.add(component);
        
        return panel;
    }
}
