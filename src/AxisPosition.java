
/**
 * AxisPosition is , for the users of this class , a descriptor for axis position right or left .
 * 
 * @bala 
 * @4th March 2011
 */
public class AxisPosition
{
 
    public String name;
     /**
     * Constructor for objects of class AxisPosition
     */
    public static AxisPosition LEFT = new AxisPosition("AxisPostion.LEFT");
    
    public static AxisPosition RIGHT = new AxisPosition("AxisPostion.RIGHT");
    
    private AxisPosition(String name)
    {
        this.name = name;
    }
   
    
    public String toString()
    {
        return name;
    }
    public boolean equals(Object obj)
    {
        if(obj==this)
            {return true;}
        else if( ! (obj instanceof AxisPosition) )
            {return false;}
        if (!((this.name).equals(((AxisPosition)obj).name)))
            {return false;} 
        return true;
    }    

}
