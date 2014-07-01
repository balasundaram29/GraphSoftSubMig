
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.*;

public class GridLineUtilities {

    public static void drawGridLines(Plot plot, Graphics2D g2D, Rectangle2D dataArea, GridLinesType type) {
        Color lineColor = Color.darkGray;
        double xLeft = dataArea.getMinX();
        double xRight = dataArea.getMaxX();
        double yTop = dataArea.getMinY();
        double yBottom = dataArea.getMaxY();
        double verticalSpacing = (yBottom - yTop) / 10.0;
        double horizontalSpacing = (xRight - xLeft) / 10.0;
        double verticalLinePosition = xLeft;
        double horizontalLinePosition = yBottom;

        if (type == GridLinesType.NO_GRID_NEEDED) {
            return;
        }
        if (type == GridLinesType.WIDE_GRID_SPACING) {

            for (int i = 0; i < 11; i++) {

                if (!(i == 0)) //Don't draw over left  side and bottom axis;
                {
                    drawHorizontalLine(g2D, dataArea, horizontalLinePosition, lineColor);
                    // if (i == 10) //Don't draw over right side axis;
                    // {
                    //minus 2 because sytem does not draw at exactly border(isRightAxis(plot) == true) {
                    // break;

                    //}
                    drawVerticalLine(g2D, dataArea, verticalLinePosition, lineColor);
                }

                horizontalLinePosition = horizontalLinePosition - verticalSpacing;
                verticalLinePosition = verticalLinePosition + horizontalSpacing;
            }

        }
        lineColor = Color.lightGray;
        verticalLinePosition = xLeft;
        horizontalLinePosition = yBottom;
        if ((type == GridLinesType.MEDIUM_GRID_SPACING) ) {

            for (int i = 0; i < 50; i++) {

                if (!(i == 0)) {
                    if (!(i % 5 == 0))//Don't draw over left  side and bottom axis;
                    {

                        drawHorizontalLine(g2D, dataArea, horizontalLinePosition, lineColor);
                        drawVerticalLine(g2D, dataArea, verticalLinePosition, lineColor);

                    }
                }
                horizontalLinePosition = horizontalLinePosition - verticalSpacing / 5.0;
                verticalLinePosition = verticalLinePosition + horizontalSpacing / 5.0;
            }

        }
        lineColor = Color.lightGray;
        verticalLinePosition = xLeft;
        horizontalLinePosition = yBottom;
      
        if (type == GridLinesType.NARROW_GRID_SPACING) {
            double cur1 = horizontalLinePosition;
            double inc1 = verticalSpacing / 10.0;
            double inc2 = horizontalSpacing / 20.0;
            double cur2;
            for (int i = 0; i < 20; i++) {
                Line2D horizontalLine = new Line2D.Double(xLeft, horizontalLinePosition, xRight, horizontalLinePosition);
                Line2D verticalLine = new Line2D.Double(verticalLinePosition, yTop, verticalLinePosition, yBottom);
                // if (!(i == 0)) //Don't draw over left  side and bottom axis;
                //{
                //  if (i % 5 == 0) {
                //    lineColor=Color.orange;
                // }
                if (i % 10 == 0) {
                    lineColor = Color.darkGray;
                }
                drawHorizontalLine(g2D, dataArea, horizontalLinePosition, lineColor);
                cur1 = horizontalLinePosition;
                inc1 = verticalSpacing / 10.0;
                inc2 = horizontalSpacing / 20.0;

                for (int j = 0; j < 5; j++) {

                    if (j == 0) {
                        drawHorizontalTick(g2D, dataArea, cur1, Color.darkGray, 5);
                    } else {
                        drawHorizontalTick(g2D, dataArea, cur1, Color.darkGray, 2);
                    }
                    cur1 = cur1 - inc1;
                }
                drawVerticalLine(g2D, dataArea, verticalLinePosition, lineColor);
                cur2 = verticalLinePosition;
                for (int j = 0; j < 5; j++) {

                    if (j == 0) {
                        drawVerticalTick(g2D, dataArea, cur2, Color.darkGray, 5);
                    } else {
                        drawVerticalTick(g2D, dataArea, cur2, Color.darkGray, 2);
                    }
                    cur2 = cur2 + inc2;
                }

                verticalLinePosition = verticalLinePosition + horizontalSpacing / 4.0;
                drawVerticalLine(g2D, dataArea, verticalLinePosition, lineColor);
                cur2 = verticalLinePosition;
                for (int j = 0; j < 5; j++) {

                    if (j == 0) {
                        drawVerticalTick(g2D, dataArea, cur2, Color.darkGray, 5);
                    } else {
                        drawVerticalTick(g2D, dataArea, cur2, Color.darkGray, 2);
                    }
                    cur2 = cur2 + inc2;
                }
                //g2D.draw(horizontalLine);
                //g2D.draw(verticalLine);
                lineColor = Color.darkGray;
                // }
                horizontalLinePosition = horizontalLinePosition - verticalSpacing / 2.0;
                verticalLinePosition = verticalLinePosition + horizontalSpacing / 4.0;
            }
            drawVerticalLine(g2D, dataArea, verticalLinePosition, lineColor);
            cur2 = verticalLinePosition;
            for (int j = 0; j < 5; j++) {

                if (j == 0) {
                    drawVerticalTick(g2D, dataArea, cur2, Color.darkGray, 5);
                } else {
                    drawVerticalTick(g2D, dataArea, cur2, Color.darkGray, 2);
                }
                cur2 = cur2 + inc2;
            }
            drawVerticalLine(g2D, dataArea, dataArea.getMinX(), Color.black);
            drawHorizontalLine(g2D, dataArea, dataArea.getMaxY(), Color.black);
            drawHorizontalLine(g2D, dataArea, dataArea.getMinY(), Color.black);
            drawVerticalLine(g2D, dataArea, dataArea.getMaxX(), Color.black);
            return;
        }
    }

    public static void drawHorizontalLine(Graphics2D g2D, Rectangle2D dataArea, double cursor, Color lineColor) {
        g2D.setColor(lineColor);
        g2D.draw(new Line2D.Double(dataArea.getMinX(), cursor, dataArea.getMaxX(), cursor));
    }

    public static void drawHorizontalTick(Graphics2D g2D, Rectangle2D dataArea, double cursor, Color lineColor, int length) {
        g2D.setColor(lineColor);
        g2D.draw(new Line2D.Double(dataArea.getMinX(), cursor, dataArea.getMinX() + length, cursor));
    }

    public static void drawVerticalLine(Graphics2D g2D, Rectangle2D dataArea, double cursor, Color lineColor) {
        g2D.setColor(lineColor);
        g2D.draw(new Line2D.Double(cursor, dataArea.getMinY(), cursor, dataArea.getMaxY()));
    }

    public static void drawVerticalTick(Graphics2D g2D, Rectangle2D dataArea, double cursor, Color lineColor, int length) {
        g2D.setColor(lineColor);
        g2D.draw(new Line2D.Double(cursor, dataArea.getMaxY(), cursor, dataArea.getMaxY() - length));
    }

    public static boolean isRightAxis(Plot plot) {
        ArrayList<RangeAxis> rangeAxesList = plot.getRangeAxesList();
        for (RangeAxis axis : rangeAxesList) {
            if (axis.axisPosition == AxisPosition.RIGHT) {
                return true;
            }
        }
        return false;
    }

    public void toggle(boolean bv) {
        if (bv) {
            bv = false;
        } else {
            bv = true;
        }
    }
}
