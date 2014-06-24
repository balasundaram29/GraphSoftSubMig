
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import javax.swing.RepaintManager;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bala
 */
public class GraphPrintUtility implements Printable{
 ReadingEntryPanel entryPanel; 
    
    public GraphPrintUtility(ReadingEntryPanel entryPanel){
        this.entryPanel=entryPanel;
    }
    

public int print(Graphics g, PageFormat pageFormat, int pageIndex){
    if (pageIndex > 0) {
      return(NO_SUCH_PAGE);
    } else {
      Graphics2D g2d = (Graphics2D)g;
      g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
      double prnWidth = pageFormat.getImageableWidth();
      double prnHeight= pageFormat.getImageableHeight();
      int pW = (int)prnWidth;
      int pH = (int)prnHeight;
      GraphPanel componentToBePrinted = new GraphPanel(new ReportPanel(entryPanel,pW,pH),pW,pH);
      disableDoubleBuffering(componentToBePrinted);
      componentToBePrinted.paint(g2d);
      enableDoubleBuffering(componentToBePrinted);
      return(PAGE_EXISTS);
    }
}
  public static void disableDoubleBuffering(Component c) {
    RepaintManager currentManager = RepaintManager.currentManager(c);
    currentManager.setDoubleBufferingEnabled(false);
  }

  public static void enableDoubleBuffering(Component c) {
    RepaintManager currentManager = RepaintManager.currentManager(c);
    currentManager.setDoubleBufferingEnabled(true);
  }
}
