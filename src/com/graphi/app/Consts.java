//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.app;

import java.awt.Toolkit;


public final class Consts 
{
    private Consts() {}
    
    //-------------------------------------------------------------
    //  APP PATHS
    //-------------------------------------------------------------
    public static final String RES_DIR  =   "resources";
    public static final String IMG_DIR  =   RES_DIR + "/images";
    public static final String DATA_DIR =   "data";
    public static final String TEST_DIR =   DATA_DIR + "/test";
    //-------------------------------------------------------------


    //-------------------------------------------------------------
    //  APP SETTINGS
    //-------------------------------------------------------------
    public static final int DEFAULT_PB_DELAY    =   500;
    public static final int WINDOW_HEIGHT       =   (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.85);
    public static final int WINDOW_WIDTH        =   (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.7);
    public static final String WINDOW_TITLE     =   "Graphi - Kyle Russell 2015";
    //-------------------------------------------------------------
    
    
    //------------------------------------------------------------------------
    //  PANEL CARD NAMES
    //------------------------------------------------------------------------
    //MainPanel/GraphPanel@gpControlsWrapper
    public static final String RECORD_CARD            =   "rec";
    public static final String PLAYBACK_CARD          =   "pb";
    
    //MainPanel/ControlPanel@genPanel
    public static final String BA_PANEL_CARD          =   "ba_panel";
    public static final String KL_PANEL_CARD          =   "kl_panel";
    public static final String RA_PANEL_CARD          =   "ra_panel";
    
    //MainPanel/ControlPanel@computeInnerPanel
    public static final String CLUSTER_PANEL_CARD     =   "cluster_panel";
    public static final String SPATH_PANEL_CARD       =   "spath_panel";
    public static final String CENTRALITY_PANEL_CARD  =   "centrality_panel";
    //------------------------------------------------------------------------
    
    
    
}
