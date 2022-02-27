
/**
 * Runs the solver for the Sudoku puzzle. 
 *
 * @author Tanish Kumar
 * @version 12/20/2021
 */

import java.util.Scanner; 
import java.io.*; 

public class SudokuMain
{
    public static void main(String args[]) throws Exception
    {
        Scanner scanner = new Scanner(System.in); 
        System.out.println("Welcome to the Sudoku Solver! This solver is capable of solving beginner/intermediate level Sudoku puzzles."); 
        int[] inputPuzzle = new int[81]; 
        int[] randomPuzzle = new int[81]; 
        
        int[] examplePuzzle1 = {0, 0, 4,   0, 0, 0,   0, 6, 7, 
                                3, 0, 0,   4, 7, 0,   0, 0, 5,
                                1, 5, 0,   8, 2, 0,   0, 0, 3,
                                
                                0, 0, 6,   0, 0, 0,   0, 3, 1,
                                8, 0, 2,   1, 0, 5,   6, 0, 4,
                                4, 1, 0,   0, 0, 0,   9, 0, 0,
                                
                                7, 0, 0,   0, 8, 0,   0, 4, 6,
                                6, 0, 0,   0, 1, 2,   0, 0, 0,
                                9, 3, 0,   0, 0, 0,   7, 1, 0};
                            
                        
        int[] examplePuzzle2 = {0,0,4,3,0,0,2,0,9,0,0,5,0,0,9,0,0,1,0,7,0,0,6,0,0,4,3,0,0,6,0,0,2,0,8,7,1,9,0,0,0,7,4,0,0,0,5,0,0,8,3,0,0,0,6,0,0,0,0,0,1,0,5,0,0,3,5,0,8,6,9,0,0,4,2,9,1,0,3,0,0}; 
        
        int[] examplePuzzle3 = {0,5,0,0,0,0,0,4,0,3,0,0,2,0,7,0,0,1,0,0,7,0,6,0,3,0,0,0,3,0,0,8,0,0,7,0,0,0,9,7,0,5,1,0,0,0,4,0,0,2,0,0,9,0,0,0,2,0,5,0,4,0,0,9,0,0,1,0,6,0,0,2,0,6,0,0,0,0,0,1,0};
        
        System.out.println("In which format would you like to input a puzzle? "); 
        System.out.println("1) Use console input to write box by box."); 
        System.out.println("2) Input using text file called 'SudokuPuzzleTextFileInput.txt'. (File format should be all 81 numbers strung together without spaces: 090534...) "); 
        System.out.println("3) Automatically use sample puzzle #1 "); 
        System.out.println("4) Automatically use sample puzzle #2 "); 
        System.out.println("5) Automatically use sample puzzle #3 (Hard) "); 
        
        int userInput = scanner.nextInt(); // Stores the choice inputted by client. 
        int index = 0; 
        if(userInput == 1) // User Choice 1
        {
            for(int i = 1; i < 10; i++)
            {
                for(int j = 1; j < 10; j++) // i and j indicate the row and column values respectively. 
                {
                    System.out.println("Enter the value of the board at (" + i + ", " + j + ") using 0 to indicate an empty space:"); 
                    inputPuzzle[index] = scanner.nextInt(); 
                    
                    while(inputPuzzle[index] > 9 || inputPuzzle[index] < 0)
                    {
                        System.out.println("Please enter an appropriate value for (" + i + ", " + j + ") using 0 to indicate an empty space:");
                        inputPuzzle[index] = scanner.nextInt(); 
                    }
                    
                    index++; 
                }
            }
        }
        
        else if(userInput == 2) // User Choice 2
        {
            File file = new File("SudokuPuzzleTextFileInput.txt");
            Scanner scannerInput = new Scanner(file);
            String puzzleLine = scannerInput.nextLine(); 
            if(puzzleLine.length() == 81)
            {
                for(int i = 0; i < 81; i++)
                {
                    inputPuzzle[i] = ((int) puzzleLine.charAt(i)) - 48; 
                }
            }
            
            else
            {
                System.out.println("Input was not valid. Default in-built puzzle will be used. "); 
                inputPuzzle = examplePuzzle1; 
            }
        }
        
        else if(userInput == 3) //User Choice 3
        {
            inputPuzzle = examplePuzzle1; 
        }
        
        else if(userInput == 4) //User Choice 4
        {
            inputPuzzle = examplePuzzle2; 
        }
        
        else if(userInput == 5) // User Choice 5
        {
            inputPuzzle = examplePuzzle3; 
        }
        
        else
        {
            System.out.println("You did not choose a valid option. Default in-built puzzle will be used. "); 
            inputPuzzle = examplePuzzle1; 
        }
        SudokuSolver sudoku = new SudokuSolver(inputPuzzle); // Generates and instance of the Sudoku Solver class and passes the input puzzle to it. 
        
        
        
        System.out.println(); //Creates Space on Console. 
        System.out.println(); 
        System.out.println(); 
        System.out.println(); 
        System.out.println(); 
        System.out.println(); 
        System.out.println(); 
        System.out.println(); 
        System.out.println(); 
        System.out.println(); 
        System.out.println(); 
        
        System.out.println("Solved to best of code's ability: "); 
        System.out.println(); 
        sudoku.printBoard(); //Prints Solved Board. 
    }
}
