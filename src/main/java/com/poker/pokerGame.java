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
        payOutBlinds();
    }
    public void payOutBlinds(){
        if(this.playerList.size() > 2){
            this.playerSmallBlind.reduceFromBalance(this.smallBlind);
            this.playersTotalBets[this.playerList.indexOf(playerSmallBlind)] = this.smallBlind;
            this.playerBigBlind.reduceFromBalance(this.bigBlind);
            this.playersTotalBets[this.playerList.indexOf(playerBigBlind)] = this.bigBlind;
        }
    }

    public void bettingRound(){
        int startingPlayerIndex = this.playerList.indexOf(playerSmallBlind);
        int currentPlayerIndex = startingPlayerIndex;
        for(int i = 0; i < this.playerList.size(); i++){
            individualBet(currentPlayerIndex);
        }
        while(!areAllBetsEqual()){
            individualBet(currentPlayerIndex);
        }
    }

    public void individualBet(int currentPlayerIndex){
        String checkOrCall = areAllBetsEqual() ? "check" : "call";
        System.out.println(this.playerList.get(currentPlayerIndex).toString() +
                "\n Do you want to fold, " + checkOrCall + " or raise?");
        String answer = scan.next();
        if(answer.toLowerCase().contains("fold")){
            fold(this.playerList.get(currentPlayerIndex));
        }
        else if(answer.toLowerCase().contains("call")){
            call(this.playerList.get(currentPlayerIndex));
        }
        else if(answer.toLowerCase().contains("raise")){
            raise(this.playerList.get(currentPlayerIndex));
        }
        currentPlayerIndex ++;
        if (currentPlayerIndex == this.playerList.size()){
            currentPlayerIndex = 0;
        }
    }

    public boolean areAllBetsEqual(){
        int highestBet = getHighestBet();
        for (int j = 0; j < this.playersTotalBets.length; j++){
            if (this.playersTotalBets[j] > highestBet && this.playerList.get(j).getIsInGame()) {
                return false;
            }
        }
        return true;

    }
    public int getHighestBet(){
        int highestBet = 0;
        for (int i = 0; i < this.playersTotalBets.length; i++) {
            if (this.playersTotalBets[i] > highestBet && this.playerList.get(i).getIsInGame()) {
                highestBet = this.playersTotalBets[i];
            }
        }
        return highestBet;
    }

    public void fold(Player player){
        player.setIsInGame(false);
    }

    public void call(Player player){
        int highestBet = getHighestBet();
        int playerBetDifference = highestBet - this.playersTotalBets[(this.playerList.indexOf(player))];
        player.reduceFromBalance(playerBetDifference);
        this.playersTotalBets[this.playerList.indexOf(player)] += playerBetDifference;
    }

    public void raise(Player player){
        System.out.println(player.getName() + " how much do you want to raise?");
        int raiseAmount = scan.nextInt();
        call(player);
        player.reduceFromBalance(raiseAmount);
        this.playersTotalBets[this.playerList.indexOf(player)] += raiseAmount;
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