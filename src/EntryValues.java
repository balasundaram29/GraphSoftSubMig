
import java.io.Serializable;
import java.util.Date;
import javax.swing.JTable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author admin
 */
public class EntryValues implements Serializable {
public long serialVersionUID = -8559476956295049665L;
    private String slNoFieldString;
    private String ipNoFieldString;
    private String pumpTypeFieldString;
    private String ratingFieldString;
    private String headFieldString;
    private String dischFieldString;
    private String effFieldString;
    private String currFieldString;
    private String voltFieldString;
    private String phaseFieldString;
    private String freqFieldString;
    private String hRangeLwrFieldString;
    private String hRangeUprFieldString;
    private String delSizeFieldString;
    private String gaugDistFieldString;
    private String isRefFieldString;
    private String remarksFieldString;
    private Date dateFieldDate;
    private String[][] tableValueStrings;

    public EntryValues(ReadingEntryPanel entryPanel) {
        this.slNoFieldString = entryPanel.getSlNoField().getText();
        this.ipNoFieldString = entryPanel.getIpNoField().getText();
        this.pumpTypeFieldString = entryPanel.getPumpTypeField().getText();
        this.ratingFieldString = entryPanel.getRatingField().getText();
        this.headFieldString = entryPanel.getHeadField().getText();
        this.dischFieldString = entryPanel.getDischField().getText();
        this.effFieldString = entryPanel.getEffField().getText();
        this.currFieldString = entryPanel.getCurrField().getText();
        this.voltFieldString = entryPanel.getVoltField().getText();
        this.phaseFieldString = entryPanel.getPhaseField().getText();
        this.freqFieldString = entryPanel.getFreqField().getText();
        this.hRangeLwrFieldString = entryPanel.gethRangeLwrField().getText();
        this.hRangeUprFieldString = entryPanel.gethRangeUprField().getText();
        this.delSizeFieldString = entryPanel.getDelSizeField().getText();
        this.gaugDistFieldString = entryPanel.getGaugDistField().getText();
        this.isRefFieldString = entryPanel.getIsRefField().getText();
        this.remarksFieldString = entryPanel.getRemarksField().getText();
        this.dateFieldDate =entryPanel.getDateChooser().getDate();
        JTable entryTable = entryPanel.getTable();
        this.tableValueStrings = new String[entryTable.getRowCount()][entryTable.getColumnCount()] ;
        for (int i = 0; i < entryTable.getRowCount(); i++) {
            for (int j = 0; j <  entryTable.getColumnCount(); j++) {

            tableValueStrings[i][j] = (entryTable.getValueAt(i, j)).toString();
            }
        }

    }

    /**
     * @return the slNoFieldString
     */
    public String getSlNoFieldString() {
        return slNoFieldString;
    }

    /**
     * @return the ipNoFieldString
     */
    public String getIpNoFieldString() {
        return ipNoFieldString;
    }

    /**
     * @return the pumpTypeFieldString
     */
    public String getPumpTypeFieldString() {
        return pumpTypeFieldString;
    }

    /**
     * @return the ratingFieldString
     */
    public String getRatingFieldString() {
        return ratingFieldString;
    }

    /**
     * @return the headFieldString
     */
    public String getHeadFieldString() {
        return headFieldString;
    }

    /**
     * @return the dischFieldString
     */
    public String getDischFieldString() {
        return dischFieldString;
    }

    /**
     * @return the effFieldString
     */
    public String getEffFieldString() {
        return effFieldString;
    }

    /**
     * @return the currFieldString
     */
    public String getCurrFieldString() {
        return currFieldString;
    }

    /**
     * @return the voltFieldString
     */
    public String getVoltFieldString() {
        return voltFieldString;
    }

    /**
     * @return the phaseFieldString
     */
    public String getPhaseFieldString() {
        return phaseFieldString;
    }

    /**
     * @return the freqFieldString
     */
    public String getFreqFieldString() {
        return freqFieldString;
    }

    /**
     * @return the hRangeLwrFieldString
     */
    public String gethRangeLwrFieldString() {
        return hRangeLwrFieldString;
    }

    /**
     * @return the hRangeUprFieldString
     */
    public String gethRangeUprFieldString() {
        return hRangeUprFieldString;
    }

    /**
     * @return the delSizeFieldString
     */
    public String getDelSizeFieldString() {
        return delSizeFieldString;
    }

    /**
     * @return the gaugDistFieldString
     */
    public String getGaugDistFieldString() {
        return gaugDistFieldString;
    }

    /**
     * @return the isRefFieldString
     */
    public String getIsRefFieldString() {
        return isRefFieldString;
    }

    /**
     * @return the remarksFieldString
     */
    public String getRemarksFieldString() {
        return remarksFieldString;
    }

   
    /**
     * @return the tableValueStrings
     */
    public String[][] getTableValueStrings() {
        return tableValueStrings;
    }

    /**
     * @return the dateFieldDate
     */
    public Date getDateFieldDate() {
        return dateFieldDate;
    }
}
