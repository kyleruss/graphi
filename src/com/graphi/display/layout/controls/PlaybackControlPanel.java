//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.display.layout.controls;

import com.graphi.display.AppResources;
import com.graphi.display.layout.DataPanel;
import com.graphi.display.layout.GraphPanel;
import com.graphi.display.menu.MainMenu;
import com.graphi.graph.Edge;
import com.graphi.graph.GraphDataManager;
import com.graphi.graph.GraphUtilities;
import com.graphi.graph.Node;
import com.graphi.graph.TableModelBean;
import com.graphi.sim.GraphPlayback;
import com.graphi.sim.PlaybackEntry;
import de.javasoft.swing.DateComboBox;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public  class PlaybackControlPanel extends JPanel implements ActionListener, ChangeListener
{
    public static final int INITIAL_DELAY           =   500;
    public static final String RECORD_CARD          =   "rec";
    public static final String PLAYBACK_CARD        =   "pb";

    private GraphPlayback gPlayback;
    private Graph<Node, Edge> prevGraph;
    private TableModelBean prevModel;
    private JPanel pbControls;
    private JButton pbToggle;
    private JSlider pbProgress;
    private JSpinner pbProgressSpeed;
    private JLabel pbName, pbDate;
    private boolean pbPlaying;
    private boolean repeatPlayer;
    private JPanel gpControlsWrapper;
    private JButton gpCtrlsClose;
    private JButton pbRepeatBtn;

    private JPanel gpRecControls;
    private JButton gpRecSaveBtn;
    private JButton gpRecRemoveBtn;
    private JTextField gpRecEntryName;
    private DateComboBox gpRecDatePicker;
    private JComboBox gpRecEntries;
    private JCheckBox recordComputeCheck, recordStateCheck;

    public PlaybackControlPanel()
    {
        setLayout(new BorderLayout());
        AppResources resources  =   AppResources.getInstance();
        pbControls              =   new JPanel(new MigLayout("fillx"));
        pbToggle                =   new JButton("Play");
        pbRepeatBtn             =   new JButton(new ImageIcon(resources.getResource("toolbarClearIcon")));
        pbProgress              =   new JSlider();
        pbProgressSpeed         =   new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        pbName                  =   new JLabel("N/A");
        pbDate                  =   new JLabel("N/A");
        gPlayback               =   new GraphPlayback();
        pbPlaying               =   false;
        repeatPlayer            =   false;

        pbToggle.setIcon(new ImageIcon(resources.getResource("playIcon")));
        pbProgress.addChangeListener(this);
        pbProgressSpeed.addChangeListener(this);

        pbProgressSpeed.setValue(INITIAL_DELAY);
        pbProgress.setPaintTicks(true);
        pbProgress.setValue(0);
        pbProgress.setMinimum(0);
        pbProgress.setPaintTrack(true);

        pbToggle.addActionListener(this);

        pbName.setFont(new Font("Arial", Font.BOLD, 12));
        pbDate.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel pbInnerWrapper   =   new JPanel();
        pbInnerWrapper.add(pbRepeatBtn);
        pbInnerWrapper.add(pbToggle);
        pbInnerWrapper.add(new JLabel("Speed"));
        pbInnerWrapper.add(pbProgressSpeed);

        JPanel pbInfoWrapper    =   new JPanel(new MigLayout());
        pbInfoWrapper.add(new JLabel("Name: "));
        pbInfoWrapper.add(pbName, "wrap");
        pbInfoWrapper.add(new JLabel("Timestamp: "));
        pbInfoWrapper.add(pbDate);

        pbControls.add(pbProgress, "al center, wrap");
        pbControls.add(pbInnerWrapper, "al center, wrap");
        pbControls.add(pbInfoWrapper, "al center");

        JPanel pbControlsWrapper    =   new JPanel(new BorderLayout());
        pbControlsWrapper.add(pbControls);


        gpRecControls       =   new JPanel(new MigLayout("fillx"));
        gpRecSaveBtn        =   new JButton("Save entry");
        gpRecRemoveBtn      =   new JButton("Remove entry");
        recordComputeCheck  =   new JCheckBox("Record computation table");
        recordStateCheck    =   new JCheckBox("Record object state");
        gpRecDatePicker     =   new DateComboBox();
        gpRecEntryName      =   new JTextField();
        gpRecEntries        =   new JComboBox();
        gpRecEntries.setPreferredSize(new Dimension(120, 20));
        gpRecEntryName.setPreferredSize(new Dimension(120, 20));
        gpRecSaveBtn.setIcon(new ImageIcon(AppResources.getInstance().getResource("addIcon")));
        gpRecRemoveBtn.setIcon(new ImageIcon(AppResources.getInstance().getResource("removeIcon")));
        gpRecEntries.addItem("-- New entry --");

        JPanel gpRecInnerWrapper    =   new JPanel(new MigLayout());
        gpRecInnerWrapper.add(gpRecSaveBtn);
        gpRecInnerWrapper.add(gpRecRemoveBtn);
        gpRecInnerWrapper.add(new JLabel("Entries"));
        gpRecInnerWrapper.add(gpRecEntries, "wrap");
        gpRecInnerWrapper.add(new JLabel("Entry date"));
        gpRecInnerWrapper.add(gpRecDatePicker, "wrap");
        gpRecInnerWrapper.add(new JLabel("Entry name (optional)"));
        gpRecInnerWrapper.add(gpRecEntryName, "span 2, wrap");
        gpRecInnerWrapper.add(recordComputeCheck, "span 2, wrap");
        gpRecInnerWrapper.add(recordStateCheck, "span 2");
        gpRecControls.add(gpRecInnerWrapper, "al center");

        JPanel gpRecWrapper =   new JPanel(new BorderLayout());
        gpRecWrapper.add(gpRecControls);

        gpControlsWrapper   =   new JPanel(new CardLayout());
        gpControlsWrapper.add(pbControlsWrapper, PLAYBACK_CARD);
        gpControlsWrapper.add(gpRecWrapper, RECORD_CARD);

        gpCtrlsClose                =   new JButton("Close");
        JPanel gpControlsExitPanel  =   new JPanel();
        gpCtrlsClose.setIcon(new ImageIcon(AppResources.getInstance().getResource("closeIcon")));

        gpControlsExitPanel.add(gpCtrlsClose);
        add(gpControlsWrapper, BorderLayout.CENTER);
        add(gpControlsExitPanel, BorderLayout.EAST);

        gpRecSaveBtn.addActionListener(this);
        gpRecRemoveBtn.addActionListener(this);
        gpRecEntries.addActionListener(this);
        gpCtrlsClose.addActionListener(this);
        pbRepeatBtn.addActionListener(this);
        
        recordComputeCheck.setSelected(true);
        recordStateCheck.setSelected(true);
    }

    public void setPreviousState()
    {
        prevGraph   =   GraphDataManager.getGraphDataInstance().getGraph();
        prevModel   =   DataPanel.getInstance().getCompModelBean();
    }

    public void reloadPreviousState()
    {
        if(prevGraph != null && prevModel != null)
        {
            DataPanel.getInstance().loadCompModelBean(prevModel);
            GraphDataManager.getGraphDataInstance().setGraph(prevGraph);
            GraphPanel.getInstance().reloadGraph();
        }
    }

    public void displayRecordedGraph()
    {
       int selectedIndex    =   gpRecEntries.getSelectedIndex();
       if(selectedIndex != 0)
       {
           PlaybackEntry entry  =   (PlaybackEntry) gpRecEntries.getSelectedItem();
           if(entry != null)
           {
                gpRecEntryName.setText(entry.getName());
                gpRecDatePicker.setDate(entry.getDate());
                GraphDataManager.getGraphDataInstance().setGraph(GraphUtilities.copyNewGraph(entry.getGraph(), false));
                GraphPanel.getInstance().reloadGraph();
                showEntryComputationModel(entry);
           }
       }

       else
       {
           gpRecEntryName.setText("");
           gpRecDatePicker.setDate(new Date());
       }
    }

    public void removeRecordedGraph()
    {
        int selectedIndex   =   gpRecEntries.getSelectedIndex();
        removeRecordedGraph(selectedIndex);
    }

    public void removeAllRecordedEntries()
    {
        while(gpRecEntries.getItemCount() > 1)
            removeRecordedGraph(1);
    }

    public void removeRecordedGraph(int index)
    {
        if(index != 0)
        {
            PlaybackEntry entry =   (PlaybackEntry) gpRecEntries.getItemAt(index);
            gPlayback.remove(entry);
            gpRecEntries.removeItemAt(index);
            gpRecEntries.setSelectedIndex(0);
            gpRecEntryName.setText("");
            gpRecDatePicker.setDate(new Date());
        }
    }

    public void closeControls()
    {
        setVisible(false);
        reloadPreviousState();
        MainMenu menu   =   MainMenu.getInstance();
        menu.getMenuItem("showRecordingItem").setIcon(null);
        menu.getMenuItem("showPlaybackItem").setIcon(null);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();

        if(src == gpRecSaveBtn)
            addRecordedGraph();

        else if(src == gpRecRemoveBtn)
            removeRecordedGraph();

        else if(src == gpRecEntries)
            displayRecordedGraph();

        else if(src == pbToggle)
            togglePlayback();

        else if(src == gpCtrlsClose)
            closeControls();
        
        else if(src == pbRepeatBtn)
            repeatPlayback();
    }
    

    public void resetEntries()
    {
        gpRecEntries.removeAllItems();
        GraphDataManager.getGraphDataInstance().setGraph(new SparseMultigraph());
        GraphPanel.getInstance().reloadGraph();
    }

    public void addPlaybackEntries()
    {
        gpRecEntries.removeAllItems();
        gpRecEntries.addItem("-- New entry --");
        List<PlaybackEntry> entries =   gPlayback.getEntries();

        for(PlaybackEntry entry : entries)
            gpRecEntries.addItem(entry);

        gpRecEntries.setSelectedIndex(0);
    }

    public void changePanelCard(String card)
    {
        if(!isVisible())
            setPreviousState();

        CardLayout cLayout  =   (CardLayout) gpControlsWrapper.getLayout();
        cLayout.show(gpControlsWrapper, card);
        AppResources resources  =   AppResources.getInstance();
        JMenuItem playbackItem  =   MainMenu.getInstance().getMenuItem("showPlaybackItem");
        JMenuItem recordItem    =   MainMenu.getInstance().getMenuItem("showRecordingItem");
        
        playbackItem.setIcon(null);
        recordItem.setIcon(null);
        

        if(card.equals(PLAYBACK_CARD))
        {
            pbProgress.setMinimum(0);
            pbProgress.setValue(0);
            pbProgress.setMaximum(gPlayback.getSize() - 1); 
            playbackItem.setIcon(new ImageIcon(resources.getResource("tickIcon")));
        }
        
        else
            recordItem.setIcon(new ImageIcon(resources.getResource("tickIcon")));

        setVisible(true);
    }

    public void showEntryComputationModel(PlaybackEntry entry)
    {
        if(entry != null)
        {
            TableModelBean modelContext   =   entry.getComputationModel();
            showComputationModel(modelContext);
        }
    }

    public void showComputationModel(TableModelBean bean)
    {
        DataPanel dataPanel =   DataPanel.getInstance();
        dataPanel.setComputationModel(bean == null? new DefaultTableModel() : bean.getModel());
        dataPanel.setComputationContext(bean == null? null : bean.getDescription());
    }
    
    public void repeatPlayback()
    {
        repeatPlayer    =   !repeatPlayer;
    }

    protected final Timer PB_TIMER =   new Timer(INITIAL_DELAY, (ActionEvent e) -> 
    {
        if(gPlayback.hasNext())
            pbProgress.setValue(pbProgress.getValue() + 1);
        
        else
        {
            if(repeatPlayer)
                pbProgress.setValue(0);
            else
                togglePlayback();
        }
    });

    public void startPlayback()
    {
        pbProgress.setMinimum(0);
        pbProgress.setMaximum(gPlayback.getSize() - 1);

        if(pbProgress.getValue() == pbProgress.getMaximum())
        {
            gPlayback.setIndex(0);
            pbProgress.setValue(0);
        }

        PB_TIMER.setRepeats(true);
        PB_TIMER.start();
        PB_TIMER.setDelay((int) pbProgressSpeed.getValue());
    }

    public void stopPlayback()
    {
        PB_TIMER.stop();
    }

    public void addRecordedGraph(String entryName, Date date, boolean recordState, boolean recordTable, boolean newEntry)
    {
        PlaybackEntry entry;

        if(newEntry)
        {
            Graph<Node, Edge> graph     =   GraphUtilities.copyNewGraph(GraphDataManager.getGraphDataInstance().getGraph(), recordState);


            if(entryName.equals(""))
                entry   =   new PlaybackEntry(graph, date);
            else
                entry   =   new PlaybackEntry(graph, date, entryName);

            if(recordTable)
            {
                DefaultTableModel tModel    =   DataPanel.getInstance().getComputationModel();
                String context              =   DataPanel.getInstance().getComputationContext();
                entry.setComputationModel(new TableModelBean(tModel, context));
            }

            gPlayback.add(entry);
            gpRecEntries.addItem(entry);
            gpRecEntryName.setText("");
        }

        else
        {
            entry   =   (PlaybackEntry) gpRecEntries.getSelectedItem();
            entry.setName(gpRecEntryName.getText());
            entry.setDate(gpRecDatePicker.getDate());
            entry.setGraph(GraphUtilities.copyNewGraph(GraphDataManager.getGraphDataInstance().getGraph(), recordStateCheck.isSelected()));
        }
    }

    public void addRecordedGraph()
    {
        int selectedIndex   =   gpRecEntries.getSelectedIndex();
        Date date           =   gpRecDatePicker.getDate();
        String name         =   gpRecEntryName.getText();
        boolean recordState =   recordStateCheck.isSelected();
        boolean recordTable =   recordComputeCheck.isSelected();
        boolean newEntry    =   selectedIndex == 0;

        addRecordedGraph(name, date, recordState, recordTable, newEntry);
    }

    public void togglePlayback()
    {
        if(pbPlaying)
        {
            pbPlaying = false;
            pbToggle.setIcon(new ImageIcon(AppResources.getInstance().getResource("playIcon")));
            pbToggle.setText("Play");
            stopPlayback();
        }

        else
        {
            pbPlaying = true;
            pbToggle.setIcon(new ImageIcon(AppResources.getInstance().getResource("stopIcon")));
            pbToggle.setText("Stop");
            startPlayback();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) 
    {
        Object src  =   e.getSource();

        if(src == pbProgressSpeed)
            PB_TIMER.setDelay((int) pbProgressSpeed.getValue());

        else if(src == pbProgress)
        {
            int index   =   pbProgress.getValue();
            gPlayback.setIndex(index);
            PlaybackEntry entry =   gPlayback.current();

            if(entry != null)
            {
                pbName.setText(entry.getName());
                pbDate.setText(entry.getDateFormatted());

                GraphDataManager.getGraphDataInstance()
                .setGraph(GraphUtilities.copyNewGraph(entry.getGraph(), recordStateCheck.isSelected()));

                GraphPanel graphPanel   =   GraphPanel.getInstance();
                graphPanel.reloadGraph();
                showEntryComputationModel(entry);
            }
        }
    }
    
    public GraphPlayback getGraphPlayback()
    {
        return gPlayback;
    }
    
    public void setGraphPlayback(GraphPlayback gPlayback)
    {
        this.gPlayback  =   gPlayback;
    }
}
