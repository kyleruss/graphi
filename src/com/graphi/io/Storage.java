
package com.graphi.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
    public static void saveGraph(SerialGraph graph, String file)
    {
        try(ObjectOutputStream oos  =   new ObjectOutputStream(new FileOutputStream(file)))
        {
            oos.writeObject(graph);
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to save graph");
        }
    }
    
    public static SerialGraph openGraph(SerialGraph graph, String file)
    {
        try(ObjectInputStream ois   =   new ObjectInputStream(new FileInputStream(file)))
        {
            SerialGraph gObj =   (SerialGraph) ois.readObject();
            return gObj;
        }
        
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to open graph");
            return null;
        }
    }
    
    public static void saveOutputLog(String logText, String file)
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
    
    public static String openOutputLog(String file)
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
