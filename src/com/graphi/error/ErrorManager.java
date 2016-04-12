//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.error;

import com.graphi.app.AppManager;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 * Manages exception display and logging
 * Maintains and mutates the most recent ErrorDump instance
 */
public class ErrorManager
{
    //The active ErrorDump instance
    //Uses most recent instance 
    private static ErrorDump errorDump;
    
    //The parent app manager
    private final AppManager appManager;
    
    public ErrorManager(AppManager appManager)
    {
        this.appManager =   appManager;
        errorDump       =   ErrorDump.getLatestErrorDump();
    }
    
    /**
     * Displays a GUI dialog with the passed message
     * @param message The error message to display
     * @param logError Log/Write this error to the ErrorDump
     * @param e The Exception that occurred
     * @param errorData Additional data that's relevant to the exception
     */
    public static void GUIErrorMessage(String message, boolean logError, Exception e, Object errorData)
    {
        JOptionPane.showMessageDialog(null, message);
        
        if(logError)
            logError(message, e, errorData);
    }
    
    /**
     * Displays a CUI ouput with the passed message
     * @param message The error message to display
     * @param logError Log/Write this error to the ErrorDump
     * @param e The Exception that occurred
     * @param errorData Additional data that's relevant to the exception
     */
    public static void CLIErrorMessage(String message, boolean logError, Exception e, Object errorData)
    {
        String prefix   =   "[Error] ";
        
        if(logError)
            logError(message, e, errorData);
    }
    
    /**
     * Creates a new ErrorBean from the exception params given
     * Adds the created ErrorBean to the ErrorDump and saves it
     * @param message An additional message to accompany the exception
     * @param e The exception that occurred
     * @param errorData Additional data that's relevant to the exception
     */
    public static void logError(String message, Exception e, Object errorData)
    {
         ErrorBean bean  =   new ErrorBean(new Date(), e, message, errorData);
         errorDump.addErrorBean(bean);
         errorDump.saveErrorDumpInstance();
    }
}
