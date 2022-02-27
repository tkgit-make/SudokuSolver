
/**
 * Solves a given Sudoku puzzle. Terminology -> box: and individual spot in which a single number can go, square: one of the nine 3 x 3 grids that make up the Sudoku board
 * 
 * To run the code, compile and run the file. The console output should prompt the client to choose a method by which to input a Sudoku Puzzle. 
 * 
 * Select an option, and input puzzle as directed. 
 * 
 * Once puzzle is input, the result should be directly output within a couple of seconds. 
 * 
 * If an invalid puzzle is input, the error will propagate through to the output. 
 *
 * @author Tanish Kumar
 * @version 12/20/2021
 */

import java.io.*;
import java.util.*;

public class SudokuSolver
{
    private int[] currentPuzzle; // Holds the uncompleted input puzzle in a one dimensional format. 
    private int[][] currentPuzzle2D; // Holds the uncompleted input puzzle in a two dimensional (9 x 9) format. 
    private int [][][] possibilities; // Holds the possibilities for each individual box that are determined by the box's row, column, and square (9 x 9 x 9 matrix). 
                                      // A potential possiblities matrix for a certain box is {1, 0, 0, 1, 0, 0, 0, 1, 0} where a 1 represents that the number (index + 1) exists as a possibility. 
                                      // This matrix would represent that {1, 4, 8} all exist as possibilities for the given box. 
    private int runCount = 0; //Counts the number of times the solve() function is run. This is used to monitor the number of times the function is run and to limit recursion. 
    
    public SudokuSolver(int[] puzzle) // Constructor that takes input puzzle and converts it to a more usable 2D format. 
    {
        currentPuzzle = puzzle;
        possibilities = new int[9][9][9]; 
        this.Conversion();
        System.out.println("Input Sudoku Puzzle: "); 
        this.printBoard(); 
        
        this.solve(); 
    }
    
    
    public void Conversion() // Performs actual conversion of 1D puzzle format to 2D puzzle format. 
    {
        int length = (int) Math.sqrt(currentPuzzle.length);
        currentPuzzle2D = new int[length][length]; 
        for(int i = 0; i < length; i++)
        {
            for(int j = 0; j < length; j++)
            {
                currentPuzzle2D[i][j] = currentPuzzle[i * length + j]; 
            }
        }
        
    }
    
    public void fillPossibilities() // Fills the possibilities 3D matrix by eliminating the possible numbers a box can contain based on its row, column, and square. 
    {
        for (int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                for(int k = 1; k < 10; k++)
                {
                    if (currentPuzzle2D[i][j] == 0)
                    {
                        if(!(checkRow(i, k)) && !(checkColumn(j, k)) && !(checkSquare(i, j, k)))
                        {
                            possibilities[i][j][(k - 1)] = 1;  
                        }
                    }
                }
            }
        }
    }
    
    public void solveUsingPossibilities() //If only one possibilities exists for a box, it will put that number for the box. 
    {
        fillPossibilities(); 
        
        pointingPairs(); 
        
        for (int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                if(sumPossibilities(i, j) == 1)
                { 
                    for(int z = 0; z < 9; z++)
                    {
                        if(possibilities[i][j][z] == 1)
                        {
                            currentPuzzle2D[i][j] = z + 1; 
                            possibilities[i][j] = new int[9]; 
                        }
                    }
                }
            }
        }
    }
    
    
    
    public void solve() // Runs all the techniques this class has for eliminating possiblities and solving for values. This is recursive but has a limit of 50 runs to avoid infinite loops and overloads. 
    {
        runSquares();
        runRows(); 
        runColumns(); 
        solveUsingPossibilities(); 
        
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                if(currentPuzzle2D[i][j] == 0 && (runCount < 50))
                {
                    runCount++; 
                    solve(); 
                }
            }
        }
    }
    
    public void pointingPairs() // This is a technique to eliminate possibilities in the possibilities 3D matrix. More about this: http://www.sudokubeginner.com/pointing-pair/
    {
        for(int i = 0; i < 9; i += 3)
        {
            for(int j = 0; j < 9; j += 3)
            {
                for(int s = 1; s < 10; s++)
                {
                    int[] rowCoordinates = new int[9];
                    int [] columnCoordinates = new int[9]; 
                    
                    int rowNum = -1; 
                    int columnNum = -1; 
                    
                    for(int a = i; a < (i + 3); a++)
                    {
                        for(int b = j; b < (j + 3); b++)
                        {
                            if(possibilities[a][b][(s - 1)] == 1)
                            {
                                rowCoordinates[a] = 1; 
                                rowNum = a; 
                                columnCoordinates[b] = 1; 
                                columnNum = b; 
                            }
                        }
                    }
                    
                    int rowSum = 0;
                    int columnSum = 0; 
                    
                    for(int r = 0; r < 9; r++)
                    {
                        if(rowCoordinates[r] == 1)
                        {
                            rowSum++; 
                        }
                        
                        if(columnCoordinates[r] == 1)
                        {
                            columnSum++; 
                        }
                    }
                    
                    if (rowSum == 1)
                    {
                        for(int g = 0; g < 9; g++)
                        {
                            if((g < j) || (g > (j + 2)))
                            {
                                possibilities[rowNum][g][(s - 1)] = 0; 
                            }
                        }
                    }
                    
                    if (columnSum == 1)
                    {
                        for(int g = 0; g < 9; g++)
                        {
                            if((g < i) || (g > (i + 2)))
                            {
                               possibilities[g][columnNum][(s - 1)] = 0; 
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void runSquares() // Will determine if there is only one spot a certain number can go within a square and place it there. 
    {
        for(int i = 0; i < 9; i += 3)
        {
            for(int j = 0; j < 9; j += 3)
            {
                for(int s = 1; s < 10; s++)
                {
                    int numOfPossibilities = 0;
                    int[][] squarePossibilities = {{-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}}; 
                    int index = 0; 
                    for(int a = i; a < (i + 3); a++)
                    {
                        for(int b = j; b < (j + 3); b++)
                        {
                            if(currentPuzzle2D[a][b] == 0)
                            {
                                if(!(checkRow(a, s)) && !(checkColumn(b, s)) && !(checkSquare(a,b, s)))
                                {
                                    squarePossibilities[index][0] = a; 
                                    squarePossibilities[index][1] = b; 
                                    index++; 
                                    numOfPossibilities++; 
                                }
                            }
                        }
                    }
                    
                    
                    if(numOfPossibilities == 1)
                    {
                        currentPuzzle2D[squarePossibilities[0][0]][squarePossibilities[0][1]] = s; 
                    }
                }
            }
        }
    }
    
    public void runColumns() // Will determine if there is only one spot a certain number can go within a column and place it there. 
    {   
        for(int j = 0; j < 9; j++)
        {
            for(int s = 1; s < 10; s++)
            {
                int numOfPossibilities = 0;
                int[][] rowPossibilities = {{-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}}; 
                int index = 0; 
                
                for(int i = 0; i < 9; i++)
                {
                    if(currentPuzzle2D[i][j] == 0)
                    {
                        if(!(checkRow(i, s)) && !(checkColumn(j, s)) && !(checkSquare(i, j, s)))
                        {
                            rowPossibilities[index][0] = i; 
                            rowPossibilities[index][1] = j; 
                            index++; 
                            numOfPossibilities++; 
                        }
                    } 
                }
                
                if(numOfPossibilities == 1)
                {
                    currentPuzzle2D[rowPossibilities[0][0]][rowPossibilities[0][1]] = s; 
                }
            }
        }
    }
    
    
    
    public void runRows() // Will determine if there is only one spot a certain number can go within a row and place it there. 
    {
        for(int i = 0; i < 9; i++)
        {
            for(int s = 1; s < 10; s++)
            {
                int numOfPossibilities = 0;
                int[][] rowPossibilities = {{-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}}; 
                int index = 0;
                
                for(int j = 0; j < 9; j++)
                { 
                    if(currentPuzzle2D[i][j] == 0)
                    {
                        if(!(checkRow(i, s)) && !(checkColumn(j, s)) && !(checkSquare(i, j, s)))
                        {
                            rowPossibilities[index][0] = i; 
                            rowPossibilities[index][1] = j; 
                            index++; 
                            numOfPossibilities++; 
                        }
                    } 
                }
                
                if(numOfPossibilities == 1)
                {
                    currentPuzzle2D[rowPossibilities[0][0]][rowPossibilities[0][1]] = s; 
                }
            }
        }
    }
    
    public int sumPossibilities(int row, int column) // Returns the total number of possibilities that exist for a box. 
    {
        int sum = 0; 
        
        for(int i = 0; i < 9; i++)
        {
            sum += possibilities[row][column][i]; 
        }
        
        return sum; 
    }
    
    public boolean checkRow(int row, int num) // Checks a row for a given number. Returns true if that number exists in the row and returns false otherwise. 
    {
        for (int i = 0; i < 9; i++)
        {
            if(currentPuzzle2D[row][i] == num)
            {
                //currentPuzzle2D[row][i] = 1; 
                return true; 
            }
        }
        
        return false; 
    }
    
    public boolean checkSquare(int row, int column, int num) // Checks a square for a given number. Returns true if that number exists in the square and returns false otherwise. 
    {
        if(row % 3 == 0) 
        {
            if(column % 3 == 0) 
            {
                for (int i = row; i < (row + 3); i++)
                {
                    for(int j = column; j < (column + 3); j++)
                    {
                        if(currentPuzzle2D[i][j] == num)
                        {
                            //currentPuzzle2D[row][column] = 1; 
                            return true; 
                        }
                    }
                }
            }
            
            if(column % 3 == 1) 
            {
                for (int i = row; i < (row + 3); i++)
                {
                    for(int j = (column - 1); j < (column + 2); j++)
                    {
                        if(currentPuzzle2D[i][j] == num)
                        {
                            return true; 
                        }
                    }
                }
            }
            
            if(column % 3 == 2) 
            {
                for (int i = row; i < (row + 3); i++)
                {
                    for(int j = (column - 2); j < (column + 1); j++)
                    {
                        if(currentPuzzle2D[i][j] == num)
                        {
                            return true; 
                        }
                    }
                }
            }
        }
        
        if(row % 3 == 1) 
        {
            if(column % 3 == 0) 
            {
                for (int i = (row - 1); i < (row + 2); i++)
                {
                    for(int j = column; j < (column + 3); j++)
                    {
                        if(currentPuzzle2D[i][j] == num)
                        {
                            return true; 
                        }
                    }
                }
            }
            
            if(column % 3 == 1) 
            {
                for (int i = (row - 1); i < (row + 2); i++)
                {
                    for(int j = (column - 1); j < (column + 2); j++)
                    {
                        if(currentPuzzle2D[i][j] == num)
                        {
                            return true; 
                        }
                    }
                }
            }
            
            if(column % 3 == 2) 
            {
                for (int i = (row - 1); i < (row + 2); i++)
                {
                    for(int j = (column - 2); j < (column + 1); j++)
                    {
                        if(currentPuzzle2D[i][j] == num)
                        {
                            return true; 
                        }
                    }
                }
            }
        }
        
        if(row % 3 == 2) 
        {
            if(column % 3 == 0) 
            {
                for (int i = (row - 2); i < (row + 1); i++)
                {
                    for(int j = column; j < (column + 3); j++)
                    {
                        if(currentPuzzle2D[i][j] == num)
                        {
                            return true; 
                        }
                    }
                }
            }
            
            if(column % 3 == 1) 
            {
                for (int i = (row - 2); i < (row + 1); i++)
                {
                    for(int j = (column - 1); j < (column + 2); j++)
                    {
                        if(currentPuzzle2D[i][j] == num)
                        {
                            return true; 
                        }
                    }
                }
            }
            
            if(column % 3 == 2) 
            {
                for (int i = (row - 2); i < (row + 1); i++)
                {
                    for(int j = (column - 2); j < (column + 1); j++)
                    {
                        if(currentPuzzle2D[i][j] == num)
                        {
                            return true; 
                        }
                    }
                }
            }
        }
        
        return false; 
    }
    
    public boolean checkColumn(int column, int num) // Checks a column for a given number. Returns true if that number exists in the column and returns false otherwise. 
    {
        for (int i = 0; i < 9; i++)
        {
            if(currentPuzzle2D[i][column] == num)
            {
                return true; 
            }
        }
        
        return false; 
    }
    
    public void printBoard() // Prints the current state of the board. 
    {
        for(int i = 0; i < 9; i++) 
        {
            if((i == 3) || (i == 6))
            { 
                System.out.println("-#######################################################################-"); 
            }
            else
            {
                System.out.println("-------------------------------------------------------------------------"); 
            }
            System.out.println("|                       #                       #                       |"); 
            for(int j = 0; j < 9; j++) 
            {
                if((j == 3) || (j == 6))
                {
                    System.out.print("#   " + currentPuzzle2D[i][j] + "   ");  
                }
                else
                {
                    System.out.print("|   " + currentPuzzle2D[i][j] + "   ");  
                }
            }
            System.out.print("|" + "\n" + "|                       #                       #                       |" + "\n"); 
        }
        System.out.println("-------------------------------------------------------------------------");
        
    }
    
    
}
