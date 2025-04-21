package solitaireMH;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Stack;

import se.egy.graphics.GameScreen;
import se.egy.graphics.ImgContainer;

public class SolitaireGame implements MouseListener, KeyListener {
    // Game constants
    private final int WIDTH = 772;
    private final int HEIGHT = 688;  // Increased height to accommodate foundation piles
    private final int CARD_WIDTH = 57;
    private final int CARD_HEIGHT = 82;
    private final int CARD_OFFSET = 15;
    
    // Game components
    private GameScreen gs;
    private Logic deck;
    private ImgContainer[][] Deck;
    private int[] shuffled;
    
    // Card piles
    private Stack<Integer>[] tableauPiles = new Stack[7];
    private Stack<Integer>[] foundationPiles = new Stack[4];
    private Stack<Integer> stockPile = new Stack<>();
    private Stack<Integer> wastePile = new Stack<>();
    
    // Game state
    private boolean[] cardTurned;
    private ImgContainer[] cardImages;
    private int selectedCard = -1;
    private int selectedPile = -1; // -1: none, 0-6: tableau, 7-10: foundation, 11: stock, 12: waste
    private int dragOffsetX, dragOffsetY;
    private boolean isDragging = false;
    
    // Positions
    private int[] tableauX = new int[7];
    private int foundationX = 500;
    private int stockX = 15;
    private int wasteX = 100;

    public SolitaireGame() {
        initializeGame();
        setupUI();
        dealCards();
        render();
    }

    private void initializeGame() {
        gs = new GameScreen("Solitaire", WIDTH, HEIGHT, false);
        deck = new Logic();
        Deck = deck.getDeck();
        shuffled = deck.shuffleCards();
        cardTurned = new boolean[52];
        cardImages = new ImgContainer[52];
        
        // Initialize piles
        for (int i = 0; i < 7; i++) {
            tableauPiles[i] = new Stack<>();
        }
        for (int i = 0; i < 4; i++) {
            foundationPiles[i] = new Stack<>();
        }
        
        // Set tableau positions
        for (int i = 0; i < 7; i++) {
            tableauX[i] = 110 + i * 85;
        }
    }

    private void setupUI() {
        gs.setMouseListener(this);
        gs.setKeyListener(this);
        gs.setBackground("solitaireBackground.png");
    }

    private void dealCards() {
        // Deal cards to tableau piles
        int cardIndex = 0;
        for (int pile = 0; pile < 7; pile++) {
            for (int cardInPile = 0; cardInPile <= pile; cardInPile++) {
                int cardId = shuffled[cardIndex++];
                tableauPiles[pile].push(cardId);
                
                // Only the top card is face up
                boolean isFaceUp = (cardInPile == pile);
                cardTurned[cardId] = !isFaceUp;
                
                // Position the card
                Deck[cardId / 13][cardId % 13].setX(tableauX[pile]);
                Deck[cardId / 13][cardId % 13].setY(150 + cardInPile * CARD_OFFSET);
                
                // Set card image
                if (isFaceUp) {
                    cardImages[cardId] = Deck[cardId / 13][cardId % 13];
                } else {
                    cardImages[cardId] = deck.getBackOfCard(cardId);
                }
            }
        }
        
        // Add remaining cards to stock pile
        while (cardIndex < 52) {
            int cardId = shuffled[cardIndex++];
            stockPile.push(cardId);
            cardTurned[cardId] = true; // Face down in stock
            
            // Position the card (stacked)
            Deck[cardId / 13][cardId % 13].setX(stockX);
            Deck[cardId / 13][cardId % 13].setY(20);
            
            cardImages[cardId] = deck.getBackOfCard(cardId);
        }
    }

    private void render() {
        // Clear previous render
        //gs.clear();
        
        // Render all cards
        for (int i = 0; i < 52; i++) {
            if (cardImages[i] != null) {
                gs.render(cardImages[i]);
            }
        }
        
        // Render waste pile (top card only)
        if (!wastePile.isEmpty()) {
            int topCard = wastePile.peek();
            gs.render(cardImages[topCard]);
        }
        
        // Render foundation piles (top cards only)
        for (int i = 0; i < 4; i++) {
            if (!foundationPiles[i].isEmpty()) {
                int topCard = foundationPiles[i].peek();
                gs.render(cardImages[topCard]);
            }
        }
        
        // Render tableau piles
        for (int i = 0; i < 7; i++) {
            for (int cardId : tableauPiles[i]) {
                gs.render(cardImages[cardId]);
            }
        }
        
        // Update display
        render();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        
        // Check if stock was clicked
        if (mouseX >= stockX && mouseX <= stockX + CARD_WIDTH &&
            mouseY >= 20 && mouseY <= 20 + CARD_HEIGHT) {
            dealFromStock();
            return;
        }
        
        // Check if waste pile was clicked
        if (!wastePile.isEmpty()) {
            int wasteCard = wastePile.peek();
            ImgContainer wasteImg = cardImages[wasteCard];
            if (mouseX >= wasteImg.getX() && mouseX <= wasteImg.getX() + CARD_WIDTH &&
                mouseY >= wasteImg.getY() && mouseY <= wasteImg.getY() + CARD_HEIGHT) {
                selectedCard = wasteCard;
                selectedPile = 12; // waste pile
                return;
            }
        }
        
        // Check if foundation piles were clicked
        for (int i = 0; i < 4; i++) {
            if (!foundationPiles[i].isEmpty()) {
                int foundationCard = foundationPiles[i].peek();
                ImgContainer foundationImg = cardImages[foundationCard];
                if (mouseX >= foundationImg.getX() && mouseX <= foundationImg.getX() + CARD_WIDTH &&
                    mouseY >= foundationImg.getY() && mouseY <= foundationImg.getY() + CARD_HEIGHT) {
                    selectedCard = foundationCard;
                    selectedPile = 7 + i; // foundation piles 7-10
                    return;
                }
            }
        }
        
        // Check if tableau piles were clicked
        for (int i = 0; i < 7; i++) {
            if (!tableauPiles[i].isEmpty()) {
                // Check from top card down
                for (int j = tableauPiles[i].size() - 1; j >= 0; j--) {
                    int cardId = tableauPiles[i].get(j);
                    if (cardTurned[cardId]) continue; // Skip face down cards
                    
                    ImgContainer cardImg = cardImages[cardId];
                    if (mouseX >= cardImg.getX() && mouseX <= cardImg.getX() + CARD_WIDTH &&
                        mouseY >= cardImg.getY() && mouseY <= cardImg.getY() + CARD_HEIGHT) {
                        selectedCard = cardId;
                        selectedPile = i;
                        return;
                    }
                }
            }
        }
    }

    private void dealFromStock() {
        if (stockPile.isEmpty()) {
            // If stock is empty, move all cards from waste back to stock
            while (!wastePile.isEmpty()) {
                int cardId = wastePile.pop();
                cardTurned[cardId] = true; // Face down
                cardImages[cardId] = deck.getBackOfCard(cardId);
                Deck[cardId / 13][cardId % 13].setX(stockX);
                Deck[cardId / 13][cardId % 13].setY(20);
                stockPile.push(cardId);
            }
        } else {
            // Deal next 3 cards (or remaining) to waste
            int dealCount = Math.min(3, stockPile.size());
            for (int i = 0; i < dealCount; i++) {
                int cardId = stockPile.pop();
                cardTurned[cardId] = false; // Face up
                cardImages[cardId] = Deck[cardId / 13][cardId % 13];
                Deck[cardId / 13][cardId % 13].setX(wasteX + i * 20); // Stagger cards
                Deck[cardId / 13][cardId % 13].setY(20);
                wastePile.push(cardId);
            }
        }
        render();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        
        // Check if we're clicking on a card to drag
        mouseClicked(e); // Reuse click logic to select card
        
        if (selectedCard != -1) {
            ImgContainer cardImg = cardImages[selectedCard];
            dragOffsetX = mouseX - cardImg.getX();
            dragOffsetY = mouseY - cardImg.getY();
            isDragging = true;
            
            // Bring card to top (temporarily)
            //cardImages[selectedCard].setZOrder(100);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!isDragging || selectedCard == -1) return;
        
        int mouseX = e.getX();
        int mouseY = e.getY();
        boolean moved = false;
        
        // Check if dropped on foundation
        for (int i = 0; i < 4; i++) {
            if (mouseX >= foundationX + i * 85 && mouseX <= foundationX + i * 85 + CARD_WIDTH &&
                mouseY >= 20 && mouseY <= 20 + CARD_HEIGHT) {
                if (canMoveToFoundation(selectedCard, i)) {
                    moveToFoundation(selectedCard, i);
                    moved = true;
                }
                break;
            }
        }
        
        // Check if dropped on tableau
        if (!moved) {
            for (int i = 0; i < 7; i++) {
                if (mouseX >= tableauX[i] && mouseX <= tableauX[i] + CARD_WIDTH) {
                    if (canMoveToTableau(selectedCard, i)) {
                        moveToTableau(selectedCard, i);
                        moved = true;
                    }
                    break;
                }
            }
        }
        
        // If not moved to valid location, return to original position
        if (!moved) {
            returnCardToOriginalPosition();
        }
        
        // Reset selection
        isDragging = false;
        selectedCard = -1;
        selectedPile = -1;
        render();
    }

    private boolean canMoveToFoundation(int cardId, int foundationIndex) {
        if (selectedPile >= 7 && selectedPile <= 10) return false; // Can't move from foundation
        
        int suit = cardId / 13;
        int rank = cardId % 13;
        
        if (foundationPiles[foundationIndex].isEmpty()) {
            return rank == 0; // Only ace can start foundation
        } else {
            int topCard = foundationPiles[foundationIndex].peek();
            int topSuit = topCard / 13;
            int topRank = topCard % 13;
            
            return (suit == topSuit) && (rank == topRank + 1);
        }
    }

    private boolean canMoveToTableau(int cardId, int tableauIndex) {
        if (selectedPile == tableauIndex) return false; // Can't move to same pile
        
        int suit = cardId / 13;
        int rank = cardId % 13;
        boolean isRed = (suit == 1 || suit == 2); // Diamonds or Hearts
        
        if (tableauPiles[tableauIndex].isEmpty()) {
            return rank == 12; // Only king can start empty tableau
        } else {
            int topCard = tableauPiles[tableauIndex].peek();
            int topSuit = topCard / 13;
            int topRank = topCard % 13;
            boolean topIsRed = (topSuit == 1 || topSuit == 2);
            
            return (isRed != topIsRed) && (rank == topRank - 1);
        }
    }

    private void moveToFoundation(int cardId, int foundationIndex) {
        // Remove from source pile
        removeFromSourcePile(cardId);
        
        // Add to foundation
        foundationPiles[foundationIndex].push(cardId);
        
        // Position card
        Deck[cardId / 13][cardId % 13].setX(foundationX + foundationIndex * 85);
        Deck[cardId / 13][cardId % 13].setY(20);
    }

    private void moveToTableau(int cardId, int tableauIndex) {
        // Remove from source pile
        removeFromSourcePile(cardId);
        
        // Add to tableau
        tableauPiles[tableauIndex].push(cardId);
        
        // Position card
        int yPos = 150 + (tableauPiles[tableauIndex].size() - 1) * CARD_OFFSET;
        Deck[cardId / 13][cardId % 13].setX(tableauX[tableauIndex]);
        Deck[cardId / 13][cardId % 13].setY(yPos);
        
        // If coming from waste, turn over next stock card if needed
        if (selectedPile == 12 && !stockPile.isEmpty()) {
            int nextCard = stockPile.peek();
            cardTurned[nextCard] = false;
            cardImages[nextCard] = Deck[nextCard / 13][nextCard % 13];
        }
    }

    private void removeFromSourcePile(int cardId) {
        if (selectedPile >= 0 && selectedPile < 7) {
            // From tableau
            tableauPiles[selectedPile].pop();
            
            // Turn over next card if it's face down
            if (!tableauPiles[selectedPile].isEmpty()) {
                int nextCard = tableauPiles[selectedPile].peek();
                if (cardTurned[nextCard]) {
                    cardTurned[nextCard] = false;
                    cardImages[nextCard] = Deck[nextCard / 13][nextCard % 13];
                }
            }
        } else if (selectedPile == 12) {
            // From waste
            wastePile.pop();
        }
    }

    private void returnCardToOriginalPosition() {
        if (selectedPile >= 0 && selectedPile < 7) {
            // Tableau pile
            int yPos = 150 + (tableauPiles[selectedPile].size() - 1) * CARD_OFFSET;
            Deck[selectedCard / 13][selectedCard % 13].setX(tableauX[selectedPile]);
            Deck[selectedCard / 13][selectedCard % 13].setY(yPos);
        } else if (selectedPile >= 7 && selectedPile <= 10) {
            // Foundation pile
            Deck[selectedCard / 13][selectedCard % 13].setX(foundationX + (selectedPile - 7) * 85);
            Deck[selectedCard / 13][selectedCard % 13].setY(20);
        } else if (selectedPile == 12) {
            // Waste pile
            Deck[selectedCard / 13][selectedCard % 13].setX(wasteX);
            Deck[selectedCard / 13][selectedCard % 13].setY(20);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        new SolitaireGame();
    }
}