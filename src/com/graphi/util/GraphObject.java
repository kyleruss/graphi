//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.util;

import java.awt.Color;

public interface GraphObject<T extends GraphObject>
{
    public int getID();
    
    public void setID(int id);
    
    public Color getFill();
    
    public void setFill(Color fill);
    
    public T copyGraphObject();
}
