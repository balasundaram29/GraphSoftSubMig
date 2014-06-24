/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bala
 */
import java.awt.*;
import java.awt.geom.*;

public class CurrValuesExtractor extends Extractor{

    Drawable function;
    private Stroke stroke = new BasicStroke(1.0f);
private Paint linePaint = Color.red;
    public CurrValuesExtractor(Drawable function) {
        this.function = function;
    }

    public PumpValues drawLinesAndExtractValues(Graphics2D g2D, Rectangle2D dataArea, PumpValues declaredValues, PumpValues obsValues) {
       
        Dataset dataset = function.getDataset();
      
        obsValues.setMaxCurrent(this.getMax(obsValues.getDischMin(), obsValues.getDischMax()));
        obsValues.setCurrResult(obsValues.getMaxCurrent() <= declaredValues.getMaxCurrent() ? TestResult.PASS : TestResult.FAIL);
      
        RangeAxis rAxis = dataset.getRangeAxis();
        DomainAxis dAxis = dataset.getDomainAxis();
        double scaleX = dAxis.getScale(dataArea);
        double scaleY = rAxis.getScale(dataArea);
       
        double xUser = obsValues.getDischMin();
      
        double yUser = function.getYValue(xUser);
       
        double xJava2D = SpaceConverter.convertFromUserToJava2D(xUser,scaleX, dataArea, RectangleEdge.LEFT);
        double yJava2D = SpaceConverter.convertFromUserToJava2D(yUser,scaleY, dataArea, RectangleEdge.BOTTOM);

        Shape currMarker = ShapeUtilities.createTranslatedShape(ShapeUtilities.createUpVMark(10.0f), xJava2D, yJava2D);
        g2D.setStroke(stroke);
          g2D.setPaint(linePaint);

        g2D.draw(currMarker);
        xUser = obsValues.getDischMax();
        yUser = function.getYValue(xUser);
        xJava2D = SpaceConverter.convertFromUserToJava2D(xUser,scaleX, dataArea, RectangleEdge.LEFT);
        yJava2D = SpaceConverter.convertFromUserToJava2D(yUser,scaleY, dataArea, RectangleEdge.BOTTOM);
        
        currMarker = ShapeUtilities.createTranslatedShape(ShapeUtilities.createUpVMark(10.0f), xJava2D, yJava2D);
        g2D.draw(currMarker);

        return obsValues;
    }

    public  double getMax(double xLowerBound, double xUpperBound) {
        double yMax=0.0;
        yMax = function.getYValue(xLowerBound);
        double inc = (xUpperBound - xLowerBound) / 1000.0;
        double xIter = xLowerBound;
        double yIter = yMax;
        while (xIter <= xUpperBound) {
            yIter = function.getYValue(xIter);
            if (yIter > yMax) {
                yMax = yIter;
            }
            xIter = xIter + inc;
        }
        return yMax;
    }

    /**
     * @return the stroke
     */
    public Stroke getStroke() {
        return stroke;
    }

    /**
     * @param stroke the stroke to set
     */
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    /**
     * @return the linePaint
     */
    public Paint getLinePaint() {
        return linePaint;
    }

    /**
     * @param linePaint the linePaint to set
     */
    public void setLinePaint(Paint linePaint) {
        this.linePaint = linePaint;
    }
}
