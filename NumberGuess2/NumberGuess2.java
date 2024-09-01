
//IF THE USER SAYS 2 MATCH, EVERY GUESS SHOULD HAVE TWO MATCHES



import java.util.Scanner; 
import java.util.Random; 
import java.util.ArrayList; 

public class NumberGuess2{
    public static Scanner scnr = new Scanner(System.in);
    public static Random rand = new Random(); 
    public static int numCorrect = 0; 
    public static ArrayList<Integer> posibilities = new ArrayList<Integer>(); 
    public static int attempts = 0; 
    public static ArrayList<Combo> previousNums = new ArrayList<Combo>(); 

    public static boolean aiMode = false; 
    public static int aiNum = 0; 


    public static int aiRounds = 0; 
    public static int scoreTally = 0; 
    public static double average = 0; 
    
    
    public static void main(String[] args){
       for(int i = 0000; i <= 9999; i++){
        posibilities.add(i);
       }
       
        startGame(); 

    }


    public static void startGame(){
        System.out.println("Think of a Number 0000 - 9999");
        System.out.println("The Ai will try to guess your number");
        System.out.println("After each guess input a number from 0-4");
        System.out.println("This is how many digits the ai has in the right spot");
        System.out.println("0 for none right, 4 for the correct number");
        System.out.println("type 1 to play! (or 2 for Ai Mode)");

        int play = scnr.nextInt(); 


        if(play == 2){


            aiMode = true; 

            aiNum = rand.nextInt(10000);

            System.out.println("Ai Mode! The number is " + aiNum);
        }
        startGuessing(); 
        
        if(play == 2){
            for(int i = 0; i < 9999; i++){
                aiNum = rand.nextInt(10000);
                System.out.println("Ai Mode! The number is " + aiNum);




                posibilities = new ArrayList<Integer>(); 
                for(int j = 0000; j <= 9999; j++){
                    posibilities.add(j);
                   }
                previousNums = new ArrayList<Combo>();
                numCorrect = 0;
                attempts = 0; 
                startGuessing(); 
                
                
            }
                
        }
        
    }



    public static void startGuessing(){
        while(numCorrect != 4){
            int guess = makeGuess();
            int number = guess; 
            int digitCount = 0; 

            do {
                number = number / 10;
                digitCount++;
            } while (number > 0);

            System.out.print("\nI predict your number is ");

            

            for(int i = 0; i < 4 - digitCount; i++){
                System.out.print("0");
            }
            System.out.println("" + guess + "!");
            System.out.print("How many digits did I guess?: ");
            if(aiMode){
                numCorrect = 0; 


                try {
                    Thread.sleep(2200); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int num = aiNum; 
                int fourthDigit = num % 10;
                num = num / 10;
                int thirdDigit = num % 10;
                num = num / 10;
                int secondDigit = num % 10;
                num = num / 10;
                int firstDigit = num; 


                num = guess;
                int fourth = num % 10;
                num = num / 10;
                int third = num % 10;
                num = num / 10;
                int second = num % 10;
                num = num / 10;
                int first = num;  

                if(first == firstDigit){
                    
                    numCorrect++;
                }
                if(second == secondDigit){
                    
                    numCorrect++;
                }
                if(third == thirdDigit){
                    
                    numCorrect++;
                }
                if(fourth == fourthDigit){
                   
                    numCorrect++;
                }


                System.out.println(numCorrect);
            }
            else{
            numCorrect = scnr.nextInt(); 
            }

            if(numCorrect == 0){
                removeZero(guess); 
            }
            addNums(guess, numCorrect); 
            attempts++;
        }
        System.out.println("Good Game! It took me " + attempts + " attempts to guess your Number!");
        scoreTally += attempts; 
        aiRounds++; 
        if(aiMode){
            average = (double)scoreTally/aiRounds; 

            System.out.println("Played " + aiRounds + " rounds with an average end at: " + average + " guesses");
        }
    }   

    public static int makeGuess(){
        ArrayList<Integer> probableGuess = new ArrayList<Integer>();



        for(int i = 0; i < posibilities.size(); i++){
            boolean fitsAll = true;
            
            
            
            
            for(int j = 0; j < previousNums.size(); j++){
                int matchingNums = 0; 
                int pastGuess = previousNums.get(j).guess();
                int pastRight = previousNums.get(j).numRight(); 



            
                int fourthDigit = pastGuess % 10;
                pastGuess  = pastGuess  / 10;
                int thirdDigit = pastGuess  % 10;
                pastGuess  = pastGuess  / 10;
                int secondDigit = pastGuess  % 10;
                pastGuess  = pastGuess  / 10;
                int firstDigit = pastGuess ;




                int num = posibilities.get(i);
                int fourth = num % 10;
                num = num / 10;
                int third = num % 10;
                num = num / 10;
                int second = num % 10;
                num = num / 10;
                int first = num;




                if(first == firstDigit){
    
                    matchingNums++;
                }
                if(second == secondDigit){

                    matchingNums++;
                }
                if(third == thirdDigit){
                    matchingNums++;
                }
                if(fourth == fourthDigit){
                    matchingNums++;
                }



                if(matchingNums != pastRight){
                    fitsAll = false;
                }




                

            }

            if(fitsAll){
                probableGuess.add(posibilities.get(i));
            }


           



        }


        
            int returnVal = rand.nextInt(probableGuess.size());

            return probableGuess.get(returnVal);


           




        }


    public static void addNums(int guess, int numCorrect){
        
        previousNums.add(new Combo(guess,numCorrect));

    }

    public static void removeZero(int guess){
        int fourth = guess % 10; 
        guess = guess / 10; 
        int third = guess % 10; 
        guess = guess / 10; 
        int second = guess % 10; 
        guess= guess / 10; 
        int first = guess; 
   



    for (int i = posibilities.size() - 1; i >= 0; i--) {
        int num = posibilities.get(i);
        int fourthDigit = num % 10;
        num = num / 10;
        int thirdDigit = num % 10;
        num = num / 10;
        int secondDigit = num % 10;
        num = num / 10;
        int firstDigit = num;

        if ((first == firstDigit) || (second == secondDigit) || (third == thirdDigit) || (fourth == fourthDigit)) {
            posibilities.remove(i);
        }
    }

}   
    
}


class Combo{
    public  int guess; 
    public  int numRight; 

    Combo(int guess, int numRight){
        this.guess = guess; 
        this.numRight = numRight; 
    }

    public int numRight(){
        return numRight; 
    }

    public int guess(){
        return guess; 
    }
}




   