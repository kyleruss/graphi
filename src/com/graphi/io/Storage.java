
package com.graphi.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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
}
