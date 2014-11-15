/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8puzzle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Erik Storla <estorla42@gmail.com>
 */
public final class Puzzle {

    Node[][] puzzle = null;//represents the sliding block puzzle
    int[] emptyCellInt = new int[2];//x,y coordinates of empty block
    Node emptyCell = null;//link to empty block
    int cost = 0;

    Puzzle() {
        puzzle = new Node[3][3];
        randomize();
    }

    Puzzle(int i) {
        if (false && i % 2 == 0) {
            throw new RuntimeException("Must be an odd puzzle!");
        }
        puzzle = new Node[i][i];
        randomize();
    }

    Puzzle(Puzzle p) {
        puzzle = p.getNewPuzzle();
        emptyCellInt = p.getEmptyCellInt();
        emptyCell = p.getEmptyCell();
        cost = p.getCost();
    }

    public Node[][] getNewPuzzle() {
        Node[][] temp = new Node[puzzle.length][puzzle.length];
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                temp[i][j] = new Node(puzzle[i][j]);
            }
        }

        return temp;
    }

    public Node[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Node[][] puzzle) {
        this.puzzle = puzzle;
    }

    public Node getEmptyCell() {
        return emptyCell;
    }

    public void setEmptyCell(Node emptyCell) {
        this.emptyCell = emptyCell;
    }

    public int[] getEmptyCellInt() {
        return emptyCellInt;
    }

    public void setEmptyCellInt(int[] emptyPos) {
        emptyCellInt = emptyPos;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    //Creates and initialzized the randomized puzzle
    void randomize() {
        int size = puzzle.length;
        int values[] = new int[size * size];//holds the values of the puzzle
        int goalValues[] = new int[size * size];//holds the goal positions of the puzzle

        for (int i = 0; i < values.length; i++) {
            values[i] = i;
            goalValues[i] = i + 1;//goals don't include 0
        }

        shuffleArray(values);//randomize the order of the values

        int valuePos = 0;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle.length; j++) {
                int[] pos = {i, j};
                puzzle[i][j] = new Node(values[valuePos], goalValues[valuePos], pos);//sets the block

                //if I set the empty cell update its trackers
                if (values[valuePos] == 0) {
                    emptyCellInt = pos;
                    emptyCell = puzzle[i][j];
                }

                //if I set the last block set as empty cell goal
                if (valuePos == goalValues.length - 1) {
                    puzzle[i][j].setGoal(0);
                }

                valuePos++;
            }
        }

        cost = calculateCost();

        System.out.println("New randomized array:");
        printArray();
    }

    boolean isSolution() {
        if (cost == 0) {
            return true;
        }
        return false;
    }

    //swaps two nodes' positions    
    Puzzle swapNode(Node n, Node m) {
        int temp = n.getValue();

        n.setValue(m.getValue());
        m.setValue(temp);

        if (n.getValue() == 0) {
            int[] pos = n.getPos();
            emptyCellInt = pos;
            emptyCell = n;
        }

        if (m.getValue() == 0) {
            int[] pos = m.getPos();
            emptyCellInt = pos;
            emptyCell = m;
        }

        cost = calculateCost();

        return this;
    }

    //finds and returns the neighboring nodes to the given node
    List<Node> findNeighbors(Node n) {
        int[] pos = n.getPos();
        List<Node> neighbors = new ArrayList<>();

        int[] range = {-1, 1};

        for (int i : range) {
            try {
                neighbors.add(puzzle[pos[0]][pos[1] + i]);
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                neighbors.add(puzzle[pos[0] + i][pos[1]]);
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }

        return neighbors;
    }

    List<Character> findNeighborsDirection(Node n) {
        int[] pos = n.getPos();
        List<Character> neighbors = new ArrayList<>();

        int[] range = {-1, 1};
        
        if(puzzle[pos[0]][pos[1] + 1]!=null){
            //neighbors.add("")
        }

//        for (int i : range) {
//            try {
//                neighbors.add(puzzle[pos[0]][pos[1] + i]);
//            } catch (ArrayIndexOutOfBoundsException e) {
//            }
//            try {
//                neighbors.add(puzzle[pos[0] + i][pos[1]]);
//            } catch (ArrayIndexOutOfBoundsException e) {
//            }
//        }

        return neighbors;
    }

    //finds the idealized cost of moving a given node to its goal
    int findCost(Node n) {
        int[] pos = n.getPos();
        //System.out.println("Finding cost of: " + pos[0] + "," + pos[1]);

        if (n.getValue() == n.getGoal()) {//I'm in the right position
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
                    Node temp = puzzle[pos[0] + range[i]][pos[1] + range[j]];
                    if (temp.getGoal() == n.getValue()) {
                        //System.out.println("\tCost at: " + range[i] + " " + range[j]);
                        //return (int) Math.ceil(Math.sqrt(Math.pow(Math.abs(range[i]), 2) + Math.pow(Math.abs(range[j]), 2)));
                        return Math.abs(range[i]) + Math.abs(range[j]);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        return -1;//if I somehow couldn't reach my goal, return -1
    }

    //calculate the total cost of all blocks in the puzzle, also returns the total cost
    int calculateCost() {
        int total = 0;

        for (Node[] a : puzzle) {
            for (Node n : a) {
                int cost = findCost(n);
                n.setCost(cost);

                if (cost > 0) {
                    total += cost;
                }

//                if (n.getValue() == n.getGoal()) {
//                    n.setCost(0);
//                }
//                for (Node neighbor : neighbors) {
//                    if (n.getValue() == neighbor.getGoal()) {
//                        n.setCost(1);
//                        total += 1;
//                    }
//                }
            }
        }

        return total;
    }

    class costComparator implements Comparator<Puzzle> {

        @Override
        public int compare(Puzzle p1, Puzzle p2) {
            return p1.getCost() - p2.getCost();
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

    //print the actual block puzzle
    public void printArray() {
        for (Node[] a : puzzle) {
            for (Node i : a) {
                if (i.getValue() != 0) {
                    System.out.print("[" + i + "]\t");
                } else {
                    System.out.print("[" + " " + "]\t");
                }
            }
//            System.out.print("\t\t");
//            for (Node i : a) {
//                if (i.getGoal() != 0) {
//                    System.out.print("[" + i.getGoal() + "]\t");
//                } else {
//                    System.out.print("[" + " " + "]\t");
//                }
//            }

            System.out.print("\t\t");
            for (Node i : a) {
                if (i.getCost() >= 0) {
                    System.out.print("[" + i.getCost() + "]\t");
                } else {
                    System.out.print("[" + " " + "]\t");
                }
            }

            System.out.println();
        }
    }
}
