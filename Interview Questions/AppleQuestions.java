
import java.util.*;

//Interview questions from apple found online 


public class AppleQuestions{
    public static void main(String args[]){
        ArrayList<Integer> arr = new ArrayList<Integer>(); 
        arr.add(3);
        arr.add(7);
        arr.add(1);
        arr.add(2);
        arr.add(8);
        arr.add(4);
        arr.add(5);


        //NUMBERS IT CAN ADD UP TO: 11, 

        int targetNum; 



        targetNum = 20; 

        ArrayList<Integer> output = find_sum_of_three(arr, targetNum);

        
        System.out.println("Testing with target sum " + targetNum + " result: " + output);
        
        
        targetNum = 21; 
        output = find_sum_of_three(arr, targetNum);

        System.out.println("Testing with target sum " + targetNum + " result: " + output);

        powerTest(); 
    }
    



        //Given an array of integers and a value, determine if there are any three integers in the array whose sum equals the given value
        //Consider this array and the target sums. [3,7,1,2,8,4,5]
        public static ArrayList<Integer> find_sum_of_three(ArrayList<Integer> arr,int required_sum) {
    
        ArrayList<Integer> sums = new ArrayList<Integer>(); 
        for(int i = 0; i <= 4; i++){
        for(int j = i+1; j <= 5; j++){
        for(int k = j+1; k <= 6; k++){
            //if(arr.get(i) + arr.get(j) + arr.get(k)  == required_sum){
            //return true; 
            //}
            boolean add = true; 
            for(int p = 0; p < sums.size(); p++){
                
                if(sums.get(p) == arr.get(i) + arr.get(j) + arr.get(k)){
                    add = false; 
                }
            
            
            }
            if(add){
                sums.add(arr.get(i) + arr.get(j) + arr.get(k)); 
            }




        }

        }
    }

        return sums;
    }


    public static void powerTest(){
        //System.out.println("2 to the 5 = " + power(2,5));

        //System.out.println("3 to the 4 = " + power(3,4));
        //System.out.println("1.5 to the 3 = " + power(1.5,3));
        //System.out.println("2 to the -2 = " + power(2,-2));
        System.out.println(power(3,-9));
    }

    public static double power(double x, int n) {
        
        double returnVal = 1; 
      
        if(n >= 0){
          for(int i = 0; i < n; i++){
            returnVal = returnVal * x; 
          }
        }
        else{
          for(int i = 0; i > n; i--){
            returnVal = returnVal * x; 
          }
      
          returnVal = 1/returnVal; 
        }
      
        return returnVal;
      }

}