
/**
 * The most simple and fundamental class of the PumpGraph project.
 * Constructs a single pair of values(x,y) array and can get individual x,y values.
 * @bala 
 * @3rd March 2011
 */
public class XYPair implements Comparable<XYPair>
{
    private double x;
    private double y;
    
    public int  compareTo(XYPair xyP)
    {
       if (getX()<=xyP.getX()) return -1; else return 1;
    }   
        
    /**
     * Constructor for objects of class XYPair
     */
    public XYPair(double xin,double yin)
    {
       x = xin; 
       y = yin;
    }

    /**
     * This method is to get the x value of the XYPair.
    
     * @return   The x value of XYPair. 
     */
    public double getX()
    {
        return x;
    }

     /**
     * This method is to get the y value of the XYPair.
    
     * @return   The y value of XYPair. 
     */
    public double getY()
    {
        return y;
    }

}
