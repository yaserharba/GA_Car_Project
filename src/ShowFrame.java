import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class ShowFrame extends JFrame {

    public ShowFrame(Board board, ArrayList<CarAgent> cars, final int[] gen) {
        cars.addAll(cars.get(0).mutatedList(6));
        JComponent component = new JComponent() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponents(g);
                Graphics2D g2 = (Graphics2D) g;
                for (Car car : cars)
                    car.paint(g2);
                board.paint(g2);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(board.dimensionOfPaint().width-410, 10, 360, 50, 15, 15));
                g2.setColor(new Color(200, 40, 40));
                g2.setFont(new Font(Font.DIALOG,  Font.BOLD, 40));
                g2.drawString("Gen # " + gen[0] + ", Car # " + cars.size(), board.dimensionOfPaint().width-400, 50);
            }
        };
        component.setPreferredSize(board.dimensionOfPaint());
        add(component);

        Timer showTimer = new Timer(32, e -> component.repaint());
        showTimer.start();

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
