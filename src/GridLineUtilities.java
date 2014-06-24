
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

        for (int i = 0; i < 11; i++) {

            if (!(i == 0)) //Don't draw over left  side and bottom axis;
            {
                drawHorizontalLine(g2D, dataArea, horizontalLinePosition, lineColor);
                if (i == 10) //Don't draw over right side axis;
                {
                     drawVerticalLine(g2D, dataArea, verticalLinePosition-2, lineColor);//minus 2 because sytem does not draw at exactly border(isRightAxis(plot) == true) {
                        break;
                    
                }
                drawVerticalLine(g2D, dataArea, verticalLinePosition, lineColor);
            }

            horizontalLinePosition = horizontalLinePosition - verticalSpacing;
            verticalLinePosition = verticalLinePosition + horizontalSpacing;
        }

        lineColor = Color.lightGray;
        verticalLinePosition = xLeft;
        horizontalLinePosition = yBottom;
        if ((type == GridLinesType.MEDIUM_GRID_SPACING) || (type == GridLinesType.NARROW_GRID_SPACING)) {

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

            for (int i = 0; i < 100; i++) {
                Line2D horizontalLine = new Line2D.Double(xLeft, horizontalLinePosition, xRight, horizontalLinePosition);
                Line2D verticalLine = new Line2D.Double(verticalLinePosition, yTop, verticalLinePosition, yBottom);
                if (!(i == 0)) //Don't draw over left  side and bottom axis;
                {
                    if (i % 5 == 0) {
                        lineColor=Color.orange;
                    }
                    if (i % 10 == 0) {
                        lineColor=Color.lightGray;
                    }
                    drawHorizontalLine(g2D, dataArea, horizontalLinePosition, lineColor);
                        drawVerticalLine(g2D, dataArea, verticalLinePosition, lineColor);
                    //g2D.draw(horizontalLine);
                    //g2D.draw(verticalLine);
                    lineColor=Color.lightGray;
                }
                horizontalLinePosition = horizontalLinePosition - verticalSpacing / 10.0;
                verticalLinePosition = verticalLinePosition + horizontalSpacing / 10.0;
            }
            return;
        }
    }

    public static void drawHorizontalLine(Graphics2D g2D, Rectangle2D dataArea, double cursor, Color lineColor) {
        g2D.setColor(lineColor);
        g2D.draw(new Line2D.Double(dataArea.getMinX(), cursor, dataArea.getMaxX(), cursor));
    }

    public static void drawVerticalLine(Graphics2D g2D, Rectangle2D dataArea, double cursor, Color lineColor) {
        g2D.setColor(lineColor);
        g2D.draw(new Line2D.Double(cursor, dataArea.getMinY(), cursor, dataArea.getMaxY()));
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
}
