
/**
 * Write a description of class Axis here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Axis
{
    Dataset dataset;
    String  axis_label;
    /**
     * Constructor for objects of class Axis
     */
    public Axis(){}
    public Axis(String axis_label)
    {
        this.axis_label=axis_label;
    }
    protected void setDataset(Dataset dataset)
    {
        this.dataset = dataset;
    }    
}
