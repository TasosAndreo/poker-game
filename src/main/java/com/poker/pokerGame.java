package com.poker;

import java.util.ArrayList;
import java.util.Scanner;

public class pokerGame {
    private ArrayList<Player> playerList;
    private standardDeck  gameDeck;
    private standardCard[] communityCards;
    private int winningPot;
    private int smallBlind;
    private int bigBlind;
    private Player dealer;
    private Player playerSmallBlind;
    private Player playerBigBlind;
    private int[] playersTotalBets;
    private int dealerButtonIndex;
    private int communityCardsDealt;

    Scanner scan = new Scanner(System.in);

    public pokerGame(int smallBlind){
        this.playerList = new ArrayList<Player>();
        this.gameDeck = new standardDeck();
        this.communityCards = new standardCard[5];
        this.winningPot = 0;
        this.smallBlind = smallBlind;
        this.bigBlind = this.smallBlind * 2;
        this.playersTotalBets = new int[0];
        this.dealerButtonIndex = 0;
        this.communityCardsDealt = 0;
        playerSetUp();
    }

    public void playerSetUp() {
        System.out.println("--- Poker Game Setup ---\n");
        boolean addAnotherPlayer = true;
        while(addAnotherPlayer) {
            addAnotherPlayer = false;
            addPlayer();
            if (playerList.size() < 2) {
                addAnotherPlayer = true;
                System.out.println("\nNeed at least 2 players.\n");
                continue;
            }
            System.out.println("\nAdd another player? (Y/N)");
            if (scan.next().toLowerCase().contains("y"))
                addAnotherPlayer = true;
        }
        System.out.println();
    }

    /** Start a new hand: reset deck, deal hole cards, assign blinds, collect blinds into pot. */
    public void startNewHand() {
        gameDeck.reset();
        gameDeck.shuffle();
        for (int i = 0; i < playerList.size(); i++) {
            standardCard[] hole = { gameDeck.getNextCard(), gameDeck.getNextCard() };
            playerList.get(i).setHoleCards(hole);
            playerList.get(i).setIsInGame(true);
        }
        playersTotalBets = new int[playerList.size()];
        for (int i = 0; i < playersTotalBets.length; i++) playersTotalBets[i] = 0;
        communityCardsDealt = 0;
        winningPot = 0;

        int n = playerList.size();
        dealer = playerList.get(dealerButtonIndex);
        if (n == 2) {
            playerSmallBlind = dealer;
            playerBigBlind = playerList.get((dealerButtonIndex + 1) % n);
        } else {
            playerSmallBlind = playerList.get((dealerButtonIndex + 1) % n);
            playerBigBlind = playerList.get((dealerButtonIndex + 2) % n);
        }
        payOutBlinds();
        System.out.println("\n--- New hand --- Dealer: " + dealer.getName() + ", SB: " + playerSmallBlind.getName() + ", BB: " + playerBigBlind.getName());
        System.out.println("Pot: " + winningPot);
    }

    public void payOutBlinds(){
        int n = playerList.size();
        if (n < 2) return;
        int sbIdx = playerList.indexOf(playerSmallBlind);
        int bbIdx = playerList.indexOf(playerBigBlind);
        playerSmallBlind.reduceFromBalance(Math.min(smallBlind, playerSmallBlind.getBalance()));
        int sbActual = Math.min(smallBlind, playerSmallBlind.getBalance());
        playersTotalBets[sbIdx] = sbActual;
        winningPot += sbActual;
        playerBigBlind.reduceFromBalance(Math.min(bigBlind, playerBigBlind.getBalance()));
        int bbActual = Math.min(bigBlind, playerBigBlind.getBalance());
        playersTotalBets[bbIdx] = bbActual;
        winningPot += bbActual;
    }

    private int getNextActivePlayerIndex(int from) {
        int next = (from + 1) % playerList.size();
        while (next != from && !playerList.get(next).getIsInGame())
            next = (next + 1) % playerList.size();
        return next;
    }

    private int countActivePlayers() {
        int c = 0;
        for (Player p : playerList) if (p.getIsInGame()) c++;
        return c;
    }

    public void bettingRound(){
        if (countActivePlayers() <= 1) return;
        int firstToAct = getNextActivePlayerIndex(playerList.indexOf(playerBigBlind));
        int current = firstToAct;
        do {
            if (countActivePlayers() <= 1) break;
            if (playerList.get(current).getIsInGame())
                individualBet(current);
            current = getNextActivePlayerIndex(current);
            if (current == firstToAct && areAllBetsEqual()) break;
        } while (true);
    }

    public void individualBet(int currentPlayerIndex){
        Player p = playerList.get(currentPlayerIndex);
        if (!p.getIsInGame()) return;
        String checkOrCall = areAllBetsEqual() ? "check" : "call";
        System.out.println("\n" + p.toString() + "Pot: " + winningPot + ", To call: " + (getHighestBet() - playersTotalBets[currentPlayerIndex]));
        System.out.println("Fold, " + checkOrCall + ", or raise?");
        String answer = scan.next();
        if (answer.toLowerCase().contains("fold")) {
            fold(p);
            System.out.println(p.getName() + " folds.\n");
        } else if (answer.toLowerCase().contains("call") || answer.toLowerCase().contains("check")) {
            call(p);
            System.out.println(p.getName() + " " + checkOrCall + "s.\n");
        } else if (answer.toLowerCase().contains("raise")) {
            raise(p);
            System.out.println(p.getName() + " raises.\n");
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
        int idx = playerList.indexOf(player);
        int playerBetDifference = Math.min(highestBet - playersTotalBets[idx], player.getBalance());
        player.reduceFromBalance(playerBetDifference);
        playersTotalBets[idx] += playerBetDifference;
        winningPot += playerBetDifference;
    }

    public void raise(Player player){
        System.out.println(player.getName() + " how much do you want to raise (extra on top of call)?");
        int raiseAmount = scan.nextInt();
        call(player);
        int idx = playerList.indexOf(player);
        int actual = Math.min(raiseAmount, player.getBalance());
        player.reduceFromBalance(actual);
        playersTotalBets[idx] += actual;
        winningPot += actual;
    }

    public void addPlayer(){
        System.out.println("Enter player name: ");
        String tempName = scan.next();
        System.out.println("Enter " + tempName + "'s starting balance: ");
        int tempBalance = scan.nextInt();
        standardCard[] placeholder = new standardCard[2];
        playerList.add(new Player(tempName, tempBalance, placeholder));
        System.out.println("Player " + tempName + " added with balance " + tempBalance + ".\n");
    }

    public void dealFlop() {
        if (communityCardsDealt != 0) return;
        gameDeck.getNextCard();
        for (int i = 0; i < 3; i++) communityCards[i] = gameDeck.getNextCard();
        communityCardsDealt = 3;
        printCommunityCards();
    }

    public void dealTurn() {
        if (communityCardsDealt != 3) return;
        gameDeck.getNextCard();
        communityCards[3] = gameDeck.getNextCard();
        communityCardsDealt = 4;
        printCommunityCards();
    }

    public void dealRiver() {
        if (communityCardsDealt != 4) return;
        gameDeck.getNextCard();
        communityCards[4] = gameDeck.getNextCard();
        communityCardsDealt = 5;
        printCommunityCards();
    }

    private void printCommunityCards() {
        System.out.print("\nCommunity cards: ");
        for (int i = 0; i < communityCardsDealt && communityCards[i] != null; i++)
            System.out.print(communityCards[i] + (i < communityCardsDealt - 1 ? ", " : ""));
        System.out.println("\n");
    }

    public standardCard[] getCommunityCards() {
        return communityCards;
    }

    public int getWinningPot() {
        return winningPot;
    }

    /** Play one full hand: deal, preflop, flop, turn, river, showdown. Returns index of winner(s); if tie, first winner. */
    public void playOneHand() {
        startNewHand();
        if (countActivePlayers() < 2) return;
        bettingRound();
        if (countActivePlayers() <= 1) {
            awardPotToLastStanding();
            return;
        }
        dealFlop();
        bettingRound();
        if (countActivePlayers() <= 1) {
            awardPotToLastStanding();
            return;
        }
        dealTurn();
        bettingRound();
        if (countActivePlayers() <= 1) {
            awardPotToLastStanding();
            return;
        }
        dealRiver();
        bettingRound();
        showdown();
    }

    private void awardPotToLastStanding() {
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getIsInGame()) {
                playerList.get(i).addToBalance(winningPot);
                System.out.println("\n" + playerList.get(i).getName() + " wins " + winningPot + " (all others folded).\n");
                winningPot = 0;
                return;
            }
        }
    }

    private void showdown() {
        int winnerIdx = HandEvaluator.showdownWinner(playerList, communityCards, playersTotalBets);
        if (winnerIdx >= 0) {
            playerList.get(winnerIdx).addToBalance(winningPot);
            System.out.println(playerList.get(winnerIdx).getName() + " wins " + winningPot + " with best hand.");
            winningPot = 0;
        }
        rotateDealerButton();
    }

    private void rotateDealerButton() {
        int n = playerList.size();
        if (n == 0) return;
        dealerButtonIndex = (dealerButtonIndex + 1) % n;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public static void main(String[] args) {
        pokerGame game = new pokerGame(2);
        while (game.getPlayerList().size() >= 2) {
            int withChips = 0;
            for (Player p : game.getPlayerList()) if (p.getBalance() > 0) withChips++;
            if (withChips < 2) {
                System.out.println("\nOnly one player with chips left. Game over.\n");
                break;
            }
            game.playOneHand();
            game.removeBustedPlayers();
            if (!game.promptAnotherHand())
                break;
        }
    }

    public boolean promptAnotherHand() {
        System.out.println("\nPlay another hand? (Y/N)");
        return scan.next().toLowerCase().contains("y");
    }

    private void removeBustedPlayers() {
        for (int i = playerList.size() - 1; i >= 0; i--) {
            if (playerList.get(i).getBalance() <= 0) {
                System.out.println(playerList.get(i).getName() + " is out (no chips left).");
                if (i < dealerButtonIndex) dealerButtonIndex--;
                else if (i == dealerButtonIndex && dealerButtonIndex >= playerList.size() - 1) dealerButtonIndex = 0;
                playerList.remove(i);
            }
        }
        if (dealerButtonIndex >= playerList.size() && !playerList.isEmpty()) dealerButtonIndex = 0;
    }
}