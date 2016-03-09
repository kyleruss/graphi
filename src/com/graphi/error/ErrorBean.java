
package com.graphi.error;

import java.io.Serializable;
import java.util.Date;


public class ErrorBean implements Serializable
{
    private Date errorTime;
    private Exception exception;
    private String message;
    
    public ErrorBean()
    {
        this(new Date(), null, "");
    }
    
    public ErrorBean(Date errorTime, Exception exception, String message)
    {
        this.errorTime  =   errorTime;
        this.exception  =   exception;
        this.message    =   message;
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
}
