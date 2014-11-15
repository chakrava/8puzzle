/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg8puzzle;

/**
 * Represents a single block in a sliding block puzzle
 *
 * @author Erik Storla <estorla42@gmail.com>
 */
public class Node {
    int value;//the current block's values
    int goal;//the value the block wants to end up with
    int cost;//the calculated cost of moving the block's current value to it's goal
    int pos[]=new int[2];//holds the block's x,y coor
    
    
    Node(){
        value=-1;
        goal=-1;
        cost=-1;
    }
    
    Node(int i){
        value=i;
        goal=-1;
        cost=0;
    }
    
    Node(int i,int g){
        value=i;
        goal=g;
        cost=0;
    }
    
    Node(int i,int g,int[] p){
        value=i;
        goal=g;
        cost=0;
        pos=p;
    }
    
    Node(int i,int g,int c){
        value=i;
        goal=g;
        cost=c;
    }
    
    Node(int i,int g,int c,int[] p){
        value=i;
        goal=g;
        cost=c;
        pos=p;
    }
    
    Node(Node n){
        value=n.getValue();
        goal=n.getGoal();
        cost=n.getCost();
        pos=n.getPos();
    }
    
    void setValue(int i){
        value=i;
    }

    public int getValue() {
        return value;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int[] getPos() {
        return pos;
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }
    
    public String printPos(){
        return pos[0]+" "+pos[1];
    }
    
    public String toString(){
        return ""+value;
    }
}
