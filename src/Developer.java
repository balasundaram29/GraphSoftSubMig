
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * Write a description of class Developer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Developer extends JFrame implements GraphDrawListener {

    private Dataset dataset, dataset2, dataset3;
    private JTable table;
    private DefaultTableModel model;
    private Plot myPlot;

    /**
     * Constructor for objects of class Developer
     */
    public Developer() {
        //double[] givenArrayQ = new double[]{0.0, 0.87, 1.53, 1.76, 2.06, 2.34, 2.60, 3.18}; //original
        double[] givenArrayH = new double[]{32.31, 26.4, 25.12, 22.99, 21.11, 19.03, 17.15, 11.55}; //original
         double[] givenArrayQ = new double[]{0.0,0.57,1.23,1.36,1.76,2.04,2.30,2.88};
        //double[] givenArrayH =new double[]{32.31,26.4,25.12,22.99,22.99,21.11,19.03,17.15,11.55};
        double[] givenArrayI = new double[]{3.85, 4.82, 4.97, 5.14, 5.41, 5.70, 5.90, 6.41};
        double[] givenArrayE = new double[]{0.00, 18.9, 31.0, 31.0, 31.90, 31.00, 30.23, 22.98};
        dataset = new Dataset(givenArrayQ, givenArrayH, DatasetAndCurveType.DISCHARGE_VS_HEAD);
        dataset2 = new Dataset(givenArrayQ, givenArrayE, DatasetAndCurveType.DISCHARGE_VS_EFFICIENCY);
        dataset3 = new Dataset(givenArrayQ, givenArrayI, DatasetAndCurveType.DISCHARGE_VS_CURRENT);
    }

    public void go() {
        Graph myGraph = new Graph("Graph Software", "X Axis", "Y Axis", dataset);
        myPlot = myGraph.getPlot();
        myPlot.addGraphDrawListener(this);
        DomainAxis xAxis = myPlot.getDomainAxis();
        xAxis.setDataset(dataset);
        //xAxis.setScaleSelectionMode(ScaleSelectionMode.MANUAL);
       // xAxis.setScale(250);
        xAxis.setAxisLineColor(Color.orange);
        RangeAxis theFirst = myPlot.getRangeAxis(0);
       // theFirst.setAxisLineColor(Color.yellow);
        theFirst.setDataset(dataset);
        LoessSmoothRenderer renderer = new LoessSmoothRenderer();
        myPlot.setRenderer(0, renderer);
        renderer.setDataset(dataset);
        RangeAxis axis2 = new RangeAxis("New Axis1", AxisPosition.RIGHT);
       // axis2.setAxisLineColor(Color.red);
        axis2.setDataset(dataset2);
        //axis2.setScaleSelectionMode(ScaleSelectionMode.MANUAL);
        //axis2.setScale(-5.0);
        xAxis.setDataset(dataset2);
        RangeAxis axis3 = new RangeAxis("New Axis2", AxisPosition.RIGHT);
       // axis3.setAxisLineColor(Color.blue);
        axis3.setDataset(dataset3);
        xAxis.setDataset(dataset3);
        //RangeAxis axis1L = new RangeAxis("New Axis2",AxisPosition.LEFT);
        myPlot.setRangeAxis(1, axis2);
        myPlot.setRangeAxis(2, axis3);
        //myPlot.setRangeAxis(3,axis1L);
        LoessSmoothRenderer renderer2 = new LoessSmoothRenderer();
        renderer2.setDataset(dataset2);

        myPlot.setRenderer(1, renderer2);
        //XYLineRenderer renderer3 = new XYLineRenderer();
        LoessSmoothRenderer renderer3 = new LoessSmoothRenderer();
        renderer3.setDataset(dataset3);
        myPlot.setRenderer(2, renderer3);
        PumpValues declaredValues = new PumpValues();
        declaredValues.setHead(23.00);
        declaredValues.setDischarge(1.40);
        declaredValues.setHeadRangeMax(24.20);
        declaredValues.setHeadRangeMin(18.40);
        declaredValues.setEfficiency(27.00);
        declaredValues.setMaxCurrent(6.20);
        myPlot.setDeclaredValues(declaredValues);
        setLayout(null);
       // GraphPanel gPanel = new GraphPanel(myGraph);
        //setSize(1024, 768);
        setSize(2892,597);
        this.setResizable(false);
        this.setBackground(Color.white);
        JPanel cPane = (JPanel) this.getContentPane();
        //gPanel.setBounds(0, 0, 2892, 580);

        model = new DefaultTableModel();
        table = new JTable(model);
        model.addColumn("Kera Pump Company");
        model.addColumn("Type : AE 100");
        model.addColumn("Duty Point");
        model.addColumn("Q(lps)");
        model.addColumn("TH(mtrs)");
        model.addColumn("OAE(%)");
        model.addColumn("I-Max(Amps)");
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        table.getTableHeader().setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 24));
        table.setFont(Font.getFont(Font.MONOSPACED));

        table.setRowHeight(24);
        table.setRowMargin(3);
        TableColumn col = table.getColumnModel().getColumn(0);
        col.setPreferredWidth(300);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        Object[] rowData1 = {"IS 9079 : 2002", "S.No : 1001 ", "Guaranteed", "1.40 ", "23.00", "27.00", "6.20"};
        Object[] rowData2 = {"Head Range 18.40 / 24.20", "Size : 32 x 25 mm ", "Actual", " ", " ", " ", ""};
        Object[] rowData3 = {"Date: 11.02.08", "Frequency : 50 Hz", "Result", " ", "", " ", " ",};
        model.addRow(rowData1);
        model.addRow(rowData2);
        model.addRow(rowData3);


        setTableCellAlignment(JLabel.LEFT, table);
        // Object[][] rowData = {
        //  {"IS9079=n - 2002", "S.No : 1001 ", "Guaranteed", "1.40 ", "23.00", "27.00", "6.20"},
        //  {"Head Range 18.40 / 24.20", "Size : 32 x 25 mm ", "Actual", , obsValues.getHead(), obsValues.getEfficiency(), obsValues.getMaxCurrent()}
        // };






        

        Object[] colNames = {"Annai Engineering Company", "Type : AE 100", "Duty Point", "Q(lps)", "TH(mtrs)", "OAE(%)", "I-Max(Amps) "};
        //   String[][] rowData= new String[1][7];

        // rowData = {{"IS9079=n - 2002"},{"S.No : 1001 "},{"Guaranteed"}, {"1.40 "}, {"23.00"},{"27.00"},{"6.20"}};
        //add(panel);
        //JTable table = new JTable(rowData, colNames);
        //JScrollPane scroller2 = new JScrollPane(table);
        //scroller2.setBounds(0, 600, 1024, 168);
        // cPane.add(gPanel);
        //cPane.add(scroller2);
      
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
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
        //table.validate();
    }

    private void setTableCellAlignment(int alignment, JTable table) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(alignment);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.setDefaultRenderer(table.getColumnClass(i), renderer);
        }
        // repaint to show table cell changes
        table.updateUI();
    }
        }










