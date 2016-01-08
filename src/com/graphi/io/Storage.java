//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.io;

import edu.uci.ics.jung.graph.Graph;
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


public class Storage
{
    public static void saveObj(Object obj, File file)
    {
        try(ObjectOutputStream oos  =   new ObjectOutputStream(new FileOutputStream(file)))
        {
            oos.writeObject(obj);
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to save graph: " + e.getMessage());
        }
    }
    
    public static Object openObj(File file)
    {
        try(ObjectInputStream ois   =   new ObjectInputStream(new FileInputStream(file)))
        {
            return ois.readObject();
        }
        
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to open graph");
            return null;
        }
    }
    
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
