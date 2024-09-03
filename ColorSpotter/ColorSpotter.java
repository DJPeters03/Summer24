import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Random; 
import java.util.ArrayList; 

public class ColorSpotter extends MouseAdapter implements Runnable{
    public JPanel panel; 
    public Random rand = new Random(); 

    public ArrayList<CamoSquare> camos = new ArrayList<CamoSquare>(); 
    public ArrayList<TargetSquare> targs = new ArrayList<TargetSquare>();
    
    public int red = rand.nextInt(256); 
    public int green = rand.nextInt(256); 
    public int blue = rand.nextInt(256); 
    public boolean redDiff = false; 
    public boolean greenDiff = false; 
    public boolean blueDiff = false; 

    public boolean underNorm = false;
    
    
    
    
    public boolean gameEnd = false; 

    public boolean gameWon = false; 





    //CONSTANTS
    
    
    
    @Override
    public void run() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("ColorSpotter");
        frame.setPreferredSize(new Dimension(1000,1000));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);




        populateGame(); 

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (TargetSquare t : targs) {
					t.paint(g);
				}
                for (CamoSquare c : camos) {
					c.paint(g);
				}

            }
        };

        panel.addMouseListener(this);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        

    }


    private void endGame(){
      
        showGameOverDialog(); 
    }

    public void showGameOverDialog(){
        StringBuilder message = new StringBuilder();
        message.append("GAME OVER!"); 
        if(gameWon){
           
           message.append("\nYOU WIN!");
        }
        else{
            message.append("\n" + targs.size() + " more squares to Win!");
        }

        JOptionPane.showMessageDialog(null, message.toString(), "GAME END", JOptionPane.INFORMATION_MESSAGE);
    }

    
    
    public void mousePressed(MouseEvent e){
		boolean targClicked = false; 

		Point p = e.getPoint();


        for(int i = targs.size() - 1; i >= 0; i--){
            if(targs.get(i).contains(p)){
                targs.get(i).swapCamo(); 
                targs.remove(i); 
                if(targs.size() == 0){
                    gameWon = true; 
                    gameEnd = true; 
                    
                }
                targClicked = true; 
                break; 
            }
        }

        if(targClicked){
            for(int i = targs.size() - 1; i >= 0; i--){
                targs.get(i).alterColor(); 
            }
        }
        else{
            
            
            
            gameEnd = true; 
            for(int i = targs.size() - 1; i >= 0; i--){
                targs.get(i).gameEndColor(); 
            }
            

        }

        

        panel.repaint();
        if(gameEnd){
            endGame(); 
        }
    
    }
    
    public static void main(String args[]){
        SwingUtilities.invokeLater(new ColorSpotter());
    }


    public void populateGame(){
        int x = 0; 
        int y = 0; 

        

        int numOfTargs = 0; 

        Color targColor = offsetColor(red,green,blue);


        for(int i = 0; i < 100; i++){
            int targOrCamo = rand.nextInt(10); 
            if(numOfTargs >= 10){
                targOrCamo = 1; 
            }


            if(i == 91  && numOfTargs < 6){
                targOrCamo = 0; 
            }
            if(i == 93  && numOfTargs < 7){
                targOrCamo = 0; 
            }

            if(i == 95  && numOfTargs < 8){
                targOrCamo = 0; 
            }

            if(i == 97  && numOfTargs < 9){
                targOrCamo = 0; 
            }
            if(i == 99  && numOfTargs < 10){
                targOrCamo = 0; 
            }

            if(targOrCamo == 0){
                targs.add(new TargetSquare(targColor, x, y));
                numOfTargs++; 
            }
            else{
                camos.add(new CamoSquare(red, green, blue, x, y));
            }
            x += 100; 
            if(x == 1000){
                x = 0; 
                y += 100; 
            }
        }


    }

    public Color offsetColor(int red, int green, int blue){

        int colorNum = rand.nextInt(3); 
        Color returnColor = new Color(0,0,0);
        if(colorNum == 0){
            redDiff = true; 
            if(red > 155){
                returnColor = new Color(red - 45, green, blue);
                underNorm = true; 
            }
            else{
                returnColor = new Color(red + 45, green, blue);
            }
        }
        else if(colorNum == 1){
            greenDiff = true; 
            if(green > 155){
                returnColor = new Color(red, green - 45, blue);
                underNorm = true; 
            }
            else{
                returnColor = new Color(red, green + 45, blue);
            }
        }
        else if (colorNum == 2) {
            blueDiff = true; 
            if(blue > 155){
                returnColor = new Color(red, green, blue - 45);
                underNorm = true; 
            }
            else{
                returnColor = new Color(red, green, blue + 45);
            }
        }

       
       
       
       
        return returnColor; 
    }



    public class CamoSquare{
        public int size = 100; 

        public int upperLeftX;
        public int upperLeftY;  

        public int redVal; 
        public int greenVal; 
        public int blueVal; 

        CamoSquare(int red, int green, int blue, int upperLeftX, int upperLeftY){
            redVal = red; 
            greenVal = green; 
            blueVal = blue; 
            
            
            this.upperLeftX = upperLeftX; 
            this.upperLeftY = upperLeftY; 
        }


        public void paint(Graphics g) {
            g.setColor(new Color(redVal, greenVal, blueVal));
            g.fillRect(upperLeftX, upperLeftY, size, size);
        }
        
    }



    public class TargetSquare{
        //CONSTANTS 
        public int size = 100; 


        public int upperLeftX;
        public int upperLeftY;  

        public Color color;

        TargetSquare(Color color, int upperLeftX, int upperLeftY){
            this.color = color; 

            this.upperLeftX = upperLeftX; 
            this.upperLeftY = upperLeftY; 
            
        }


        public void paint(Graphics g) {
            g.setColor(color);
            g.fillRect(upperLeftX, upperLeftY, size, size);
        }

        public void gameEndColor(){
            color = new Color(0,0,0);
        }

        public void alterColor(){

            //System.out.println("Moving towards -->> (" + red + ", " + green + ", " + blue +") ");
            if(redDiff){
                if(underNorm){
                    color = new Color(color.getRed() + 5, color.getGreen(), color.getBlue());
                }
                else{
                    color = new Color(color.getRed() - 5, color.getGreen(), color.getBlue());
                }
            }
            else if(greenDiff){
                if(underNorm){
                    color = new Color(color.getRed(), color.getGreen() + 5, color.getBlue());
                }
                else{
                    color = new Color(color.getRed(), color.getGreen() - 5, color.getBlue());
                }
            }
            else if (blueDiff){
                if(underNorm){
                    color = new Color(color.getRed(), color.getGreen(), color.getBlue() + 5);
                }
                else{
                    color = new Color(color.getRed(), color.getGreen(), color.getBlue() - 5);
                }
            }
            //System.out.println("At --> (" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")");
        }

        public boolean contains(Point p){
            return p.x >= upperLeftX && p.x <= upperLeftX + size && p.y >= upperLeftY && p.y <= upperLeftY+ size;
        }

        public void swapCamo(){
            camos.add(new CamoSquare(red,green,blue, upperLeftX, upperLeftY));
        }
    }
}


