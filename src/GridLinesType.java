
/**
 * Write a description of class GridLinesType here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GridLinesType
{
    String name;
    
    public static GridLinesType  NO_GRID_NEEDED =    new GridLinesType("No Grid Needed");
    public static GridLinesType  WIDE_GRID_SPACING = new GridLinesType("High Grid Spacing");
    public static GridLinesType  MEDIUM_GRID_SPACING=new GridLinesType("Medium Grid Spacing");
    public static GridLinesType  NARROW_GRID_SPACING  = new GridLinesType("Low Grid Spacing");
    private GridLinesType(String name)
    {
        this.name = name;
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
   public boolean equals(Object obj)
    {
        if(obj==this)
            {return true;}
        else if( ! (obj instanceof GridLinesType))
            {return false;}
        if (!((this.name).equals(((GridLinesType)obj).name)))
            {return false;} 
        return true;
    }    
}
