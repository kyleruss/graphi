//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.menu;

import com.graphi.display.layout.util.PluginMenuListener;
import com.sun.glass.events.KeyEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The default menu bar attached to Window.frame
 */
public class MainMenu extends JMenuBar
{
    protected final JMenu fileMenu;
    protected final JMenu dataMenu;
    protected final JMenu graphMenu;
    protected final JMenu pluginMenu;
    protected final JMenu viewMenu;
    protected final JMenu aboutMenu;
    
    protected ActionListener pluginMenuListener;
    protected ActionListener defaultMenuListener;
    
    protected final JMenu vertexMenu, edgeMenu;
    protected final JMenu exportTableMenu;
    protected final JMenu customizationMenu;
    protected Map<String, JMenuItem> menuItems;
    protected PluginsMenu pluginsListMenu;
    private static MainMenu instance;

    private MainMenu()
    {
        menuItems           =   new HashMap<>();
        fileMenu            =   new JMenu("File");
        graphMenu           =   new JMenu("Graph");
        viewMenu            =   new JMenu("View");
        pluginMenu          =   new JMenu("Plugin");
        dataMenu            =   new JMenu("Data");
        aboutMenu           =   new JMenu("About");
        exportTableMenu     =   new JMenu("Export table");  
        customizationMenu   =   new JMenu("Customization");
        vertexMenu          =   new JMenu("Vertex");
        edgeMenu            =   new JMenu("Edge");
        pluginsListMenu     =   new PluginsMenu();
        
        //----------------------------------------------------------------------
        //  ADD MENU ITEMS
        //----------------------------------------------------------------------;
        //- Menu items are stored in the menuItems map for later indexing
        //- Use getMenuItem for access to an item
        
        //File menu items
        addMenuItem("mainMenuItem", new JMenuItem("Menu"), fileMenu);
        addMenuItem("settingsItem", new JMenuItem("Settings"), fileMenu);
        addMenuItem("newProjectItem", new JMenuItem("New Project"), fileMenu);
        addMenuItem("openProjectItem", new JMenuItem("Open Project"), fileMenu);
        addMenuItem("saveProjectItem", new JMenuItem("Save Project"), fileMenu);
        addMenuItem("miniItem", new JMenuItem("Minimize"), fileMenu);
        addMenuItem("maxItem", new JMenuItem("Maximize"), fileMenu);
        addMenuItem("exitItem", new JMenuItem("Exit"), fileMenu);
        
        //Graph menu items
        addMenuItem("importGraphItem", new JMenuItem("Import Graph"), graphMenu);
        addMenuItem("exportGraphItem", new JMenuItem("Export Graph"), graphMenu);
        addMenuItem("resetGraphItem", new JMenuItem("Reset"), graphMenu);
        
        //Vertex sub menu items
        addMenuItem("addVertexItem", new JMenuItem("Add"), vertexMenu);
        addMenuItem("editVertexItem", new JMenuItem("Edit"), vertexMenu);
        addMenuItem("removeVertexItem", new JMenuItem("Remove"), vertexMenu);
        
        //Edge  sub menu items
        addMenuItem("addEdgeItem", new JMenuItem("Add"), edgeMenu);
        addMenuItem("editEdgeItem", new JMenuItem("Edit"), edgeMenu);
        addMenuItem("removeEdgeItem", new JMenuItem("Remove"), edgeMenu);
        
        //Customization sub menu items
        addMenuItem("vertexColourItem", new JMenuItem("Vertex colour"), customizationMenu);
        addMenuItem("edgeColourItem", new JMenuItem("Edge colour"), customizationMenu);
        addMenuItem("selectColourItem", new JMenuItem("Selection colour"), customizationMenu);
        
        //Data menu items
        addMenuItem("searchObjectItem", new JMenuItem("Search", KeyEvent.VK_S), dataMenu);
        addMenuItem("exportLogItem", new JMenuItem("Export Log"), dataMenu);
        addMenuItem("clearLogItem", new JMenuItem("Clear"), dataMenu);
        
        //Export table menu items
        addMenuItem("exportVerticesItem", new JMenuItem("Vertices table"), exportTableMenu);
        addMenuItem("exportEdgesItem", new JMenuItem("Edges table"), exportTableMenu);
        addMenuItem("exportComputationsItem", new JMenuItem("Computations table"), exportTableMenu);
        
        //View menu items
        addMenuItem("showToolsItem", new JMenuItem("Tools"), viewMenu);
        addMenuItem("showVisuals", new JMenuItem("Visuals"), viewMenu);
        addMenuItem("vLabelsItem", new JMenuItem("Vertex labels"), viewMenu);
        addMenuItem("eLabelsItem", new JMenuItem("Edge labels"), viewMenu);
        
        
        //About menu items
        addMenuItem("aboutItem", new JMenuItem("Author"), aboutMenu);
        addMenuItem("documentationItem", new JMenuItem("Documentation"), aboutMenu);
        addMenuItem("licenseItem", new JMenuItem("License"), aboutMenu);
        addMenuItem("repositoryItem", new JMenuItem("Repository"), aboutMenu);
        
        //----------------------------------------------------------------------
        
        graphMenu.add(customizationMenu);
        graphMenu.add(vertexMenu);
        graphMenu.add(edgeMenu);
        dataMenu.add(exportTableMenu);
        pluginMenu.add(pluginsListMenu);
        pluginMenu.addSeparator();
        addMenuItem("loadPluginItem", new JMenuItem("Import plugin"), pluginMenu);
        addMenuItem("managePluginsItem", new JMenuItem("Manage plugins"), pluginMenu);
        
        add(fileMenu);
        add(graphMenu);
        add(pluginMenu);
        add(dataMenu);
        add(viewMenu);
        add(aboutMenu);
        
        initDefaultListeners();
    }
    
    public void showAbout()
    {
        JLabel nameLabel    =   new JLabel("Kyle Russell 2015", SwingConstants.CENTER);
        JLabel locLabel     =   new JLabel("AUT University");
        JLabel repoLabel    =   new JLabel("https://github.com/denkers/graphi");

        JPanel aboutPanel   =   new JPanel();
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
        aboutPanel.add(nameLabel);
        aboutPanel.add(locLabel);
        aboutPanel.add(repoLabel);

        JOptionPane.showMessageDialog(null, aboutPanel, "Graphi - Author", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void initDefaultListeners()
    {
        setMenuItemListener(new MenuActionListener());
        setPluginMenuListener(new PluginMenuListener());
    }
    
    public void setPluginMenuListener(ActionListener listener)
    {
        pluginMenuListener  =   listener;
    }
    
    public void addPluginItemMenuListener(JMenuItem pluginMenuItem)
    {
        pluginMenuItem.addActionListener(pluginMenuListener);
    }
    
    /**
     * Adds a menu item to a menu and to the menuItems map 
     * @param name The key name used when added to the menuItems map
     * @param item the item to add
     * @param menu the menu to add the item to
     */
    public void addMenuItem(String name, JMenuItem item, JMenu menu)
    {
        menuItems.put(name, item);
        menu.add(item);
    }
    
    /**
     * @param name The key name in the menu items map that was used to initialy add the item
     * @return The menu item for the given key name
     */
    public JMenuItem getMenuItem(String name)
    {
        return menuItems.get(name);
    }
    
    /**
     * @return The menu items map
     * keys are the name param used when calling addMenuItem
     */
    public Map<String, JMenuItem> getMenuItems()
    {
        return menuItems;
    }
    
    /**
     * Sets the ActionListener for all added menu items
     * @param listener An ActionListener instance 
     */
    public void setMenuItemListener(ActionListener listener)
    {
        defaultMenuListener =   listener;
        
        for(JMenuItem item : menuItems.values())
        {
            for(ActionListener otherListener : item.getActionListeners())
                item.removeActionListener(otherListener);
            
            item.addActionListener(listener);
        }
    }
    
    /**
     * @return the PluginListMenu sub menu 
     */
    public PluginsMenu getPluginListMenu() 
    {
        return pluginsListMenu;
    }
    
    public JMenu getFileMenu() 
    {
        return fileMenu;
    }

    public JMenu getGraphMenu() 
    {
        return graphMenu;
    }

    public JMenu getVertexMenu() 
    {
        return vertexMenu;
    }

    public JMenu getEdgeMenu() 
    {
        return edgeMenu;
    }

    public JMenu getViewMenu() 
    {
        return viewMenu;
    }

    public JMenu getPluginMenu() 
    {
        return pluginMenu;
    }
    
    public static MainMenu getInstance()
    {
        if(instance == null) instance = new MainMenu();
        return instance;
    }
}
