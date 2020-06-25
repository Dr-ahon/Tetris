package cz.cuni.mff;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Square implements Enemy {
    Point position;

    public Square(Point p) {
        position = p;
    }

    @Override
    public int getX() {
        return position.x;
    }

    @Override
    public int getY() {
        return position.y;
    }

    @Override
    public void move(int n) {
        Point prevPos = position;
        position = new Point(prevPos.x, prevPos.y - 30);
    }

    @Override
    public ArrayList<Point> getShape() {
        return new ArrayList<>(List.of(position));
    }
}
