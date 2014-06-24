/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bala
 */

import org.apache.commons.math.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math.analysis.interpolation.LoessInterpolator;
import org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction;

public class LoessFunction implements Drawable {
  
    Dataset dataset;
    private double bandwidth ;
    private double desiredSmoothnessPercentage = 0.0;
    private double desiredLinearityFactor=0.0;

    public LoessFunction(Dataset dataset) {
        this.dataset = dataset;
        bandwidth = 0.01 + (2.0 / dataset.getXArray().length);

    }

   

   public void setBandwidth(double desiredSmoothnessPercentage){
       this.desiredSmoothnessPercentage =desiredSmoothnessPercentage;
       double minBandwidth = 0.01 + (2.0 / dataset.getXArray().length);
        if (desiredSmoothnessPercentage>99.99) desiredSmoothnessPercentage  = 99.99;
        if (desiredSmoothnessPercentage<0.0) desiredSmoothnessPercentage  = 0.0;
        bandwidth = minBandwidth+((0.99-minBandwidth)*desiredSmoothnessPercentage/100.0);
         
   }




    public double getYValue(double x) {
        LinearInterpolator linear;
        LoessInterpolator spline;
        PolynomialSplineFunction function1;
         PolynomialSplineFunction function2;
        double[] xArray = dataset.getXArray();
        double[] yArray = dataset.getYArray();
        //CubicSpline spline= new CubicSpline(xArray,yArray);
      //  HenreichInterpolation spline = new HenreichInterpolation(xArray, yArray);
        double y = 0.0;
        try {

            spline = new LoessInterpolator(bandwidth, 10);
            linear = new LinearInterpolator();
            function1 = spline.interpolate(xArray, yArray);
            function2 = linear.interpolate(xArray, yArray);
            y = (function1.value(x)+(this.desiredLinearityFactor*function2.value(x)))/(desiredLinearityFactor+1);
            
        } catch (Exception ex) {
        ex.printStackTrace();
        }
        return y;
    }

    public double getXMax() {
        return GraphUtilities.getMax(dataset.getXArray());
    }

    public Dataset getDataset() {
        return this.dataset;
    }

    /**
     * @return the bandwidth
     */
    public double getBandwidth() {
        return bandwidth;
    }

    /**
     * @return the desiredSmoothnessPercentage
     */
    public double getDesiredSmoothnessPercentage() {
        return desiredSmoothnessPercentage;
    }

    /**
     * @return the desiredLinearityFactor
     */
    public double getDesiredLinearityFactor() {
        return desiredLinearityFactor;
    }

    /**
     * @param desiredLinearityFactor the desiredLinearityFactor to set
     */
    public void setDesiredLinearityFactor(double desiredLinearityFactor) {
        this.desiredLinearityFactor = desiredLinearityFactor;
    }
}
