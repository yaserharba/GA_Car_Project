import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

public class Board {
    private final Random random;
    private final Dimension dimension;
    private final int width;
    private final boolean[][][] gates;

    public Board(Dimension dimension, int width) throws Exception {
        this.width = width;
        random = new Random();
        this.dimension = dimension;
        gates = new boolean[dimension.width][dimension.height][4];
        generatePath();
    }

    public void generatePath() throws Exception {
        for (int i = 0; i < dimension.width; i++) {
            for (int j = 0; j < dimension.height; j++) {
                for (int k = 0; k < 4; k++) {
                    gates[i][j][k] = false;
                }
            }
        }
        ArrayList<Point> path = new ArrayList<>();
        boolean[][] vis = new boolean[dimension.width][dimension.height];
        dfs(new Point(0, 0), vis, path);
    }

    private void dfs(Point block, boolean[][] vis, ArrayList<Point> path) throws Exception {
        vis[block.x][block.y] = true;
        path.add(block);
        ArrayList<Point> goodNeighbors = new ArrayList<>();
        if (block.x == 0 && block.y == 0) {
            goodNeighbors.add(new Point(1, 0));
        } else {
            goodNeighbors.addAll(getGoodNeighbors(block, vis));
        }
        while (!goodNeighbors.isEmpty()) {
            Point neighbor = goodNeighbors.get(random.nextInt(goodNeighbors.size()));
            openGates(block, neighbor);
            dfs(neighbor, vis, path);
            goodNeighbors = getGoodNeighbors(block, vis);
        }
    }

    private void openGates(Point block, Point neighbor) throws Exception {
        int direction = getDirection(block, neighbor);
        gates[block.x][block.y][direction] = true;
        gates[neighbor.x][neighbor.y][(2 + direction) % 4] = true;
    }

    private ArrayList<Point> getGoodNeighbors(Point block, boolean[][] vis) {
        ArrayList<Point> ret = new ArrayList<>();
        ret.add(new Point(block.x - 1, block.y));
        ret.add(new Point(block.x + 1, block.y));
        ret.add(new Point(block.x, block.y - 1));
        ret.add(new Point(block.x, block.y + 1));
        ret.removeIf(this::badBlock);
        ret.removeIf(b -> vis[b.x][b.y]);
        return ret;
    }

    public ArrayList<Point> get8Neighbors(Point block) {
        ArrayList<Point> ret = new ArrayList<>();
        ret.add(new Point(block.x - 1, block.y));
        ret.add(new Point(block.x + 1, block.y));
        ret.add(new Point(block.x, block.y - 1));
        ret.add(new Point(block.x, block.y + 1));
        ret.add(new Point(block.x + 1, block.y + 1));
        ret.add(new Point(block.x - 1, block.y + 1));
        ret.add(new Point(block.x + 1, block.y - 1));
        ret.add(new Point(block.x - 1, block.y - 1));
        ret.removeIf(this::badBlock);
        return ret;
    }

    public boolean badBlock(Point block) {
        return block.x >= dimension.width
                || block.x < 0
                || block.y < 0
                || block.y >= dimension.height;
    }

    private int getDirection(Point block1, Point block2) throws Exception {
        if (block1.x == block2.x - 1 && block1.y == block2.y)
            return 0;
        else if (block1.x == block2.x && block1.y == block2.y + 1)
            return 1;
        else if (block1.x == block2.x + 1 && block1.y == block2.y)
            return 2;
        else if (block1.x == block2.x && block1.y == block2.y - 1)
            return 3;
        throw new Exception("not neighbors");
    }

    public void paint(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        for (int i = 0; i < dimension.width; i++) {
            for (int j = 0; j < dimension.height; j++) {
                if (!gates[i][j][0]) {
                    g2.draw(new Line2D.Double((i + 1) * width, j * width, (i + 1) * width, (j + 1) * width));
                }
                if (!gates[i][j][3]) {
                    g2.draw(new Line2D.Double(i * width, (j + 1) * width, (i + 1) * width, (j + 1) * width));
                }
            }
        }
    }

    public Dimension dimensionOfPaint() {
        return new Dimension(dimension.width * width, dimension.height * width);
    }

    public Point blockAtPos(Vector2D pos) {
        return new Point((int) pos.getX() / width, (int) pos.getY() / width);
    }

    public ArrayList<Line2D> getLines2DWalls(Point block) {
        ArrayList<Line2D> ret = new ArrayList<>();
        if (!gates[block.x][block.y][0])
            ret.add(new Line2D.Double((block.x + 1) * width, block.y * width, (block.x + 1) * width, (block.y + 1) * width));
        if (!gates[block.x][block.y][1])
            ret.add(new Line2D.Double(block.x * width, block.y * width, (block.x + 1) * width, block.y * width));
        if (!gates[block.x][block.y][2])
            ret.add(new Line2D.Double(block.x * width, block.y * width, block.x * width, (block.y + 1) * width));
        if (!gates[block.x][block.y][3])
            ret.add(new Line2D.Double(block.x * width, (block.y + 1) * width, (block.x + 1) * width, (block.y + 1) * width));
        return ret;
    }
}
