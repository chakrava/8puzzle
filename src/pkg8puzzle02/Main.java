/**
 * Course: CS 4523
 * Student name: Erik Storla
 * Student ID: 000159035
 * Assignment #: 2
 * Due Date: 9/30/2014
 * Signature: ____________________
 * Score: ____________________
 */
package pkg8puzzle02;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author Erik Storla <estorla42@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    final static char[] DIRECTIONS = {'u', 'r', 'd', 'l'};

    public static void main(String[] args) {

        //some initial values
        int puzzleSize = 3;
        int[] incInt = null;
        Comparator comparator = new State.AStarComparator();
        boolean hillClimb = false;
        Queue<State> queue = new PriorityQueue(50000, comparator);

        int slowTime = 0;

        if (args.length > 0) {
            for (String argument : args) {
                if (argument.contains("-")) {//look for flags
                    if (argument.toLowerCase().contains("pathlength")) {
                        comparator = new State.PathLengthComparator();
                        queue = new PriorityQueue(50000, comparator);
                    } else if (argument.toLowerCase().contains("manhattan")) {
                        comparator = new State.ManhattanComparator();
                        queue = new PriorityQueue(50000, comparator);
                    } else if (argument.toLowerCase().contains("bfs")) {
                        queue = new LinkedList();//breadth-first}
                    } else if (argument.toLowerCase().contains("hill")) {
                        hillClimb = true;
                    } else if (argument.toLowerCase().contains("slow")) {
                        slowTime = 3000;
                    } else {
                        printHelp();
                    }
                } else if (!argument.contains(",")) {
                    puzzleSize = Integer.parseInt(argument);
                } else {
                    String[] incStrings = argument.split(",");
                    incInt = new int[incStrings.length];
                    for (int i = 0; i < incInt.length; i++) {
                        incInt[i] = Integer.parseInt(incStrings[i]);
                    }
                }
            }
        }

        //int[] incPuzzle = new int[incInt.length];
        Puzzle initialPuzzle = new Puzzle(puzzleSize);
        List<Puzzle> visited = new ArrayList<>();
        //visited.add(initialPuzzle);

        //if puzzle isn't (n x n) it can't be solved
        if (incInt != null && incInt.length > 0 && Math.sqrt(incInt.length) % 1.0 == 0) {
            initialPuzzle = new Puzzle((int) Math.sqrt(incInt.length));
            initialPuzzle.setPuzzleTo(incInt);
        } else if (incInt != null && incInt.length > 0) {
            System.err.println("Invalid puzzle!");
            System.exit(1);
        } else {
            while (!initialPuzzle.randomize()) {
            }
        }
        //initialPuzzle.setToTest();
        //initialPuzzle.setPuzzleTo20();

        Puzzle goal = new Puzzle(initialPuzzle.puzzle.length);
        State initialState = new State(initialPuzzle, goal);

        goal.setToGoal();
        System.out.print("Goal:");
        goal.printArray();

        System.out.print("\nIntial puzzle:");
        initialPuzzle.printArray();
        System.out.println("Initial cost: " + initialPuzzle.calculateManhattanCost(goal));

        if (initialState.isSolution()) {
            System.out.println("Given puzzle is already solved");
            System.exit(0);
        }

        queue.add(initialState);

        while (!queue.isEmpty()) {
            //System.out.println(queue.size()+" items in queue");
            
            State currentState = queue.remove();//get next state to search
            if (!visited.contains(currentState.getPuzzle())) {//if we haven't searched state...
                currentState.printPath();
                currentState.expand();//get children
                visited.add(currentState.getPuzzle());//add current state to visited list

                try {//slows loop down if "-slow" flag is set on run
                    Thread.sleep(slowTime);
                } catch (InterruptedException ex) {
                    
                }

                for (State child : currentState.getChildren()) {
                    //look if any of the children are the solution
                    if (child.isSolution()) {
                        System.out.println("\nSolution found!");
                        System.out.print("\nInitial Puzzle:");
                        initialState.printPath();
                        System.out.print("\nSolution:");
                        child.printPath();
                        System.exit(0);
                    }

                    //add child to search queue
                    //if hillClimb is set only allows children <= current cost
                    if (!hillClimb ||
                            (child.getManhattanCost() <= currentState.getManhattanCost())) {
                        queue.add(child);
                    }
                }
            } else {
                //System.out.println("I skipped something");
            }
        }

        System.out.println("\nNo solution found!");
    }

    private static void printHelp() {
        System.out.println("\n8PUZZLE [puzzle] -[flags]");
        System.out.println("\n[puzzle]\n   Either a single number or the puzzle to be solved\n"
                + "   \"8puzzle 3\"  generates a 3x3 puzzle\n"
                + "   \"8puzzle 1,2,3,4,5,6,7,8,0\"  generates the puzzle:\n"
                + "   \t\t[1] [2] [3]\n\t\t[4] [5] [6]\n\t\t[7] [8] [ ]");
        System.out.println("\n-[flags]\n"
                + "   -slow\trun each search slowly (3 seconds)\n"
                + "   -hill\thill-climb search, non-optimal and may not find an answer\n"
                + "   -bfs\t\tbreadth-first search\n"
                + "   -manhattan\tsearch based on manhattan cost of children (non-optimal)");
        System.exit(0);
    }
}
