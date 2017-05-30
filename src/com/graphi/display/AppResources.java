//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display;

import com.graphi.display.layout.util.ResourceMap;
import com.graphi.error.ErrorManager;
import java.io.IOException;
import java.util.HashMap;

public class AppResources extends ResourceMap
{
    protected static AppResources instance;
    
    /**
     * Initializes all image resources for the application
     */
    @Override
    protected void initResources()
    {
        try
        {
            resourceMap  =   new HashMap<>();
            resourceMap.put("addIcon", getImage("addSmallIcon.png"));
            resourceMap.put("removeIcon", getImage("removeSmallIcon.png"));
            resourceMap.put("colourIcon", getImage("color_icon.png"));
            resourceMap.put("clipIcon", getImage("clipboard.png"));
            resourceMap.put("saveIcon", getImage("new_file.png"));
            resourceMap.put("openIcon", getImage("open_icon.png"));
            resourceMap.put("graphIcon", getImage("graph.png"));
            resourceMap.put("tableIcon", getImage("table.png"));
            resourceMap.put("executeIcon", getImage("execute.png"));
            resourceMap.put("resetIcon", getImage("reset.png"));
            resourceMap.put("editIcon", getImage("edit.png"));
            resourceMap.put("playIcon", getImage("play.png"));
            resourceMap.put("stopIcon", getImage("stop.png"));
            resourceMap.put("recordIcon", getImage("record.png"));
            resourceMap.put("closeIcon", getImage("close.png"));
            resourceMap.put("graphIconV2", getImage("graph2.png"));
            resourceMap.put("settingsIcon", getImage("settingsIcon.png"));
            resourceMap.put("helpIcon", getImage("help.png"));
            resourceMap.put("logoIcon", getImage("logo.png"));
            resourceMap.put("newProjectBtn", getImage("newProjectBtn.png"));
            resourceMap.put("openProjectBtn", getImage("openProjectBtn.png"));
            resourceMap.put("exitBtn", getImage("exitBtn.png"));
            resourceMap.put("pluginBtn", getImage("pluginBtn.png"));
            resourceMap.put("settingsBtn", getImage("settingsBtn.png"));
            resourceMap.put("settingsTitleIcon", getImage("settingsTitleIcon.png"));
            resourceMap.put("pluginsTitleIcon", getImage("pluginsTitleIcon.png"));
            resourceMap.put("githubIcon", getImage("githubIcon.png"));
            resourceMap.put("menuIcon", getImage("menuIcon.png"));
            resourceMap.put("aboutTitleIcon", getImage("aboutTitleIcon.png"));
            resourceMap.put("aboutUserIcon", getImage("aboutUserIcon.png"));
            resourceMap.put("aboutLicenseIcon", getImage("aboutLicenseIcon.png"));
            resourceMap.put("aboutVersionIcon", getImage("aboutVersionIcon.png"));
            resourceMap.put("aboutGithubIcon", getImage("aboutGithubIcon.png"));
            resourceMap.put("aboutUniversityIcon", getImage("aboutUniversityIcon.png"));
            resourceMap.put("saveLargeBtn", getImage("saveLgBtn.png"));
            resourceMap.put("advancedTabIcon", getImage("advancedTabIcon.png"));
            resourceMap.put("viewTabIcon", getImage("viewTabIcon.png"));
            resourceMap.put("customizationTabIcon", getImage("customizationTabIcon.png"));
            resourceMap.put("displayNavBackground", getImage("displayNavBackground.png"));
            resourceMap.put("displayNavSelect", getImage("displayNavSelect.png"));
            resourceMap.put("displayNavMove", getImage("displayNavMove.png"));
            resourceMap.put("displayNavEdit", getImage("displayNavEdit.png"));
            resourceMap.put("docsIcon", getImage("docsIcon.png"));
            resourceMap.put("tickIcon", getImage("tickIcon.png"));
            resourceMap.put("pluginActivateIcon", getImage("pluginActivateIcon.png"));
            resourceMap.put("pluginOpenIcon", getImage("pluginOpenIcon.png"));
            resourceMap.put("pluginDefaultIcon", getImage("pluginDefaultIcon.png"));
            resourceMap.put("pluginInfoIcon", getImage("pluginInfoIcon.png"));
            resourceMap.put("toolbarSaveIcon", getImage("toolbarSave.png"));
            resourceMap.put("toolbarOpenIcon", getImage("toolbarOpen.png"));
            resourceMap.put("toolbarNewIcon", getImage("toolbarNew.png"));
            resourceMap.put("toolbarClearIcon", getImage("toolbarClear.png"));
            resourceMap.put("toolbarSearchIcon", getImage("toolbarSearch.png"));
            resourceMap.put("toolbarExecuteIcon", getImage("toolbarExecute.png"));
            resourceMap.put("toolbarRecordIcon", getImage("toolbarRecord.png"));
            resourceMap.put("toolbarPlayIcon", getImage("toolbarPlay.png"));
            resourceMap.put("toolbarTasksIcon", getImage("toolbarTasks.png"));
            resourceMap.put("statusTickMedium", getImage("statusTickMedium.png"));
            resourceMap.put("statusXMedium", getImage("statusXMedium.png"));
        }
        
        catch(IOException e)
        {
            ErrorManager.GUIErrorMessage("Failed to load resources", true, e, null);
        }
    }
    
    public static AppResources getInstance()
    {
        if(instance == null) instance   =   new AppResources();
        return instance;
    }
}
