//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout;

import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import com.graphi.app.AppManager;
import com.graphi.app.Consts;
import com.graphi.display.MainMenu;
import com.graphi.io.AdjMatrixParser;
import com.graphi.io.GMLParser;
import com.graphi.io.GraphMLParser;
import com.graphi.io.Storage;
import com.graphi.plugins.Plugin;
import com.graphi.plugins.PluginConfig;
import com.graphi.plugins.PluginManager;
import com.graphi.sim.GraphPlayback;
import com.graphi.util.Edge;
import com.graphi.sim.Network;
import com.graphi.sim.PlaybackEntry;
import com.graphi.util.EdgeLabelTransformer;
import com.graphi.util.GraphData;
import com.graphi.util.GraphUtilities;
import com.graphi.util.MatrixTools;
import com.graphi.util.Node;
import com.graphi.util.ObjectFillTransformer;
import com.graphi.util.VertexLabelTransformer;
import com.graphi.util.WeightTransformer;
import de.javasoft.swing.DateComboBox;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.matrix.GraphMatrixOperations;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.io.FilenameUtils;

public class MainPanel extends JPanel
{
    protected final ControlPanel controlPanel;
    protected final ScreenPanel screenPanel;
    protected final JSplitPane splitPane;
    protected final JScrollPane controlScroll;
    
    protected BufferedImage addIcon, removeIcon, colourIcon;
    protected BufferedImage clipIcon, openIcon, saveIcon;
    protected BufferedImage editBlackIcon, pointerIcon, moveIcon;
    protected BufferedImage moveSelectedIcon, editSelectedIcon, pointerSelectedIcon;
    protected BufferedImage graphIcon, tableIcon, resetIcon, executeIcon;
    protected BufferedImage editIcon, playIcon, stopIcon, recordIcon, closeIcon;
    
    protected GraphData data;
    protected MainMenu menu;
    protected JFrame frame; 
    protected AppManager appManager;
    
    public MainPanel(AppManager appManager)
    {
        setPreferredSize(new Dimension(Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT));
        setLayout(new BorderLayout());        
        initResources();
        
        this.appManager     =   appManager;
        menu                =   appManager.getWindow().getMenu();
        frame               =   appManager.getWindow().getFrame();
        data                =   new GraphData();
        controlPanel        =   new ControlPanel(this);
        screenPanel         =   new ScreenPanel(this);
        splitPane           =   new JSplitPane();
        controlScroll       =   new JScrollPane(controlPanel);

        controlScroll.setBorder(null);
        splitPane.setLeftComponent(screenPanel);
        splitPane.setRightComponent(controlScroll); 
        splitPane.setResizeWeight(Consts.MAIN_SPLIT_WG);
        add(splitPane, BorderLayout.CENTER);
    }
    
    public GraphData getGraphData()
    {
        return data;
    }
    
    public void setGraphData(GraphData data)
    {
        this.data   =   data;
    }
    
    protected void sendToOutput(String output)
    {
        SimpleDateFormat sdf    =   new SimpleDateFormat("K:MM a dd.MM.yy");
        String date             =   sdf.format(new Date());
        String prefix           =   "\n[" + date + "] ";
        JTextArea outputArea    =   screenPanel.outputPanel.outputArea;
        
        SwingUtilities.invokeLater(()->
        {
            outputArea.setText(outputArea.getText() + prefix + output);
        });
    }
    
    protected File getFile(boolean open, String desc, String...extensions)
    {
        JFileChooser jfc                =   new JFileChooser();
        FileNameExtensionFilter filter  =   new FileNameExtensionFilter(desc, extensions);
        jfc.setFileFilter(filter);
        
        if(open)
            jfc.showOpenDialog(null);
        else
            jfc.showSaveDialog(null);
        
        return jfc.getSelectedFile();
    }
    
    protected String getFileExtension(File file)
    {
        if(file == null) return "";
        return FilenameUtils.getExtension(file.getPath());
    }

    public static JPanel wrapComponents(Border border, Component... components)
    {
        JPanel panel    =   new JPanel();
        panel.setBorder(border);
        
        for(Component component : components)
            panel.add(component);
        
        return panel;
    }
    
    protected void initResources()
    {
        try
        {
            addIcon             =   ImageIO.read(new File(Consts.IMG_DIR + "addSmallIcon.png"));
            removeIcon          =   ImageIO.read(new File(Consts.IMG_DIR + "removeSmallIcon.png"));   
            colourIcon          =   ImageIO.read(new File(Consts.IMG_DIR + "color_icon.png"));   
            clipIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "clipboard.png"));  
            saveIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "new_file.png"));
            openIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "open_icon.png"));
            editBlackIcon       =   ImageIO.read(new File(Consts.IMG_DIR + "editblack.png"));
            pointerIcon         =   ImageIO.read(new File(Consts.IMG_DIR + "pointer.png"));
            moveIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "move.png"));
            moveSelectedIcon    =   ImageIO.read(new File(Consts.IMG_DIR + "move_selected.png"));
            editSelectedIcon    =   ImageIO.read(new File(Consts.IMG_DIR + "editblack_selected.png"));
            pointerSelectedIcon =   ImageIO.read(new File(Consts.IMG_DIR + "pointer_selected.png"));
            graphIcon           =   ImageIO.read(new File(Consts.IMG_DIR + "graph.png"));
            tableIcon           =   ImageIO.read(new File(Consts.IMG_DIR + "table.png"));
            executeIcon         =   ImageIO.read(new File(Consts.IMG_DIR + "execute.png"));
            resetIcon           =   ImageIO.read(new File(Consts.IMG_DIR + "reset.png"));
            editIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "edit.png"));
            playIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "play.png"));
            stopIcon            =   ImageIO.read(new File(Consts.IMG_DIR + "stop.png"));
            recordIcon          =   ImageIO.read(new File(Consts.IMG_DIR + "record.png"));
            closeIcon           =   ImageIO.read(new File(Consts.IMG_DIR + "close.png"));
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Failed to load resources: " + e.getMessage());
        }
    }
    
    public void initConfigPlugins(PluginManager pm)
    {
        controlPanel.loadConfigPlugins(pm);
    }
}
