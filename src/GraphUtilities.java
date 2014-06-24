
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.*;

public class GraphUtilities {

    public GraphUtilities() {
    }

    public static double getMax(double[] array) {
        double max = array[0];
        for (double member : array) {
            if (max < member) {
                max = member;
            }
        }
        return max;
    }

    public static double getScaleY(Dataset dataset, Rectangle2D dataArea) {

        double yMax = GraphUtilities.getMax(dataset.getYArray());

        double scaleY = dataArea.getHeight() / (1.20 * yMax);
        return scaleY;
    }

    public static double getScaleX(Dataset dataset, Rectangle2D dataArea) {
        double xMax = GraphUtilities.getMax(dataset.getXArray());

        double scaleX = dataArea.getWidth() / (1.20 * xMax);

        return scaleX;
    }
}
