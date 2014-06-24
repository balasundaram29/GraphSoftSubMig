
import java.util.Date;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class SavableObservedValues {

    private int id;
    private String type;
    private String sNo;
    private Date date;
    private double head;
    private double disch;
    private double eff;
    private double mcurrent;

    public SavableObservedValues(){
        
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
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
     * @return the head
     */
    public double getHead() {
        return head;
    }

    /**
     * @param head the head to set
     */
    public void setHead(double head) {
        this.head = head;
    }

    /**
     * @return the disch
     */
    public double getDisch() {
        return disch;
    }

    /**
     * @param disch the disch to set
     */
    public void setDisch(double disch) {
        this.disch = disch;
    }

    /**
     * @return the eff
     */
    public double getEff() {
        return eff;
    }

    /**
     * @param eff the eff to set
     */
    public void setEff(double eff) {
        this.eff = eff;
    }

    /**
     * @return the current
     */
    public double getMcurrent() {
        return mcurrent;
    }

    /**
     * @param current the current to set
     */
    public void setMcurrent(double current) {
        this.mcurrent = mcurrent;
    }
}
