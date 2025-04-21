package solitaireMH;

import se.egy.graphics.ImgContainer;

public class Logic {
    private ImgContainer[][] deck = new ImgContainer[4][13];
    private ImgContainer[] backOfCards = new ImgContainer[52];

    public Logic() {
        initializeDeck();
    }

    private void initializeDeck() {
        String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
        String[] ranks = {"ace", "two", "three", "four", "five", "six", "seven", 
                         "eight", "nine", "ten", "jack", "queen", "king"};

        // Create all cards
        for (int suit = 0; suit < 4; suit++) {
            for (int rank = 0; rank < 13; rank++) {
                String imageName = ranks[rank] + "Of" + suits[suit] + ".png";
                deck[suit][rank] = new ImgContainer(0, 0, imageName);
            }
        }

        // Create card backs
        for (int i = 0; i < 52; i++) {
            backOfCards[i] = new ImgContainer(0, 0, "backOfCard.png");
        }
    }

    public int[] shuffleCards() {
        int[] shuffled = new int[52];
        for (int i = 0; i < 52; i++) {
            shuffled[i] = i;
        }

        // Fisher-Yates shuffle
        for (int i = 51; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            int temp = shuffled[i];
            shuffled[i] = shuffled[j];
            shuffled[j] = temp;
        }

        return shuffled;
    }

    public ImgContainer[][] getDeck() {
        return deck;
    }

    public ImgContainer getBackOfCard(int cardId) {
        ImgContainer back = backOfCards[cardId];
        ImgContainer front = deck[cardId / 13][cardId % 13];
        back.setX(front.getX());
        back.setY(front.getY());
        return back;
    }
}