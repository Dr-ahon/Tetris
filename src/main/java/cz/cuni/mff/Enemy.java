package cz.cuni.mff;

import java.awt.*;
import java.util.List;

public interface Enemy {
    int getX();

    int getY();

    void move(int n);

    List<Point> getShape();
}