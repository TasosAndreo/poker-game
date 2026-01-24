package com.poker;

public class standardCard {

    public int value;
    public String suit;
    public String colour;

    public standardCard(int value, String suit) {
        this.value = value;
        this.suit = suit;
        if (this.suit.equals("Hearts") || this.suit.equals("Diamonds")) {
            this.colour ="Red";
        }
        else if (this.suit.equals("Spades") || this.suit.equals("Clubs")) {
            this.colour ="Black";
        }
    }

    public int getValue() {
        return value;
    }
    public String getSuit() {
        return suit;
    }
    public String getColour() {
        return colour;
    }

    public String convertValueToName() {
        switch (value) {
            case 1: return "Ace";
            case 2: return "Two";
            case 3: return "Three";
            case 4: return "Four";
            case 5: return "Five";
            case 6: return "Six";
            case 7: return "Seven";
            case 8: return "Eight";
            case 9: return "Nine";
            case 10: return "Ten";
            case 11: return "Jack";
            case 12: return "Queen";
            case 13: return "King";
            case 14: return "Ace";
            default: return "Value not valid";
        }
    }

    public String toString () {
        return convertValueToName() + " of " + this.suit;
    }

    static void main(String[] args) {
        standardCard card = new standardCard(1, "Spades");
        System.out.println(card.toString());
    }
}
