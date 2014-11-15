/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg8puzzle;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Erik Storla <estorla42@gmail.com>
 */
public class State {
    Puzzle puzzle=null;
    List<Character> instructions=null;
    int steps;
    
    State(Puzzle p){
        puzzle=p;
        instructions=new ArrayList<>();
        steps=0;
    }
    
    Puzzle getPuzzle(){
        return puzzle;
    }
    
    void setPuzzle(Puzzle p){
        puzzle=p;
    }

    public List<Character> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Character> i) {
        instructions = i;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int s) {
        steps=s;
    }
    
    String printInstructions(){
        String temp="";
        for(char c:instructions){
            temp+=c;
        }
        return temp;
    }
    
    void addInstruction(char c){
        instructions.add(c);
    }
}
