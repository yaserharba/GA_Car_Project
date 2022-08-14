import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main {
    private static final int minCarNumber = 10;
    final static int[] gen = {1};

    public static void main(String[] args) throws Exception {
        Board board = new Board(new Dimension(13, 7), 110);
        ArrayList<CarAgent> cars = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            cars.add(new CarAgent(new Vector2D(55, 55)));
        }
        new ShowFrame(board, cars, gen);

        Timer runTimer = new Timer(25, e -> {
            LinkedList<CarAgent> toRemove = new LinkedList<>();
            for (CarAgent car : cars) {
                car.run();
                car.update(board);
                if (car.dieCheck(board)) {
                    toRemove.add(car);
                }
            }
            for (CarAgent car : toRemove) {
                if (cars.size() == minCarNumber)
                    break;
                cars.remove(car);
            }
            if (cars.size() == minCarNumber) {

                NN.randFactor *= 0.999;
                gen[0]++;

                ArrayList<CarAgent> newCars = new ArrayList<>();
                for (CarAgent car : cars) {
                    newCars.addAll(car.mutatedList(10));
                    car.reset();
                }
                cars.addAll(newCars);

                try {
                    board.generatePath();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }
        });
        runTimer.start();
    }
}
