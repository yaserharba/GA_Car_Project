import java.util.ArrayList;

public class CarAgent extends Car{
    private NN nn;
    private double drivingAngle;
    private static final double maxDrivingAngleChange = 0.05;
    public CarAgent(Vector2D pos) {
        super(pos);
        nn = new NN(new int[] {5, 10, 10, 2});
        drivingAngle = 0;
    }
    public void run(){
        Matrix X = getSensorsValues();
        Matrix Y = nn.forwardProp(X);
        double drivingAngleChange = Y.getDataAt(0, 0)-Y.getDataAt(1, 0);
        drivingAngleChange = Math.max(drivingAngleChange, -maxDrivingAngleChange);
        drivingAngleChange = Math.min(drivingAngleChange, maxDrivingAngleChange);
        drivingAngle += drivingAngleChange;
        seek(drivingAngle);
    }

    public ArrayList<CarAgent> mutatedList(int n){
        ArrayList<CarAgent> ret = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ret.add(newMutateSon());
        }
        return ret;
    }

    public CarAgent newMutateSon(){
        CarAgent ret = new CarAgent(getStartPos());
        ret.setNN(nn.clone());
        ret.nn.mutate();
        return ret;
    }

    public void setNN(NN nn){
        this.nn = nn;
    }
}
