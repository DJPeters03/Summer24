import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList; 

public class PoleCart extends KeyAdapter implements Runnable, ActionListener {

    // CONSTANTS
    public static final int PANEL_WIDTH = 1000;
    public static final int PANEL_HEIGHT = 500;

    private Cart cart = new Cart();
    private Pole pole = new Pole();
    private JPanel panel;
    private int attempts = 1;
    private boolean gameOver = false;
    private int uprightCounter = 0;

    private boolean aiMode = false; 
    private int solved = 0;

    private int clock = 0;
    private int clockInc = 0;  
    private int timeToSolve = 0; 

    public ArrayList<Log> log = new ArrayList<Log>(); 
   

    public Timer timer = new Timer(100, this);

    @Override
    public void run() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("PoleCart");
        frame.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.fillRect(0, 350, PANEL_WIDTH, 1);
                cart.paint(g);
                pole.paint(g, cart.cartX + Cart.CART_WIDTH / 2, Cart.CART_STARTING_Y);
                g.setColor(Color.BLACK);
                if(aiMode){
                    g.drawString("Solved: " + solved, 10, 20);
                }
                else{
                    g.drawString("Attempt #: " + attempts, 10, 20);
                }
                g.drawString("Time: " + clock, 100, 20);
                
                if (gameOver) {
                    if(attempts != 1){
                        g.drawString("Congratulations! It took " + attempts + " attempts and " + clock + "seconds !", PANEL_WIDTH / 2 - 100, PANEL_HEIGHT / 2);
                    }
                    else{
                        g.drawString("Congratulations! It took only " + attempts + " attempt and " + clock + "seconds !", PANEL_WIDTH / 2 - 100, PANEL_HEIGHT / 2);
                            
                    }
                }
            }
        };

        panel.addKeyListener(this);
        panel.setFocusable(true);

        frame.add(panel);
        
        showRulesDialog(frame);
        frame.pack();
        frame.setVisible(true);

        
            
        
    }

    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            pole.update();
            if (pole.hasFallen()) {
                attempts++;
                pole.resetPosition(); // Reset the position but keep the same length and angle
                cart.reset();
                uprightCounter = 0;
            } else if (Math.abs(pole.getAngle()) < 0.01) {
                uprightCounter++;
                if (uprightCounter >= 5) {
                    if(aiMode){
                        solved++; 
                        //START NEW GAME   
                        
                        log.add(new Log(cart.timesOverShot, cart.curMoves, solved));
                        
                       if(solved % 5 == 0){
                           log.get(solved - 1).adjustAi(log); 
                        }
                        cart.curMoves = 0;
                        cart.timesOverShot = 0;
                        cart.reset(); 
                        pole.reset(); 
                        System.out.println("Time it took to solve #" + solved + ": " + timeToSolve + " seconds");
                        uprightCounter = 0; 
                        timeToSolve = 0; 


                       
                        
                        
                    }
                    else{
                        gameOver = true;
                    }
                }
            } else {
                uprightCounter = 0;
            }
            
        
            if(aiMode){
                pole.decideToMove(pole.angularVelocity, pole.angle);
            }
            clockInc++; 
            if(clockInc == 10){
                clock++; 
                timeToSolve++; 
                clockInc = 0; 
            }
            panel.repaint();
        }
    }
    

    private void showRulesDialog(JFrame frame) {
        JDialog dialog = new JDialog(frame, "Game Rules", true);
        dialog.setLayout(new BorderLayout());
        

        String rules = "<html>Welcome to PoleCart!<br><br>" +
                       "Rules: <br>" +
                       "1. Use the arrow keys or A and D to move the cart left and right <br>" +
                       "2. Play until you are able to balance the pole <br>" +
                       "3. Press start or the X in the corner to start <br>" +
                       "4. Press AI mode to watch an endless AI game<br><br>" +
                       "Good Luck!</html>";


        JLabel rulesLabel = new JLabel(rules); 
        dialog.add(rulesLabel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton buttonNorm = new JButton("Start");
        
        JButton buttonAi= new JButton("AI");

        buttonNorm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               dialog.dispose();
               startGame(); 
            }
        });

        buttonAi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aiMode = true;
                dialog.dispose();
                startGame(); 
            }
        });

        buttonPanel.add(buttonNorm);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dialog.dispose();
                startGame(); 
            }
        });


        buttonPanel.add(buttonNorm);
        buttonPanel.add(buttonAi);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    public void startGame(){
        timer.start();
    }

    public void keyPressed(KeyEvent e) {
        int event = e.getKeyCode();
        if(!aiMode){
        if (event == KeyEvent.VK_LEFT || event == KeyEvent.VK_RIGHT || event == KeyEvent.VK_A || event == KeyEvent.VK_D) {
            if (event == KeyEvent.VK_LEFT || event == KeyEvent.VK_A) {
                cart.cartLeft();
               // pole.applyForce(0.05); // Compensate rightward falling by adding force to the left
            } else {
                cart.cartRight();
                //pole.applyForce(-0.05); // Compensate leftward falling by adding force to the right
            }
            panel.repaint();
        }
    }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new PoleCart());
    }


    class Cart {
        public static final int CART_WIDTH = 100;
        public static final int CART_HEIGHT = 60;
        public static final int CART_STARTING_X = 450;
        public static final int CART_STARTING_Y = 320;
        public static final int CART_MAX_X = 900;
        public static final int CART_MIN_X = 0;
        public static final int INC = 30;

        public Color color;
        public int cartX;

        public boolean lastMoveLeft = false; 
        public boolean lastMoveRight = false;


        //times a movement sends the bar beyonds the score zone
        public int timesOverShot = 0;
        
        //moves per reset 
        public int curMoves = 0; 




        public Cart() {
            color = new Color(171, 141, 76);
            cartX = CART_STARTING_X;
        }

        public void paint(Graphics g) {
            g.setColor(color);
            g.fillRect(cartX, CART_STARTING_Y, CART_WIDTH, CART_HEIGHT);
        }

        public void cartLeft() {
            if (cartX > INC) {
                cartX -= INC;
                
                if(lastMoveRight){
                    timesOverShot++;
                    //System.out.println("Times overshot: " + timesOverShot);
                }

                lastMoveLeft = true; 
                lastMoveRight = false; 
                //System.out.println("Left. Force: " + pole.angularVelocity + " Angle: " + pole.getAngle()); 
                pole.applyForce(0.05);
                
            } else {
                cartX = 0;
            }
        }

        public void cartRight() {
            if (cartX < CART_MAX_X - INC) {
                cartX += INC;

                if(lastMoveLeft){
                    timesOverShot++;
                   // System.out.println("Times overshot: " + timesOverShot);
                }

                lastMoveLeft = false; 
                lastMoveRight = true; 
                //System.out.println("Right. Force: " + pole.angularVelocity + " Angle: " + pole.getAngle()); 
                pole.applyForce(-0.05);
                
            } else {
                cartX = CART_MAX_X;
            }
        }

        public void addMove(){
           curMoves ++; 
           //System.out.println("Moves"  + curMoves);
        }



        public void reset() {
            cartX = CART_STARTING_X;
        }
    }

    class Pole {
        private double angle;
        private double angularVelocity;
        private double length;
        private static final int POLE_WIDTH = 10;
        private final double initialAngle;
        private final double initialLength;
        private Random rand; 


        public double angleMinPositive = .049; 
        public double angleMaxPositive = .7; 
       
       
        public double angleMinNegative = -.4; 
        public double angleMaxNegative = -.049;


        public Pole() {
            rand = new Random();
            initialAngle = rand.nextDouble() * Math.PI / 4 - Math.PI / 8;
            initialLength = 100 + rand.nextInt(101);
            reset();
        }

        public void reset() {
            angle = rand.nextDouble() * Math.PI / 4 - Math.PI / 8;
            angularVelocity = 0;
            length = 100 + rand.nextInt(101);
        }

        public void resetPosition() {
            angle = initialAngle;
            angularVelocity = 0;

        }

        public void update() {
            double gravity = 0.01;
            angularVelocity += gravity * Math.sin(angle);
            angle += angularVelocity;
        }

        public void applyForce(double force) {
            angularVelocity += force;
        }

        public boolean hasFallen() {
            return Math.abs(angle) > Math.PI / 2;
        }

        public double getAngle() {
            return angle;
        }
        public void decideToMove(double force, double curAngle){
            
            
            
            
            
            
            
            
            if(force >= 0 && force <= .07){
                    if(angle >= angleMinPositive && angle <= angleMaxPositive){
                        cart.addMove(); 
                        cart.cartRight(); 
                    }
            }
            else if(force >= -.07 && force <= -.005){
                if(angle >= angleMinNegative && angle <= angleMaxNegative){
                    cart.addMove(); 
                    cart.cartLeft(); 

                }
            }
        }

        public void paint(Graphics g, int x, int y) {
            int x2 = (int) (x + length * Math.sin(angle));
            int y2 = (int) (y - length * Math.cos(angle));
            g.setColor(Color.BLACK);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(POLE_WIDTH));
            g2d.drawLine(x, y, x2, y2);


           
        }
    }

    
    class Log{
        public int overshot; 
        public int moves; 
        public int puzzNum; 




        public Log(int overshot, int moves, int puzzNum){
            this.overshot = overshot; 
            this.moves = moves; 
            this.puzzNum = puzzNum; 
            printLog(); 

            
        }

         

        public void printLog(){
            System.out.println("Puzzle " + puzzNum + " Moves "  + moves + " Overshoots " + overshot);
        }

        public void adjustAi(ArrayList<Log> log){
            int lastAttempt = log.get(log.size() - 1).puzzNum;
            int totalTimesOvershot = 0; 
            int totalMoves = 0;


            for(int i = 0; i < 5; i++){
                totalMoves = totalMoves + log.get(log.size() - i -  1).moves; 
                totalTimesOvershot = totalTimesOvershot + log.get(log.size() - i -  1).overshot;
            }
            
            if(totalMoves > 50){
                if(totalTimesOvershot > 30){
                    overshooting(); 
                }
                else{
                    undershooting(); 
                }
            }
            

        }   


        //WAITING FOR POLE TO FALL MORE BEFORE MOVING 
        //TO ADJUST FOR IT PUSHING THE POLE OVER THE GOAL SPOT 
        public void overshooting(){
            pole.angleMinPositive += .001; 
            pole.angleMaxNegative -= .001; 
            System.out.println("move angle lower due to overshooting");
        }

        //MOVING THE POLE AT A HIGHER ANGLE 
        //TO ADJUST FOR IT NOT PUSHING THE POLE UP HIGH ENOUGH  
        public void undershooting(){
            pole.angleMinPositive -= .001; 
            pole.angleMaxNegative += .001; 
             System.out.println("move angle higher due to undershooting"); 
        }


    }
        
}

