public class Vector2D {
    private double x, y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(double x1, double y1){
        this.x = x1;
        this.y = y1;
    }

    public void setMag(double mag){
        norm();
        mul(mag);
    }

    public void limit(double limitVal){
        if(mag()>limitVal) {
            setMag(limitVal);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void add(Vector2D v) {
        x += v.getX();
        y += v.getY();
    }

    public void sub(Vector2D v) {
        x -= v.getX();
        y -= v.getY();
    }

    public void mul(double k) {
        x *= k;
        y *= k;
    }

    public void div(double k) {
        if (k == 0)
            throw new IllegalArgumentException();
        x /= k;
        y /= k;
    }

    public Vector2D clone() {
        return new Vector2D(this.x, this.y);
    }

    public double mag() {
        return Math.sqrt(x * x + y * y);
    }

    public void norm() {
        div(mag());
    }
}
