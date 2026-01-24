package com.poker;

import java.util.ArrayList;

public class standardDeck {
    private ArrayList<standardCard> deck = new ArrayList<standardCard>();

    public standardDeck() {
        reset();
        shuffle();
    }
    public void reset() {
        this.deck.clear();

        // Adding Spades Suit
        for (int i = 2; i < 15; i++) {
            deck.add(new standardCard(i, "Spades"));
        }
        // Adding Hearts Suit
        for (int j = 2; j < 15; j++) {
            deck.add(new standardCard(j, "Hearts"));
        }
        // Adding Diamonds Suit
        for (int k = 2; k < 15; k++) {
            deck.add(new standardCard(k, "Diamonds"));
        }
        // Adding Clubs Suit
        for (int l = 2; l < 15; l++) {
            deck.add(new standardCard(l, "Clubs"));
        }
    }
    public void shuffle() {
        ArrayList<standardCard> tempDeck = new ArrayList<standardCard>();
        while (!this.deck.isEmpty()) {
            int randomIndex = ((int) (Math.random() * 100)) % this.deck.size();
            tempDeck.add(this.deck.remove(randomIndex));
        }
        this.deck = tempDeck;
    }
    public standardCard getNextCard() {
        return this.deck.remove(this.deck.size() - 1);
    }
    public int getCardRemaining() {
        return this.deck.size();
    }
    public static void main (String[] args) {
        standardDeck sd = new standardDeck();
        System.out.println(sd.deck.toString());
        System.out.println(sd.getNextCard());
        System.out.println(sd.getCardRemaining());
        System.out.println(sd.getNextCard());
        System.out.println(sd.getNextCard());
        System.out.println(sd.getNextCard());
        System.out.println(sd.getCardRemaining());
    }
}
