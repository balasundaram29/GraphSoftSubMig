
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.*;//Rectangle2D;

/**
 * Write a description of class Plot here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Plot {

    private PumpValues declaredValues, obsValues;
    private GridLinesType gridLinesType;
    private ArrayList<RangeAxis> rangeAxesList = new ArrayList<RangeAxis>();
    private ArrayList<Dataset> datasetList = new ArrayList<Dataset>();
    private ArrayList<Renderer> rendererList = new ArrayList<Renderer>();
    private ArrayList<GraphDrawListener> listenerList = new ArrayList<GraphDrawListener>();
    private int DEFAULT_BORDER_SPACING = 10;
    private GridLinesType DEFAULT_GRID_LINES_TYPE = GridLinesType.WIDE_GRID_SPACING;
    private int LEFT_MARGIN = 15;
    private int RIGHT_MARGIN = 0;
    private int TOP_MARGIN = 0;
    private int BOTTOM_MARGIN = 15;
    private int GAP_BETWEEN_RANGE_AXES=20;
    DomainAxis domainAxis;
    private int rangeAxisIndex = 0;
    private HeadValuesExtractor headExtractor;
    private EffValuesExtractor effExtractor;
    private CurrValuesExtractor currExtractor;
    private Color hExrColor = Color.red;
    private Stroke hExrStroke = new BasicStroke(1.0f);
    private Color eExrColor = Color.red;
    private Stroke eExrStroke = new BasicStroke(1.0f);
    private Color cExrColor = Color.red;
    private Stroke cExrStroke = new BasicStroke(1.0f);

    public Plot(String domain_axis_label, String range_axis_label, Dataset dataset) {

        this.datasetList.add(dataset);

        domainAxis = new DomainAxis(domain_axis_label);

        RangeAxis range_axis = new RangeAxis(range_axis_label, AxisPosition.LEFT);
        range_axis.setDataset(dataset);
        this.rangeAxesList.add(range_axis);
        this.gridLinesType = DEFAULT_GRID_LINES_TYPE;
        //  XYLineRenderer renderer = new XYLineRenderer();
        // renderer.setDataset(dataset);
        //this.rendererList.add(renderer);
        // renderer.setPlot(this);

    }

    public Rectangle2D drawAxesAndCurves(Graphics2D gc, Rectangle2D plotArea) {

        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        gc.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        Rectangle2D dataArea = drawAxesAndGetDataArea(gc, plotArea);

        GridLineUtilities.drawGridLines(this, gc, dataArea, gridLinesType);
        gc.setColor(Color.blue);
        domainAxis.drawTickMarks(gc, dataArea);
        for (RangeAxis rangeAxis : rangeAxesList) //{
        {

            rangeAxis.drawTickMarks(gc, dataArea);
        }

        int i = 0;
        for (Renderer renderer : getRendererList()) {
            renderer.drawCurve(gc, dataArea);
        }

        obsValues = new PumpValues();
        obsValues.setsNo(declaredValues.getsNo());
        obsValues.setDate(declaredValues.getDate());
        obsValues.setType(declaredValues.getType());
        Renderer headRenderer = this.getRendererByDatasetType(DatasetAndCurveType.DISCHARGE_VS_HEAD);
        headExtractor = new HeadValuesExtractor(headRenderer.getFunction());
        headExtractor.setLinePaint(hExrColor);
        headExtractor.setStroke(hExrStroke);
                obsValues = headExtractor.drawLinesAndExtractValues(gc, dataArea, declaredValues, obsValues);

        Renderer effRenderer = this.getRendererByDatasetType(DatasetAndCurveType.DISCHARGE_VS_EFFICIENCY);
        effExtractor = new EffValuesExtractor(effRenderer.getFunction());
         effExtractor.setLinePaint(eExrColor);
        effExtractor.setStroke(eExrStroke);

        obsValues = effExtractor.drawLinesAndExtractValues(gc, dataArea, declaredValues, obsValues);

        Renderer currRenderer = this.getRendererByDatasetType(DatasetAndCurveType.DISCHARGE_VS_CURRENT);
        currExtractor = new CurrValuesExtractor(currRenderer.getFunction());
         currExtractor.setLinePaint(cExrColor);
        currExtractor.setStroke(cExrStroke);
        obsValues = currExtractor.drawLinesAndExtractValues(gc, dataArea, declaredValues, obsValues);



        this.fireGraphDrawn();
        return dataArea;
    }

    public Rectangle2D drawAxesAndGetDataArea(Graphics2D gc, Rectangle2D plotArea) {

        double bottomLeft = plotArea.getMaxX();
        double yBottom = plotArea.getMaxY() -BOTTOM_MARGIN;
        double yTop = plotArea.getMinY() +  TOP_MARGIN;
        double xLeft = plotArea.getMinX()+LEFT_MARGIN;
        double xRight = plotArea.getMaxX() - RIGHT_MARGIN;
        for (RangeAxis rangeAxis : rangeAxesList) {
            if (rangeAxis.axisPosition == AxisPosition.LEFT) {
                
                rangeAxis.setX(xLeft);
                xLeft = xLeft + GAP_BETWEEN_RANGE_AXES;
                rangeAxis.setYTop(yTop);
                rangeAxis.setYBottom(yBottom);
            } else {
                xRight = xRight - getMARGIN();
                rangeAxis.setX(xRight);
                rangeAxis.setYTop(yTop);
                rangeAxis.setYBottom(yBottom);
            }
            rangeAxis.drawAxis(gc);
        }
        domainAxis.setY(yBottom);
        xLeft=xLeft-GAP_BETWEEN_RANGE_AXES;
        domainAxis.setXLeft(xLeft);
        domainAxis.setXRight(xRight);
        Rectangle2D dataArea = new Rectangle2D.Double(xLeft, yTop, xRight - xLeft, yBottom - yTop);
        domainAxis.drawAxis(gc);
        return dataArea;
    }

    public ArrayList<RangeAxis> getRangeAxesList() {
        return rangeAxesList;
    }

    public void setRenderer(int index, Renderer renderer) {
        if (index >= getRendererList().size()) {
            this.getRendererList().add(renderer);
        } else {
            this.getRendererList().set(index, renderer);
        }
    }

    public void setRangeAxis(int index, RangeAxis axis) {
        if (index < this.rangeAxesList.size()) {
            this.rangeAxesList.set(index, axis);
        } else {
            this.rangeAxesList.add(axis);
        }
    }

    public void addGraphDrawListener(GraphDrawListener listener) {
        listenerList.add(listener);
    }

    public void fireGraphDrawn() {
        GraphDrawEvent event = new GraphDrawEvent(this);
        for (GraphDrawListener listener : listenerList) {
            listener.graphDrawn(event);
        }
    }

    public void setDataset(int index, Dataset set) {
        this.datasetList.set(index, set);
    }

    public Renderer getRenderer(int index) {
        return getRendererList().get(index);
    }

    public Renderer getRendererByDatasetType(DatasetAndCurveType type) {
        for (Renderer renderer : getRendererList()) {
            if (renderer.getDataset().getType() == type) {
                return renderer;
            }
        }
        return null;
    }

    public void setDeclaredValues(PumpValues declaredValues) {
        this.declaredValues = declaredValues;
    }

    public PumpValues getObsValues() {
        return this.obsValues;
    }

    public RangeAxis getRangeAxis(int index) {
        return rangeAxesList.get(index);
    }

    public DomainAxis getDomainAxis() {
        return this.domainAxis;
    }

    /**
     * @return the rendererList
     */
    public ArrayList<Renderer> getRendererList() {
        return rendererList;
    }

    /**
     * @return the MARGIN
     */
    public int getMARGIN() {
        return 0;//MARGIN;
    }

    /**
     * @param MARGIN the MARGIN to set
     */
    public void setMARGIN(int MARGIN) {
       // this.MARGIN = MARGIN;
    }

    /**
     * @return the gridLinesType
     */
    public GridLinesType getGridLinesType() {
        return gridLinesType;
    }

    /**
     * @param gridLinesType the gridLinesType to set
     */
    public void setGridLinesType(GridLinesType gridLinesType) {
        this.gridLinesType = gridLinesType;
    }

    /**
     * @return the headExtractor
     */
    public HeadValuesExtractor getHeadExtractor() {
        return headExtractor;
    }

    /**
     * @return the effExtractor
     */
    public EffValuesExtractor getEffExtractor() {
        return effExtractor;
    }

    /**
     * @return the currExtractor
     */
    public CurrValuesExtractor getCurrExtractor() {
        return currExtractor;
    }

    /**
     * @param hExrColor the hExrColor to set
     */
    public void sethExrColor(Color hExrColor) {
        this.hExrColor = hExrColor;
    }

    /**
     * @param hExrStroke the hExrStroke to set
     */
    public void sethExrStroke(Stroke hExrStroke) {
        this.hExrStroke = hExrStroke;
    }

    /**
     * @param eExrColor the eExrColor to set
     */
    public void seteExrColor(Color eExrColor) {
        this.eExrColor = eExrColor;
    }

    /**
     * @param eExrStroke the eExrStroke to set
     */
    public void seteExrStroke(Stroke eExrStroke) {
        this.eExrStroke = eExrStroke;
    }

    /**
     * @param cExrColor the cExrColor to set
     */
    public void setcExrColor(Color cExrColor) {
        this.cExrColor = cExrColor;
    }

    /**
     * @param cExrStroke the cExrStroke to set
     */
    public void setcExrStroke(Stroke cExrStroke) {
        this.cExrStroke = cExrStroke;
    }
}
