//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.error;

public class ErrorManager
{
    private static ErrorDump errorDump;
    
    public ErrorManager()
    {
        errorDump   =   ErrorDump.getLatestErrorDump();
    }
}
