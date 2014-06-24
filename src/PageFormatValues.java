
import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bala
 */
public class PageFormatValues implements Serializable{
  private GSPageOrientation orient;
  private  double h;
  private  double w;
  private double imX;
  private double imY;
  private double imH;
  private double imW;

    public PageFormatValues(GSPageOrientation orient, double w, double h, double imX, double imY, double imW, double imH) {
        this.orient = orient;
        this.h = h;
        this.w = w;
        this.imX = imX;
        this.imY = imY;
        this.imH = imH;
        this.imW = imW;
    }

    /**
     * @return the orient
     */
    public GSPageOrientation getOrient() {
        return orient;
    }

    /**
     * @param orient the orient to set
     */
    public void setOrient(GSPageOrientation orient) {
        this.orient = orient;
    }

    /**
     * @return the h
     */
    public double getH() {
        return h;
    }

    /**
     * @param h the h to set
     */
    public void setH(double h) {
        this.h = h;
    }

    /**
     * @return the w
     */
    public double getW() {
        return w;
    }

    /**
     * @param w the w to set
     */
    public void setW(double w) {
        this.w = w;
    }

    /**
     * @return the imX
     */
    public double getImX() {
        return imX;
    }

    /**
     * @param imX the imX to set
     */
    public void setImX(double imX) {
        this.imX = imX;
    }

    /**
     * @return the imY
     */
    public double getImY() {
        return imY;
    }

    /**
     * @param imY the imY to set
     */
    public void setImY(double imY) {
        this.imY = imY;
    }

    /**
     * @return the imH
     */
    public double getImH() {
        return imH;
    }

    /**
     * @param imH the imH to set
     */
    public void setImH(double imH) {
        this.imH = imH;
    }

    /**
     * @return the imW
     */
    public double getImW() {
        return imW;
    }

    /**
     * @param imW the imW to set
     */
    public void setImW(double imW) {
        this.imW = imW;
    }
  

}
