/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8puzzle02;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Erik Storla <estorla42@gmail.com>
 */
public class Puzzle {

    int[][] puzzle = null;
    int[] emptyPos = new int[2];

    Puzzle() {
        puzzle = new int[3][3];
    }

    Puzzle(int i) {
        puzzle = new int[i][i];
    }

    Puzzle(Puzzle pInc) {
        int[][] p = pInc.getPuzzle();

        puzzle = new int[p.length][p[0].length];

        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                puzzle[i][j] = p[i][j];
            }
        }

        emptyPos = pInc.getEmptyPos();
    }

    Puzzle(int[][] p, int[] em) {
        puzzle = new int[p.length][p[0].length];

        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                puzzle[i][j] = p[i][j];
            }
        }

        emptyPos = em;
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(int[][] i) {
        puzzle = i;
    }

    public int[] getEmptyPos() {
        return emptyPos;
    }

    public void setEmptyPos(int[] i) {
        emptyPos = i;
    }

    @Override
    //two puzzles are equal if their states are the same
    public boolean equals(Object p2) {
        return Arrays.deepEquals(this.getPuzzle(), ((Puzzle) p2).getPuzzle());

//        for (int i = 0; i < puzzle.length; i++) {
//            for (int j = 0; j < puzzle[i].length; j++) {
//                if(puzzle[i][j] != ((Puzzle)p2).getPuzzle()[i][j]){
//                    return false;
//                }
//            }
//        }
//        return true;
    }

    //sets the puzzle to be the goal state
    void setToGoal() {
        int size = puzzle.length;
        int values[] = new int[size * size];//holds the values of the puzzle

        for (int i = 0; i < values.length; i++) {
            values[i] = i + 1;
        }

        int valuePos = 0;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                int[] pos = {i, j};
                puzzle[i][j] = values[valuePos];//sets the block

                //if I set the empty cell update its trackers
                if (values[valuePos] == 0) {
                    emptyPos = pos;
                }

                //if I set the last block set as empty cell goal
                if (valuePos == values.length - 1) {
                    puzzle[i][j] = 0;
                }
                valuePos++;
            }
        }
    }

    boolean randomize() {
        int sizeX = puzzle.length;
        int sizeY = puzzle[0].length;
        int values[] = new int[sizeX * sizeY];//holds the values of the puzzle
        //int goalValues[] = new int[size * size];//holds the goal positions of the puzzle

        for (int i = 0; i < values.length; i++) {
            values[i] = i;
            //goalValues[i] = i + 1;//goals don't include 0
        }

        shuffleArray(values);//randomize the order of the values

        if (!solveable(values)) {
            System.out.println("Unsolveable random puzzle, generating new puzzle...");
            //System.exit(1);
            return false;
        }

        int valuePos = 0;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                int[] pos = {i, j};
                puzzle[i][j] = values[valuePos];//sets the block

                //if I set the empty cell update its trackers
                if (values[valuePos] == 0) {
                    emptyPos = pos;
                }

                valuePos++;
            }
        }
        return true;
    }

    //sets puzzle to a known state for testing, solvable in 20 moves
    void setPuzzleTo20() {
        int size = puzzle.length;
        int values[] = {7, 6, 2, 5, 3, 1, 0, 4, 8};//solvable in 20 moves

        int valuePos = 0;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle.length; j++) {
                int[] pos = {i, j};
                puzzle[i][j] = values[valuePos];//sets the block

                //if I set the empty cell update its trackers
                if (values[valuePos] == 0) {
                    emptyPos = pos;
                }

                valuePos++;
            }
        }
    }

    //set puzzle to given values
    void setPuzzleTo(int[] values) {
        if (!solveable(values)) {
            System.out.println("Unsolveable!");
            System.exit(0);
        }

        int size = puzzle.length;

        int valuePos = 0;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                int[] pos = {i, j};
                puzzle[i][j] = values[valuePos];//sets the block

                //if I set the empty cell update its trackers
                if (values[valuePos] == 0) {
                    emptyPos = pos;
                }

                valuePos++;
            }
        }
    }

    //checks to see if the current puzzle can be solved
    boolean solveable(int[] v) {
        int inversions = 0;

        for (int i : v) {
            System.out.print(i + " ");
        }

        for (int i = 0; i < v.length - 1; i++) {
            for (int j = i + 1; j < v.length; j++) {
                if (v[i] != 0 && v[j] != 0 && v[i] > v[j]) {
                    inversions+=1;
                    //System.out.println(inversions+": "+v[i]+" "+v[j]);
                }
            }
        }

        System.out.println("Inversions: " + inversions);
        
        if (this.getPuzzle().length % 2 != 0) {//odd puzzle
            if (inversions % 2 == 0) {//solvable if even inversions
                return true;
            } else {
                return false;
            }
        } else {//even puzzle
            int emptyPos = -1;
            for (int i = 0; i < v.length && emptyPos < 1; i++) {
                if (v[i] == 0) {
                    emptyPos = i;
                }
            }
            int emptyRow = (int) (emptyPos) / ((int) Math.sqrt(v.length));//find row of empty block
            
            System.out.println(emptyPos + "\tRow Length: " +
                    (int) Math.sqrt(v.length) + "\tEmpty row: " + emptyRow);
            
            return ((inversions + emptyRow) % 2 != 0);//solvable if inversions+emptyRow is odd
        }
    }

    Puzzle simSwap(int[] pos) {
        //int temp=puzzle[pos[0]][pos[1]];
        Puzzle temp = new Puzzle(puzzle.length);
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                temp.puzzle[i][j] = puzzle[i][j];
            }
        }

        temp.getPuzzle()[emptyPos[0]][emptyPos[1]] = temp.getPuzzle()[pos[0]][pos[1]];
        temp.getPuzzle()[pos[0]][pos[1]] = 0;
        temp.getEmptyPos()[0] = pos[0];
        temp.getEmptyPos()[1] = pos[1];

        return temp;
    }

    int findCost(int[] pos, Puzzle g) {
        int[][] goal = g.getPuzzle();
        int currentValue = puzzle[pos[0]][pos[1]];
        int goalValue = goal[pos[0]][pos[1]];
        //System.out.println("Finding cost of: " + pos[0] + "," + pos[1]);
        //System.out.println(currentValue + " " + goalValue);

        if (currentValue == goalValue) {//I'm in the right position
            //System.out.println("\tIn position! 0 cost");
            return 0;
        }

        //create an array of all the possible x,y combinations that could reach my goal
        //0,1,-1,2,-2,etc...
        int maxRange = puzzle.length - 1;
        int[] range = new int[(maxRange * 2) + 1];

        for (int i = 0; i < maxRange; i++) {
            range[i * 2] = i + 1;
            range[i * 2 + 1] = -(i + 1);
        }
        range[range.length - 1] = 0;//make sure to include 0!

        for (int i = 0; i <= range.length; i++) {//y coor
            for (int j = 0; j <= range.length; j++) {//x coor
                try {//lazier/easier than properly catching array edges
                    //System.out.println("\t"+range[i]+" "+range[j]);
                    int temp = puzzle[pos[0] + range[i]][pos[1] + range[j]];
                    //if (temp.getGoal() == n.getValue()) {
                    if (temp == goalValue) {
                        //System.out.println("\tCost at: " + range[i] + " " + range[j]);
                        return Math.abs(range[i]) + Math.abs(range[j]);

                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        return -1;//if I somehow couldn't reach my goal, return -1
    }

    //calculate the total cost of all blocks in the puzzle, also returns the total cost
    int calculateManhattanCost(Puzzle g) {
        int total = 0;

        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                int[] pos = {i, j};
                total += findCost(pos, g);
            }
        }

        return total;
    }

    public void printArray() {
        System.out.println();
        for (int[] a : puzzle) {
            for (int i : a) {
                if (i != 0) {
                    System.out.print("[" + i + "]\t");
                } else {
                    System.out.print("[" + " " + "]\t");
                }
            }
            System.out.println();
        }
    }

    //Fisher-Yates shuffle
    //http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
    static void shuffleArray(int[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);

            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
