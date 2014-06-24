
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bala
 */
public class ReportFrame extends JPanel {

    private ReadingEntryPanel entryPanel;
    private JTable entryTable;

    public ReportFrame(ReadingEntryPanel entryPanel) {
        this.entryPanel = entryPanel;
        double[][] given = parseEntryTable();
        double[][] data = this.findReportValues(given);
        this.getAndAddTable(data);
        setBounds(0,0,1064,768);
        setLayout(null);
    }

    /**
     * Returns true when the values are usable
     */
    public boolean parseEnteredValues() {
        boolean ok = true;
        entryTable = entryPanel.getTable();
        int rows = entryTable.getModel().getRowCount();
        int cols = entryTable.getModel().getColumnCount();
        /**for(int i=0;i<rows;i++){
        for(int j=0;j<cols;j++){
        if (entryTable.getValueAt(i,j)== NaN())
        }
        }**/
        return ok;


    }

    public int findRowCount() {
        entryTable = entryPanel.getTable();
        int i = 0;
        for (i = 0; i < 10; i++) {
            String str = (entryTable.getValueAt(i, 1)).toString();
            if(str.trim().length()==0) break;
        }
           
          System.out.println("Row count is " + i);
                 return i;
        }
      
    

    public double[][] parseEntryTable() {
        int rows = findRowCount();
        int cols = 7;
        double[][] given = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                given[i][j] = Double.parseDouble((entryTable.getValueAt(i, j)).toString());
            }
        }
        return given;

    }

    public double[][] findReportValues(double[][] given) {
        int rows = findRowCount();
        int cols = 14;
        double data[][] = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            double rater = 50.0 / given[i][EntryTableConstants.FREQ_COL_INDEX];
            data[i][ReportTableConstants.SLNO_COL_INDEX] = i + 1;
            data[i][ReportTableConstants.FREQ_COL_INDEX] = given[i][EntryTableConstants.FREQ_COL_INDEX];
            data[i][ReportTableConstants.DGR_COL_INDEX] = given[i][EntryTableConstants.DGR_COL_INDEX];
            data[i][ReportTableConstants.VHC_COL_INDEX] = 0;
            data[i][ReportTableConstants.TH_COL_INDEX] = given[i][EntryTableConstants.DGR_COL_INDEX]
                    + Double.parseDouble((entryPanel.getGaugDistField()).getText());
            data[i][ReportTableConstants.DISCH_COL_INDEX] = given[i][EntryTableConstants.DISCH_COL_INDEX];
            data[i][ReportTableConstants.VOL_COL_INDEX] = given[i][EntryTableConstants.VOL_COL_INDEX];

            data[i][ReportTableConstants.CURR_COL_INDEX] = given[i][EntryTableConstants.CURR_COL_INDEX];
            data[i][ReportTableConstants.MINPUT_COL_INDEX] = given[i][EntryTableConstants.POWER_COL_INDEX];
            data[i][ReportTableConstants.RDISCH_COL_INDEX] = rater * data[i][ReportTableConstants.DISCH_COL_INDEX];
            data[i][ReportTableConstants.RHEAD_COL_INDEX] = rater * rater * data[i][ReportTableConstants.TH_COL_INDEX];
            data[i][ReportTableConstants.RINPUT_COL_INDEX] = rater * rater * rater * data[i][ReportTableConstants.MINPUT_COL_INDEX];
            data[i][ReportTableConstants.POP_COL_INDEX] = data[i][ReportTableConstants.RDISCH_COL_INDEX]
                    * data[i][ReportTableConstants.RHEAD_COL_INDEX] / 102.00;
            data[i][ReportTableConstants.EFF_COL_INDEX] = data[i][ReportTableConstants.POP_COL_INDEX] / data[i][ReportTableConstants.RINPUT_COL_INDEX];
        }

        return data;
    }

    public void getAndAddTable(double[][] data) {


        Object[] columnNames = {"Sl.No", "Freq", "Del.G.R", "VHC", "Total Head",
            "Disch", "Voltage", "Current", "MInput",
            "Rdisch", "RHead", "RInput", "O/P", "Eff"};
        Object[][] dataObj = new Object[this.findRowCount()][14];
        for (int i = 0; i < findRowCount(); i++) {
            for (int j = 0; j < 14; j++) {
                dataObj[i][j] = new Double(data[i][j]);
            }
        }
            JTable reportTable = new JTable(dataObj, columnNames);
            JScrollPane tScroller = new JScrollPane(reportTable);
            tScroller.setBounds(0,0,1064,768);
            add(tScroller);
          //  tScroller.setVisible(true);


        }

    }

