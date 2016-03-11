//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.error;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;

/**
 * A bean to capture useful exception data
 * Is the primary exception structure in Graphi
 * Used primarily in the error module
 */
public class ErrorBean implements Serializable, Comparable<ErrorBean>
{
    //The time at which the exception occured
    private Date errorTime;
    
    //The Exception caused
    private Exception exception;
    
    //Additional message to help explain the exception
    private String message;
    
    //Additional data related to exception - Note: must be serialized
    private Object errorData;
    
    public ErrorBean()
    {
        this(new Date(), null, "", null);
    }
    
    public ErrorBean(Date errorTime, Exception exception, String message, Object errorData)
    {
        this.errorTime  =   errorTime;
        this.exception  =   exception;
        this.message    =   message;
        this.errorData  =   errorData;
    }
    
    public Date getErrorTime() 
    {
        return errorTime;
    }

    public void setErrorTime(Date errorTime) 
    {
        this.errorTime = errorTime;
    }

    public Exception getException() 
    {
        return exception;
    }

    public void setException(Exception exception)
    {
        this.exception = exception;
    }

    public String getMessage() 
    {
        return message;
    }

    public void setMessage(String message) 
    {
        this.message = message;
    }
    
    public Object getErrorData()
    {
        return errorData;
    }
    
    public void setErrorData(Object errorData)
    {
        this.errorData  =   errorData;
    }
    
    
    /**
     * Returns a summary of the exception
     * @return The error time, user and exception messages
     */
    @Override
    public String toString()
    {
        String output    =   MessageFormat.format("Error time: {0}\nError message: {1}\nError: {2}", errorTime, message, exception.getMessage());
        return output;
    }

    /**
     * ErrorBeans are compared by their times
     * Compares two ErrorBeans error times
     * @param other The other ErrorBean to compare to
     * @return java.util.Date result comparison of the ErrorBean exception times
     */
    @Override
    public int compareTo(ErrorBean other) 
    {
        return this.errorTime.compareTo(other.getErrorTime());
    }
}
