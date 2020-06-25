package cz.cuni.mff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;

public class RenderPanel extends JPanel implements ActionListener {

    public static final Color MYGREEN = new Color(8562050);
    public static final Color MYYELLOW = new Color(14203392);
    private final Game game;
    public JLabel scoreLabel;
    public JLabel levelLabel;
    public JLabel pausedLabel;
    public JLabel gameOverlabel;
    public JLabel themeLabel;
    public JButton STARTGAME;
    public JButton pauseGame;
    public Font myFont;

    public RenderPanel(Game game_) {
        this.setLayout(null);
        game = game_;
        setFont();

        scoreLabel = new JLabel("SCORE: 0");
        scoreLabel.setBounds(450, 100, 100, 50);
        scoreLabel.setBackground(MYGREEN);
        scoreLabel.setFont(myFont);
        scoreLabel.setSize(150, 100);

        levelLabel = new JLabel("level: 1");
        levelLabel.setBounds(450, 50, 100, 50);
        levelLabel.setBackground(MYGREEN);
        levelLabel.setFont(myFont);
        levelLabel.setSize(150, 100);

        themeLabel = new JLabel("theme : none");
        themeLabel.setBounds(350, 665, 300, 100);
        themeLabel.setFont(myFont);
        themeLabel.setSize(300, 100);
        themeLabel.setHorizontalAlignment(JLabel.CENTER);

        pausedLabel = new JLabel("GAME PAUSED", SwingConstants.CENTER);
        pausedLabel.setFont(myFont);
        pausedLabel.setBounds(80, 300, 300, 100);
        pausedLabel.setBackground(Color.gray.brighter());
        pausedLabel.setVisible(false);
        pausedLabel.setOpaque(true);

        gameOverlabel = new JLabel("GAME OVER", SwingConstants.CENTER);
        gameOverlabel.setFont(myFont);
        gameOverlabel.setBounds(80, 300, 300, 100);
        gameOverlabel.setVisible(false);
        gameOverlabel.setOpaque(false);

        add(STARTGAME = new JButton());
        add(pauseGame = new JButton());

        STARTGAME.setBounds(450, 315, 100, 100);
        pauseGame.setBounds(463, 440, 75, 75);
        STARTGAME.setBorder(BorderFactory.createEmptyBorder());
        pauseGame.setBorder(BorderFactory.createEmptyBorder());
        STARTGAME.setContentAreaFilled(false);
        pauseGame.setContentAreaFilled(false);

        Image button = new ImageIcon("src/main/resources/red-button.png").getImage();
        STARTGAME.setIcon(new ImageIcon(button.getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
        pauseGame.setIcon(new ImageIcon(button.getScaledInstance(65, 65, Image.SCALE_DEFAULT)));
        STARTGAME.addActionListener(this);
        pauseGame.addActionListener(this);

        pauseGame.setFocusPainted(false);

        this.add(scoreLabel);
        this.add(levelLabel);
        this.add(pausedLabel);
        this.add(gameOverlabel);
        this.add(themeLabel);
    }

    private void setFont() {
        try {
            myFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/digital-7.ttf")).deriveFont(Font.PLAIN, 22f);
        } catch (Exception ex) {
            myFont = new Font("Monospaced", Font.BOLD, 16);
            ex.printStackTrace();
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(MYYELLOW);

        g.setColor(MYGREEN);
        g.fillRect(50, 50, 360, 720); // cele zelene pole
        g.fillRect(410, 50, 200, 720); // zelene pole vpravo na body

        g.setColor(Color.lightGray);
        g.fillRect(80, 80, 300, 660); //####### 80,80   380,80   380,740   740,80  pouzitelny vnitrek ########

        g.setColor(Color.DARK_GRAY);
        g.drawRect(75, 75, 309, 669); // vnejsi ramecek
        g.drawRect(73, 73, 313, 673); // vnitrni ramecek

        game.floor.forEach(a -> {
            if (a.y >= 80) {
                drawSquare(g, a, Color.DARK_GRAY.brighter(), 1);
            }
        });


        if (!game.paused && game.activeBlock.shape != null) {
            game.activeBlock.getShape().forEach(a -> {
                if (a.y >= 80) {
                    drawSquare(g, a, Color.DARK_GRAY, 1);
                }
            });
        }

        g.drawRect(75, 75, 309, 669); // vnejsi ramecek

        game.enemies.stream()
                .filter(a -> a instanceof Square)
                .map(Enemy::getShape)
                .flatMap(Collection::stream)
                .forEach(a -> drawSquare(g, a, Color.gray, 1));
        g.setColor(Color.red.darker());
        game.enemies.forEach(a -> {
            if (a instanceof Meteore) {
                g.fillOval(a.getX(), a.getY(), 60, 60);
                g.setColor(Color.lightGray);
                g.fillOval(a.getX() + 5, a.getY() + 5, 50, 50);
                g.setColor(Color.red.darker());
                g.fillOval(a.getX() + 10, a.getY() + 10, 40, 40);
            }
        });


        if (game.wallBool) {
            g.setColor(Color.lightGray);
            g.fillRect(80, 440, 300, 300);
            game.wall.forEach(a -> drawSquare(g, a, Color.ORANGE.darker(), 2));
        }

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == pauseGame) {
            game.pauseGame();
        }
        if (actionEvent.getSource() == STARTGAME) {
            game.startGame();
        }

    }

    private void drawSquare(Graphics g, Point p, Color c, int k) {
        g.setColor(c);
        g.fillRect(p.x + 2 * k, p.y + 2 * k, 26 * k, 26 * k);
        g.setColor(Color.lightGray);
        g.fillRect(p.x + 5 * k, p.y + 5 * k, 20 * k, 20 * k);
        g.setColor(c);
        g.fillRect(p.x + 8 * k, p.y + 8 * k, 14 * k, 14 * k);
    }
}

