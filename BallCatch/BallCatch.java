import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.Timer;

public class BallCatch extends KeyAdapter implements Runnable, ActionListener {

    // CONSTANTS
    public static final int PANEL_WIDTH = 1050;
    public static final int PANEL_HEIGHT = 750;

    private Cart cart = new Cart();
    private JPanel panel;
    private Random r = new Random();
    
    
    private Timer timer = new Timer(50, this);
    private int spawnDelay = 0;
    
  
  
    private int blackBallsSpawned = 0; 
    private int blueBallsSpawned  = 0; 
    private int redBallsSpawned  = 0; 
    private int greenBallsSpawned  = 0; 
    private int yellowBallsSpawned = 0; 
    private int totalBallsSpawned = 0; 
    

    private int blackBallsCaught = 0; 
    private int blueBallsCaught = 0; 
    private int redBallsCaught = 0; 
    private int greenBallsCaught = 0; 
    private int yellowBallsCaught = 0; 
    private int totalBallsCaught = 0; 


    private int blackBallsShot = 0; 
    private int blueBallsShot = 0; 
    private int redBallsShot = 0; 
    private int greenBallsShot = 0; 
    private int yellowBallsShot = 0; 
    private int totalBallsShot = 0; 


    //private int ballsExploded = 0; 

    private int score = 0;
    private String scoreString = "Score: " + score;
    
    private JPanel scorePanel;
    private JLabel scoreLabel;



    private boolean cantMove = false; 
    private boolean start5SecondTimer = true; 
    private int fiveSecondTimer = 0;
    private boolean cartFlash = false;


    private boolean frenzy = false; 
    private int tenSecondTimer = 0; 

    private boolean boostTimer = false; 
    private int boostClock = 0; 

    private boolean canFire = false; 
    private int shotClock = 0; 


    private int numberPressed; 
    private int gameOverTick = 0; 
    private boolean countDown = true; 

    private int clockTimeOnScreen = 0; 
    private String timeString = "Time: " + clockTimeOnScreen;


    private java.util.List<Target> circles = new ArrayList<Target>();
    public ArrayList<Ammo> shots = new ArrayList<Ammo>(); 

    public ArtificialGame AI; 
    public boolean aiMode = false; 


    public boolean redNeighbor = false; 

    // Overriding
    @Override
    public void run() {
        
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("BallCatch");
        frame.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT + 70));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                

                //HITBOXES 
                 /* 
                //AI X ZONES
                g.fillRect(150, 0, 1, 800);
                g.fillRect(300, 0, 1, 800);
                g.fillRect(450, 0, 1, 800);
                g.fillRect(600, 0, 1, 800);
                g.fillRect(750, 0, 1, 800);
                g.fillRect(900, 0, 1, 800);



                //AI Y ZONES
                g.setColor(new Color(255,0,0));
                g.fillRect(0, 200, 1050, 1);
                g.fillRect(0, 400, 1050, 1);
                g.fillRect(0, 600, 1050, 1);
                g.fillRect(0, 625, 1050, 1);
            
            
                g.setColor(new Color(0,0,0));
                //CART HITBOX
                g.drawRect(0, cart.CART_STARTING_Y, 1500, 1);
                g.drawRect(0, cart.CART_STARTING_Y + cart.CART_HEIGHT, 1500, 1);

                */


                



                cart.paint(g);
                for (int i = 0; i < circles.size(); i++) {
                    circles.get(i).paint(g);
                }
                if(canFire){
                for(Ammo s: shots){
                    s.paint(g); 
                }
            }
            }
        };

        panel.addKeyListener(this);
        panel.setFocusable(true);

        scoreLabel = new JLabel(scoreString + "    " + timeString);
        scorePanel = new JPanel();
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scorePanel.add(scoreLabel);

        frame.setLayout(new BorderLayout());
        frame.add(scorePanel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);

        showRulesDialog(frame);
        frame.pack();
        frame.setVisible(true);

        
    }
    
    
    
    public void keyPressed(KeyEvent e){
        int event = e.getKeyCode(); 
        if(cantMove == false){
            if(cart.color.equals(new Color(255,0,0))){
                cart.color = new Color(0,0,0); 
            }
            
            if(event == KeyEvent.VK_W || event == KeyEvent.VK_UP){
                if(canFire){
                    cart.launchProjectile(); 
                }
            }

            if(event == KeyEvent.VK_A || event == KeyEvent.VK_D || event == KeyEvent.VK_RIGHT || event == KeyEvent.VK_LEFT){
            
                if(event == KeyEvent.VK_A || event == KeyEvent.VK_LEFT){
                    cart.cartLeft(); 
                 
                }
                else{
                    cart.cartRight();
                }


                panel.repaint(); 
            
             }   
        }
    }
    

    public void start(){
        timer.start(); 
    }

    @Override
    public void actionPerformed(ActionEvent e){
        //CODE FOR GREEN BALL <--> FRENZY 
        if(frenzy){
            if(spawnDelay == 0 || spawnDelay == 5 || spawnDelay == 10 || spawnDelay == 15){
                spawnTarget();
            }
            tenSecondTimer++; 
        }
        //NORMAL SPAWN 
        else if(spawnDelay == 0){
            spawnTarget();
        }

        //CODE FOR GREEN BALL <--> FRENZY
        if(tenSecondTimer == 200){
            tenSecondTimer = 0; 
            frenzy = false; 
        }

        //CODE FOR BLUE BALL <--> CART SIZE BOOST 
        if(boostTimer){
            boostClock++; 
        }
        if(boostClock == 200){

            cart.normalWidth(); 
            boostClock = 0; 
            boostTimer = false; 
            
        }
        

        //CODE FOR RED BALL <--> CART STUN 
        if(start5SecondTimer == true){
            fiveSecondTimer++; 
        }
        if(fiveSecondTimer == 100){
            fiveSecondTimer = 0;
            start5SecondTimer = false; 
            cantMove = false; 
            cartFlash = false;
        }
        if(cartFlash == true){
            if(spawnDelay == 0 || spawnDelay == 10){
                cart.flashCart();
            } 
        }

        //CODE FOR YELLOW BALL <--> SHOOTER 
         
        if(canFire){
            shotClock++; 
            for(Ammo s: shots){
                    s.launched(); 
            }
        }

       

        if(shotClock == 200){
            shotClock = 0; 
            canFire = false; 
        }


        //AI MODE CODE 
        if(aiMode){
            if(gameOverTick % 2 == 0){
                if(cantMove == false){
                    if(cart.color.equals(new Color(255,0,0))){
                        cart.color = new Color(0,0,0);
                    }

                    AI.checkZones(); 

                }
            }
            
            if(canFire){
                if(cantMove == false){
                    cart.launchProjectile(); 
                }
            }
        }


        //CODE FOR GAME LENGTH
        if(gameOverTick == 20 * numberPressed){
            endGame(); 
        }
        gameOverTick++; 

        if(gameOverTick % 20 == 0){
            if(countDown){
                clockTimeOnScreen--;
            }
            else{
                clockTimeOnScreen++;
            }
            timeString = "Time: " + clockTimeOnScreen;
            scoreLabel.setText(scoreString + "    " +timeString);

        }
        
         
        for(Target c : circles){ 
            if(c.caught){
                if(c.dead != true){
                if(c.color.equals(new Color(255,0,0))){
                    cart.stun(); 
                    redBallsCaught++;
                    System.out.println("Times Stunned: " + redBallsCaught);
                }
                else if(c.color.equals(new Color(255, 255, 0))){
                    score++;
                    shots = new ArrayList<Ammo>(); 
                    cart.canFire(); 
                    yellowBallsCaught++;
                    
                }
                else if(c.color.equals(new Color(0,255,0))){
                    score++; 
                    cart.frenzy(); 
                    greenBallsCaught++;
                }
                else if(c.color.equals(new Color(0,0,255))){
                    score++; 
                    cart.sizeBoost(); 
                    blueBallsCaught++;
                }
                else{
                    score++; 
                     
                    blackBallsCaught++;
                }
            
                totalBallsCaught++; 
                c.dead = true; 
                scoreString = "Score: " + score;
                scoreLabel.setText(scoreString + "    " +timeString);
            
                }
                
            }



            if(c.getY() > 770){
                c.stopTheFall();
            }


            c.fall(); 

        }  


        
        

        spawnDelay++;
        if(spawnDelay == 20){
            spawnDelay = 0; 
        }

        panel.repaint(); 
        
    }   

    
    public static void main(String args[]){
        SwingUtilities.invokeLater(new BallCatch()); 
    }

    public void spawnTarget(){
        circles.add(new Target()); 
    }

    private void showRulesDialog(JFrame frame) {
        JDialog dialog = new JDialog(frame, "Game Rules", true);
        dialog.setLayout(new BorderLayout());

        String rules = "<html>Welcome to BallCatch!<br><br>" +
                       "Rules: <br>" +
                       "1. Catch all target balls falling down the screen except EXCEPT FOR RED ONES <br>" +
                       "2. Use Arrow Keys or the WAD keys to move <br>" +
                       "3. Red targets will stun all movement for 5 seconds <br>" +
                       "4. Blue targets will increase the paddle size for 10 seconds <br>" +
                       "5. Green targets will spawn tons of targets for 10 seconds <br>" +
                       "6. Yellow targets are a suprise buff... PRESS/HOLD W OR UP for 10 seconds <br>" +
                       "7. Black targets are neutral <br>" +
                       "8. Each target collected grants 1 point besides Red<br>" +
                       "9. Press a number button to play a game with that many seconds<br>" +
                       "10. Press the AI button to watch AI play the game!<br><br>" +
                       "... (or click the X in the corner for endless mode!)</html>";
       

        JLabel rulesLabel = new JLabel(rules); 
        dialog.add(rulesLabel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton button30 = new JButton("30");
        JButton button60 = new JButton("60");
        JButton button120 = new JButton("120");
        JButton buttonAi= new JButton("AI");

        button30.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberPressed = 30;
                clockTimeOnScreen = 30; 
                dialog.dispose();
                startGame();
            }
        });

        button60.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberPressed = 60;
                clockTimeOnScreen = 60; 
                dialog.dispose();
                startGame();
            }
        });

        button120.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberPressed = 120;
                clockTimeOnScreen = 120; 
                dialog.dispose();
                startGame();
            }
        });
        buttonAi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberPressed =  99999;
                countDown = false; 
                dialog.dispose();
                AI = new ArtificialGame();
                aiMode = true; 
                
                startGame();
            }
        });

        buttonPanel.add(button30);
        buttonPanel.add(button60);
        buttonPanel.add(button120);
        buttonPanel.add(buttonAi);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                numberPressed =  99999;
                countDown = false; 
                dialog.dispose();
                startGame();
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void startGame(){
        timeString = "Time: " + clockTimeOnScreen; 
        scoreLabel.setText(scoreString + "    " + timeString);
        timer.start();
    }
    private void endGame(){
        timer.stop(); 
        showGameOverDialog(); 
    }
    private void showGameOverDialog() {
        StringBuilder message = new StringBuilder();
    
        message.append("<html>");
        message.append(" " + numberPressed).append(" second Game End!\n\n");
        message.append("Score: ").append(score).append("\n\n");
        message.append(String.format("Black Targets  - Spawned: %d  Caught: %d  Shot: %d\n", blackBallsSpawned, blackBallsCaught, blackBallsShot));
        message.append(String.format("Blue Targets   - Spawned: %d  Caught: %d  Shot: %d\n", blueBallsSpawned, blueBallsCaught, blueBallsShot));
        message.append(String.format("Yellow Targets - Spawned: %d  Caught: %d  Shot: %d\n", yellowBallsSpawned, yellowBallsCaught, yellowBallsShot));
        message.append(String.format("Green Targets  - Spawned: %d  Caught: %d  Shot: %d\n", greenBallsSpawned, greenBallsCaught, greenBallsShot));
        message.append(String.format("Red Targets    - Spawned: %d  Caught: %d  Shot: %d\n", redBallsSpawned, redBallsCaught, redBallsShot));
        message.append(String.format("Total Targets  - Spawned: %d  Caught: %d  Shot: %d\n", totalBallsSpawned, totalBallsCaught, totalBallsShot));
        message.append("Targets Exploded -").append(score - (totalBallsCaught + totalBallsShot)).append("\n\n");
    
        JOptionPane.showMessageDialog(null, message.toString(), "GAME END STATS", JOptionPane.INFORMATION_MESSAGE);
    }
    




    class Cart{
        public static final int CART_WIDTH = 150; 
        public static final int CART_HEIGHT = 30; 
        public static final int CART_STARTING_X = 450; 
        public static final int CART_STARTING_Y = 685; 
        
        public static final int CART_MAX_X = 900; 
        public static final int CART_MIN_X = 0; 



        public static final int INC = 150; 


        
        
        
        
        
        public Color color; 

        //CAN ONLY CHANGE X, Y CAN NOT CHANGE 
        public int cartX; 
        public int cartCurWidth; 
        public int cartMaxX; 
        
        
        
        public Cart(){
            color = new Color(0, 0, 0);
            cartX = CART_STARTING_X; 
            cartCurWidth = CART_WIDTH;
            cartMaxX = PANEL_WIDTH - cartCurWidth; 
        }

        public void paint(Graphics g){
            g.setColor(color);
            g.fillRect(cartX, CART_STARTING_Y, cartCurWidth, CART_HEIGHT); 

            g.setColor(new Color(0,0,0));
            
        }


        public void cartLeft(){
            if(cartX > INC){
                cartX -= INC; 
            }
            else{
                //System.out.println("Reached left end");
                cartX = 0; 
            }
            
        }
        
        public void cartRight(){
            if(cartX < cartMaxX - INC){
                cartX += INC; 
            }
            else{
                //System.out.println("Reached right end");
                cartX = cartMaxX; 
            }
            
        }

        public int getX(){
            return cartX; 
        }


        public boolean touchedCart(int ballLeftX, int ballRightX){
            boolean returnVal = false; 
            
            if(ballLeftX > cartX && ballLeftX < (cartX + cartCurWidth)){
                returnVal = true;
            }
            if(ballRightX > cartX && ballRightX < (cartX + cartCurWidth)){
                returnVal = true;
            }


            return returnVal; 
        }

        public void stun(){
            start5SecondTimer = true; 
            fiveSecondTimer = 0; 
            cantMove = true; 
            cartFlash = true; 
        }

        public void flashCart(){
            if(color.equals(new Color(0, 0, 0)) || color.equals(new Color(0, 0, 255))){
                color = new Color(255, 0, 0);
            }
            else{
                if(boostTimer){
                    color = new Color(0,0,255);
                }
                else{

                color = new Color(0,0,0);
                }
            }
             
        }
        public void frenzy(){
            frenzy = true; 
            tenSecondTimer = 0; 
        }
        
        public void canFire(){
            canFire = true;
            shotClock = 0; 
        }

       
        public void sizeBoost(){
            boostTimer = true; 
            boostClock = 0; 

            if(cartX > 750){
                cartX = 750; 
            }
            if(cartCurWidth == CART_WIDTH){
            cartCurWidth = cartCurWidth * 2; 
            cartMaxX = PANEL_WIDTH - cartCurWidth;
            color = new Color(0,0,255);
            }
        }

        public void normalWidth(){
            cartCurWidth = CART_WIDTH; 
            cartMaxX = PANEL_WIDTH - cartCurWidth;
            color = new Color(0,0,0);
        }

        public void launchProjectile(){
            

            if(aiMode){
                if(shotClock % 5 == 0 ){
                    shots.add(new Ammo(cartX + (cartCurWidth/2) - 7));
                    shots.add(new Ammo(cartX + (cartCurWidth/4) - 7));
                    shots.add(new Ammo(cartX + (cartCurWidth/2) + (cartCurWidth/4) - 7));
                    if(boostTimer){
                        shots.add(new Ammo(cartX + (cartCurWidth/2) + (cartCurWidth/8) - 7));
                        shots.add(new Ammo(cartX + (cartCurWidth/2) - (cartCurWidth/8) - 7));
                    }
                 }
            }
            else{
               
                    shots.add(new Ammo(cartX + (cartCurWidth/2) - 7));
                    shots.add(new Ammo(cartX + (cartCurWidth/4) - 7));
                    shots.add(new Ammo(cartX + (cartCurWidth/2) + (cartCurWidth/4) - 7));
                    if(boostTimer){
                        shots.add(new Ammo(cartX + (cartCurWidth/2) + (cartCurWidth/8) - 7));
                        shots.add(new Ammo(cartX + (cartCurWidth/2) - (cartCurWidth/8) - 7));
                    }
                
            }
            
            
            
            
    }
    }





    class Target{

        private final int SIZE = 30; 
        private static final int MIN_X = 15; 
        private static final int MAX_X = 1020; 


        private boolean caught; 
        public int targetX; 

        public int curY; 

        public Color color; 

        public boolean stopFalling = true; 
        public boolean dead = false; 


        public boolean boom = false; 
       

        

        public Target(){
            caught = false; 
            curY = -30; 
            targetX = r.nextInt(MAX_X - MIN_X);


            int colorGen = r.nextInt(100);
            
            if(frenzy){
                colorGen = r.nextInt(95);
            }
            if(canFire == true){
                while(colorGen > 80 && colorGen < 90){
                    colorGen = r.nextInt(100);
                } 
            }
        

            if(colorGen < 60){
                color = new Color(0,0,0);
                blackBallsSpawned++;
                
            }
            else if(colorGen < 80){
                color = new Color(255, 0 , 0); 
                redBallsSpawned++;
            }
            else if(colorGen < 90){
                color = new Color(255, 255,0);
                yellowBallsSpawned++;
            }
            else if(colorGen < 95){
                int returnYellowSquigle; 
                //if(aiMode == false){
                color = new Color(0, 0, 255);
                blueBallsSpawned++;
                //}
                //else{
                   // color = new Color(0, 0, 0);
                   // blackBallsSpawned++;
                //}
            }
            else{
                color = new Color(0,255,0);
                greenBallsSpawned++;
            }
            totalBallsSpawned++; 
        }  

        public void stopTheFall(){
            dead = true; 
        }

        public void fall(){
            if(dead == false){
            if(curY >= (685 - SIZE) && curY <= 715){
               if(cart.touchedCart(targetX, targetX + SIZE)){
                    caught = true; 
                    
               }
            }
            
            
            if(stopFalling){
            curY = curY + 5; 
            }
        }
        }


        public int getY(){
            return curY; 
        }


        public void explode(){
    
            for(Target c: circles){
                if(c.dead == false){
                    boolean blowUp = false; 
                    
                    boolean leftCheck = false; 
                    boolean rightCheck = false; 
                    boolean upCheck = false; 
                    boolean downCheck = false; 

                    if((c.targetX > targetX - 75) || ((c.targetX + c.SIZE > targetX - 75))){
                        leftCheck = true; 
                    }

                    if(c.targetX < targetX + 75 + SIZE){
                        rightCheck = true; 
                    }

                    if((c.curY > curY - 75) || (c.curY + c.SIZE > curY - 75)){
                        upCheck = true; 
                    }

                    if(c.curY < curY + 75 + SIZE){
                        downCheck = true; 
                    }




                    if(leftCheck && rightCheck && upCheck && downCheck){
                        blowUp = true; 
                    }

                    if(blowUp){ 
                       
                        score++; 
                            scoreString = "Score: " + score; 
                            scoreLabel.setText(scoreString + "    " +timeString);
                        c.dead = true; 
                        //ballsExploded++; 
                    }
                }
            }

           
        }

        public void boom(){
            boom = true; 
            dead = true; 
        }

        public void paint(Graphics g){
            
            
            if(dead == false){
            g.setColor(color); 

            g.fillOval(targetX, curY, SIZE, SIZE);
            }

            if(boom == true){
                g.setColor(color);
                g.fillRect(targetX - 75, curY - 75, SIZE + 150, SIZE + 150);
                boom = false; 
            }

            
        }
    }

    class Ammo{
        private static final int SIZE = 14; 
        
        public int xSpawn; 
        public int currentY; 

        public boolean hit; 
        public boolean dead = false; 

        public boolean boom; 
       
        public Ammo(int xSpawn){
            this.xSpawn = xSpawn; 
            currentY = 685; 
        }

        public void launched(){
            currentY -= 10;  

            if(dead == false){
                for(Target c: circles){
                    if(c.dead == false){
                        if((currentY > c.curY && currentY <c.curY + c.SIZE) && ((xSpawn > c.targetX && xSpawn < c.targetX + c.SIZE) || (xSpawn + SIZE > c.targetX && xSpawn + SIZE < c.targetX + c.SIZE))){
                            //System.out.println("SHOT A BALL");
                            //score++;
                            
                            
                            if(c.color.equals(new Color(255,0,0))){
                                redBallsShot++;
                            }
                            else if(c.color.equals(new Color(255, 255, 0))){
                                cart.canFire(); 
                                yellowBallsShot++;
                            }
                            else if(c.color.equals(new Color(0,255,0))){
                                cart.frenzy(); 
                                
                                greenBallsShot++;
                            }
                            else if(c.color.equals(new Color(0,0,255))){
                                
                                cart.sizeBoost(); 
                                blueBallsShot++;
                            }
                            else{
                                blackBallsShot++;
                            }
                            totalBallsShot++;
                            
                            
                            
                            
                            
                            c.explode(); 
                            c.boom(); 
                            
                              
                            
                            
                            
                            
                            c.dead = true; 
                            dead = true; 
                        }
                    }
                }
            }


        }

        public void paint(Graphics g){
            if(dead == false){
                g.setColor(new Color(255, 255, 0));

                g.fillOval(xSpawn, currentY, SIZE, SIZE);

                
            }
        }
    }




    class ArtificialGame{
        //ALL OF THE ZONES TO CHECK
       /* 
        int zone1Start = 0;
        int zone2Start = 150;
        int zone3Start = 300;
        int zone4Start = 450;
        int zone5Start = 600;
        int zone6Start = 750;
        int zone7Start = 900;
        */

        private ArrayList<Integer> zoneBounds; 



        private int maxY =  720;
        private int minY = 200; 


        private int desiredX; 


        private ArrayList<DangerousTargets> borderAvoid = new ArrayList<DangerousTargets>(); 

        //private boolean redNeighbor = false; 


        public ArtificialGame(){
            //ADD NUMBERS FROM 0-1050 INCREMENTING BY 150, CART SIZE IS 150 AND 1050 IS THE BORDER END
            zoneBounds = new ArrayList<Integer>(); 
            int inc = 0; 
            for(int i = 0; i <= 7; i++){
                zoneBounds.add(inc);
                inc += 150; 
            }
        } 

        public void checkZones(){
            int curScore; 
            int maxScore = -50; 
            int incOfMax = 0; 

            if(boostTimer){
                for(int i = 0; i < 6; i++){
                    curScore = getZoneScore(zoneBounds.get(i), zoneBounds.get(i+2), minY, maxY);
                if(i == 0){
                    maxScore = curScore; 
                    incOfMax = i; 
                }
                if(curScore > maxScore){
                    maxScore = curScore; 
                    incOfMax = i; 
                }
                }
            }
            else{
            for(int i = 0; i < 7; i++){
                curScore = getZoneScore(zoneBounds.get(i), zoneBounds.get(i+1), minY, maxY);
                if(i == 0){
                    maxScore = curScore; 
                    incOfMax = i; 
                }
                if(curScore > maxScore){
                    maxScore = curScore; 
                    incOfMax = i; 
                }
            }
            }

            desiredX = zoneBounds.get(incOfMax); 
            autoMove(); 
            
            
        
       
        }

        public int getZoneScore(int minX, int maxX, int minY, int maxY){
            int returnVal = 0; 
            //COUNT ALL THE BALLS IN THE AREA
            //RUNNING TOTAL +1 for EVERY BALL, EXCEPT RED ONES, -5 FOR RED ONES
            
            
            for(Target c: circles){
                if(c.dead != true){
                    if((c.targetX > minX && c.targetX < maxX) || c.targetX + c.SIZE > minX && c.targetX /*+ c.SIZE*/ < maxX){
                        //CURRENTLY PASSES THE X CHECK
                        if(c.curY + c.SIZE > minY && c.curY < maxY){
                            //CURRENTLY PASSES THE Y CHECK TOO

                            if(c.color.equals(new Color(255, 0, 0))){
                                
                                
                                if(c.curY + c.SIZE  > 665){
                                    returnVal -= 1000;
                                    DangerousTargets watchOut = new DangerousTargets(c, minX);
                                    borderAvoid.add(watchOut);
                                }
                                else if(canFire){
                                    returnVal += 5; 
                                }
                                else if(c.curY + c.SIZE > 575){
                                     returnVal -= 50; 
                                }
                                else{
                                    returnVal -= 1;
                                }
                        
                            }
                            else{
                                if(c.curY + c.SIZE  > 685){
                                    returnVal += 100;
                                }
                                else if(c.curY + c.SIZE > 600){
                                    returnVal += 10; 
                                }
                                else if(c.curY + c.SIZE  > 400){
                                    returnVal +=  3;
                                }
                                else{
                                    returnVal++; 
                                }
                                
                            }



                        }
                    }
                }
            }



            return returnVal; 
        }

        public void autoMove(){
            
            boolean safeToMoveRight = true; 
            boolean safeToMoveLeft = true;

            for(DangerousTargets t: borderAvoid){
                if(t.t.dead == false){
                    if(t.zone > cart.cartX && desiredX > cart.cartX && t.zone < desiredX){
                        safeToMoveRight = false;
                    }
                    if(t.zone < cart.cartX && desiredX < cart.cartX && t.zone > desiredX){
                        safeToMoveLeft = false;
                    }
                }

                
            }


            

            if(cart.cartX < desiredX){
                if(safeToMoveRight){
                    cart.cartRight(); 
                }
            }
            else if(cart.cartX > desiredX){
                if(safeToMoveLeft){
                    cart.cartLeft(); 
                }
            }
        }

    }

    class DangerousTargets{

        public Target t; 
        public int zone; 
    
    
        DangerousTargets(Target t, int zone){
            this.t = t; 
            this.zone = zone; 
        }
    }
}

