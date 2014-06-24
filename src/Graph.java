import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Write a description of class Graph here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Graph
{
    Plot plot;
    Rectangle2D.Double plotArea,dataArea;
    /**
     * Constructor for objects of class Graph
     */
    public Graph(String title, String domain_axis_label,String range_axis_label,Dataset dataset)
    {
        this.plot=new Plot(domain_axis_label,range_axis_label,dataset);
    }
    public Plot getPlot()
    {
        return this.plot;
    }    
    public Rectangle2D drawGraph(Graphics2D gc,Rectangle2D plotArea)
    {
        Rectangle2D dataArea = plot.drawAxesAndCurves(gc,plotArea);
        return dataArea;
    }    
    
    
}
