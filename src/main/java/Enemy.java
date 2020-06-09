package main.java;

import java.awt.*;
import java.util.ArrayList;

public interface Enemy {
    Integer getX();

    Integer getY();

    void move(int n);

    ArrayList<Point> getShape();
}