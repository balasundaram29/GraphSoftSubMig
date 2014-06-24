
import java.awt.geom.Rectangle2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author admin
 */
public class SpaceConverter {

    private double scaleX;
    private double scaleY;

    /**
     * @return the scaleX
     */
    public double getScaleX() {
        return scaleX;
    }

    /**
     * @param scaleX the scaleX to set
     */
    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    /**
     * @return the scaleY
     */
    public double getScaleY() {
        return scaleY;
    }

    /**
     * @param scaleY the scaleY to set
     */
    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public static double convertFromUserToJava2D(double user2DValue, double java2DDistanceDividedByUserDistance, Rectangle2D dataArea, RectangleEdge edge) {
        double scale = java2DDistanceDividedByUserDistance;

        double java2DValue;
        if (edge == RectangleEdge.LEFT) {
            java2DValue = (user2DValue * scale) + dataArea.getMinX();
        } else {
            java2DValue = dataArea.getMaxY() - (user2DValue * scale);
        }
        return java2DValue;
    }

  public static double convertFromJava2DToUser2D(double java2DValue, double java2DDistanceDividedByUserDistance, Rectangle2D dataArea, RectangleEdge edge) {
        double scale = java2DDistanceDividedByUserDistance;

        double user2DValue;
        if (edge == RectangleEdge.LEFT) {
            user2DValue = (java2DValue-dataArea.getMinX())/scale;
            //java2DValue = (user2DValue * scale) + dataArea.getMinX();
        } else {
            user2DValue = (dataArea.getMaxY()-java2DValue)/scale;
            //java2DValue = dataArea.getMaxY() - (user2DValue * scale);
        }
        return user2DValue;
    }








}
