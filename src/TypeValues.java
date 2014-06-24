
import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class TypeValues implements Serializable {
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
  private String isRefFieldString;

    public TypeValues(ReadingEntryPanel entryPanel){
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
        this.isRefFieldString = entryPanel.getIsRefField().getText();
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
     * @return the isRefFieldString
     */
    public String getIsRefFieldString() {
        return isRefFieldString;
    }


}
