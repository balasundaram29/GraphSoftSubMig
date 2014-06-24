import java.util.*;
/**
 * Dataset  contains the  (x,y) pair values as an ArrayList.We can add new pair values to an existing 
 * Dataset  also.
 * @bala 
 * @3rd March 2011
 */
public class Dataset
{
    private DomainAxis domainAxis;
    private RangeAxis rangeAxis;
    private XYPair xyPair;
    private double[] xArray;//= new double[100];
    private double[] yArray;//= new double[100];
    private ArrayList<XYPair>dataList = new ArrayList<XYPair>();
    public DatasetAndCurveType type;
    /**
     * Constructor for objects of class Dataset
     */
    public Dataset()
    {
    }    

    public Dataset(double[] array1, double[] array2,DatasetAndCurveType type)
    {
        for(int i=0;i<array1.length;i++)
        {
            xyPair=new XYPair(array1[i],array2[i]);
            dataList.add(xyPair);
        }
        Collections.sort(dataList);

        this.type = type;
    }

    protected void setRangeAxis(RangeAxis rangeAxis)
    {
        this.rangeAxis=rangeAxis;
    }    
    protected void setDomainAxis(DomainAxis domainAxis)
    {
        this.domainAxis=domainAxis;
    }    
    public DatasetAndCurveType getType()
    {
        return this.type;
    }
    public RangeAxis getRangeAxis()
    {
        return rangeAxis;
    }    
    public DomainAxis getDomainAxis()
    {
        return domainAxis;
    }    
    
    
    public void  add(double x, double y)
    {
        xyPair=new XYPair(x,y);
        dataList.add(xyPair);
        Collections.sort(dataList);
    }    

    public XYPair getXYPair(int index)
    {
        xyPair=dataList.get(index);
        return xyPair;
    }    

    public double getXValue(int index)
    {
        xyPair=dataList.get(index);
        return xyPair.getX();
    }

    public double getYValue(int index)
    {
        xyPair=dataList.get(index);
        return xyPair.getY();
    }

    public int getSize()
    {
        return dataList.size();
    }    

    /**
     *  Since Dataset is actually a collection of points, you may need to get only 
     *  the X values. The returned array is the array of X-values in the dataset.
     *  @return An Array containing the X-Values of the Dataset. 
     */
    public double[] getXArray()
    {
        xArray = new double[getSize()]; 
        for(int i=0;i<getSize();i++)
        {
            xArray[i]=getXYPair(i).getX();
        }
        return xArray;
    }
/**
     *  Since Dataset is actually a collection of points, you may need to get only
     *  the Y values. The returned array is the array of Y-values in the dataset.
     *  @return An Array containing the Y-Values of the Dataset.
     */
    public double[] getYArray()
    {  
        yArray = new double[getSize()]; 
        for(int i=0;i<getSize();i++)
        {
            yArray[i]=getXYPair(i).getY();
        }
        return yArray;
    }
}    