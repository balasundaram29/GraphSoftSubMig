
import java.awt.Paint;
import java.awt.Stroke;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public abstract class Extractor {
   private Stroke stroke;
    private Paint linePaint;

    /**
     * @param stroke the stroke to set
     */
    public abstract void setStroke(Stroke stroke);

    /**
     * @param linePaint the linePaint to set
     */
    public abstract void setLinePaint(Paint linePaint);
}
