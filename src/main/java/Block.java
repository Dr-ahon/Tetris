package main.java;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

enum blockType {
    THREE, FOUR, CUBE, RIL, LEL, RIZ, LEZ, T;

    public ArrayList<Point> makeShape(Point position) {
        ArrayList<Point> list = new ArrayList<>();
        int x = position.x;
        int y = position.y;

        switch (this) {
            case THREE -> {
                list.add(new Point(0, -30));
                list.add(new Point(0, -60));
            }
            case FOUR -> {
                list.add(new Point(0, -90));
                list.add(new Point(0, -30));
                list.add(new Point(0, -60));
            }
            case CUBE -> {
                list.add(new Point(0, -30));
                list.add(new Point(-30, -30));
                list.add(new Point(-30, 0));
            }
            case RIL -> {
                list.add(new Point(-30, 0));
                list.add(new Point(-30, -30));
                list.add(new Point(-30, -60));
            }
            case LEL -> {
                list.add(new Point(-30, 0));
                list.add(new Point(0, -30));
                list.add(new Point(0, -60));
            }
            case RIZ -> {
                list.add(new Point(-30, 0));
                list.add(new Point(30, -30));
                list.add(new Point(0, -30));
            }
            case LEZ -> {
                list.add(new Point(-30, 0));
                list.add(new Point(-30, -30));
                list.add(new Point(-60, -30));
            }
            case T -> {
                list.add(new Point(-30, 0));
                list.add(new Point(30, 0));
                list.add(new Point(-0, -30));
            }
        }
        list.add(new Point(0, 0));
        return list;
    }

}

public class Block {
    blockType bt;
    ArrayList<Point> shape;
    Point position;
    boolean canFallSafely;


    public Block(int num) {
        bt = blockType.values()[num];
        position = new Point(230, 80);
        shape = bt.makeShape(position);
        canFallSafely = true;
    }

    public ArrayList<Point> rotate() {
        int X = switch (this.bt) {
            case THREE, RIL, LEL -> -15;
            case FOUR -> this.shape.get(1).x;
            case RIZ, LEZ -> 0;
            case T -> 0;
            case CUBE -> -15;
        };

        return this.shape.stream()
                .map(p -> new Point((p.y), (p.x)))
                .map(p -> new Point((p.x + (X - p.x) * 2), p.y))
                .collect(Collectors.toCollection(ArrayList::new));

    }

    public void movePosition(int X, int Y) {
        position = new Point(position.x + X, position.y + Y);
    }

    public ArrayList<Point> getShape() {
        return this.shape.stream()
                .map(p -> new Point(p.x + position.x, p.y + position.y))
                .collect(Collectors.toCollection(ArrayList::new));
    }


}
