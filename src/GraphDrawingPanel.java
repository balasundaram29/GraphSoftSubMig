
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ANNAIENG
 */
public class GraphDrawingPanel extends JPanel implements MouseListener,MouseMotionListener{
    Graph graph;
   private  boolean tracing = false;
   private JFrame traceFrame;
   private Rectangle2D dataArea,plotArea;
    
    Cursor oldCursor;

JFrame f;
    public GraphDrawingPanel(Graph graph){
        this.graph = graph;
        setBackground(Color.white);
        addMouseListener(this);
        addMouseMotionListener(this);
        oldCursor = this.getCursor();
    }
    @Override
 public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        int w = this.getWidth();
        int h = this.getHeight();
        Insets insets = getInsets();
        plotArea = new Rectangle2D.Double(insets.left, insets.top, w - insets.left - insets.right, h - insets.top - insets.bottom);
       
        Color saved = g2D.getColor();
        g2D.setColor(Color.white);
        g2D.fill(plotArea);
        g2D.setColor(saved);
        Rectangle2D panelArea = new Rectangle2D.Double(0, 0, w, h);
      
          dataArea =graph.drawGraph(g2D, plotArea);

    }

    public void mouseClicked(MouseEvent e) {
         if (tracing) {
             traceFrame.dispose();
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

         }//already tracing and mouse is now clicked needing frame closure.
        Cursor cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
        this.setCursor(cursor);
         tracing = !tracing;
        traceFrame = new JFrame("Trace Values Against Discharge");
        traceFrame.setBounds((int)dataArea.getMinX()+10,(int)plotArea.getMinY()+40,
               (int)dataArea.getWidth()-10,(int)(dataArea.getMinY()-plotArea.getMinY()));
        traceFrame.getContentPane().setBackground(Color.white);
        traceFrame.setVisible(tracing);
        traceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mouseMoved(e);
    }

    public void mousePressed(MouseEvent e) {
    

    }

    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
        
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseDragged(MouseEvent e) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseMoved(MouseEvent e) {
        if (traceFrame==null)return;
       
       double x = e.getX();
       double y = e.getY();
       Plot plot = graph.getPlot();
       Dataset cSet = plot.getRendererByDatasetType(DatasetAndCurveType.DISCHARGE_VS_CURRENT).getDataset();
       double scaleX =cSet.getDomainAxis().getScale(dataArea);
       double disch = SpaceConverter.convertFromJava2DToUser2D(x, scaleX, dataArea, RectangleEdge.LEFT);
        Double maxValueX = GraphUtilities.getMax(cSet.getXArray());
        if (disch<0 ||disch>maxValueX) return;
       double scaleY = cSet.getRangeAxis().getScale(dataArea);
       Drawable func =  plot.getRendererByDatasetType(DatasetAndCurveType.DISCHARGE_VS_CURRENT).getFunction();//new LoessFunction(cSet);
       double current =func.getYValue(disch);

      Dataset hSet = plot.getRendererByDatasetType(DatasetAndCurveType.DISCHARGE_VS_HEAD).getDataset();
      scaleY = hSet.getRangeAxis().getScale(dataArea);
      //func = new LoessFunction(hSet);
       func =  plot.getRendererByDatasetType(DatasetAndCurveType.DISCHARGE_VS_HEAD).getFunction();
      double head =func.getYValue(disch);

       Dataset eSet = plot.getRendererByDatasetType(DatasetAndCurveType.DISCHARGE_VS_EFFICIENCY).getDataset();
      scaleY = eSet.getRangeAxis().getScale(dataArea);
      //func = new LoessFunction(eSet);
       func =  plot.getRendererByDatasetType(DatasetAndCurveType.DISCHARGE_VS_EFFICIENCY).getFunction();
      double eff =func.getYValue(disch);

   traceFrame.getContentPane().removeAll();
       JLabel l = new JLabel();
     //l.setFont(new Font(Font.SANS_SERIF,8,Font.PLAIN));
       l.setBackground(Color.white);
       traceFrame.getContentPane().add(l,BorderLayout.CENTER);
       String dischStr = String.format("%,.2f",disch);
       String currStr = String.format("%,.2f",current);
       String headStr = String.format("%,.2f",head);
       String effStr = String.format("%,.2f",eff);


       String s = "Disch =  "+dischStr+" lps ;    "+"Head  =  "+headStr+ "  m  ;       "+"Overall Eff.  " +effStr
               +"  % ;       " + " Current =  "+ currStr+ "  Amps ;";
       l.setText(s);
       traceFrame.validate();

    }

   
}
