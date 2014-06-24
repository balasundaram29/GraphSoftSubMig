
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.*;

public class PlotUtilities {

    

    private PlotUtilities() {
    }

    public static void setScale(Graphics2D g2D, Dataset dataset,Rectangle2D dataArea) {
        AffineTransform tfm = new AffineTransform();
        AffineTransform saved = g2D.getTransform();
        // tfm.translate(dataArea.getMinX(), dataArea.getMaxY());
       g2D.translate(dataArea.getMinX(), dataArea.getMaxY());
        RangeAxis rAxis = dataset.getRangeAxis();
        DomainAxis dAxis = dataset.getDomainAxis();
        double scaleX = 1.0;
        double scaleY = 1.0;
        if (dAxis.getScaleSelectionMode() == ScaleSelectionMode.MANUAL) {
            scaleX = dAxis.getScale(dataArea);
        } else {
            scaleX = GraphUtilities.getScaleX(dataset, dataArea);
        }
        if (rAxis.getScaleSelectionMode() == ScaleSelectionMode.MANUAL) {
            scaleY = rAxis.getScale(dataArea);
            scaleY=(-1.0)*scaleY;
        } else {
            scaleY = GraphUtilities.getScaleY(dataset, dataArea);

        }

        //tfm.scale(scaleX, scaleY);
        g2D.scale(scaleX, scaleY);
        //g2D.setTransform(tfm);
        try {
            g2D.setStroke(new TransformedStroke(new BasicStroke(2.0f),
                    g2D.getTransform()));
        } catch (NoninvertibleTransformException ex) {
            // should not occur if width and height > 0
            ex.printStackTrace();
        }
    }
}
        