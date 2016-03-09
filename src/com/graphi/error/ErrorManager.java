//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.error;

import com.graphi.app.AppManager;
import java.util.Date;
import javax.swing.JOptionPane;

public class ErrorManager
{
    private static ErrorDump errorDump;
    private final AppManager appManager;
    
    public ErrorManager(AppManager appManager)
    {
        this.appManager =   appManager;
        errorDump       =   ErrorDump.getLatestErrorDump();
    }
    
    public static void GUIErrorMessage(String message, boolean logError, Exception e, Object errorData)
    {
        JOptionPane.showMessageDialog(null, message);
        
        if(logError)
            logError(message, e, errorData);
    }
    
    public static void CLIErrorMessage(String message, boolean logError, Exception e, Object errorData)
    {
        String prefix   =   "[Error] ";
        System.out.println(prefix + message);
        
        if(logError)
            logError(message, e, errorData);
    }
    
    public static void logError(String message, Exception e, Object errorData)
    {
         ErrorBean bean  =   new ErrorBean(new Date(), e, message, errorData);
         errorDump.addErrorBean(bean);
         errorDump.saveErrorDumpInstance();
    }
}
