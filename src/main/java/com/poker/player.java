package com.poker;

import java.util.Arrays;
import java.util.Random;

public class player {
    private String name;
    private int balance;
    private standardCard[] holeCards = new standardCard[2];
    private boolean isInGame;

    public player(String name, int balance, standardCard[] holeCards){
        this.name = name;
        this.balance = balance;
        this.holeCards = holeCards;
        this.isInGame = true;
    }
    public String getName(){
        return this.name;
    }
    public int getBalance(){
        return this.balance;
    }
    public void addToBalance(int amount){
        this.balance += amount;
    }
    public void subtractFromBalance(int amount){
        this.balance -= amount;
    }
    public standardCard[] getHoleCards(){
        return this.holeCards;
    }
    public void setHoleCards(standardCard[] holeCards){
        this.holeCards = holeCards;
    }
    public boolean isInGame(){
        return this.isInGame;
    }
    public void setInGame (boolean isInGame){
        this.isInGame = isInGame;
    }
    public String toString(){
        return ("Player " +this.name + " has a balance of " + this.balance +
                " and a hole hand of " + this.holeCards[0] + " with " + this.holeCards[1] +
                ".\nStill in game: " + this.isInGame);
    }


    public static void main(String[] args) {
        standardCard[] holeCards = {new standardCard(1,"Hearts"), new standardCard(2,"Hearts")};
        player p1 = new player("Nick",100,holeCards);
        System.out.println(p1.getName());
        System.out.println(p1.getBalance());
        p1.addToBalance(50);
        System.out.println(p1.getBalance());
        p1.subtractFromBalance(25);
        System.out.println(p1.getBalance());
        System.out.println(p1.isInGame());
        System.out.println(Arrays.toString(p1.getHoleCards()));
        System.out.println(p1.toString());
        standardCard[] holeCard2 = {new standardCard(10,"Diamonds"), new standardCard(7,"Clubs")};
        p1.setHoleCards(holeCard2);
        System.out.println(p1.toString());

    }
}
