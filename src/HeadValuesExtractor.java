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

public class HeadValuesExtractor extends Extractor {

    Drawable function;
    private Stroke stroke = new BasicStroke(1.0f);
    private Paint linePaint = Color.red;
    public HeadValuesExtractor(Drawable function) {
        this.function = function;
    }

    public PumpValues drawLinesAndExtractValues(Graphics2D g2D, Rectangle2D dataArea, PumpValues declaredValues, PumpValues obsValues) {
       
        Stroke savedStroke = g2D.getStroke();
        Dataset dataset = function.getDataset();
       
        g2D.setStroke(stroke);
        g2D.setPaint(linePaint);
        double declaredHead = declaredValues.getHead();
        double declaredDisch = declaredValues.getDischarge();
        double headMax = declaredValues.getHeadRangeMax();
        double headMin = declaredValues.getHeadRangeMin();
        double slope = declaredHead / declaredDisch;

        double maxValueX = function.getXMax();
        double obsDPDisch = drawIntersectingLine(g2D,dataArea,dataset, 0, 0, maxValueX, maxValueX * slope, IntersectingLineType.DUTY_POINT_LINE);
        obsValues.setDischarge(obsDPDisch);
        obsValues.setHead(slope * obsDPDisch);
        obsValues.setDischResult(obsDPDisch >= declaredValues.getDischarge() ? TestResult.PASS : getResultWithTol(declaredValues, obsValues, 0.04, 0.07));
        obsValues.setHeadResult(slope * obsDPDisch >= declaredValues.getHead() ? TestResult.PASS : getResultWithTol(declaredValues, obsValues, 0.04, 0.07));
        
        double dischMax = drawIntersectingLine(g2D,dataArea,dataset, 0, headMax, maxValueX, headMax, IntersectingLineType.HEAD_RANGE_MARKER);
        double dischMin = drawIntersectingLine(g2D,dataArea,dataset, 0, headMin, maxValueX, headMin, IntersectingLineType.HEAD_RANGE_MARKER);
        obsValues.setDischMax(dischMax);
        obsValues.setDischMin(dischMin);
      
        RangeAxis rAxis = dataset.getRangeAxis();
        DomainAxis dAxis = dataset.getDomainAxis();
        double scaleX = 1.0;
        double scaleY = 1.0;
      
        scaleX = dAxis.getScale(dataArea);
        scaleY=rAxis.getScale(dataArea);

        double xZeroUser = dataArea.getMinX();
        double xUser = declaredValues.getDischarge();
        double yZeroUser = dataArea.getMaxY();
        double yUser = declaredValues.getHead();
        double xJava2D = SpaceConverter.convertFromUserToJava2D(xUser,scaleX, dataArea, RectangleEdge.LEFT);//xZeroUser + xUser * scaleX;
        double yJava2D = SpaceConverter.convertFromUserToJava2D(yUser,scaleY, dataArea, RectangleEdge.BOTTOM);//yZeroUser - yUser * scaleY * (-1.0);
        Shape dPMarker=null;
        if(obsDPDisch>=declaredDisch){
           dPMarker = ShapeUtilities.createTranslatedShape(ShapeUtilities.createUpDPTriangle(15.0f), xJava2D, yJava2D);
        }else{
           dPMarker = ShapeUtilities.createTranslatedShape(ShapeUtilities.createDownDPTriangle(15.0f), xJava2D, yJava2D);
        }
        g2D.setStroke(savedStroke);
        
        g2D.fill(dPMarker);
        //this line is longer for obs DP<del DP 
        g2D.draw(new Line2D.Double(xJava2D, yJava2D,xZeroUser,yZeroUser));
        return obsValues;

    }

    public double drawIntersectingLine(Graphics2D g2D,Rectangle2D dataArea,Dataset dataset, double x1In, double y1In, double x2In, double y2In, IntersectingLineType lineType) {
        double x1 = x1In; //= line.getX1();
        double x2 = x2In;// = line.getX2();
        double y1 = y1In;//= line.getY1();
        double y2 = y2In;//=  line.getY2();
        double k = y2In / x2In;
        DomainAxis dAxis = dataset.getDomainAxis();
        RangeAxis rAxis = dataset.getRangeAxis();
        double scaleX = dAxis.getScale(dataArea);
        double scaleY = rAxis.getScale(dataArea);
       
        double x3 = 0.0;
        //  double x2 ;
        if (lineType == IntersectingLineType.DUTY_POINT_LINE) {
            
            for (int i = 0; i < 20; i++) {
                x3 = (x1 + x2) / 2.0;
                if (function.getYValue(x3) < (k * x3)) {
                    x2 = x3;
                } else {
                    x1 = x3;
                }
            }
            double x1InJava2D = SpaceConverter.convertFromUserToJava2D(x1In, scaleX, dataArea, RectangleEdge.LEFT);
            double y1InJava2D = SpaceConverter.convertFromUserToJava2D(y1In, scaleY, dataArea, RectangleEdge.BOTTOM);
           double x2InJava2D = SpaceConverter.convertFromUserToJava2D(x2In, scaleX, dataArea, RectangleEdge.LEFT);
            double y2InJava2D = SpaceConverter.convertFromUserToJava2D(y2In, scaleY, dataArea, RectangleEdge.BOTTOM);
            
            double x3Java2D =SpaceConverter.convertFromUserToJava2D(x3,scaleX,  dataArea, RectangleEdge.LEFT);
            double y3Java2D = SpaceConverter.convertFromUserToJava2D(function.getYValue(x3),scaleY,dataArea, RectangleEdge.BOTTOM);
            
            g2D.draw(new Line2D.Double(x1InJava2D, y1InJava2D,x3Java2D,y3Java2D));
           // g2D.draw(new Line2D.Double(x1InJava2D, y1InJava2D,x2InJava2D,y2InJava2D));
        } else if (lineType == IntersectingLineType.HEAD_RANGE_MARKER) {
            for (int i = 0; i < 20; i++) {
                x3 = (x1 + x2) / 2.0;
                if (function.getYValue(x3) < y1In) {
                    x2 = x3;
                } else {
                    x1 = x3;
                }
            }
            double x1InJava2D = SpaceConverter.convertFromUserToJava2D(x1In, scaleX, dataArea, RectangleEdge.LEFT);
            double y1InJava2D = SpaceConverter.convertFromUserToJava2D(y1In, scaleY, dataArea, RectangleEdge.BOTTOM);
            double x3Java2D =SpaceConverter.convertFromUserToJava2D(x3,scaleX,  dataArea, RectangleEdge.LEFT);
            double zeroYJava2D=SpaceConverter.convertFromUserToJava2D(0,scaleY,dataArea,RectangleEdge.BOTTOM);
            g2D.draw(new Line2D.Double(x1InJava2D, y1InJava2D, x3Java2D, y1InJava2D));
            g2D.draw(new Line2D.Double(x3Java2D, y1InJava2D, x3Java2D,zeroYJava2D));
        }
        return x3;
    }

    public TestResult getResultWithTol(PumpValues declaredValues, PumpValues obsValues, double headTol, double dischTol) {
        TestResult result = TestResult.FAIL;
        double deltaH = declaredValues.getHead() - obsValues.getHead();
        double deltaQ = declaredValues.getDischarge() - obsValues.getDischarge();
        double value = StrictMath.pow(declaredValues.getHead() * headTol / deltaH, 2.0) + StrictMath.pow(declaredValues.getDischarge() * dischTol / deltaQ, 2.0);
        if (value >= 1.0) {
            result = TestResult.PASS;
        }
        System.out.println(" value " + value);
        return result;

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
