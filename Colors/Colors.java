import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Colors implements Runnable, ActionListener{
    public Timer timer = new Timer(1, this);

    private JPanel panel; 

    public int redVal1 = 5; 
    public int greenVal1 = 5; 
    public int blueVal1 = 5; 


    public boolean finished = false; 
    public boolean blueCountUp = true; 

    public boolean greenCountUp = true; 

    public Color color1 = new Color(redVal1, greenVal1, blueVal1);


    @Override
    public void run() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Colors");
        frame.setPreferredSize(new Dimension(500,500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel() {
            @Override 
            protected void paintComponent(Graphics g){
                super.paintComponent(g); 
                g.setColor(color1);
                g.fillRect(100, 100, 200, 200);
            }
        };  

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        timer.start(); 
    }  

  

    public void actionPerformed(ActionEvent e){
        
        
        
        if(!finished){
            blueGreenRed(); 
        }





        color1 = new Color(redVal1, greenVal1, blueVal1);
        panel.repaint(); 
    }

    public void blueGreenRed(){
        if(blueCountUp){
            blueVal1+= 10 ; 
            if(blueVal1 == 255){
                bgrHelper(); 
                blueCountUp = false;
            }
        }
        else{
            blueVal1-= 10; 
            if(blueVal1 == 5){
                bgrHelper(); 
                blueCountUp = true; 
            }
        }
        
        
    }

    public void bgrHelper(){
        if(greenCountUp){
            greenVal1+= 10;
            if(greenVal1 == 255){
                redVal1 += 10; 
                System.out.println("Current Color: (" + redVal1 + ", " + greenVal1 + ", " + blueVal1 + ")");
                if(redVal1 == 255){
                    finished = true; 
                }
                greenCountUp = false;
            }
            
        }
        else{
            greenVal1 -= 10;
            if(greenVal1  == 5){
                redVal1 += 10; 
                System.out.println("Current Color: (" + redVal1 + ", " + greenVal1 + ", " + blueVal1 + ")");
                if(redVal1 == 255){
                    finished = true; 
                }
                greenCountUp = true; 
            }
        }
    }


    public void redGreenBlue(){

    }

    public void redBlueGreen(){
        
    }

    public void greenRedBlue(){
        
    }

    public void greenBlueRed(){
        
    }

    public void blueRedGreen(){

    }


    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Colors());
    }


}