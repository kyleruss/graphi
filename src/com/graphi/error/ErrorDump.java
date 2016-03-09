//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.error;

import com.graphi.app.Consts;
import com.graphi.io.Storage;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ErrorDump implements Serializable, Iterable<ErrorBean>
{
    private static final String NAME_PREFIX    =   "ErrorDump_";
    private List<ErrorBean> errorBeans;
    private String fileName;
    
    public ErrorDump()
    {
        errorBeans  =   new ArrayList<>();
        fileName    =   createFileName();
    }
    
    @Override
    public Iterator<ErrorBean> iterator() 
    {
        return errorBeans.iterator();
    }
    
    public void addErrorBean(ErrorBean errorBean)
    {
        errorBeans.add(errorBean);
    }
    
    public ErrorBean removeErrorBean(int index)
    {
        return errorBeans.remove(index);
    }
    
    public boolean removeErrorBean(ErrorBean bean)
    {
        return errorBeans.remove(bean);
    }
    
    public boolean hasBean(ErrorBean bean)
    {
        return errorBeans.contains(bean);
    }
    
    public List<ErrorBean> getErrorBeans()
    {
        return errorBeans;
    }
    
    public String getFileName()
    {
        return fileName;
    }
    
    private static String createFileName()
    {
        SimpleDateFormat formatter =   new SimpleDateFormat("dd-MM-yy");
        Date date   =   new Date();
        return NAME_PREFIX + formatter.format(date);
    }
    
    @Override
    public String toString()
    {
        String output   =   "";
        
        for(ErrorBean bean : errorBeans)
            output  +=  bean + "\n";
        
        return output;
    }
    
    public static ErrorDump getLatestErrorDump()
    {
        String fileName =   createFileName();
        File file       =   new File(Consts.ERROR_DUMP_DIR + fileName);
        ErrorDump dump  =   openErrorDump(file);
            
        return dump == null? new ErrorDump() : dump;
    }
    
    public static ErrorDump openErrorDump(File file)
    {
        ErrorDump dump  =   (ErrorDump) Storage.openObj(file);
        return dump;
    }
    
    public void saveErrorDumpInstance()
    {
        File file   =   new File(Consts.ERROR_DUMP_DIR + fileName);
        saveErrorDump(file, this);
    }
    
    public static void saveErrorDump(File file, ErrorDump dump)
    {
        Storage.saveObj(dump, file);
    }
}
