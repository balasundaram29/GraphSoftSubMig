
import javax.swing.JPanel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bala
 */
public class GraphPanelCreator  {
    ReportPanel rPanel;

    public GraphPanelCreator(ReportPanel rPanel){
    this.rPanel=rPanel;
}

    JPanel getGraphWithResultPanel(){
      return new JPanel();
    }

}
