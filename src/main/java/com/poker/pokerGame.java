package com.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class pokerGame {
    private ArrayList<player> playerList;
    private standardDeck  gameDeck;
    private standardCard[] communityCards;
    private int winningPot;

    public pokerGame(){
        playerList = new ArrayList<player>();
        gameDeck = new standardDeck();
        communityCards = new standardCard[5];
        winningPot =0;
        playerSetUp();
    }

    public void playerSetUp() {
        boolean addAnotherPlayer = true;
        Scanner scan = new Scanner(System.in);
        while(addAnotherPlayer){
            addAnotherPlayer = false;
            addPlayer();
            System.out.println("Do you want to add another player? (Y/N)");
            if (scan.next().toLowerCase().contains("y")){
                addAnotherPlayer = true;
            }
        }
    }

    public void addPlayer(){
        System.out.println("Enter player name: ");
        Scanner sc = new Scanner(System.in);
        String playerName = sc.nextLine();
        System.out.println("Enter " + playerName + "s starting balance: ");
        int startingBalance = sc.nextInt();
        standardCard[] tempHoleCards = {gameDeck.getNextCard(), gameDeck.getNextCard()};
        System.out.println("Your card are: "+ Arrays.toString(tempHoleCards));
        playerList.add(new player(playerName, startingBalance, tempHoleCards));
        System.out.println(playerList.get(playerList.size()-1).toString());
    }

    public static  void main(String[] args) {
        pokerGame firstGame = new pokerGame();
    }
}