package com.company;

import java.awt.*;
import java.util.ArrayList;

class Meteore implements Enemy {
    Point position;
    int velX;
    int velY;

    public Meteore(Point pos, int x, int y) {
        position = pos;
        velX = x;
        velY = y;
    }

    @Override
    public ArrayList<Point> getShape() {
        ArrayList<Point> l = new ArrayList<>();
        l.add(position);
        l.add(new Point(position.x + 30, position.y));
        l.add(new Point(position.x + 30, position.y + 30));
        l.add(new Point(position.x, position.y + 30));
        return l;
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
        position = new Point(prevPos.x + velX, prevPos.y + velY);
    }
}
