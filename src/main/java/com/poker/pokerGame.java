package com.poker;

import java.util.ArrayList;
import java.util.Scanner;

public class pokerGame {
    private ArrayList<player> playerList;
    private standardDeck  gameDeck;
    private standardCard[] communityCards;
    private int winningPot;
    private int smallBlind;
    private int bigBlind;
    private player dealer;
    private player playerSmallBlind;
    private player playerBigBlind;
    private int[] playersTotalBets;

    Scanner scan = new Scanner(System.in);

    public pokerGame(int smallBlind){
        this.playerList = new ArrayList<player>();
        this.gameDeck = new standardDeck();
        this.communityCards = new standardCard[5];
        this.winningPot =0;
        this.smallBlind = smallBlind;
        this.bigBlind = this.smallBlind * 2;
        playerSetUp();
    }

    public void playerSetUp() {
        boolean addAnotherPlayer = true;
        while(addAnotherPlayer) {
            addAnotherPlayer = false;
            addPlayer();
            System.out.println("Do you want to add another player? (Y/N)");
            if (scan.next().toLowerCase().contains("y")){
                addAnotherPlayer = true;
            }
        }
        this.dealer = playerList.get(0);
        if (playerList.size() > 2){
            this.playerSmallBlind = playerList.get(1);
            this.playerBigBlind = playerList.get(2);
        }
        this.playersTotalBets = new int[playerList.size()];
    }

    public void addPlayer(){
        System.out.println("Enter player name: ");
        String tempName = scan.next();
        System.out.println("Enter " + tempName + "s starting balance: ");
        int tempBalance = scan.nextInt();
        standardCard[] tempHoleCards = {gameDeck.getNextCard(), gameDeck.getNextCard()};
        playerList.add(new player(tempName, tempBalance, tempHoleCards));
        System.out.println(playerList.get(playerList.size()-1).toString());
    }

    public static  void main(String[] args) {
        pokerGame firstGame = new pokerGame(1);
    }
}