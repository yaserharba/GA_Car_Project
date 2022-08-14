import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Sensor {
    private double activatedValue;
    private Line2D line2D;
    private final Car car;
    private final double length = 150;
    private final double angle;

    public Sensor(Car car, double angle) {
        this.car = car;
        this.angle = angle;
        reset();
    }

    public void update() {
        setLine();
    }

    private void setLine() {
        Vector2D v = new Vector2D(Math.cos(car.getAngle() + angle), Math.sin(car.getAngle() + angle));
        v.setMag(length);
        line2D = new Line2D.Double(
                car.getPos().getX(), car.getPos().getY(),
                car.getPos().getX() + v.getX(), car.getPos().getY() + v.getY());
    }

    public void reset() {
        activatedValue = 0;
    }

    public double getActivatedValue() {
        return activatedValue;
    }

    public void paint(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1));
        g2.setColor(new Color(220, 19, 35, 171));
        if(line2D!=null)
            g2.draw(line2D);
        Vector2D v = new Vector2D(Math.cos(car.getAngle() + angle), Math.sin(car.getAngle() + angle));
        v.setMag(activatedValue*length);
        line2D = new Line2D.Double(
                car.getPos().getX(), car.getPos().getY(),
                car.getPos().getX() + v.getX(), car.getPos().getY() + v.getY());
        g2.setColor(new Color(255, 255, 255, 200));
        g2.draw(line2D);
    }

    public void sensorsSense(ArrayList<Line2D> walls) {
        activatedValue = 1;
        for (Line2D wall : walls) {
            if (line2D.intersectsLine(wall)) {
                Point2D intPoint = segmentIntersection(line2D, wall);
                assert intPoint != null;
                Vector2D temp = new Vector2D(
                        intPoint.getX() - car.getPos().getX(),
                        intPoint.getY() - car.getPos().getY());
                activatedValue = Math.min(activatedValue, temp.mag()/length);
            }
        }
    }

    public static Point2D segmentIntersection(Line2D a, Line2D b) {
        double x1 = a.getX1(), y1 = a.getY1(), x2 = a.getX2(), y2 = a.getY2(), x3 = b.getX1(), y3 = b.getY1(),
                x4 = b.getX2(), y4 = b.getY2();
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0) {
            return null;
        }
        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        return new Point2D.Double(xi, yi);
    }
}
