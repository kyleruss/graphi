//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display;

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
    protected final JMenu helpMenu;
    protected final JMenu optionsMenu;
    protected final JMenu graphMenu;
    protected final JMenu loggingMenu;
    protected final JMenu vertexMenu, edgeMenu;
    protected final JMenu viewMenu;
    protected final JMenu pluginMenu;
    protected Map<String, JMenuItem> menuItems;
    protected PluginsMenu pluginsListMenu;

    public MainMenu()
    {
        menuItems           =   new HashMap<>();
        fileMenu            =   new JMenu("File");
        optionsMenu         =   new JMenu("Options");
        helpMenu            =   new JMenu("About");
        graphMenu           =   new JMenu("Graph");
        loggingMenu         =   new JMenu("Log");
        vertexMenu          =   new JMenu("Vertex");
        edgeMenu            =   new JMenu("Edge");
        viewMenu            =   new JMenu("View");
        pluginMenu          =   new JMenu("Plugin");
        pluginsListMenu     =   new PluginsMenu();
        
        //----------------------------------------------------------------------
        //  ADD MENU ITEMS
        //----------------------------------------------------------------------;
        //- Menu items are stored in the menuItems map for later indexing
        //- Use getMenuItem for access to an item
        
        addMenuItem("mainMenuItem", new JMenuItem("Menu"), fileMenu);
        addMenuItem("miniItem", new JMenuItem("Minimize"), fileMenu);
        addMenuItem("maxItem", new JMenuItem("Maximize"), fileMenu);
        addMenuItem("exitItem", new JMenuItem("Exit"), fileMenu);
        addMenuItem("aboutItem", new JMenuItem("Author"), helpMenu);
        addMenuItem("importGraphItem", new JMenuItem("Import Graph"), graphMenu);
        addMenuItem("exportGraphItem", new JMenuItem("Export Graph"), graphMenu);
        addMenuItem("importLogItem", new JMenuItem("Import Log"), loggingMenu);
        addMenuItem("exportLogItem", new JMenuItem("Export Log"), loggingMenu);
        addMenuItem("vLabelsItem", new JMenuItem("Vertex labels"), viewMenu);
        addMenuItem("eLabelsItem", new JMenuItem("Eedge labels"), viewMenu);
        addMenuItem("viewerBGItem", new JMenuItem("Change viewer background"), optionsMenu);
        addMenuItem("edgeBGItem", new JMenuItem("Change edge fill"), optionsMenu);
        addMenuItem("vertexBGItem", new JMenuItem("Change Vertex fill"), optionsMenu);
        addMenuItem("clearLogItem", new JMenuItem("Clear"), loggingMenu);
        addMenuItem("resetGraphItem", new JMenuItem("Reset"), graphMenu);
        addMenuItem("addVertexItem", new JMenuItem("Add"), vertexMenu);
        addMenuItem("editVertexItem", new JMenuItem("Edit"), vertexMenu);
        addMenuItem("removeVertexItem", new JMenuItem("Remove"), vertexMenu);
        addMenuItem("addEdgeItem", new JMenuItem("Add"), edgeMenu);
        addMenuItem("editEdgeItem", new JMenuItem("Edit"), edgeMenu);
        addMenuItem("removeEdgeItem", new JMenuItem("Remove"), edgeMenu);
        addMenuItem("searchObjectItem", new JMenuItem("Search", KeyEvent.VK_S), graphMenu);
        //----------------------------------------------------------------------
        
        graphMenu.add(vertexMenu);
        graphMenu.add(edgeMenu);
        pluginMenu.add(pluginsListMenu);
        pluginMenu.addSeparator();
        addMenuItem("loadPluginItem", new JMenuItem("Load"), pluginMenu);
        
        add(fileMenu);
        add(graphMenu);
        add(loggingMenu);
        add(viewMenu);
        add(optionsMenu);
        add(helpMenu);
        add(pluginMenu);
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

    @Override
    public JMenu getHelpMenu() 
    {
        return helpMenu;
    }

    public JMenu getOptionsMenu() 
    {
        return optionsMenu;
    }

    public JMenu getGraphMenu() 
    {
        return graphMenu;
    }

    public JMenu getLoggingMenu() 
    {
        return loggingMenu;
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
}
