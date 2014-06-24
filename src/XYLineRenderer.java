
import java.awt.*;
import java.awt.geom.*;

/**
 * Write a description of class XYLineRenderer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class XYLineRenderer extends Renderer {

    Dataset dataset;
    Drawable lowessFunction;
    public XYLineRenderer() {
    }

    public void drawCurve(Graphics2D g2, Rectangle2D dataArea){
        AffineTransform tfm = new AffineTransform();
        tfm.translate(dataArea.getMinX(), dataArea.getMaxY());
        //double xMax = GraphUtilities.getMax(dataset.getXArray());
        //double yMax = GraphUtilities.getMax(dataset.getYArray());
        //double scaleX=dataArea.getWidth()/(1.20*xMax);
        //double scaleY=(-1.0)*dataArea.getHeight()/(1.20*yMax);
        Dataset dataset = this.getDataset();
        RangeAxis rAxis = dataset.getRangeAxis();
        DomainAxis dAxis = dataset.getDomainAxis();
        double scaleX = 0.0;
        double scaleY = 0.0;
        if (dAxis.getScaleSelectionMode() == ScaleSelectionMode.MANUAL) {
            scaleX = dAxis.getScale(dataArea);
        } else {
            scaleX = GraphUtilities.getScaleX(dataset, dataArea);
        }
        if (rAxis.getScaleSelectionMode() == ScaleSelectionMode.MANUAL) {
            scaleY = rAxis.getScale(dataArea);
        } else {
            scaleY = GraphUtilities.getScaleY(dataset, dataArea);
        }

        tfm.scale(scaleX, scaleY);
        g2.setTransform(tfm);
        try {
            g2.setStroke(new TransformedStroke(new BasicStroke(2.0f),
                    g2.getTransform()));
        } catch (NoninvertibleTransformException ex) {
            // should not occur if width and height > 0
            ex.printStackTrace();
        }
        //g2.setStroke(new BasicStroke(.01f));
        for (int i = 0; i < dataset.getSize() - 1; i++) {
            g2.draw(new Line2D.Double(dataset.getXValue(i), dataset.getYValue(i), dataset.getXValue(i + 1), dataset.getYValue(i + 1)));
        }
    }

    public void setDataset(Dataset set) {
        this.dataset = set;
    }

    public Dataset getDataset() {
        return this.dataset;
    }

     public Drawable getFunction(){
        return this.lowessFunction;
    }

    @Override
    public void setStroke(Stroke stroke) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
