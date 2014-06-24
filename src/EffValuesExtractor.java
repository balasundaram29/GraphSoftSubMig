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


public class EffValuesExtractor extends Extractor{

    Drawable function;
    private Stroke stroke = new BasicStroke(1.0f);
    Paint paint = Color.red;
private Paint linePaint = Color.red;
    public EffValuesExtractor(Drawable function) {
        this.function = function;
    }

    public PumpValues drawLinesAndExtractValues(Graphics2D g2D,Rectangle2D dataArea,PumpValues declaredValues,PumpValues obsValues) {
       
        Dataset dataset = function.getDataset();
        double obsDisch = obsValues.getDischarge();
        double obsEff = function.getYValue(obsDisch);
        obsValues.setEfficiency(obsEff);
        obsValues.setEffResult(obsEff>=declaredValues.getEfficiency() ? TestResult.PASS : TestResult.FAIL);
        RangeAxis rAxis = dataset.getRangeAxis();
        DomainAxis dAxis = dataset.getDomainAxis();
        double scaleX = dAxis.getScale(dataArea);
        double scaleY = rAxis.getScale(dataArea);
        double xUser = obsDisch;
        double yUser = obsEff;
        double xJava2D = SpaceConverter.convertFromUserToJava2D(xUser, scaleX, dataArea, RectangleEdge.LEFT);
        double yJava2D = SpaceConverter.convertFromUserToJava2D(yUser, scaleY, dataArea, RectangleEdge.BOTTOM);
        Shape effMarker = ShapeUtilities.createTranslatedShape(ShapeUtilities.createUpVMarkNarrow(10.0f), xJava2D, yJava2D);
        g2D.setPaint(linePaint);
        g2D.setStroke(stroke);
        g2D.draw(effMarker);
        return obsValues;
    }
public double drawIntersectingLine(Graphics2D g2D, double x1In, double y1In, double x2In, double y2In, IntersectingLineType lineType) {
        return 0;
    }

    /**
     * @param stroke the stroke to set
     */
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    /**
     * @param linePaint the linePaint to set
     */
    public void setLinePaint(Paint linePaint) {
        this.linePaint = linePaint;
    }



}
