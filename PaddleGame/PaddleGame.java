import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PaddleGame extends JFrame implements KeyListener, ActionListener {
    private int paddle1Y = 250;
    private int paddle2Y = 250;
    private int ballX = 350;
    private int ballY = 250;
    private int ballXDirection = -1;
    private int ballYDirection = 1;
    private Timer timer;
    private Timer aiTimer;
    private boolean isOnePlayer = false; 
    private boolean isZeroPlayer = false; 
    private boolean isTwoPlayer = false; 
    private Image dbImage;
    private Graphics dbg;
    private int player1Score = 0;
    private int player2Score = 0;
    private final int WINNING_SCORE = 7;
    private int predictedBallYLeft;
    private int predictedBallYRight;

    public PaddleGame() {
        setTitle("Paddle Game");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        addKeyListener(this);
        setLocationRelativeTo(null);

        timer = new Timer(5, this);
        aiTimer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isTwoPlayer) {
                    if (ballXDirection > 0) {
                        predictedBallYRight = predictBallLanding();
                    }
                    if (ballXDirection < 0) {
                        predictedBallYLeft = predictBallLandingLeft();
                    }
                }
            }
        });
        showInstructions();
    }

    private void showInstructions() {
        String message = "Welcome to Paddle Game!\n\nControls:\n" +
                         "Player 1 (Left Paddle): W/S\n" +
                         "Player 2 (Right Paddle): Up/Down Arrow\n\n" +
                         "Choose Mode:";
        Object[] options = {"1 Player", "2 Players", "0 Players"};
        int response = JOptionPane.showOptionDialog(this, message, "Instructions",
                                                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                                                    null, options, options[0]);

        
                                                    
        isOnePlayer = (response == 0);
        isTwoPlayer = (response == 1);
        isZeroPlayer = (response == 2);
        
        timer.start();
        aiTimer.start();
    }

    @Override
    public void paint(Graphics g) {
        dbImage = createImage(getWidth(), getHeight());
        dbg = dbImage.getGraphics();
        paintComponent(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 700, 500);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Player 1: " + player1Score, 100, 48);
        g.drawString("Player 2: " + player2Score, 500, 48);

        g.drawRect(0, 50, 700, 1);

        g.fillRect(50, paddle1Y, 10, 50);
        g.fillRect(640, paddle2Y, 10, 50);
        g.fillOval(ballX, ballY, 15, 15);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (player1Score >= WINNING_SCORE || player2Score >= WINNING_SCORE) {
            timer.stop();
            aiTimer.stop();
            String winner = player1Score >= WINNING_SCORE ? "Player 1" : "Player 2";
            JOptionPane.showMessageDialog(this, winner + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        ballX += ballXDirection;
        ballY += ballYDirection;

        if (ballY <= 50 || ballY >= 475) { // Adjusted to prevent clipping at the top
            ballYDirection = -ballYDirection;
        }

        if (ballX <= 60 && ballY >= paddle1Y && ballY <= paddle1Y + 50) {
            ballXDirection = -ballXDirection;
        }

        if (ballX >= 625 && ballY >= paddle2Y && ballY <= paddle2Y + 50) {
            ballXDirection = -ballXDirection;
        }

        if (ballX < 0) {
            player2Score++;
            resetBall();
        }

        if (ballX > 685) {
            player1Score++;
            resetBall();
        }

        if (isOnePlayer) {
            aiMovePaddleRight();
        }
        if (isZeroPlayer) {
            aiMovePaddleLeft();
            aiMovePaddleRight();
        }

        repaint();
    }

    private void resetBall() {
        ballX = 350;
        ballY = 250;
        ballXDirection = -ballXDirection;
    }

    //FOR LEFT PADDLE
    private void aiMovePaddleLeft() {
        int paddleMiddle = paddle1Y + 25;
        if (paddleMiddle < predictedBallYLeft - 5 && paddle1Y < 450) {
            paddle1Y += 2;
        } else if (paddleMiddle > predictedBallYLeft + 5 && paddle1Y > 50) {
            paddle1Y -= 2;
        }
    }
    
    //FOR RIGHT PADDLE
    private void aiMovePaddleRight() {
        int paddleMiddle = paddle2Y + 25;
        if (paddleMiddle < predictedBallYRight - 5 && paddle2Y < 450) {
            paddle2Y += 2;
        } else if (paddleMiddle > predictedBallYRight + 5 && paddle2Y > 50) {
            paddle2Y -= 2;
        }
    }

    private int predictBallLanding() {
        int tempBallX = ballX;
        int tempBallY = ballY;
        int tempBallXDirection = ballXDirection;
        int tempBallYDirection = ballYDirection;

        while (tempBallX < 625) {
            tempBallX += tempBallXDirection;
            tempBallY += tempBallYDirection;

            if (tempBallY <= 50 || tempBallY >= 475) { // Adjusted to prevent clipping at the top
                tempBallYDirection = -tempBallYDirection;
            }
        }

        return tempBallY;
    }

    private int predictBallLandingLeft() {
        int tempBallX = ballX;
        int tempBallY = ballY;
        int tempBallXDirection = ballXDirection;
        int tempBallYDirection = ballYDirection;

        while (tempBallX > 60) {
            tempBallX += tempBallXDirection;
            tempBallY += tempBallYDirection;

            if (tempBallY <= 50 || tempBallY >= 475) { // Adjusted to prevent clipping at the top
                tempBallYDirection = -tempBallYDirection;
            }
        }

        return tempBallY;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isTwoPlayer || !isTwoPlayer && (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_W && paddle1Y > 50) {
                if(!isZeroPlayer){
                    paddle1Y -= 50;
                    }
            }

            if (key == KeyEvent.VK_S && paddle1Y < 450) {
                if(!isZeroPlayer){
                paddle1Y += 50;
                }
            }

            if (key == KeyEvent.VK_UP && isTwoPlayer && paddle2Y > 50) {
                paddle2Y -= 50;
            }

            if (key == KeyEvent.VK_DOWN && isTwoPlayer && paddle2Y < 450) {
                paddle2Y += 50;
            }
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PaddleGame game = new PaddleGame();
            game.setVisible(true);
        });
    }
}
