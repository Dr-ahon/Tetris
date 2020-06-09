package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Outro implements ActionListener {
    private final Timer outroTimer;
    private final Game game;
    private int x;
    private int y;

    public Outro(Game game_) {
        outroTimer = new Timer(10, this);
        outroTimer.start();
        x = 350;
        y = 710;
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
            game.renderPanel.repaint(80, 80, 300, 660);
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
