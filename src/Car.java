import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class Car {
    private final Vector2D startPos;
    private Vector2D pos;
    private Vector2D vel;
    private Vector2D acc;
    private final Dimension dimension = new Dimension(20, 40);
    private double angle;
    private static final double maxSpeed = 3.1;
    private static final double maxForce = 0.24;
    private final ArrayList<Sensor> sensors;

    public Car(Vector2D pos) {
        this.startPos = pos;
        sensors = new ArrayList<>();
        //sensors.add(new Sensor(this, -3*Math.PI / 4));
        sensors.add(new Sensor(this, -Math.PI / 2));
        sensors.add(new Sensor(this, -Math.PI / 4));
        sensors.add(new Sensor(this, 0));
        sensors.add(new Sensor(this, Math.PI / 4));
        sensors.add(new Sensor(this, Math.PI / 2));
        //sensors.add(new Sensor(this, 3*Math.PI / 4));
        reset();
    }

    public void reset() {
        this.pos = startPos.clone();
        this.vel = new Vector2D(0, 0);
        this.acc = new Vector2D(0, 0);
        this.angle = 0;
        for (Sensor sensor : sensors)
            sensor.reset();
    }

    public Vector2D getPos() {
        return pos;
    }

    public Vector2D getStartPos() {
        return startPos;
    }

    public double getAngle() {
        return angle;
    }

    void seek(double drivingAngle) {
        Vector2D force = new Vector2D(Math.cos(drivingAngle), Math.sin(drivingAngle));
        force.setMag(maxSpeed);
        force.sub(this.vel);
        force.limit(maxForce);
        applyForce(force);
    }

    public void applyForce(Vector2D force) {
        this.acc.add(force);
    }

    public void update(Board board) {
        this.vel.add(this.acc);
        this.vel.limit(maxSpeed);
        vel.mul(0.99);
        this.pos.add(this.vel);
        updateAngle();
        this.acc.set(0, 0);
        for (Sensor sensor : sensors) {
            sensor.update();
        }
        updateSensorsValues(board);
    }

    private void updateAngle() {
        Vector2D fVel = vel.clone();
        angle = Math.atan2(fVel.getY(), fVel.getX());
    }

    void paint(Graphics2D g2) {
        for (Sensor sensor : sensors)
            sensor.paint(g2);
        g2.translate(pos.getX(), pos.getY());
        g2.rotate(angle);
        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.darkGray);
        g2.fill(new RoundRectangle2D.Double(-dimension.getHeight() / 2, -dimension.getWidth() / 2, dimension.getHeight(), dimension.getWidth(), 5, 20));
        g2.setStroke(new BasicStroke(8));
        g2.setColor(new Color(19, 116, 220));
        g2.draw(new Arc2D.Double(-dimension.getHeight() / 10, -dimension.getWidth() / 2 + 2, dimension.getHeight() / 2 - 2, dimension.getWidth() - 4, -90.0, 180.0, Arc2D.OPEN));
        g2.setStroke(new BasicStroke(5));
        g2.setColor(new Color(126, 220, 19));
        g2.draw(new RoundRectangle2D.Double(-dimension.getHeight() / 2, -dimension.getWidth() / 2, dimension.getHeight(), dimension.getWidth(), 5, 20));
        g2.rotate(-angle);
        g2.translate(-pos.getX(), -pos.getY());
    }

    public boolean dieCheck(Board board) {
        ArrayList<Point> blocks = new ArrayList<>();
        Point currentBlock = board.blockAtPos(pos);
        blocks.add(currentBlock);
        blocks.addAll(board.get8Neighbors(currentBlock));
        for (Point block : blocks) {
            if (checkColliding(block, board)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColliding(Point block, Board board) {
        Line2D[] carBorders = getLine2dBorders();
        ArrayList<Line2D> walls = board.getLines2DWalls(block);
        for (Line2D wall : walls) {
            for (Line2D border : carBorders) {
                if (wall.intersectsLine(border)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Line2D[] getLine2dBorders() {
        Matrix[] corners = getCarCorners();
        Line2D[] borders = new Line2D[4];
        borders[0] = new Line2D.Double(corners[0].getDataAt(0,0), corners[0].getDataAt(1,0), corners[1].getDataAt(0,0), corners[1].getDataAt(1,0));
        borders[1] = new Line2D.Double(corners[0].getDataAt(0,0), corners[0].getDataAt(1,0), corners[2].getDataAt(0,0), corners[2].getDataAt(1,0));
        borders[2] = new Line2D.Double(corners[3].getDataAt(0,0), corners[3].getDataAt(1,0), corners[1].getDataAt(0,0), corners[1].getDataAt(1,0));
        borders[3] = new Line2D.Double(corners[3].getDataAt(0,0), corners[3].getDataAt(1,0), corners[2].getDataAt(0,0), corners[2].getDataAt(1,0));
        return borders;
    }

    private Matrix[] getCarCorners() {
        Matrix posMat = new Matrix(new double[][]{
                {pos.getX()},
                {pos.getY()}});
        Matrix rotationMatrix = new Matrix(new double[][]{
                {Math.cos(-angle), Math.sin(-angle)},
                {-Math.sin(-angle), Math.cos(-angle)}});
        Matrix[] corners = {
                new Matrix(new double[][]{
                        {dimension.getHeight() / 2},
                        {dimension.getWidth() / 2}}),
                new Matrix(new double[][]{
                        {-dimension.getHeight() / 2},
                        {dimension.getWidth() / 2}}),
                new Matrix(new double[][]{
                        {dimension.getHeight() / 2},
                        {-dimension.getWidth() / 2}}),
                new Matrix(new double[][]{
                        {-dimension.getHeight() / 2},
                        {-dimension.getWidth() / 2}})
        };
        for (int i = 0; i < 4; i++) {
            corners[i] = Matrix.dot(rotationMatrix, corners[i]);
            corners[i] = Matrix.add(corners[i], posMat);
        }
        return corners;
    }

    private void updateSensorsValues(Board board) {
        ArrayList<Point> blocks = new ArrayList<>();
        Point currentBlock = board.blockAtPos(pos);

        blocks.add(currentBlock);
        blocks.addAll(board.get8Neighbors(currentBlock));

        ArrayList<Line2D> walls = new ArrayList<>();
        for (Point block : blocks) {
            walls.addAll(board.getLines2DWalls(block));
        }
        for (Sensor sensor : sensors) {
            sensor.sensorsSense(walls);
        }
    }

    public Matrix getSensorsValues() {
        Matrix ret = new Matrix(sensors.size(),1);
        int i = 0;
        for (Sensor sensor : sensors)
            ret.setDataAt(i++,0, sensor.getActivatedValue());
        return ret;
    }
}
