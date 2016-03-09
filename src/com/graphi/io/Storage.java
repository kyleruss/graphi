//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;

/**
 * A utility class for various io operations
 * Includes methods for importing/exporting serialized objects and log files
 */
public class Storage
{
    /**
     * Exports the serialized object to file
     * @param obj The object to export. Must implement Serializable interface
     * @param file The file to export the object to
     */
    public static void saveObj(Object obj, File file)
    {
        try(ObjectOutputStream oos  =   new ObjectOutputStream(new FileOutputStream(file)))
        {
            oos.writeObject(obj);
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to save file: " + e.getMessage());
        }
    }
    
    /**
     * Imports an object from file
     * @param file The file to import the object from
     * @return The object read from the file
     */
    public static Object openObj(File file)
    {
        try(ObjectInputStream ois   =   new ObjectInputStream(new FileInputStream(file)))
        {
            return ois.readObject();
        }
        
        catch(Exception e)
        {
            return null;
        }
    }
    
    /**
     * Saves the passed text to file
     * @param logText The text to write to file
     * @param file The file to save the text to
     */
    public static void saveOutputLog(String logText, File file)
    {
        try(BufferedWriter bw   =   new BufferedWriter(new FileWriter(file)))
        {
            bw.write(logText);
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to save output log");
        }
    }
    
    /**
     * Reads & returns the text contained in the file
     * @param file The file to get the containing text
     * @return The text read from the file
     */
    public static String openOutputLog(File file)
    {
        try(BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String text =   "";
            String line;
            while((line = br.readLine()) != null)
                text += line + "\n";
            
            return text;
        }
        
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to read output log");
            return "";
        }
    }
}
