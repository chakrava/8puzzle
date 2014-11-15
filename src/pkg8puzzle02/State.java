/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8puzzle02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Erik Storla <estorla42@gmail.com>
 */
public class State implements Comparable<State> {

    final static char[] DIRECTIONS = {'u', 'r', 'd', 'l'};

    Puzzle puzzle = null;//puzzle's state
    Puzzle goal = null;//puzzle's goal
    List<Character> path = null;//the steps taken to reach this state
    int manhattanCost;
    List<State> children = new ArrayList<>();//child nodes to this state

    State(Puzzle p, Puzzle g) {
        puzzle = p;
        goal = g;
        path = new ArrayList();
        manhattanCost = p.calculateManhattanCost(g);
    }

    State(Puzzle p, Puzzle g, List<Character> pa) {
        puzzle = p;
        goal = g;
        path = new ArrayList(pa);
        manhattanCost = p.calculateManhattanCost(g);
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        manhattanCost = this.getPuzzle().calculateManhattanCost(goal);
    }

    public Puzzle getGoal() {
        return goal;
    }

    public void setGoal(Puzzle goal) {
        this.goal = goal;
        manhattanCost = this.getPuzzle().calculateManhattanCost(goal);
    }

    public List<Character> getPath() {
        return path;
    }

    public void setPath(List<Character> path) {
        this.path = path;
    }

    public float getManhattanCost() {
        return manhattanCost;
    }

    public void setManhattanCost(int i) {
        manhattanCost = i;
    }

    public List<State> getChildren() {
        return children;
    }

    public void setChildren(List<State> children) {
        this.children = children;
    }

    int manhattanCost() {
        return puzzle.calculateManhattanCost(goal);
    }

    int getPathLength() {
        return path.size();
    }

    boolean isSolution() {
        return Arrays.deepEquals(this.getPuzzle().getPuzzle(), goal.getPuzzle());
//        if (puzzle.calculateManhattanCost(goal) == 0) {
//            return true;
//        }
//        return false;
    }

    //add another s
    void addPath(char c) {
        path.add(c);
    }

    //get child in direction u, d, l, or r
    State branch(char c) {
        State temp = new State(new Puzzle(this.getPuzzle()), goal, this.getPath());
        int[] emptyPos = temp.getPuzzle().getEmptyPos();
        int[] direction = branchGetInt(c);

        int[] swapCell = new int[2];
        swapCell[0] = emptyPos[0] + direction[0];
        swapCell[1] = emptyPos[1] + direction[1];

        try {
            temp.setPuzzle(temp.getPuzzle().simSwap(swapCell));
        } catch (ArrayIndexOutOfBoundsException e) {
            //System.out.println("Out of bounds");
            return null;
        }
        temp.addPath(c);

        return temp;
    }

    //converts direction char into a usable int
    private int[] branchGetInt(char c) {
        int[] direction = new int[2];
        switch (c) {
            case 'u':
                direction[0] = -1;
                direction[1] = 0;
                break;
            case 'r':
                direction[0] = 0;
                direction[1] = 1;
                break;
            case 'd':
                direction[0] = 1;
                direction[1] = 0;
                break;
            case 'l':
                direction[0] = 0;
                direction[1] = -1;
                break;
        }
        return direction;
    }

    //get all children of state
    List<State> expand() {
        List<State> temp = new ArrayList<>();

        for (char direction : DIRECTIONS) {
            State tempState = this.branch(direction);
            if (tempState != null) {
                //tempState.printPath();
                temp.add(tempState);
            }
        }
        children = temp;
        return temp;
    }

    void printPath() {
        System.out.println();
        List<Character> tempPath = this.getPath();
        System.out.print("(" + tempPath.size() + ") ");
        for (char c : tempPath) {
            System.out.print(c + " ");
        }

        this.getPuzzle().printArray();
        System.out.println("Cost: " + (this.manhattanCost()+this.getPathLength()));
    }

    @Override
    public int compareTo(State s2) {
        if (this.getManhattanCost() > ((State) s2).getManhattanCost()) {
            return 1;
        } else if (this.getManhattanCost() < ((State) s2).getManhattanCost()) {
            return -1;
        } else {
            return 0;
        }
    }

    //compares two states by Manhattan cost
    static class ManhattanComparator implements Comparator<State> {

        @Override
        public int compare(State s1, State s2) {
            if (s1.getManhattanCost() > ((State) s2).getManhattanCost()) {
                return 1;
            } else if (s1.getManhattanCost() < ((State) s2).getManhattanCost()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    //compares two states by A* cost
    static class AStarComparator implements Comparator<State> {

        @Override
        public int compare(State s1, State s2) {
            if (s1.getManhattanCost() + s1.getPathLength() >
                    s2.getManhattanCost() + s2.getPathLength()) {
                return 1;
            } else if (s1.getManhattanCost() + s1.getPathLength() <
                    s2.getManhattanCost() + s2.getPathLength()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    //compares two states by path length
    static class PathLengthComparator implements Comparator<State> {

        @Override
        public int compare(State s1, State s2) {
            if (s1.getPathLength() > s2.getPathLength()) {
                return 1;
            } else if (s1.getPathLength() < s2.getPathLength()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
