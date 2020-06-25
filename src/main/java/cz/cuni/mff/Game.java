package cz.cuni.mff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class Game implements ActionListener, KeyListener {

    public static Integer ACTIVE_WINDOW_HEIGHT = 859;
    public static Integer INITIAL_GAMETIMER_SPEED = 13;
    public static Integer LINE_LENGTH = 10;
    private final Random rand;
    private final JFrame frame;
    private final int themeIndex;
    public Block activeBlock;
    // fallen blocks
    public java.util.List<Point> floor;
    public java.util.List<Enemy> enemies;
    public java.util.List<Point> wall;
    public Timer tmr;
    public boolean gameOver;
    public RenderPanel renderPanel;
    boolean paused;
    boolean wallBool;
    boolean anitigBool;
    boolean meteoreBool;
    private int score;
    private int currentTheme;
    private int gravityIndex;
    //counter for moving blocks downwards
    private int currGravIndex;
    //counter for changing themes
    private int currThemeIndex;
    private int level;

    public Game() {


        currentTheme = 0;
        gravityIndex = 30;
        currGravIndex = 0;
        themeIndex = 600;
        currThemeIndex = 0;
        level = 1;

        floor = new ArrayList<>();
        enemies = new ArrayList<>();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        score = 0;

        frame = new JFrame();
        frame.setLayout(null);
        frame.addKeyListener(this);
        frame.setSize(676, ACTIVE_WINDOW_HEIGHT);
        frame.setResizable(false);
        frame.setLocation(dim.width / 2 - frame.getWidth() / 2, dim.height / 2 - frame.getHeight() / 2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(renderPanel = new RenderPanel(this));
        renderPanel.setBounds(0, 0, 760, ACTIVE_WINDOW_HEIGHT);

        tmr = new Timer(INITIAL_GAMETIMER_SPEED, this);
        rand = new Random();
        paused = true;

        prepareGame();
        focusManager();
    }

    private void focusManager() {
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.addKeyEventDispatcher(e -> {
            if (focusManager.getFocusOwner() != frame) {
                focusManager.redispatchEvent(frame, e);
                return true;
            } else return false;
        });
    }

    private int isThereARow() {
        int numOfRows = 0;
        List<Point> toRemove = new ArrayList<>();

        for (int i = 80; i < 730; i += 30) {

            int finalI = i;
            if (floor.stream().filter(a -> a.y == finalI).count() == LINE_LENGTH) {
                numOfRows++;
                floor.forEach(a -> {
                    if (a.y == finalI) {
                        toRemove.add(a);
                    }
                    if (a.y < finalI) {
                        a.y += 30;
                    }
                });
            }

            floor.removeAll(toRemove);

        }

        return numOfRows;
    }

    private boolean canGoFreely(int X, int Y) {

        List<Point> newShape = activeBlock.getShape().stream()
                .map(p -> new Point(p.x + X, p.y + Y))
                .collect(Collectors.toCollection(ArrayList::new));
        return newShape.stream()
                .allMatch(p -> !floor.contains(new Point(p.x, p.y)) &&
                        p.x >= 80 &&
                        p.x < 380 &&
                        p.y < 740);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        renderPanel.repaint();
        currGravIndex++;
        currThemeIndex++;
        renderPanel.scoreLabel.setText("SCORE: " + score);
        renderPanel.levelLabel.setText("level: " + level);
        deleteEnemy();
        if (collision()) {
            gameOver();
        }

        if (currThemeIndex == themeIndex - gravityIndex * 2) {
            currentTheme = (currentTheme + 1) % (Theme.values().length);
            renderPanel.themeLabel.setText("Theme : " + Theme.values()[currentTheme].toString());
        } else if (currThemeIndex == themeIndex) {
            currThemeIndex = 0;
            wallBool = false;
            meteoreBool = false;
            anitigBool = false;
            switch (Theme.values()[currentTheme]) {
                case WALL -> wallBool = true;
                case METEORE -> meteoreBool = true;
                case ANTIGRAVITY -> anitigBool = true;
            }
        }

        if (currGravIndex == gravityIndex) {

            catastropher();

            currGravIndex = 0;
            enemies.forEach(a -> a.move(level));
            enemies.stream()
                    .filter(a -> a instanceof Meteore)
                    .forEach(this::eraseFloor);

            if (canGoFreely(0, 30)) {
                activeBlock.movePosition(0, 30);
            } else {
                floor.addAll(activeBlock.getShape());
                int n = isThereARow();

                int sc = switch (n) {
                    case 0 -> 0;
                    case 1 -> 40;
                    case 2 -> 100;
                    case 3 -> 300;
                    case 4 -> 1200;
                    default -> throw new IllegalStateException("Unexpected value: " + n);
                }; // score switch

                score += sc * level;
                score++;
                if (requiredScore(level) <= score) {
                    level++;

                    if (gravityIndex > 1) {
                        gravityIndex--;
                    }
                }

                if (floor.stream().anyMatch(b -> b.y <= 80)) gameOver();
                activeBlock = new Block(rand.nextInt(8));
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int i = keyEvent.getKeyCode();

        if (paused || gameOver) {
            return;
        }

        if (i == KeyEvent.VK_A || i == KeyEvent.VK_LEFT) {
            if (canGoFreely(-30, 0)) {
                activeBlock.movePosition(-30, 0);
            }

        }

        if (i == KeyEvent.VK_D || i == KeyEvent.VK_RIGHT) {
            if (canGoFreely(30, 0)) {
                activeBlock.movePosition(30, 0);
            }
        }

        if (i == KeyEvent.VK_S || i == KeyEvent.VK_DOWN) {
            if (canGoFreely(0, 30)) {
                activeBlock.movePosition(0, 30);
            }
        }

        if (i == KeyEvent.VK_SPACE || i == KeyEvent.VK_P) {
            ArrayList<Point> l = activeBlock.rotate();
            ArrayList<Point> k = l.stream()
                    .map(p -> new Point(p.x + activeBlock.position.x, p.y + activeBlock.position.y))
                    .collect(Collectors.toCollection(ArrayList::new));
            if (k.stream()
                    .allMatch(p -> !floor.contains(p) &&
                            p.x < 380 &&
                            p.y < 740 &&
                            p.x > 80)) {
                activeBlock.shape = l;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }

    private void catastropher() {
        int sw = rand.nextInt(30);
        int r1 = rand.nextInt(25) * 30 + 20;
        int r2 = rand.nextInt(13) * 30 + 20;

        int x = (rand.nextInt(3) - 1) * 30;
        int y = (rand.nextInt(3) - 1) * 30;

        while (x == 0 && y == 0) {
            x = (rand.nextInt(3) - 1) * 30;
            y = (rand.nextInt(3) - 1) * 30;
        }

        if (meteoreBool) {
            switch (sw) {
                case 1, 5 -> enemies.add(new Meteore(new Point(20, r1), x, y));
                case 2, 6 -> enemies.add(new Meteore(new Point(380, r1), x, y));
                case 4, 7 -> enemies.add(new Meteore(new Point(r2, 740), x, y));
                default -> {
                }
            }
            // new enemy switch
        }

        if (anitigBool) {
            switch (sw) {
                case 1, 2 -> {
                    if (!floor.isEmpty()) {
                        Point p = floor.get(rand.nextInt(floor.size()));
                        enemies.add(new Square(p));
                        floor.remove(p);
                    }
                }
                default -> {
                }
            }
            // new antig switch
        }
    }

    private void eraseFloor(Enemy e) {
        ArrayList<Point> erase = new ArrayList<>();
        e.getShape().forEach(a -> {
            if (floor.contains(a)) erase.add(a);
        });
        floor.removeAll(erase);
    }

    private Boolean collision() {
        ArrayList<Point> actBlcl = activeBlock.getShape();

        long intersectCount = enemies.stream()
                .filter(a -> a instanceof Meteore)
                .map(Enemy::getShape)
                .flatMap(Collection::stream)
                .distinct()
                .filter(actBlcl::contains)
                .count();

        return (intersectCount > 0);

    }

    private void deleteEnemy() {
        ArrayList<Enemy> deletion = new ArrayList<>();
        enemies.forEach(a -> {
            if (a.getX() < 20 || a.getX() > 380 || a.getY() < 20 || a.getY() > 740) {
                deletion.add(a);
            }
        });
        enemies.stream()
                .filter(a -> a instanceof Square)
                .forEach(a -> {
                    if (a.getY() < 80) deletion.add(a);
                });
        enemies.removeAll(deletion);
    }

    public void pauseGame() {
        if (!gameOver) {
            paused = !paused;
            renderPanel.pausedLabel.setVisible(paused);
            if (!paused) {
                tmr.start();
            } else {
                tmr.stop();
            }
        }
    }

    public void startGame() {
        prepareGame();
        enemies = new ArrayList<>();
        gameOver = false;
        score = 0;
        level = 1;
        renderPanel.pausedLabel.setVisible(false);
        paused = false;
        tmr.start();
    }

    public void gameOver() {
        paused = true;
        gameOver = true;
        wallBool = false;
        meteoreBool = false;
        anitigBool = false;
        tmr.stop();
        renderPanel.gameOverlabel.setVisible(true);
        Outro o = new Outro(this);
        prepareGame();
    }

    private void prepareGame() {

        wallBool = false;
        anitigBool = false;
        meteoreBool = false;
        wall = new ArrayList<>();
        for (int i = 680; i >= 400; i -= 60) {
            for (int j = 320; j >= 80; j -= 60) {
                wall.add(new Point(j, i));
            }
        }
        floor.clear();
        currThemeIndex = 0;
        currentTheme = 0;
        score = 0;
        level = 1;
        activeBlock = new Block(rand.nextInt(8));
        enemies.clear();
        renderPanel.gameOverlabel.setVisible(false);
    }

    private int requiredScore(int level) {
        int score = 1;
        for (int i = 1; i <= level; i++) {
            score += 10 * i;
        }
        return score;
    }

    enum Theme {
        WALL, METEORE, ANTIGRAVITY
    }

}
