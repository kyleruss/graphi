//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.error;

import com.graphi.io.Storage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ErrorDump implements Serializable, Iterable<ErrorBean>
{
    private List<ErrorBean> errorBeans;
    
    public ErrorDump()
    {
        errorBeans  =   new ArrayList<>();
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
    
    @Override
    public String toString()
    {
        String output   =   "";
        
        for(ErrorBean bean : errorBeans)
            output  +=  bean + "\n";
        
        return output;
    }
    
    public static ErrorDump openErrorDump(File file)
    {
        ErrorDump dump  =   (ErrorDump) Storage.openObj(file);
        return dump;
    }
    
    public void saveErrorDumpInstance(File file)
    {
        saveErrorDump(file, this);
    }
    
    public static void saveErrorDump(File file, ErrorDump dump)
    {
        Storage.saveObj(dump, file);
    }
}
