import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Colors implements Runnable, ActionListener{
    public Timer timer = new Timer(1, this);

    private JPanel panel; 

    public int redVal1 = 5; 
    public int greenVal1 = 5; 
    public int blueVal1 = 5; 
    public Color color1 = new Color(redVal1, greenVal1, blueVal1);
    public boolean blueCountUp1 = true; 
    public boolean greenCountUp1 = true; 


    public int redVal2 = 5; 
    public int greenVal2 = 5; 
    public int blueVal2 = 5; 
    public Color color2 = new Color(redVal2, greenVal2, blueVal2);
    public boolean blueCountUp2 = true; 
    public boolean redCountUp2 = true; 


    public int redVal3 = 5; 
    public int greenVal3 = 5; 
    public int blueVal3 = 5; 
    public Color color3 = new Color(redVal3, greenVal3, blueVal3);
    public boolean greenCountUp3 = true; 
    public boolean redCountUp3 = true; 


    public int redVal4 = 5; 
    public int greenVal4 = 5; 
    public int blueVal4 = 5; 
    public Color color4 = new Color(redVal4, greenVal4, blueVal4);
    public boolean greenCountUp4 = true; 
    public boolean blueCountUp4 = true; 



    public int redVal5 = 5; 
    public int greenVal5 = 5; 
    public int blueVal5 = 5; 
    public Color color5 = new Color(redVal5, greenVal5, blueVal5);
    public boolean redCountUp5 = true; 
    public boolean greenCountUp5 = true; 


    public int redVal6 = 5; 
    public int greenVal6 = 5; 
    public int blueVal6 = 5; 
    public Color color6 = new Color(redVal6, greenVal6, blueVal6);
    public boolean redCountUp6 = true; 
    public boolean blueCountUp6 = true; 



    public boolean finished = false; 



    

    


    @Override
    public void run() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Colors");
        frame.setPreferredSize(new Dimension(1000,1000));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int baseX = 100;
                int baseY = 100;
                int offset = 150;  // Adjust the spacing as needed for your pattern

                // Draw the original pattern in four different locations
                drawPattern(g, baseX, baseY);                         // Top-left (original)
                drawPattern(g, baseX + offset + 50, baseY);            // Top-right
                drawPattern(g, baseX, baseY + offset * 2);            // Bottom-left
                drawPattern(g, baseX + offset + 50, baseY + offset * 2); // Bottom-right
            }

            private void drawPattern(Graphics g, int x, int y) {
                g.setColor(color1);
                g.fillRect(x, y, 50, 50);
                g.fillRect(x + 100, y, 50, 50);
                g.fillRect(x, y + 150, 50, 50);
                g.fillRect(x + 100, y + 150, 50, 50);

                g.setColor(color2);
                g.fillRect(x + 50, y, 50, 50);
                g.fillRect(x + 150, y, 50, 50);
                g.fillRect(x + 50, y + 150, 50, 50);
                g.fillRect(x + 150, y + 150, 50, 50);

                g.setColor(color3);
                g.fillRect(x, y + 50, 50, 50);
                g.fillRect(x + 100, y + 50, 50, 50);
                g.fillRect(x, y + 200, 50, 50);
                g.fillRect(x + 100, y + 200, 50, 50);

                g.setColor(color4);
                g.fillRect(x + 50, y + 50, 50, 50);
                g.fillRect(x + 150, y + 50, 50, 50);
                g.fillRect(x + 50, y + 200, 50, 50);
                g.fillRect(x + 150, y + 200, 50, 50);

                g.setColor(color6);
                g.fillRect(x, y + 100, 50, 50);
                g.fillRect(x + 100, y + 100, 50, 50);
                g.fillRect(x, y + 250, 50, 50);
                g.fillRect(x + 100, y + 250, 50, 50);

                g.setColor(color5);
                g.fillRect(x + 50, y + 100, 50, 50);
                g.fillRect(x + 150, y + 100, 50, 50);
                g.fillRect(x + 50, y + 250, 50, 50);
                g.fillRect(x + 150, y + 250, 50, 50);
            }
        };

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        timer.start(); 
    }  

  

    public void actionPerformed(ActionEvent e){
        
        
        
        //if(!finished){
            blueGreenRed(); 
            blueRedGreen(); 
            greenRedBlue(); 
            greenBlueRed(); 
            redBlueGreen(); 
            redGreenBlue(); 
            
        //}





        color1 = new Color(redVal1, greenVal1, blueVal1);
        color2 = new Color(redVal2, greenVal2, blueVal2);
        color3 = new Color(redVal3, greenVal3, blueVal3);
        color4 = new Color(redVal4, greenVal4, blueVal4);
        color5 = new Color(redVal5, greenVal5, blueVal5);
        color6 = new Color(redVal6, greenVal6, blueVal6);
        
        panel.repaint(); 
    }

    public void blueGreenRed(){
        if(blueCountUp1){
            blueVal1+= 1 ; 
            if(blueVal1 == 255){
                bgrHelper(); 
                blueCountUp1 = false;
            }
        }
        else{
            blueVal1-= 1; 
            if(blueVal1 == 5){
                bgrHelper(); 
                blueCountUp1 = true; 
            }
        }
        
        
    }

    public void bgrHelper(){
        if(greenCountUp1){
            greenVal1+= 1;
            if(greenVal1 == 255){
                redVal1 += 1; 
                System.out.println("Current Color: (" + redVal1 + ", " + greenVal1 + ", " + blueVal1 + ")");
                if(redVal1 == 255){
                    finished = true; 
                }
                greenCountUp1 = false;
            }
            
        }
        else{
            greenVal1 -= 1;
            if(greenVal1  == 5){
                redVal1 += 1; 
                //System.out.println("Current Color: (" + redVal1 + ", " + greenVal1 + ", " + blueVal1 + ")");
                if(redVal1 == 255){
                    finished = true; 
                }
                greenCountUp1 = true; 
            }
        }
    }
    public void blueRedGreen(){
        if(blueCountUp2){
            blueVal2+= 1 ; 
            if(blueVal2 == 255){
                brgHelper(); 
                blueCountUp2 = false;
            }
        }
        else{
            blueVal2-= 1; 
            if(blueVal2 == 5){
                brgHelper(); 
                blueCountUp2 = true; 
            }
        }
    }

    public void brgHelper(){
        if(redCountUp2){
            redVal2+= 1;
            if(redVal2 == 255){
                greenVal2 += 1; 
                if(greenVal2 == 255){
                    finished = true; 
                }
                redCountUp2 = false;
            }
            
        }
        else{
            redVal2 -= 1;
            if(redVal2 == 5){
                greenVal2 += 1; 
                if(greenVal2 == 255){
                    finished = true; 
                }
                redCountUp2 = true; 
            }
        }
    }

    public void greenRedBlue(){
        if(greenCountUp3){
            greenVal3+= 1; 
            if(greenVal3== 255){
                grbHelper(); 
                greenCountUp3 = false;
            }
        }
        else{
            greenVal3-= 1; 
            if(greenVal3 == 5){
                grbHelper(); 
                greenCountUp3 = true; 
            }
        }
    }

    public void grbHelper(){
        if(redCountUp3){
            redVal3+= 1;
            if(redVal3 == 255){
                blueVal3 += 1; 
                if(blueVal3 == 255){
                    finished = true; 
                }
                redCountUp3 = false;
            }
            
        }
        else{
            redVal3 -= 1;
            if(redVal3 == 5){
                blueVal3 += 1; 
                if(blueVal3 == 255){
                    finished = true; 
                }
                redCountUp3 = true; 
            }
        }
    }

    public void greenBlueRed(){
        if(greenCountUp4){
            greenVal4 += 1 ; 
            if(greenVal4== 255){
                gbrHelper(); 
                greenCountUp4 = false;
            }
        }
        else{
            greenVal4 -= 1; 
            if(greenVal4 == 5){
                gbrHelper(); 
                greenCountUp4 = true; 
            }
        }
    }

    public void gbrHelper(){
        if(blueCountUp4){
            blueVal4+= 1;
            if(blueVal4 == 255){
                redVal4+= 1; 
                if(redVal4 == 255){
                    finished = true; 
                }
                blueCountUp4 = false;
            }
            
        }
        else{
            blueVal4 -= 1;
            if(blueVal4 == 5){
                redVal4 += 1; 
                if(redVal4 == 255){
                    finished = true; 
                }
                blueCountUp4 = true; 
            }
        }
    }
    

    public void redGreenBlue(){
        if(redCountUp5){
            redVal5 += 1; 
            if(redVal5== 255){
                rgbHelper(); 
                redCountUp5 = false;
            }
        }
        else{
            redVal5 -= 1; 
            if(redVal5 == 5){
                rgbHelper(); 
                redCountUp5 = true; 
            }
        }
    }

    public void rgbHelper(){
        if(greenCountUp5){
            greenVal5+= 1;
            if(greenVal5 == 255){
                blueVal5 += 1; 
                if(blueVal5 == 255){
                    finished = true; 
                }
                greenCountUp5 = false;
            }
            
        }
        else{
            greenVal5 -= 1;
            if(greenVal5== 5){
                blueVal5 += 1; 
                if(blueVal5 == 255){
                    finished = true; 
                }
                greenCountUp5 = true; 
            }
        }
    }

    public void redBlueGreen(){
        if(redCountUp6){
            redVal6 += 1; 
            if(redVal6== 255){
                rbgHelper(); 
                redCountUp6 = false;
            }
        }
        else{
            redVal6 -= 1; 
            if(redVal6 == 5){
                rbgHelper(); 
                redCountUp6 = true; 
            }
        }
    }

    public void rbgHelper(){
        if(blueCountUp6){
            blueVal6 += 1;
            if(blueVal6 == 255){
                greenVal6+= 1; 
                if(greenVal6 == 255){
                    finished = true; 
                }
                blueCountUp6 = false;
            }
            
        }
        else{
            blueVal6 -= 1;
            if(blueVal6== 5){
                greenVal6 += 1; 
                if(greenVal6 == 255){
                    finished = true; 
                }
                blueCountUp6 = true; 
            }
        }
    }

    

    


    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Colors());
    }


}
