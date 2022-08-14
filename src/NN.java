import java.util.Random;

public class NN {
    private final Random random = new Random();
    private final int[] shape;
    private final Matrix[] W;
    private final Matrix[] B;
    private static final double possibilityOfChange = 0.02;
    public static double randFactor = 0.5;


    public NN(int[] shape) {
        this.shape = shape;
        W = new Matrix[shape.length-1];
        B = new Matrix[shape.length-1];
        for (int i = 0; i < shape.length-1; i++) {
            W[i] = Matrix.randMatrix(shape[i+1], shape[i]);
            B[i] = Matrix.randMatrix(shape[i+1], 1);
        }
    }

    public Matrix reLU(Matrix Z) {
        Matrix ret = new Matrix(Z.getN(),Z.getM());
        for (int i = 0; i < Z.getN(); i++) {
            for (int j = 0; j < Z.getM(); j++) {
                ret.setDataAt(i, j, Math.max(Z.getDataAt(i, j), 0));
            }
        }
        return ret;
    }

    public Matrix forwardProp(Matrix X) {
        Matrix ret = X.clone();
        for (int k = 0; k < shape.length-1; k++) {
            ret = Matrix.dot(W[k], ret);
            ret = Matrix.add(ret, B[k]);
            ret = reLU(ret);
        }
        return ret;
    }

    public void mutate() {
        for (int k = 0; k < shape.length-1; k++) {
            for (int i = 0; i < W[k].getN(); i++) {
                for (int j = 0; j < W[k].getM(); j++) {
                    double p = random.nextDouble();
                    if (p < possibilityOfChange) {
                        W[k].setDataAt(i, j, W[k].getDataAt(i, j) + (random.nextDouble() * 2 * randFactor) - randFactor);
                        W[k].setDataAt(i, j, Math.max(-1, W[k].getDataAt(i, j)));
                        W[k].setDataAt(i, j, Math.min(1, W[k].getDataAt(i, j)));
                    }
                }
            }
            for (int i = 0; i < B[k].getN(); i++) {
                for (int j = 0; j < B[k].getM(); j++) {
                    double p = random.nextDouble();
                    if (p < possibilityOfChange) {
                        B[k].setDataAt(i, j, B[k].getDataAt(i, j) + 2 * ((random.nextDouble() * 2 * randFactor) - randFactor));
                    }
                }
            }
        }
    }

    public NN clone() {
        NN nn = new NN(shape);
        for (int k = 0; k < shape.length-1; k++) {
            nn.W[k] = this.W[k].clone();
            nn.B[k] = this.B[k].clone();
        }
        return nn;
    }
}
