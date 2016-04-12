//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.error;

import com.graphi.app.AppManager;
import com.graphi.app.Consts;
import com.graphi.io.Storage;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * An exportable vessel for ErrorBean's 
 * Used to maintain running exceptions in Graphi
 * Many of the Graphi exceptions are recorded on a live ErrorDump instance
 */
public class ErrorDump implements Serializable, Iterable<ErrorBean>
{
    //The file name prefix/pattern of ErrorDump files
    //Dates are appended for each unique file
    private static final String NAME_PREFIX    =   "ErrorDump_";
    
    //The ErrorBeans container
    private List<ErrorBean> errorBeans;
    
    //The unique file name given to the ErrorDump
    private String fileName;
    
    public ErrorDump()
    {
        errorBeans  =   new ArrayList<>();
        fileName    =   createFileName();
    }
    
    /**
     * Returns the ErrorBean's container iterator
     * @return The iterator from the ErrorBean's container in a java.util.List
     */
    @Override
    public Iterator<ErrorBean> iterator() 
    {
        return errorBeans.iterator();
    }
    
    /**
     * Adds an ErrorBean to the container 
     * @param errorBean The ErrorBean to add to the container
     */
    public void addErrorBean(ErrorBean errorBean)
    {
        errorBeans.add(errorBean);
    }
    
    /**
     * Removes an ErrorBean at the index in the container
     * @param index The index of the ErrorBean to remove
     * @return The ErrorBean removed if founds; null otherwise
     */
    public ErrorBean removeErrorBean(int index)
    {
        return errorBeans.remove(index);
    }

    /**
     * Removes the passed ErrorBean in the container
     * @param bean The ErrorBean to remove
     * @return true if the ErrorBean was founds & removed; false otherwise
     */
    public boolean removeErrorBean(ErrorBean bean)
    {
        return errorBeans.remove(bean);
    }
    
    /**
     * Checks the ErrorBean container for the passed bean
     * @param bean The bean to check for
     * @return true if the bean was found in the container; false otherwise
     */
    public boolean hasBean(ErrorBean bean)
    {
        return errorBeans.contains(bean);
    }
    
    /**
     * Returns the ErrorBean container
     * @return A java.util.ArrayList of ErrorBean's
     */
    public List<ErrorBean> getErrorBeans()
    {
        return errorBeans;
    }
    
    /**
     * @return The ErrorDump's String file name
     */
    public String getFileName()
    {
        return fileName;
    }
    
    /**
     * Creates and returns a file name
     * The name includes the ErrorDump.NAME_PREFIX and a formatted current time
     * @return A file name String with the format ErrorDump.NAME_PREFIX + _dd-MM-yy
     */
    private static String createFileName()
    {
        SimpleDateFormat formatter  =   new SimpleDateFormat("dd-MM-yy");
        Date date                   =   new Date();
        return NAME_PREFIX + formatter.format(date);
    }
    
    /**
     * Concatenates all for each ErrorBean in the container, their toString()
     * @return A String summary of all ErrorBean's
     */
    @Override
    public String toString()
    {
        String output   =   "";
        
        for(ErrorBean bean : errorBeans)
            output  +=  bean + "\n";
        
        return output;
    }
    
    /**
     * Loads the most recent ErrorDump
     * Creates a new ErrorDump instance if none are found
     * @return The most recent ErrorDump instance
     */
    public static ErrorDump getLatestErrorDump()
    {
        String fileName =   createFileName();
        File file       =   new File(Consts.ERROR_DUMP_DIR + fileName);
        ErrorDump dump  =   openErrorDump(file);
            
        return dump == null? new ErrorDump() : dump;
    }
    
    /**
     * Imports an ErrorDump found in the passed file
     * @param file The file containing the ErrorDump
     * @return An ErrorDump from the file; Can be null
     */
    public static ErrorDump openErrorDump(File file)
    {
        ErrorDump dump  =   (ErrorDump) Storage.openObj(file, null);
        return dump;
    }
    
    /**
     * Saves this ErrorDump instance
     * ErrorDumps by default are stored in Consts.ERROR_DUMP_DIR
     */
    public void saveErrorDumpInstance()
    {
        File file   =   new File(Consts.ERROR_DUMP_DIR + fileName);
        saveErrorDump(file, this);
    }
    
    /**
     * Saves the ErrorDump to the specified File
     * @param file The file to save the ErrorDump to
     * @param dump The ErrorDump instance to save
     */
    public static void saveErrorDump(File file, ErrorDump dump)
    {
        Storage.saveObj(dump, file);
    }
}
