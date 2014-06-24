
import java.util.Date;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bala
 */
public class PumpValues {

    /**
     * The head value in meters.
     */
  
    /** The discharge value  in liters per second.
     */
    private String type;
    private String sNo ;
    private Date date ;
    private double discharge;
    private double head;
    private double efficiency;
    private double maxCurrent;
    private double headRangeMax;
    private double headRangeMin;
    private double dischMax, dischMin;
    private TestResult dischResult, headResult, effResult, currResult;

    public double getHead() {
        return head;
    }

    public double getDischarge() {
        return discharge;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public double getMaxCurrent() {
        return maxCurrent;
    }

    public double getHeadRangeMax() {
        return headRangeMax;
    }

    public double getHeadRangeMin() {
        return headRangeMin;
    }

    public double getDischMax() {
        return dischMax;
    }

    public double getDischMin() {
        return dischMin;
    }
    public TestResult getDischResult() {
        return dischResult;
    }
    public TestResult getHeadResult() {
        return headResult;
    }
    public TestResult getEffResult() {
        return effResult;
    }

    public TestResult getCurrResult() {
        return currResult;
    }
    public TestResult getFinalResult() {
        if(
                (this.dischResult == TestResult.PASS)&&
                (this.headResult== TestResult.PASS)&&
                (this.effResult==TestResult.PASS)&&
                (this.currResult == TestResult.PASS)
          )

            return TestResult.PASS;
        else
            return TestResult.FAIL;

    }


    public void setDischarge(double disch) {
        discharge = disch;
    }

    public void setHead(double head) {
        this.head = head;
    }

    public void setEfficiency(double eff) {
        efficiency = eff;
    }

    public void setMaxCurrent(double maxCurr) {
        maxCurrent = maxCurr;
    }

    public void setHeadRangeMax(double max) {
        headRangeMax = max;
    }

    public void setHeadRangeMin(double min) {
        headRangeMin = min;
    }

    public void setDischMax(double max) {
        dischMax = max;
    }

    public void setDischMin(double min) {
        dischMin = min;
    }
    public void setDischResult(TestResult result) {

        this.dischResult = result;
    }
    public void setHeadResult(TestResult result) {

        this.headResult = result;
    }
    public void setEffResult(TestResult result) {

        this.effResult = result;
    }
    public void setCurrResult(TestResult result) {

        this.currResult = result;
    }

    /**
     * @return the sNo
     */
    public String getsNo() {
        return sNo;
    }

    /**
     * @param sNo the sNo to set
     */
    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }





}
