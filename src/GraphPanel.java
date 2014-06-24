
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.miginfocom.swing.MigLayout;

/**
 * Write a description of class GraphPanel here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public final class GraphPanel extends JPanel implements GraphDrawListener {

    private GraphDrawingPanel drawingPanel;
    private Graph graph;
   
  
    private  ReportPanel reportPanel;
    private  int w;
    private  int h;
    private DefaultTableModel model;
    private JTable table;
    private JScrollPane tScroller;
    Plot myPlot;
    private JPanel tableScrollerPanel;
    PumpValues declaredValues;

    /**
     * Constructor for objects of class GraphPanel
     */


    public GraphPanel(ReportPanel reportPanel,int w, int h) {
        this.w = w;
        this.h = h-35;
        this.reportPanel = reportPanel;
        makeDrawingPanel();
        makeTableScrollerPanel();
        //setLayout(null);
        setLayout(new MigLayout("","[grow][]","[grow][]"));
       // setBounds(0,0,this.w,this.h);
        // this.setPreferredSize(new Dimension(1024,680));
       // this.drawingPanel.setBounds(0, 0, this.w, this.h-78);

     //   this.tableScrollerPanel.setBounds(0, this.h-77, this.w, 78);
        setBackground(Color.white);
        add(drawingPanel,"grow,span,wrap");
        add(tScroller,"grow,span,height : :90");
       // add(tableScrollerPanel);
    }
    //to change the size of the already existing  GraphPanel following constructor is used
    public GraphPanel(GraphPanel gp,int w, int h) {
        this.w = w;
        this.h = h-35;
        this.reportPanel = gp.reportPanel;
        makeDrawingPanel();
        makeTableScrollerPanel();
       // setLayout(null);
       // setBounds(0,0,this.w,this.h);
        // this.setPreferredSize(new Dimension(1024,680));
       // this.drawingPanel.setBounds(0, 0, this.w, this.h-78);

      //  this.tableScrollerPanel.setBounds(0, this.h-77, this.w, 78);
        setBackground(Color.white);
        add(drawingPanel);
        //add(tableScrollerPanel);
        add(tScroller);
    }
     public void paintComponent(Graphics g) {
         this.w = getWidth();
        this.h = getHeight()-35;
      //  this.reportPanel = gp.reportPanel;
        //makeDrawingPanel();
        //makeTableScrollerPanel();
        //setLayout(null);
        //setBounds(0,0,this.w,this.h);
        // this.setPreferredSize(new Dimension(1024,680));
       // this.drawingPanel.setBounds(0, 0, this.w, this.h-78);

       // this.tableScrollerPanel.setBounds(0, this.h-77, this.w, 78);
        setBackground(Color.white);
        //add(drawingPanel);
        //add(tableScrollerPanel);
     }
    public void setWAndH(int w, int h){
         this.w = w;
        this.h = h-35;
        setBounds(0,0,this.w,this.h);
         //remove(drawingPanel);
       // remove(tableScrollerPanel);
        
        this.drawingPanel.setBounds(0, 0, this.w, this.h-78);

        this.tableScrollerPanel.setBounds(0, this.h-77, this.w, 78);
        tScroller.setBounds(5,0,w-15, 74);
        setBackground(Color.white);
        // add(drawingPanel);
        //add(tableScrollerPanel);
    }
    
    void makeDrawingPanel() {
        Dataset headDataset = reportPanel.getDataset(DatasetAndCurveType.DISCHARGE_VS_HEAD);
        Dataset currDataset = reportPanel.getDataset(DatasetAndCurveType.DISCHARGE_VS_CURRENT);
        Dataset effDataset = reportPanel.getDataset(DatasetAndCurveType.DISCHARGE_VS_EFFICIENCY);
        Graph myGraph = new Graph("Graph Software", "Discharge , lps", "Total Head , m", headDataset);
        graph = myGraph;
        myPlot = myGraph.getPlot();
        myPlot.addGraphDrawListener(this);
        DomainAxis xAxis = myPlot.getDomainAxis();
        xAxis.setDataset(headDataset);
        //xAxis.setScaleSelectionMode(ScaleSelectionMode.MANUAL);
        // xAxis.setScale(250);
        xAxis.setAxisLineColor(Color.blue);
        xAxis.setMaxAxisValue(reportPanel.getValuesForScale().getDischMax());
        RangeAxis theFirst = myPlot.getRangeAxis(0);
        theFirst.setAxisLinePaint(Color.magenta);
        theFirst.setDataset(headDataset);
        theFirst.setMaxAxisValue(reportPanel.getValuesForScale().getHeadMax());
        LoessSmoothRenderer renderer1 = new LoessSmoothRenderer();
        myPlot.setRenderer(0, renderer1);
        renderer1.setDataset(headDataset);
        RangeAxis axis2 = new RangeAxis("Overall Efficiency , %", AxisPosition.LEFT);
        axis2.setAxisLinePaint(Color.red);
        axis2.setDataset(effDataset);
        axis2.setMaxAxisValue(reportPanel.getValuesForScale().getEffMax());
       
        xAxis.setDataset(effDataset);
        RangeAxis axis3 = new RangeAxis("Current , A", AxisPosition.LEFT);
        axis3.setAxisLinePaint(Color.blue);
        axis3.setDataset(currDataset);
        axis3.setMaxAxisValue(reportPanel.getValuesForScale().getCurrMax());
        xAxis.setDataset(currDataset);

       
        myPlot.setRangeAxis(1, axis2);
        myPlot.setRangeAxis(2, axis3);
       
        LoessSmoothRenderer renderer2 = new LoessSmoothRenderer();
        renderer2.setDataset(effDataset);

        myPlot.setRenderer(1, renderer2);
        
        LoessSmoothRenderer renderer3 = new LoessSmoothRenderer();
        renderer3.setDataset(currDataset);
        myPlot.setRenderer(2, renderer3);
        declaredValues = this.reportPanel.getDeclaredValues();
        myPlot.setDeclaredValues(declaredValues);
        //setLayout(null);
        drawingPanel = new GraphDrawingPanel(myGraph);
        setCurveAndAxisPaintTheSame();

    }

    private void makeTableScrollerPanel() {
        tableScrollerPanel = new JPanel();
     //  tableScrollerPanel.setLayout(null);
       tableScrollerPanel.setBackground(Color.white);
         
        model = new DefaultTableModel();
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        model.addColumn("Annai Engineering Company");
        model.addColumn("Type : "+reportPanel.getEntryPanel().getPumpTypeField().getText());
        model.addColumn("Duty Point");
        model.addColumn("Q(lps)");
        model.addColumn("TH(mtrs)");
        model.addColumn("OAE(%)");
        model.addColumn("I-Max(Amps)");
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
         table.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 8));
        table.getTableHeader().setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 20));
       table.getTableHeader().setBackground(Color.WHITE);
          table.setFont(new Font("SansSerif", Font.PLAIN, 8));
        table.setRowHeight(17);
        table.setRowMargin(3);
         table.setFont(new Font("SansSerif", Font.PLAIN, 8));
        TableColumn col = table.getColumnModel().getColumn(0);
        col.setPreferredWidth(300);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        ReadingEntryPanel entryPanel = reportPanel.getEntryPanel();
        Object[] rowData1 = {reportPanel.getEntryPanel().getIsRefField().getText(), "S.No : "+entryPanel.getSlNoField().getText(), "Guaranteed", declaredValues.getDischarge(),
                           declaredValues.getHead(),declaredValues.getEfficiency(), declaredValues.getMaxCurrent()};
        Object[] rowData2 = {"Head Range : "+declaredValues.getHeadRangeMin()+"/"+declaredValues.getHeadRangeMax()+"  (m)",
                             "Size :"+entryPanel.getDelSizeField().getText()+ " mm ", "Actual", " ", " ", " ", ""};
       DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = formatter.format(entryPanel.getDateChooser().getDate());
        Object[] rowData3 = {"Date: "+dateString, "Frequency : 50 Hz", "Result", " ", "", " ", " "};
        model.addRow(rowData1);
        model.addRow(rowData2);
        model.addRow(rowData3);
        //table.setBorder(BorderFactory.createLineBorder(Color.black));
        setTableCellAlignment(JLabel.LEFT, table);
        tScroller = new JScrollPane(table);
      // tScroller.setBorder(BorderFactory.createLineBorder(Color.black));
       // tScroller.setBounds(5,0,w-15, 74);
       tableScrollerPanel.add(tScroller);
    }
private void setTableCellAlignment(int alignment, JTable table) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(alignment);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.setDefaultRenderer(table.getColumnClass(i), renderer);
        }
        table.updateUI();
    }
   
public void graphDrawn(GraphDrawEvent ev) {


        PumpValues obsValues = myPlot.getObsValues();
        String s = String.format("%,.2f", obsValues.getDischarge());
        table.setValueAt(s, 1, 3);
        s = String.format("%,.2f", obsValues.getHead());
        table.setValueAt(s, 1, 4);
        s = String.format("%,.2f", obsValues.getEfficiency());
        table.setValueAt(s, 1, 5);
        s = String.format("%,.2f", obsValues.getMaxCurrent());
        table.setValueAt(s, 1, 6);
        table.setValueAt(obsValues.getDischResult().toString(), 2, 3);
        table.setValueAt(obsValues.getHeadResult().toString(),2,4);
        table.setValueAt(obsValues.getEffResult().toString(),2,5);
        table.setValueAt(obsValues.getCurrResult().toString(),2,6);
        table.doLayout();
        table.validate();
    }

    public void setCurveAndAxisPaintTheSame() {
        for( Renderer renderer : myPlot.getRendererList()){
            renderer.setCurvePaint(renderer.getDataset().getRangeAxis().getAxisLinePaint());
        }
    }

    /**
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @param graph the graph to set
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    /**
     * @return the w
     */
    public int getW() {
        return w;
    }

    /**
     * @param w the w to set
     */
    public void setW(int w) {
        this.w = w;
    }

    /**
     * @return the h
     */
    public int getH() {
        return h;
    }

    /**
     * @param h the h to set
     */
    public void setH(int h) {
        this.h = h;
    }
}