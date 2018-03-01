/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Stefan
 */
public class Sudoku {
    public static int n = 4; // number of boxes in a row
    public static int numberOfSquaresToHide = 6;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Square[][] sArray = initiateSquares();
        sArray = calculateRegion (sArray);
        sArray = populateSquareArray(sArray);
        displayGame(sArray);
     
    }
    private static Square[][] initiateSquares (){
        Square[][] sArray = new Square[n][n];
        for(int row = 0; row < n; row++){
            for(int column = 0; column < n; column++){
                Square s = new Square();
                s.row = row;
                s.column = column;
                s.possibleNumbers = populatePossibleNumbers();
                s.value = 0;//ran.nextInt(n)+1;
                s.visable = true;
                s.userChangeable = false;
                sArray[row][column] = s;
            }
        }
    return sArray;
    }
    private static Square[][] calculateRegion (Square[][] sArray){
         int rootOfSquaresForRow = (int)Math.sqrt((double) n);
        int region = 0;
        for (int row = 0; row< rootOfSquaresForRow; row++){
            int rootOfSquaresForColumn = (int)Math.sqrt((double) n);
            for(int column = 0; column < rootOfSquaresForColumn; column++ ){
                            
                /*
                *setRegion for Squares;
                */
                sArray[row][column].region = region;
               
                /*
                *If Satser som stegar igenom loopen correkt för att kolla alla squares i rätt ordning
                *koordinat 0x0 - n-1*n-1 så att sista om man kör 16 rutor är 3x3
                */
                //System.out.println("Row: " + row + "  Column: " + column + " Region: " + region);
                if(column == rootOfSquaresForColumn-1 && row < rootOfSquaresForRow-1 ){
                    row++;
                    column = column - (int)Math.sqrt((double) n);
                    
                }
                if(column == rootOfSquaresForColumn-1 && row == rootOfSquaresForRow-1 && rootOfSquaresForColumn != n ){
                    rootOfSquaresForColumn = rootOfSquaresForColumn + (int)Math.sqrt((double) n);
                    row = row - (int)Math.sqrt((double) n)+1;
                    region++;
                }
                if(column == rootOfSquaresForColumn-1 && rootOfSquaresForColumn == n && row == rootOfSquaresForRow-1 && rootOfSquaresForRow < n){
                     rootOfSquaresForRow = rootOfSquaresForRow + (int)Math.sqrt((double) n);
                     rootOfSquaresForColumn = (int)Math.sqrt((double) n);
                     column= -1;
                     row++;
                     region++;
                }
                
            }
        }
        
        return sArray;
    }
    
    private static Square[][] generatePossibleNumbers (Square[][] sArray){
        /*
        *Skapar en array med möjliga nummer för att sedan skicka tillbaka 
        *Funktionen ska kallas en gång för varje gång man har lagt till eller ändrat ett nummer
        *
        */
    for(int row = 0; row < n; row++){
        for(int column = 0; column < n; column++){
            Square s = sArray[row][column];

            for(int i = 0; i < n; i++){
                for(int j = 0; j<n; j++){

                        for(int x = 0; x<n; x++){
                            Square test = sArray[i][x];
                            if(s.column != test.column){
                                if(s.row == test.row){
                                    if(test.possibleNumbers.contains(s.value)){
                                        test.possibleNumbers.remove(test.possibleNumbers.indexOf(s.value));
                                    }
                                }                                     
                            }
                        }
                        for(int x = 0; x<n; x++){
                            Square test = sArray[x][j];
                            if(s.row != test.row){
                                if(s.column == test.column){
                                    if(test.possibleNumbers.contains(s.value)){
                                        test.possibleNumbers.remove(test.possibleNumbers.indexOf(s.value));
                                    }
                                }                                                                       
                            }   
                        }
                        for(int x = 0; x<n; x++){
                            Square test = sArray[x][j];
                            if(s.row != test.row){
                                if(s.region == test.region){
                                    if(test.possibleNumbers.contains(s.value)){
                                        test.possibleNumbers.remove(test.possibleNumbers.indexOf(s.value));
                                    }
                                }                                                                       
                            }   
                        }

                    }
                }
            }
        }
               
               
        return sArray;
    }
       
    private static int numberOfPossibleIntegersWhileHidden(Square[][] sArray, Square s){
        
        for(int row = 0; row < n ; row++){
            for(int column = 0; column <n ; column++){
                if(s.row != sArray[row][column].row && s.column != sArray[row][column].column && sArray[row][column].visable){
                    if(s.possibleNumbers.contains(sArray[row][column].value)){
                        s.possibleNumbers.remove(s.possibleNumbers.indexOf(sArray[row][column].value));
                    }
                }
            }
        }   
        return s.possibleNumbers.size();
    }
    private static ArrayList<Integer> populatePossibleNumbers(){
        ArrayList<Integer> p = new ArrayList<Integer>();
        for(int i = 0; i<n; i++){
            p.add(i+1); //Skapar en array med siffrorna 1 -4
        }
        return p;
    }
    
    private static Square[][] populateSquareArray (Square[][] sArray){
        Random ran = new Random();
        for(int i = 0; i<n; i++){
            for(int j = 0; j <n; j++){
                Square s = sArray[i][j];
                /*
                *Kolla så att det finns en siffra utan konflikt
                */
                
                if(!s.possibleNumbers.isEmpty()){
                    int numberToTry;
                    numberToTry = s.possibleNumbers.get(ran.nextInt(s.possibleNumbers.size()));
                    s.value = numberToTry;

                    sArray = generatePossibleNumbers(sArray);
                }

                else if(j!=0) {
                    sArray[i][j].possibleNumbers = populatePossibleNumbers();
                    sArray[i][j-1].possibleNumbers = populatePossibleNumbers();
                    sArray[i][j-1].possibleNumbers.remove(sArray[i][j-1].possibleNumbers.indexOf(sArray[i][j-1].value));
                    j=j-1;

                }else{
                    sArray[i-1][n-1].possibleNumbers = populatePossibleNumbers();
                    sArray[i][j].possibleNumbers = populatePossibleNumbers();
                    sArray[i-1][n-1].possibleNumbers.remove(sArray[i-1][n-1].possibleNumbers.indexOf(sArray[i-1][n-1].value));
                    i=i-1;
                    j=n-1;
                }
                s.visableValue = s.value;
             }            
        } 
        return sArray;
    }
  
    private static void displayGame (Square[][] sArray){
        Random ran = new Random();
        while(!hasHiddenSuffiecientNumberOfSquares(sArray)){
            int row = ran.nextInt(n-1);
            int column = ran.nextInt(n-1);
            if(isAppropriateToHideSquare(sArray, sArray[row][column])){
                sArray[row][column].visable = false;
                sArray[row][column].userChangeable = true;
                sArray[row][column].visableValue = 0;
            }
        }
        System.out.println("To Play you first enter the x(horizontal)coordinat (starting with 0 counting from the left).\nThen you supply the Y coordinat in the same fashion and ending with the number you'd like to input.");
        
        while (!pusselComplete(sArray)){
            printScreen(sArray);
            int[] user = userInput(sArray);

            sArray[user[1]][user[0]].visableValue = user[2];
            sArray[user[1]][user[0]].visable = true;
            
        }
        
    }
    
    private static boolean pusselComplete (Square[][] sArray){
        
        for(int row = 0; row < n ; row++){
            for(int column = 0; column < n; column++){
                Square test = sArray[row][column];
                if(test.value != test.visableValue){
                    return false;
                }
            }
        }
        
        
        return true; 
    }
    
    private static void printScreen (Square[][] sArray){
        for(int row = 0; row<n; row++){
            for(int column = 0; column <n; column++){
                Square t = sArray[row][column];
                if(t.visable == true){
                    System.out.print(t.visableValue + "   ");
                }else{
                    System.out.print("x"+ "   ");
                }
            }
            System.out.println();
        }
    }
    private static boolean isAppropriateToHideSquare(Square[][] sArray, Square s){
        s.possibleNumbers = populatePossibleNumbers();
        if(numberOfPossibleIntegersWhileHidden(sArray, s) > 1)
        {
            System.out.println("Not Good");
            return false;
        }
        return true;
    }
    
    private static boolean hasHiddenSuffiecientNumberOfSquares(Square[][] sArray){
        int numberOfhiddenSquares = 0 ; 
        for(int row = 0; row < n; row++){
            for(int column = 0; column < n; column++){
                if(!sArray[row][column].visable){
                    numberOfhiddenSquares++;
                   
                }
            }
        }
        if(numberOfhiddenSquares >= 5){
            return true;
        }
        return false;
    }
    
    private static int[] userInput(Square[][] sArray){
        Scanner scan = new Scanner(System.in);
        int[] answer = new int [3];
        
        boolean doAgain = true;
        while(doAgain){
            int x = n;
            while(x > n-1){
                System.out.println("X: ");
                x = scan.nextInt();
                answer[0] = x;
            }
            int y = n;
            while(y > n-1){
                System.out.println("Y: ");
                y = scan.nextInt();
                answer[1] =y;
            }
            int num = n+1;
            while(num > n){
                System.out.println("Number: ");
                num = scan.nextInt();
                answer[2] = num;
            }

            if(sArray[x][y].userChangeable){
                
                doAgain = false;
            } 
            else{
                printScreen(sArray);
                System.out.println("That spot is Taken!");
                
            }
        
        }
        
        return answer;
    }

}
    