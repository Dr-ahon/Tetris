package com.company;

import java.awt.*;
import java.util.ArrayList;

public class Square implements Enemy {
    Point position;

    public Square(Point p) {
        position = p;
    }

    @Override
    public Integer getX() {
        return position.x;
    }

    @Override
    public Integer getY() {
        return position.y;
    }

    @Override
    public void move(int n) {
        Point prevPos = position;
        position = new Point(prevPos.x, prevPos.y - 30);
    }

    @Override
    public ArrayList<Point> getShape() {
        return new ArrayList<>() {{
            add(position);
        }};
    }
}
