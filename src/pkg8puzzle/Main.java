/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Erik Storla <estorla42@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Puzzle originalPuzzle = new Puzzle(2);
        Node[][] rawPuzzle = originalPuzzle.getPuzzle();

        //System.out.println();
        List<Puzzle> puzzles = new ArrayList<>();
        List<Node> neighbors = originalPuzzle.findNeighbors(originalPuzzle.getEmptyCell());
        puzzles.add(originalPuzzle);

        boolean solutionFound = false;

        while (!solutionFound) {
            //Puzzle currentPuz = puzzles.get(0);
            //currentPuz = puzzles.get(0);
            Puzzle currentPuz=puzzles.remove(0);
            
            System.out.println("\nLooking at:");
            currentPuz.printArray();
            
            for (Node n : neighbors) {
                //System.out.println("\n"+n.printPos());

                Node temp = currentPuz.getEmptyCell();

                Puzzle tempPuz = new Puzzle(currentPuz.swapNode(temp, n));
                if (tempPuz.isSolution()) {
                    System.out.println("Found solution!");
                    solutionFound = true;
                }
                puzzles.add(tempPuz);
                System.out.println("\nCost: " + tempPuz.calculateCost());
                tempPuz.printArray();
                currentPuz.swapNode(n, temp);
            }
            
            //Collections.sort(puzzles, originalPuzzle.new costComparator());

            System.out.print("\npuzzles' costs: ");
            for (Puzzle p : puzzles) {
                System.out.print(p.getCost() + " ");
            }
            System.out.println();
        }

        for (int i = 0; i < puzzles.size(); i++) {
//            if(puzzles.get(i).isSolution()){
//                System.out.println("FOUND A SOLUTION!");
//            }
            System.out.println(puzzles.get(i).calculateCost());
            puzzles.get(i).printArray();
        }

//        for(Puzzle p:puzzles){
//            System.out.println(puzzles.size());
//            p.printArray();
//        }
//        
//        System.out.println();
//        originalPuzzle.swapNode(originalPuzzle.getEmptyCell(),rawPuzzle[0][1]);
//        originalPuzzle.printArray();
//        System.out.println();
//        originalPuzzle.swapNode(originalPuzzle.getEmptyCell(),rawPuzzle[0][1]);
//        originalPuzzle.printArray();
        //System.out.println(puzzle.calculateCost());
//        Node empty = puzzle.getEmptyCell();
//        List<Node> neighbors = puzzle.findNeighbors(empty);
//        for(Node n:neighbors){
//            System.out.println(n.printPos());
//        }
    }

}
