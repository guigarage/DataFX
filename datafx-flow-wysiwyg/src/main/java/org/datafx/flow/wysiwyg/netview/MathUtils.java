package org.datafx.flow.wysiwyg.netview;


import javafx.geometry.Point2D;

public class MathUtils {

    public static Point2D intersectLines(Point2D line1Start, Point2D line1End, Point2D line2Start, Point2D line2End)throws IllegalArgumentException
    {
        // Wegen der Lesbarkeit
        double x1 = line1Start.getX();
        double x2 = line1End.getX();
        double y1 = line1Start.getY();
        double y2 = line1End.getY();

        double x3 = line2Start.getX();
        double x4 = line2End.getX();
        double y3 = line2Start.getY();
        double y4 = line2End.getY();

        // Zaehler
        double zx = (x1 * y2 - y1 * x2)*(x3-x4) - (x1 - x2) * (x3 * y4 - y3 * x4);
        double zy = (x1 * y2 - y1 * x2)*(y3-y4) - (y1 - y2) * (x3 * y4 - y3 * x4);

        // Nenner
        double n = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        // Koordinaten des Schnittpunktes
        double x = zx/n;
        double y = zy/n;

        // Vielleicht ist bei der Division durch n etwas schief gelaufen
        if (Double.isNaN(x)& Double.isNaN(y))
        {
            throw new IllegalArgumentException("Schnittpunkt nicht eindeutig.");
        }
        // Test ob der Schnittpunkt auf den angebenen Strecken liegt oder außerhalb.
        if ((x - x1) / (x2 - x1) > 1 || (x - x3) / (x4 - x3) > 1 || (y - y1) / (y2 - y1) > 1 || (y - y3) / (y4 - y3) > 1 )
        {
            throw new IllegalArgumentException("Schnittpunkt liegt außerhalb.");
        }
        return new Point2D(x,y);
    }

}
