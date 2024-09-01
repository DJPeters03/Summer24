import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.text.DecimalFormat;

public class AimGame extends MouseAdapter implements Runnable, ActionListener {

    // CONSTANTS
    public static final int MAX_SIZE = 400;
    public static final int PANEL_SIZE = 1000;
    public static final int MIN_SIZE = 100;

    public static Thread thresh = new Thread();

    private java.util.List<Target> squares;
    private Point mousePoint;

    private int timesClickedOnRed = 0;
    private int greenSquaresMissed = 0;
    private int greenSquaresClicked = 0;
    private int currSeconds = 0;

    private int score = 0;

    private String scoreString = "Score: " + score;
    private String timeString = "    Time: " + currSeconds;
    private String gameOverString = "";

    private int targetClicks = 0;
    private int blueClicks = 0;
    private int redSpawned = 0;

    private int clockTime = 0;

    // number of shapes we'll be creating
    private int count;

    // original panel
    private JPanel panel;

    // NEEDS A PANEL FOR SCORE AND TIME
    private JPanel scorePanel;
    private JPanel timePanel;

    private Timer timer;

    private Random r = new Random();

    public AimGame(int count) {
        this.count = count;
    }

    @Override
    public void run() {
        // Display rules dialog
        showRulesDialog();

        // set up the GUI "look and feel" which should match
        // the OS on which we are running
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("AimGame");
        frame.setPreferredSize(new Dimension(2000, PANEL_SIZE));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // tell the JFrame that when someone closes the
        // window, the application should terminate
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // JPanel with a paintComponent method
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                // first, we should call the paintComponent method we are
                // overriding in JPanel
                super.paintComponent(g);

                // for each shape we have, paint it on the Graphics object
                for (Target s : squares) {
                    s.paint(g);
                }

                // ALSO NEEDS TO UPDATE THE SCORE AND TIME
            }
        };
        JPanel framePanel = new JPanel(new BorderLayout());

        scorePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Font font = new Font("Arial", Font.BOLD, 24);
                g.setFont(font);

                FontMetrics fm = g.getFontMetrics(font);
                int scoreStringWidth = fm.stringWidth(scoreString);
                int timeStringWidth = fm.stringWidth(timeString);

                int x = (getWidth() - scoreStringWidth - timeStringWidth) / 2;
                int y = fm.getAscent();

                g.drawString(scoreString, x, y);
                g.drawString(timeString, x + scoreStringWidth, y);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(2000, 50); // Adjust as necessary
            }
        };

        framePanel.add(scorePanel, BorderLayout.NORTH);
        framePanel.add(panel, BorderLayout.CENTER);
        frame.add(framePanel);

        panel.addMouseListener(this);

        // construct our list of shapes
        squares = new ArrayList<>(count);

        timer = new Timer(500, this);

        Random r = new Random();

        count = 1;
        timer.start();

        // clockTime is the int that directly translates to "Seconds since game has started"

        for (int i = 0; i < count; i++) {
            int size = MIN_SIZE + r.nextInt(MAX_SIZE - MIN_SIZE);

            Point point = new Point(r.nextInt(2000 - size), r.nextInt(PANEL_SIZE - size));

            Color color;

            int greenToRedRatio = r.nextInt(10);
            if (greenToRedRatio < 3) {
                color = new Color(255, 0, 0);
            } else {
                color = new Color(0, 155, 0);
            }

            squares.add(new Target(size, point, color, 0));

            // display the window we've created
            frame.pack();
            frame.setVisible(true);

            panel.repaint();
        }
    }

    private void showRulesDialog() {
        String rules = "Welcome to AimGame!\n\n" +
                       "Rules:\n" +
                       "1. Click on the squares that appear on the screen.\n" +
                       "2. Clicking on a red square ends the game.\n" +
                       "3. Green squares are worth 1 point, but the blue centers are worth 3 \n" +
                       "4. Green squares will despawn after 2 seconds, missing one ends the game\n" +
                       "5. The game keeps track of your score and the time played.\n\n" +
                       "Good luck!";
        JOptionPane.showMessageDialog(null, rules, "Game Rules", JOptionPane.INFORMATION_MESSAGE);
    }

    public void start() {
        timer.start();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        // if we pressed within an object in our list, REMOVE IT FROM LIST
        // note the reverse loop so we first encounter the object drawn on
        // top in the case of any overlap
        for (int i = squares.size() - 1; i >= 0; i--) {
            int containsReturn = squares.get(i).contains(p);

            if (containsReturn == 1 || containsReturn == 2) {

                if (squares.get(i).getColor().equals(new Color(255, 0, 0))) {
                    timesClickedOnRed++;
                    System.out.println("clicked on red: " + timesClickedOnRed);
                    gameOverString = "Clicked On Red";
                    scorePanel.repaint();
                } else {
                    if (containsReturn == 2) {
                        score += 3;
                        blueClicks++;
                    } else {
                        score++;
                    }

                    targetClicks++;
                    scoreString = "Score: " + score;
                    squares.remove(i);
                    scorePanel.repaint();
                }
                break;
            }
        }

        panel.repaint();
    }

    private void showGameOverDialog() {
        StringBuilder message = new StringBuilder();

        message.append("Game Over!\n").append(gameOverString).append("\n\n");
        message.append("Score: ").append(score).append("\n");
        message.append("Time Played: ").append(currSeconds).append(" seconds\n");
        message.append("Blue Zones: ").append(blueClicks).append("\n");
        message.append("Total Targets: ").append(targetClicks).append("\n");

        double percentage = (double) blueClicks / targetClicks;
        DecimalFormat df = new DecimalFormat("0.00%");
        String percentageFormatted = df.format(percentage);

        message.append("Blue Percentage: ").append(percentageFormatted).append("\n");

        message.append("Red Spawned: ").append(redSpawned).append("\n");

        JOptionPane.showMessageDialog(null, message.toString(), "GAME END STATS", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (timesClickedOnRed == 0 && greenSquaresMissed == 0) {
            clockTime++;
            if ((clockTime != 0) && (clockTime % 2 == 0)) {
                currSeconds++;
                timeString = "    Time: " + currSeconds;
                scorePanel.repaint();
            }
            spawnSquares(clockTime);
        } else {
            timer.stop();
            showGameOverDialog();
        }
    }

    public void spawnSquares(int gameTime) {
        int max = MAX_SIZE;
        int min = MIN_SIZE;

        // Create an iterator to safely remove elements
        Iterator<Target> iterator = squares.iterator();

        while (iterator.hasNext()) {
            Target s = iterator.next();
            if (s.getTimeSpawned() < gameTime - 3) {
                if (s.getColor().equals(new Color(0, 155, 0))) {
                    greenSquaresMissed++;
                    System.out.println("Green squares missed:" + greenSquaresMissed);
                    gameOverString = "Missed Green Square";
                    scorePanel.repaint();
                }
                iterator.remove();
            }
        }
        
       

        if(gameTime < 30){
            count = r.nextInt(2) + 1;
            r.nextInt(10); 
        }
        else if(gameTime < 60){
            count = r.nextInt(3) + 1;
            min = min - 10; 
        }
        else if(gameTime < 90){
            count = r.nextInt(4) + 1;
            min = min - 20; 
        }
        else if(gameTime < 120){
            count = r.nextInt(5) + 1;
            min = min - 30; 
        }
        
        for (int i = 0; i < count; i++) {
            int size = min + r.nextInt(max - min);
            

            Point point = new Point(r.nextInt(PANEL_SIZE - size), r.nextInt(PANEL_SIZE - size));

            Color color;
            int greenToRedRatio = r.nextInt(10); 
            
            if(greenToRedRatio < 3){
                color = new Color(255,0,0);
                redSpawned++; 
            }
            else{
                color = new Color(0,155,0); 
            }
            
            squares.add(new Target(size, point, color, gameTime));

    }
        panel.repaint();    
    }   

    

    public static void main(String args[]) {

        
        Random rand = new Random();
        int count = rand.nextInt(10);
        count = 5;

        
        javax.swing.SwingUtilities.invokeLater(new AimGame(count));
    }

    

}


//SQUARE TARGET THAT WILL BE PLACED IN CANVAS

//EITHER GREEN OR RED 



class Target {

    // some named constants to define shapes
    public static final int SQUARE = 1;
    

    // shape info
    
    private int size;
    private Point upperLeft;
    private Color color;
    private int timeSpawned; 

    public Target(int size, Point upperLeft, Color color, int timeSpawned) {

        
        this.timeSpawned = timeSpawned;  
        this.size = size;
        this.color = color;
        this.upperLeft = new Point(upperLeft);
    }

    public Color getColor(){
        return color; 
    }
    /**
     * paint this object onto the given Graphics area
     * 
     * @param g the Graphics object where the shape should be drawn
     */
    public void paint(Graphics g) {

        g.setColor(color);
        g.fillRect(upperLeft.x, upperLeft.y, size, size);
        if(color.equals(new Color(0,155,0))){
            g.setColor(new Color(0,0,255));

            //FILL CENTER WITH BLUE TARGET FOR TRIPLE POINTS
            g.fillRect(upperLeft.x + size/2 - size/10, upperLeft.y + size/2 - size/10, size/5, size/5);
        }
        
        g.setColor(new Color(0,0,0));
        g.fillRect(upperLeft.x - (size/20), upperLeft.y - (size/20), size + size/20, size/20);
        g.fillRect(upperLeft.x - (size/20), upperLeft.y - (size/20), size/20, size + size/20);
        g.fillRect(upperLeft.x - (size/20), upperLeft.y + size , size + size/20, size/20); 
        g.fillRect(upperLeft.x + size, upperLeft.y - (size/20), size/20, size + size/20);

        //BOTTOM RIGHT SQUARE
        g.fillRect(upperLeft.x + size, upperLeft.y + size, size/20, size/20);
        
    }
    
    public int getTimeSpawned(){
        return timeSpawned;
    }

    /**
     * Determine if the given point is within this shape.
     * 
     * @param p Point to check
     */
    public int contains(Point p) {
        if(p.x >= upperLeft.x + size/2 - size/10 && p.x <= upperLeft.x + size/2 - size/10 + size/5 && p.y >= upperLeft.y + size/2 - size/10 && p.y <= upperLeft.y + size/2 - size/10 + size/5){
            return 2; 
        }
        else if(p.x >= upperLeft.x && p.x <= upperLeft.x + size && p.y >= upperLeft.y && p.y <= upperLeft.y + size){
            return 1;
        }
        else{
            return 0; 
        }
        
    }


}