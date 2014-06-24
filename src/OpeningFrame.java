
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author bala
 */
public final class OpeningFrame extends JFrame implements DocumentListener, WindowListener {

    private ReadingEntryPanel entryPanel;
    private GraphPanel graphPanel;
    private final int w, h;
    private File openFile;
    private String baseTitle = " Annai : Graph Software - Openwell Submersible Pumpsets";
    private JToolBar toolBar;
    final Action newAction = new NewAction("New Window", new ImageIcon("resources\\Add24.gif"), "New pump test file in new window");
    final Action newFileAction = new NewFileAction("New File", new ImageIcon("resources\\New24.gif"), "New pump test file ");
    final Action openAction = new OpenAction("Open file", new ImageIcon("resources\\Open24.gif"), "Open file from disk");
    final Action saveAction = new SaveAction("Save file", new ImageIcon("resources\\Save24.gif"), "Save file to disk");
    final Action saveAsAction = new SaveAsAction("Save As", new ImageIcon("resources\\SaveAs24.gif"), "Save in new name to disk");
    final Action viewGraphAction = new ViewGraphAction("View the graph", new ImageIcon("resources\\Graph24.png"), "View the graph");
    final Action viewReportAction = new ViewReportAction("View the report", new ImageIcon("resources\\Report24.gif"), "View the report");
    final Action printReportAction = new PrintReportAction("Print the report", new ImageIcon("resources\\Print24.gif"), "Preview and print the report");
    final Action printGraphAction = new PrintGraphAction("Print the graph", new ImageIcon("resources\\Print24.gif"), "Preview and print the graph");
    final Action editCurveAction = new EditCurveAction("Edit Curve", new ImageIcon("resources\\Edit24.gif"), "Edit curve by changing smoothness of fit");
    final Action entryScreenAction = new EntryScreenAction("Back to Reading Entry Screen", new ImageIcon("resources\\Back24.png"), "Back To Reading Entry Screen");
    final Action saveTypeAction = new SaveTypeAction("Save declared values", new ImageIcon("resources\\Save24.gif"), "Save pump declared values only in a file");
    final Action openTypeAction = new OpenTypeAction("Use saved declared values", new ImageIcon("resources\\Open24.gif"), "Load pump declared values from a file");
    final JMenuItem changeGridMenu = new JMenu("Change GridSpacing");
    final Action noGridAction = new NoGridAction("No Gride Needed", new ImageIcon("resources\\NoGrid24.gif"), "Remove the  Grid ");
    final Action wideGridAction = new WideGridAction("Wide GridSpacing", new ImageIcon("resources\\WideGrid24.gif"), "Change Grid Spacing to Wide");
    final Action medGridAction = new MedGridAction("Medium GridSpacing", new ImageIcon("resources\\MedGrid24.gif"), "Change Grid Spacing to Medium");
    final Action narrowGridAction = new NarrowGridAction("Narrow GridSpacing", new ImageIcon("resources\\NarrowGrid24.gif"), "Change Grid Spacing to Narrow");
    final Action excelReportAction = new ExcelReportAction("View the excel report", new ImageIcon("resources\\Report24.gif"), "View the excel report");//;"Narrow GridSpacing", new ImageIcon("resources\\NarrowGrid24.gif"), "Change Grid Spacing to Narrow");
    final Action exitAction = new ExitAction("Exit", null, "");
    final Action enterTypeDBAction = new EnterTypeDBAction("Enter a Pump Type", new ImageIcon("resources\\Graph24.png"), "Enter a Pump Type");
    //Action for viewing ,eding and saving edited type values to DB
    Action viewTypeDBAction = new ViewTypeDBAction("View Pump Types", new ImageIcon("resources\\Graph24.png"), "View  Pump Types");
    Action saveObsValuesAction = new SaveObsValuesAction("Save The Observed Values For Variation Report", new ImageIcon("resources\\Save24.gif"), "Save The Observed Values ");
    Action viewVarReportAction = new ViewVarReportAction("View Variation Report or edit observed values DB", new ImageIcon("resources\\Report24.gif"), "View Variation Report or edit observed values DB");
    Action saveReadingsToDBAction = new SaveReadingsToDBAction("Save readings to  Database", new ImageIcon("resources\\Save24.gif"), "Save readings to Database");
    Action loadReadingsFromDBAction = new LoadReadingsFromDBAction("Load  readings from  Database", new ImageIcon("resources\\Open24.gif"), "Load readings from Database");
    private boolean savePending = false;
    private String saveMarker = "";
    final JSlider smooth = new JSlider(0, 100, 0);
    final JSlider rough = new JSlider(0, 50, 0);

    public OpeningFrame(int w, int h) {
        this.w = w;
        this.h = h;
      //  setSize(w, h);
        // setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(baseTitle);
        this.entryPanel = new ReadingEntryPanel();
        //setVisible(true);
        setBackground(new Color(236, 233, 216));
        TableCreator.createTablesIfNotExists();
    }

    public void go() {
        //add(new JScrollPane(entryPanel));
        add(new JScrollPane(entryPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS));
        setTheMenuBar();
        setVisible(true);
        validate();
        repaint();
        entryPanel.setFocusable(true);
        listenForEntries();
        this.addWindowListener(this);
        pack();
        smooth.setToolTipText("Set the factor of smoothing for curve(0 to 100");
        rough.setToolTipText("Set the factor of linearity for the curve(0 to 50");

    }

    /**
     * Sets the MenuBar and MenuItems.
     */
    public final void setTheMenuBar() {
        printReportAction.setEnabled(false);
        printGraphAction.setEnabled(false);
        editCurveAction.setEnabled(false);
        entryScreenAction.setEnabled(false);
        noGridAction.setEnabled(false);
        wideGridAction.setEnabled(false);
        medGridAction.setEnabled(false);
        narrowGridAction.setEnabled(false);
        excelReportAction.setEnabled(false);




        setJMenuBar(null);
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(236, 233, 216));
        final JMenu fileMenu = new JMenu("File");
        final JMenu printMenu = new JMenu("Print");

        JMenu viewMenu = new JMenu("View");

        JMenuItem newMenuItem = new JMenuItem(newAction);
        JButton newButton = new JButton(newAction);
        newButton.setText("");


        JMenuItem newFileMenuItem = new JMenuItem(newFileAction);
        JButton newFileButton = new JButton(newFileAction);
        newFileButton.setText("");



        JMenuItem openMenuItem = new JMenuItem(openAction);//"Open");
        JButton openButton = new JButton(openAction);
        openButton.setText(null);
        JMenuItem saveMenuItem = new JMenuItem(saveAction);
        JButton saveButton = new JButton(saveAction);
        saveButton.setText(null);
        JMenuItem saveAsMenuItem = new JMenuItem(saveAsAction);
        JButton saveAsButton = new JButton(saveAsAction);
        saveAsButton.setText(null);
        JButton viewReportButton = new JButton(viewReportAction);
        viewReportButton.setText("");

        JButton printReportButton = new JButton(printReportAction);
        printReportButton.setText("");
        JButton printGraphButton = new JButton(printGraphAction);
        printGraphButton.setText("");
        JButton viewGraphButton = new JButton(viewGraphAction);
        viewGraphButton.setText("");
        JButton editCurveButton = new JButton(editCurveAction);
        editCurveButton.setText("");
        JButton entryScreenButton = new JButton(entryScreenAction);
        entryScreenButton.setText("");
        JButton noGridButton = new JButton(noGridAction);
        noGridButton.setText("");
        JButton wideGridButton = new JButton(wideGridAction);
        wideGridButton.setText("");
        JButton medGridButton = new JButton(medGridAction);
        medGridButton.setText("");
        JButton narrowGridButton = new JButton(narrowGridAction);
        narrowGridButton.setText("");
        JMenuItem saveTypeMenuItem = new JMenuItem(saveTypeAction);//"Open");
        JButton saveTypeButton = new JButton(saveTypeAction);
        saveTypeButton.setText("");
        JButton viewTypeDBButton = new JButton(viewTypeDBAction);
        viewTypeDBButton.setText("");
        JButton enterTypeDBButton = new JButton(enterTypeDBAction);
        enterTypeDBButton.setText("");

        JMenuItem openTypeMenuItem = new JMenuItem(openTypeAction);//"Open");
        JButton openTypeButton = new JButton(openTypeAction);
        openTypeButton.setText("");

        // JMenuItem openTypeMenuItem = new JMenuItem(openTypeAction);//"Open");
        JButton excelReportButton = new JButton(excelReportAction);
        excelReportButton.setText("");

        JButton saveObsValuesButton = new JButton(saveObsValuesAction);
        saveObsValuesButton.setText("");
        enterTypeDBButton.setText("");

        JButton viewVarReportButton = new JButton(viewVarReportAction);
        viewVarReportButton.setText("");
        JButton saveReadingsToDBButton = new JButton(saveReadingsToDBAction);
        saveReadingsToDBButton.setText("");
        JButton loadReadingsFromDBButton = new JButton(loadReadingsFromDBAction);
        loadReadingsFromDBButton.setText("");

        JMenuItem exitMenuItem = new JMenuItem(exitAction);
        exitMenuItem.setMnemonic('x');

        fileMenu.add(newFileMenuItem);

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(openTypeMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(saveTypeMenuItem);

        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        JMenuItem viewReportMenuItem = new JMenuItem(viewReportAction);
        // changeGridMenu = new JMenu("Change GridSpacing");
        changeGridMenu.setEnabled(false);
        JMenuItem viewGraphMenuItem = new JMenuItem(viewGraphAction);
        JMenuItem mediumGridMenuItem = new JMenuItem(medGridAction);
        changeGridMenu.add(mediumGridMenuItem);

        JMenuItem wideGridMenuItem = new JMenuItem(wideGridAction);
        changeGridMenu.add(wideGridMenuItem);

        JMenuItem narrowGridMenuItem = new JMenuItem(narrowGridAction);
        changeGridMenu.add(narrowGridMenuItem);
        JMenuItem noGridMenuItem = new JMenuItem(noGridAction);
        changeGridMenu.add(noGridMenuItem);

        JMenuItem editCurveMenuItem = new JMenuItem(editCurveAction);
        viewMenu.add(editCurveMenuItem);

        JMenuItem entryMenuItem = new JMenuItem(entryScreenAction);
        //begin

        //end
        viewMenu.add(viewReportMenuItem);
        viewMenu.add(viewGraphMenuItem);
        viewMenu.add(entryMenuItem);
        viewMenu.add(changeGridMenu);
        menuBar.add(viewMenu);
        JMenuItem printReportMenuItem = new JMenuItem(printReportAction);
        printMenu.add(printReportMenuItem);
        JMenuItem printGraphMenuItem = new JMenuItem(printGraphAction);
        printMenu.add(printGraphMenuItem);
        printReportAction.setEnabled(false);
        printGraphAction.setEnabled(false);
        menuBar.add(printMenu);
        final JMenu otherMenu = new JMenu("Other");
        JMenuItem calcMenuItem = new JMenuItem("Calculator");
        otherMenu.add(calcMenuItem);
        calcMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    Runtime.getRuntime().exec("calc");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        menuBar.add(otherMenu);
        setJMenuBar(menuBar);
//if (1==1)return;
toolBar = new JToolBar();
        //toolBar.setBackground(Color.CYAN);
        toolBar.add(newFileButton);
        toolBar.add(newButton);
        toolBar.add(openButton);
        toolBar.add(openTypeButton);
        toolBar.add(saveButton);
        toolBar.add(saveAsButton);
        toolBar.add(saveTypeButton);
        toolBar.add(viewReportButton);
        toolBar.add(printReportButton);
        toolBar.add(viewGraphButton);
        toolBar.add(printGraphButton);
        toolBar.add(editCurveButton);
        toolBar.add(entryScreenButton);
        toolBar.add(openTypeButton);
        toolBar.add(saveTypeButton);
        toolBar.add(noGridButton);
        toolBar.add(wideGridButton);
        toolBar.add(medGridButton);
        toolBar.add(narrowGridButton);
        toolBar.add(excelReportButton);
        toolBar.add(enterTypeDBButton);
        toolBar.add(viewTypeDBButton);
        toolBar.add(saveObsValuesButton);
        toolBar.add(viewVarReportButton);
        toolBar.add(saveReadingsToDBButton);
        toolBar.add(loadReadingsFromDBButton);
        toolBar.add(smooth);
        toolBar.add(rough);
        add(toolBar, BorderLayout.PAGE_START);
        //setSize(1024, 768);
        setVisible(true);
        validate();
        repaint();
    }

    public static void main(String[] args) {

        final OpeningFrame opFrame = new OpeningFrame(1024, 720);

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                opFrame.go();
            }
        });
    }

    public void entryPerformed() {
        savePending = true;
        saveMarker = "*";
        if (openFile == null) {
            OpeningFrame.this.setTitle(baseTitle + saveMarker);
        } else {
            OpeningFrame.this.setTitle(baseTitle + " : " + saveMarker + openFile.getName());
        }

    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {


        {
            if (!savePending) {
                OpeningFrame.this.dispose();
                return;
            }
            int choice = JOptionPane.showConfirmDialog(OpeningFrame.this, "Save the current  screen before exit?");
            if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                return;
            }
            if (choice == JOptionPane.NO_OPTION) {
                OpeningFrame.this.dispose();
            }
            if (choice == JOptionPane.YES_OPTION) {

                try {
                    if (openFile == null) {
                        File directory = getFileDirectory();
                        JFileChooser chooser = new JFileChooser(directory);

                        chooser.showSaveDialog(OpeningFrame.this);

                        openFile = chooser.getSelectedFile();
                        if (chooser.getCurrentDirectory() != null) {
                            OpeningFrame.this.saveFileDirectory(chooser.getCurrentDirectory());
                        }
                    }
                    if (openFile == null) {
                        return;
                    }

                    ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(openFile));
                    os.writeObject(new EntryValues(entryPanel));
                    OpeningFrame.this.savePending = false;
                    OpeningFrame.this.saveMarker = "";
                    OpeningFrame.this.setTitle(baseTitle + "  :  " + saveMarker + openFile.getName());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(OpeningFrame.this, "Cannot save . Please check filename");
                }

            }
            OpeningFrame.this.dispose();
        }
    }

    public void windowClosed(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowIconified(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowDeiconified(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowActivated(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowDeactivated(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    class ExitAction extends AbstractAction {

        public ExitAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent e) {
            if (!savePending) {
                OpeningFrame.this.dispose();
                return;
            }
            int choice = JOptionPane.showConfirmDialog(OpeningFrame.this, "Save the current  screen before exit?");
            if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                return;
            }
            if (choice == JOptionPane.NO_OPTION) {
                OpeningFrame.this.dispose();
            }
            if (choice == JOptionPane.YES_OPTION) {

                try {
                    if (openFile == null) {
                        File directory = getFileDirectory();
                        JFileChooser chooser = new JFileChooser(directory);

                        chooser.showSaveDialog(OpeningFrame.this);

                        openFile = chooser.getSelectedFile();
                        if (chooser.getCurrentDirectory() != null) {
                            OpeningFrame.this.saveFileDirectory(chooser.getCurrentDirectory());
                        }
                    }
                    if (openFile == null) {
                        return;
                    }
                    // OpeningFrame.this.saveFileDirectory(chooser.getCurrentDirectory());
                    ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(openFile));
                    os.writeObject(new EntryValues(entryPanel));
                    OpeningFrame.this.savePending = false;
                    OpeningFrame.this.saveMarker = "";
                    OpeningFrame.this.setTitle(baseTitle + "  :  " + saveMarker + openFile.getName());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(OpeningFrame.this, "Cannot save . Please check filename");
                }

            }
            OpeningFrame.this.dispose();
        }
    }

    class SaveAction extends AbstractAction {

        public SaveAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {
            //JFileChooser chooser=null;
            try {
                if (openFile == null) {
                    File directory = getFileDirectory();
                    JFileChooser chooser = new JFileChooser(directory);

                    chooser.showSaveDialog(OpeningFrame.this);

                    openFile = chooser.getSelectedFile();
                    if (chooser.getCurrentDirectory() != null) {
                        OpeningFrame.this.saveFileDirectory(chooser.getCurrentDirectory());
                    }
                }
                if (openFile == null) {
                    return;
                }
                // OpeningFrame.this.saveFileDirectory(chooser.getCurrentDirectory());
                ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(openFile));
                os.writeObject(new EntryValues(entryPanel));
                OpeningFrame.this.savePending = false;
                OpeningFrame.this.saveMarker = "";
                OpeningFrame.this.setTitle(baseTitle + "  :  " + saveMarker + openFile.getName());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(OpeningFrame.this, "Cannot save . Please check filename");
            }

        }
    }

    class SaveAsAction extends AbstractAction {

        public SaveAsAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {
            try {
                JFileChooser chooser = new JFileChooser(OpeningFrame.this.getFileDirectory());
                //  FileNameExtensionFilter filter = new FileNameExtensionFilter("Pump Test Files","ptf");
                //chooser.setFileFilter(filter);
                int day = OpeningFrame.this.entryPanel.dateChooser.getDate().getDate();
                String dayString;
                if (day < 10) {
                    dayString = "" + "0" + day;
                } else {
                    dayString = "" + day;
                }
                int month = OpeningFrame.this.entryPanel.dateChooser.getDate().getMonth();
                String monthString;
                if (month + 1 < 10) {
                    monthString = "" + "0" + (month + 1);
                } else {
                    monthString = "" + (month + 1);
                }


                chooser.setSelectedFile(new File(OpeningFrame.this.entryPanel.pumpTypeField.getText() + "_"
                        + OpeningFrame.this.entryPanel.slNoField.getText() + "_"
                        + dayString + "_"
                        + monthString + "_"
                        + (OpeningFrame.this.entryPanel.dateChooser.getDate().getYear() + 1900) + "_"
                        + ".ptf"));
                chooser.showSaveDialog(OpeningFrame.this);
                if (chooser.getSelectedFile() == null) {
                    return;
                }
                ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(chooser.getSelectedFile()));
                os.writeObject(new EntryValues(entryPanel));
                openFile = chooser.getSelectedFile();
                OpeningFrame.this.savePending = false;
                OpeningFrame.this.saveMarker = "";
                OpeningFrame.this.setTitle(baseTitle + "  :  " + saveMarker + openFile.getName());

                // OpeningFrame.this.setTitle(baseTitle + "  :  " + openFile.getName());
                OpeningFrame.this.saveFileDirectory(chooser.getCurrentDirectory());
            } catch (Exception ex) {

                JOptionPane.showMessageDialog(OpeningFrame.this, "Cannot save . Please check filename");
            }
        }
    }

    class NewAction extends AbstractAction {

        public NewAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent e) {


            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    OpeningFrame opFrame = new OpeningFrame(1024, 720);
                    Rectangle2D bounds = OpeningFrame.this.getBounds();
                    opFrame.setBounds((int) bounds.getMinX() + 50, (int) bounds.getMinY() + 50, (int) bounds.getWidth(), (int) bounds.getHeight());
                    opFrame.go();
                }
            });
        }
    }

    class NewFileAction extends AbstractAction {

        public NewFileAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                if (OpeningFrame.this.savePending) {
                    int choice = JOptionPane.showConfirmDialog(OpeningFrame.this, "Save the current  screen before loading new page?");
                    if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                        return;
                    }
                    //if (choice == JOptionPane.NO_OPTION) {
                    //  OpeningFrame.this.dispose();
                    //}
                    if (choice == JOptionPane.YES_OPTION) {
                        OpeningFrame.this.saveAction.actionPerformed(e);
                    }
                }
                OpeningFrame.this.entryPanel = new ReadingEntryPanel();
                OpeningFrame.this.listenForEntries();
                getContentPane().removeAll();
                getContentPane().invalidate();
                //invalidate();
                //add(entryPanel);
                add(new JScrollPane(entryPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS));

                add(toolBar, BorderLayout.PAGE_START);
                openFile = null;
                OpeningFrame.this.setTitle(baseTitle);
                validate();
                repaint();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    class OpenAction extends AbstractAction {

        public OpenAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {


            try {
                //for prompting the user for saving changes
                if (OpeningFrame.this.savePending) {
                    int choice = JOptionPane.showConfirmDialog(OpeningFrame.this, "Save the current  screen  before loading new page?");
                    if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                        return;
                    }

                    if (choice == JOptionPane.YES_OPTION) {
                        OpeningFrame.this.saveAction.actionPerformed(ae);
                    }
                }
                JFileChooser chooser = new JFileChooser(OpeningFrame.this.getFileDirectory());
                chooser.showOpenDialog(OpeningFrame.this);
                if (chooser.getSelectedFile() == null) {
                    return;
                }
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()));
                entryPanel.useSavedValues((EntryValues) is.readObject());
                //because the values hve been enterd by usesavedValues() method, savePending has been changed
                //we want it set only for user changes(typing0.Reset it .
                OpeningFrame.this.savePending = false;
                OpeningFrame.this.saveMarker = "";
                OpeningFrame.this.setTitle(baseTitle + "  :  " + saveMarker + chooser.getSelectedFile().getName());
                openFile = chooser.getSelectedFile();
                OpeningFrame.this.saveFileDirectory(chooser.getCurrentDirectory());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    class ViewReportAction extends AbstractAction {

        public ViewReportAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {
            try {
                ReportPanel rp = new ReportPanel(entryPanel, w, h);
                getContentPane().removeAll();
                getContentPane().invalidate();
                add(toolBar, BorderLayout.PAGE_START);
                //add(rp);
                add(new JScrollPane(rp, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS));

                changeGridMenu.setEnabled(false);
                newFileAction.setEnabled(false);
                newAction.setEnabled(false);
                openAction.setEnabled(false);
                openTypeAction.setEnabled(false);
                saveAction.setEnabled(false);
                saveAsAction.setEnabled(false);
                saveTypeAction.setEnabled(false);
                printReportAction.setEnabled(true);
                printGraphAction.setEnabled(false);
                editCurveAction.setEnabled(false);
                entryScreenAction.setEnabled(true);
                noGridAction.setEnabled(false);
                wideGridAction.setEnabled(false);
                medGridAction.setEnabled(false);
                narrowGridAction.setEnabled(false);
                excelReportAction.setEnabled(true);
                validate();
                repaint();
            } catch (Exception ex) {
                OpeningFrame.this.showErrorMessage();
            }
        }
    }

    class ExcelReportAction extends AbstractAction {

        public ExcelReportAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {


            ReportPanel rp = new ReportPanel(entryPanel, w, h);
            rp.generateExcelReport(graphPanel);




        }
    }

    class ViewGraphAction extends AbstractAction {

        public ViewGraphAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {
            try {
                graphPanel = new GraphPanel(new ReportPanel(entryPanel, w, h), w, h - 25);
                getContentPane().removeAll();
                getContentPane().invalidate();
                //add(graphPanel);
                add(new JScrollPane(graphPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS));

                add(toolBar, BorderLayout.PAGE_START);
                validate();
                changeGridMenu.setEnabled(true);
                newFileAction.setEnabled(false);
                newAction.setEnabled(false);
                openAction.setEnabled(false);
                openTypeAction.setEnabled(false);
                saveAction.setEnabled(false);
                saveAsAction.setEnabled(false);
                saveTypeAction.setEnabled(false);
                printReportAction.setEnabled(false);
                printGraphAction.setEnabled(true);
                editCurveAction.setEnabled(true);
                noGridAction.setEnabled(true);
                wideGridAction.setEnabled(true);
                medGridAction.setEnabled(true);
                narrowGridAction.setEnabled(true);
                excelReportAction.setEnabled(true);
                entryScreenAction.setEnabled(true);
            } catch (Exception ex) {
                //OpeningFrame.this.showErrorMessage();
                ex.printStackTrace();

            }

            repaint();
        }
    }

    class EditCurveAction extends AbstractAction {

        public EditCurveAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {

            smooth.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent ce) {
                    try {
                        if (graphPanel != null) {
                            for (Renderer renderer : graphPanel.getGraph().getPlot().getRendererList()) {
                                ((LoessSmoothRenderer) renderer).getLoessFunction().setBandwidth(smooth.getValue());
                            }
                            //f.dispose();
                            setVisible(true);
                            validate();
                            repaint();


                        }
                    } catch (Exception e) {
                    }
                }
            });
            rough.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent ce) {
                    try {
                        if (graphPanel != null) {
                            for (Renderer renderer : graphPanel.getGraph().getPlot().getRendererList()) {
                                ((LoessSmoothRenderer) renderer).getLoessFunction().setDesiredLinearityFactor(rough.getValue());


                            }
                            //f.dispose();
                            setVisible(true);
                            validate();
                            repaint();


                        }
                    } catch (Exception e) {
                    }
                }
            });




        }
    }

    class PrintGraphAction extends AbstractAction {

        public PrintGraphAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);

        }

        public void actionPerformed(ActionEvent ae) {


            try {

                if (graphPanel != null) {
                    new ModifiedPrintUtility(graphPanel, ComponentToBePrintedType.GRAPH).print();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    class PrintReportAction extends AbstractAction {

        public PrintReportAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);

        }

        public void actionPerformed(ActionEvent ae) {
            try {

                new ModifiedPrintUtility(entryPanel, ComponentToBePrintedType.REPORT).print();
            } catch (Exception ex) {
            }
        }
    }

    class EntryScreenAction extends AbstractAction {

        public EntryScreenAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {
            getContentPane().removeAll();
            getContentPane().invalidate();
            //add(entryPanel);
            add(new JScrollPane(entryPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS));

            openAction.setEnabled(true);
            newFileAction.setEnabled(true);
            newAction.setEnabled(true);
            openTypeAction.setEnabled(true);
            saveAction.setEnabled(true);
            saveAsAction.setEnabled(true);
            saveTypeAction.setEnabled(true);
            printReportAction.setEnabled(false);
            printGraphAction.setEnabled(false);
            editCurveAction.setEnabled(false);
            entryScreenAction.setEnabled(false);
            changeGridMenu.setEnabled(false);
            noGridAction.setEnabled(false);
            wideGridAction.setEnabled(false);
            medGridAction.setEnabled(false);
            narrowGridAction.setEnabled(false);
            excelReportAction.setEnabled(false);
            add(toolBar, BorderLayout.PAGE_START);
            setVisible(true);
            validate();
            repaint();
        }
    }

    class SaveTypeAction extends AbstractAction {

        public SaveTypeAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {
            try {
                JFileChooser chooser = new JFileChooser(OpeningFrame.this.getTypeFileDirectory());
                chooser.showSaveDialog(OpeningFrame.this);
                if (chooser.getSelectedFile() == null) {
                    return;
                }

                ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(chooser.getSelectedFile()));
                os.writeObject(new TypeValues(entryPanel));
                OpeningFrame.this.saveTypeDirectory(chooser.getCurrentDirectory());
                //   openFile = chooser.getSelectedFile();
                // OpeningFrame.this.setTitle(baseTitle + "  :  " + openFile.getName());
            } catch (Exception ex) {
                ex.printStackTrace();

                //}
                JOptionPane.showMessageDialog(OpeningFrame.this, "Cannot save . Please check filename");
            }
        }
    }

    class OpenTypeAction extends AbstractAction {

        public OpenTypeAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {
            try {
                JFileChooser chooser = new JFileChooser(OpeningFrame.this.getTypeFileDirectory());
                chooser.showOpenDialog(OpeningFrame.this);
                if (chooser.getSelectedFile() == null) {
                    return;
                }
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()));
                entryPanel.useSavedTypeValues((TypeValues) is.readObject());
                OpeningFrame.this.saveTypeDirectory(chooser.getCurrentDirectory());
                // OpeningFrame.this.setTitle(baseTitle + "  :  " + chooser.getSelectedFile().getName());
                // openFile = chooser.getSelectedFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    class EnterTypeDBAction extends AbstractAction {

        public EnterTypeDBAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {
            try {
                TypeEntryFrame tf = new TypeEntryFrame();
                tf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                tf.setVisible(true);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    class ViewTypeDBAction extends AbstractAction {

        public ViewTypeDBAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {
            try {
                TypeDisplayFrame f = new TypeDisplayFrame();
                f.setVisible(true);
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                //Entry panel is passed to the frame f.;Values if "Load" is selected are loaded to the entry panel;
                f.setEntryPanel(entryPanel);
                f.getTypeValuesAndDisplay();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    class NoGridAction extends AbstractAction {

        public NoGridAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {
            if (graphPanel != null) {
                graphPanel.getGraph().getPlot().setGridLinesType(GridLinesType.NO_GRID_NEEDED);
            }
            setVisible(true);
            validate();
            repaint();
        }
    }

    class WideGridAction extends AbstractAction {

        public WideGridAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {

            if (graphPanel != null) {
                graphPanel.getGraph().getPlot().setGridLinesType(GridLinesType.WIDE_GRID_SPACING);
            }

            setVisible(true);
            validate();
            repaint();
        }
    }

    class MedGridAction extends AbstractAction {

        public MedGridAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {

            if (graphPanel != null) {
                graphPanel.getGraph().getPlot().setGridLinesType(GridLinesType.MEDIUM_GRID_SPACING);
            }
            setVisible(true);
            validate();
            repaint();
        }
    }

    class NarrowGridAction extends AbstractAction {

        public NarrowGridAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {
            if (graphPanel != null) {
                graphPanel.getGraph().getPlot().setGridLinesType(GridLinesType.NARROW_GRID_SPACING);
            }
            setVisible(true);
            validate();
            repaint();
        }
    }

    class SaveObsValuesAction extends AbstractAction {

        public SaveObsValuesAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
        }

        public float round(float d, int decimalPlace) {
            BigDecimal bd = new BigDecimal(Float.toString(d));
            bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
            return bd.floatValue();
        }

        public void actionPerformed(ActionEvent ae) {
            if (graphPanel == null) {
                return;
            }
            try {
                PumpValues values = graphPanel.getGraph().getPlot().getObsValues();
                int sno = Integer.parseInt(values.getsNo());
                java.util.Date uDate = values.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String sqlDateString = sdf.format(uDate);
                java.sql.Date sqlDate = java.sql.Date.valueOf(sqlDateString);
                String type = values.getType();
                double rdisch = values.getDischarge();
                double rhead = values.getHead();
                double oaeff = values.getEfficiency();
                double mcurr = values.getMaxCurrent();

                Class.forName("com.mysql.jdbc.Driver");
                // Setup the connection with the DB
                Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/db_for_bis_sub", "root", "");

                // Statements allow to issue SQL queries to the database
                java.sql.PreparedStatement statement = connect.prepareStatement(""
                        + "INSERT INTO `observed_values`"
                        + "(`sno`,`date`,`at_type`,`rdisch`,`rhead`,`oaeff`,`maxcurrent`)"
                        + "VALUES(?,?,?,?,?,?,?)");
                statement.setInt(1, sno);
                statement.setDate(2, sqlDate);
                statement.setString(3, type);
                statement.setFloat(4, round((float) rdisch, 2));
                statement.setFloat(5, round((float) rhead, 2));
                statement.setFloat(6, round((float) oaeff, 2));
                statement.setFloat(7, round((float) mcurr, 2));
                statement.executeUpdate();

            } catch (Exception ex) {

                ex.printStackTrace();

            }

            //ObsValuesFrame.saveValues(graphPanel.getGraph().getPlot().getObsValues());
        }
    }

    class ViewVarReportAction extends AbstractAction {

        public ViewVarReportAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {
            ObsValuesFrame f = new ObsValuesFrame();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setVisible(true);
            f.displayAllValues();
        }
    }

    class SaveReadingsToDBAction extends AbstractAction {

        public SaveReadingsToDBAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {
            try {
                new ReadingsInDBFrame().saveReadingsToDB(entryPanel, graphPanel);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(OpeningFrame.this, "Cannot save . ");
            }
        }
    }

    class LoadReadingsFromDBAction extends AbstractAction {

        public LoadReadingsFromDBAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent ae) {
            ReadingsInDBFrame f = new ReadingsInDBFrame(OpeningFrame.this.entryPanel);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setVisible(true);
            f.displayPumpSelectionTable();
        }
    }

    public File getFileDirectory() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("PumpFileDirectory")));
            File directory = (File) ois.readObject();
            return directory;
        } catch (Exception ex) {
        }
        return null;
    }

    public void saveFileDirectory(File directory) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("PumpFileDirectory")));
            oos.writeObject(directory);

        } catch (Exception ex) {
        }
        //return null;
    }

    public File getTypeFileDirectory() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("TypeFileDirectory")));
            File directory = (File) ois.readObject();
            return directory;
        } catch (Exception ex) {
        }
        return null;
    }

    public void saveTypeDirectory(File directory) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("TypeFileDirectory")));
            oos.writeObject(directory);

        } catch (Exception ex) {
        }
        //return null;
    }

    public void showErrorMessage() {
        JOptionPane.showMessageDialog(this,
                "Enter only  numbers as values and ensure that no two discharge values are equal.");
        return;
    }

    public void removeTheMenuBar() {
        setJMenuBar(null);
        validate();
    }

    public void listenForEntries() {
        JTable table = this.entryPanel.getTable();//table;
        Component[] components = this.entryPanel.fieldsPanel.getComponents();
        Class cls = null;
        try {
            cls = Class.forName("javax.swing.JTextField");
        } catch (Exception ex) {
        }
        for (int i = 0; i < components.length; i++) {
            if (cls.isInstance(components[i])) {
                ((JTextField) components[i]).getDocument().addDocumentListener(this);
            }


        }
        DefaultCellEditor dce = (DefaultCellEditor) table.getDefaultEditor(Double.class);
        JTextField editor = (JTextField) dce.getComponent();
        editor.getDocument().addDocumentListener(this);

    }

    public void insertUpdate(DocumentEvent e) {
        this.savePending = true;
        this.entryPerformed();
    }

    public void removeUpdate(DocumentEvent e) {
        this.savePending = true;
        this.entryPerformed();
    }

    public void changedUpdate(DocumentEvent e) {
        this.savePending = true;
        this.entryPerformed();
    }
}
