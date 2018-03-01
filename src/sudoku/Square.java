/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.ArrayList;
import static sudoku.Sudoku.n;

/**
 *
 * @author Stefan
 */
public class Square {
    int row;
    int column;
    int value;
    ArrayList<Integer> possibleNumbers;
    boolean visable;
    int region;
    boolean userChangeable;
    int visableValue;
    
}


