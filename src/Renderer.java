import java.awt.*;
import java.awt.geom.*;

public abstract class Renderer
{
    Dataset dataset;
    private Plot plot;
    private Paint curvePaint;
    private Stroke stroke;
    public abstract void drawCurve(Graphics2D  g2D,Rectangle2D dataArea);
       
    

    void setPlot(Plot plot) {
        this.plot= plot;

    }
    public abstract Dataset getDataset() ;
        
    public abstract Drawable getFunction();

    /**
     * @return the curvePaint
     */
    public Paint getCurvePaint() {
        return curvePaint;
    }

    /**
     * @param curvePaint the curvePaint to set
     */
    public void setCurvePaint(Paint curvePaint) {
        this.curvePaint = curvePaint;
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
    public abstract void setStroke(Stroke stroke) ;
}
