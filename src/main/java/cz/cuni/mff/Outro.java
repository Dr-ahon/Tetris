package cz.cuni.mff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Outro implements ActionListener {
    public static Integer INITIAL_OUTROTIMER_SPEED = 10;
    public static Integer RIGHT_BOTTOM_SQUARE_HEIGHT = 710;
    public static Integer RIGHT_BOTTOM_SQUARE_WIDTH = 350;
    private final Timer outroTimer;
    private final Game game;
    private int x;
    private int y;


    public Outro(Game game_) {
        outroTimer = new Timer(INITIAL_OUTROTIMER_SPEED, this);
        outroTimer.start();
        x = RIGHT_BOTTOM_SQUARE_WIDTH;
        y = RIGHT_BOTTOM_SQUARE_HEIGHT;
        game = game_;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (!game.gameOver) {
            outroTimer.stop();
            game.floor.clear();
        } else {
            game.floor.add(new Point(x, y));
            game.renderPanel.revalidate();
            game.renderPanel.repaint();
            renewXY();
        }
    }

    private void renewXY() {
        if (y == 80) {
            if (x == 80) {
                game.renderPanel.gameOverlabel.setVisible(true);
                game.floor.clear();
                outroTimer.stop();
            } else {
                x -= 30;
            }
        } else if (x == 80) {
            y -= 30;
            x = 350;
        } else {
            x -= 30;
        }
    }
}
