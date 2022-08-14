import java.util.Random;

public class Matrix {
    private static final Random random = new Random();
    private final int n, m;
    private final double[][] data;

    public Matrix(int n, int m) {
        this.data = new double[n][m];
        this.n = n;
        this.m = m;
    }

    public Matrix(double[][] data) {
        this.n = data.length;
        this.m = data[0].length;
        this.data = data;
    }

    public static Matrix randMatrix(int n, int m) {
        double[][] ret = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ret[i][j] = (2 * random.nextDouble()) - 1;
            }
        }
        return new Matrix(ret);
    }

    public double getDataAt(int i, int j) {
        return data[i][j];
    }

    public void setDataAt(int i, int j, double d) {
        this.data[i][j] = d;
    }

    public static Matrix dot(Matrix matrix1, Matrix matrix2){
        assert matrix1.m == matrix2.n;
        Matrix ret = new Matrix(matrix1.n, matrix2.m);
        for (int i = 0; i < matrix1.n; i++) {
            for (int j = 0; j < matrix2.m; j++) {
                for (int k = 0; k < matrix1.m; k++) {
                    ret.setDataAt(i, j, ret.getDataAt(i, j)+ matrix1.getDataAt(i, k)* matrix2.getDataAt(k, j));
                }
            }
        }
        return ret;
    }

    public static Matrix add(Matrix matrix1, Matrix matrix2){
        assert matrix1.n == matrix2.n && matrix1.m == matrix2.m;
        Matrix ret = new Matrix(matrix1.n, matrix1.m);
        for (int i = 0; i < matrix1.n; i++) {
            for (int j = 0; j < matrix2.m; j++) {
                ret.setDataAt(i, j, matrix1.getDataAt(i, j) + matrix2.getDataAt(i, j));
            }
        }
        return ret;
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public Matrix clone() {
        Matrix ret = new Matrix(n, m);
        for (int i = 0; i < getN(); i++)
            System.arraycopy(data[i], 0, ret.data[i], 0, data[i].length);
        return ret;
    }
}
