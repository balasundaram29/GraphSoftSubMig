
import java.awt.*;
import java.awt.geom.*;

/**
 * Write a description of class DomainAxis here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DomainAxis extends Axis {

    private double y, xRight, xLeft, scale;
    String label;
    private ScaleSelectionMode scaleMode;
    private AxisPosition axisPosition;
    private Dataset dataset;
    private Color DEFAULT_AXIS_LINE_COLOR = Color.black;
    private Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 10);
    private Font font;
    private ScaleSelectionMode DEFAULT_SCALE_MODE = ScaleSelectionMode.CALCULATE_FROM_DATASET;
    Color axisLineColor;
    private Stroke stroke;
    private Stroke DEFAULT_AXIS_LINE_STROKE = new BasicStroke(1.5f);
    private double maxAxisValue;

    public DomainAxis(String label) {
        this.label = label;
        this.axisLineColor = this.DEFAULT_AXIS_LINE_COLOR;
        this.scaleMode = this.DEFAULT_SCALE_MODE;
        this.maxAxisValue = 0.0;
        this.font = this.DEFAULT_FONT;
        this.stroke = this.DEFAULT_AXIS_LINE_STROKE;
    }

    public ScaleSelectionMode getScaleSelectionMode() {
        return scaleMode;
    }

    public void setScaleSelectionMode(ScaleSelectionMode scaleMode) {
        this.scaleMode = scaleMode;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
        dataset.setDomainAxis(this);
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getScale(Rectangle2D dataArea) {

        double scaleX;
        scaleMode = ScaleSelectionMode.MANUAL;
        if (this.maxAxisValue == 0.0) {
            scaleMode = ScaleSelectionMode.CALCULATE_FROM_DATASET;
        }
        if (scaleMode == ScaleSelectionMode.MANUAL) {
            scaleX = (xRight - xLeft) / maxAxisValue;
        } else {
            scaleX = GraphUtilities.getScaleX(dataset, dataArea);
        }
        // scale=(scaleY*-1.0);
        return scaleX;




    }

    public void setAxisLineColor(Color color) {
        this.axisLineColor = color;
    }

    /**
     * Set the vertical position of the axis
     * @param y  The Y value of the axis 
     */
    public void setY(double y) {
        this.y = y;

    }

    /**
     * Set the rightmost point of the axis.
     * @param xRight  the rightmost point of the axis.
     */
    public void setXRight(double xRight) {
        this.xRight = xRight;
    }

    /**
     * Set the leftmost point of the axis.
     * @param xLeft  the leftmost  point of the axis.
     */
    public void setXLeft(double xLeft) {
        this.xLeft = xLeft;
    }

    public void drawTickMarks(Graphics2D g2, Rectangle2D dataArea) {
        //double tScale;
        //if (scaleMode == ScaleSelectionMode.CALCULATE_FROM_DATASET) tScale = GraphUtilities.getScaleX(dataset,dataArea);
        // else tScale = scale;
        double tScale = this.getScale(dataArea);
        double xIter = dataArea.getMaxX();//xRight;
        double yPos;
        yPos = y + 10.0;
        double increment = dataArea.getWidth()/10.0;//(xRight - xLeft) / 10.0;
        g2.setPaint(axisLineColor);
        g2.setFont(font);

        synchronized (this) {
            while (xIter > dataArea.getMinX()) {
                float val = (float) ((xIter - dataArea.getMinX()) / tScale);//(float) ((xIter - xLeft) / tScale);
                String s = String.format("%,.2f", val);
                String tickString = Integer.toString((int) ((xIter - xLeft) / tScale));
                g2.drawString(s, (int) xIter, (int) yPos);
               // xIter -= increment;

                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("Discharge , lps", (int) xRight - 2 * fm.stringWidth("Discharge , lps") - 5, (int) y + 2 * fm.getHeight());
                double tml = 5;
                g2.draw(new Line2D.Double(xIter, y, xIter, y - tml));
                 xIter -= increment;
            }
        }
    }

    public void drawAxis(Graphics2D gc) {
        gc.setColor(axisLineColor);
        gc.draw(new Line2D.Double(xLeft, y, xRight, y));
        gc.drawPolygon(new Polygon(new int[]{(int) xRight - 20, (int) xRight, (int) xRight - 20}, new int[]{(int) y - 3, (int) y, (int) y + 3}, 3));
    }

    /**
     * @param maxAxisValue the maxAxisValue to set
     */
    public void setMaxAxisValue(double maxAxisValue) {
        this.maxAxisValue = maxAxisValue;
    }

    /**
     * @return the font
     */
    public Font getFont() {
        return font;
    }

    /**
     * @param font the font to set
     */
    public void setFont(Font font) {
        this.font = font;
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
}
