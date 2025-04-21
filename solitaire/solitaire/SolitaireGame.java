package solitaire;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.HashMap;

import se.egy.graphics.GameScreen;
import se.egy.graphics.ImgContainer;

public class SolitaireGame implements MouseListener, KeyListener{
	//Instansvariabler
	private int height = 388;
	private int width = 772;
	private int cardWidth = 57;
	private int cardHeight = 82;
	private boolean gameRun = true;
	private GameScreen gs = new GameScreen("Game", width, height, false);
	private ImgContainer[] card = new ImgContainer[52];	
	private Logic deck = new Logic();
	private ImgContainer[] Deck = deck.getDeck();
	private int deckPos = 0;
	private int[] shuffled = deck.shuffleCards();
	private int[] extraDeck = Arrays.copyOfRange(shuffled, 28, 51);
	private int extraDeckAmount = 24;
	private int extraDeckTopCard = 0;
	private int selectedCard = 52;
	private int selectedCard2 = 52;
	int [][] gridNumbers = new int [7][18];
	boolean underst = true;

	public SolitaireGame() {
		gs.setMouseListener(this);
		gs.setKeyListener(this);
		loadBackgroundImg();
		startingPos();
		//render();
		gameLoop();
	}

	public void loadBackgroundImg() {
		gs.setBackground("solitaireBackground.png");        
	}

	public void render() {
		ImgContainer [] renderCards = Arrays.copyOfRange(card, 0, 52);
		renderCards = deck.orderByY(renderCards);
		gs.render(renderCards);
	}

	public void gameLoop() {
		while(gameRun) {
			render();
			try {
				Thread.sleep(30);
			}catch(Exception e) {}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();

		//Kollar om man trycker på extrahögen för att vända upp ett kort
		if(deck.checkClickInExtraDeck(mouseX, mouseY)) {
			for(int i = 0; i < 52; i++) {
				int currentCard = i;
				if(card[currentCard].getY() == 15 && card[currentCard].getX() == 15 && deck.checkIfInExtraDeck(currentCard)) {
					deck.turnCard(currentCard);
					Deck[currentCard].setY(100);
					card[currentCard] = Deck[currentCard];
					render();
					break;
				}else if(card[51].getY() == 100) {
					for(int k = 52 - extraDeckAmount; k < 52; k++) {
						int currentCard2 = k;
						if(deck.checkIfInExtraDeck(currentCard2)) {
							card[currentCard2] = deck.getBackOfCard(currentCard2);
							deck.turnCard(currentCard2);
						}
					}
				}
			}

			//			if(extraDeckTopCard < extraDeckAmount) {
			//				deck.setYPos(extraDeck[extraDeckTopCard], 150);
			//			}
		}

		//Kollar om man trycker på framsidan av ett kort


		boolean under = true;
		if(under) {
			//Kollar om klicket är på "spelplanen"
			if(deck.checkIfInGrid(mouseX, mouseY)) {
				// Loopar igenom korten och kollar om något kort blev klickat
				for (int i = 51; i >= 0; i--) {
					ImgContainer currentCard = card[i];
					int cardX = currentCard.getX();
					int cardY = currentCard.getY();

					if(mouseX >= cardX && mouseX <= cardX + cardWidth &&
							mouseY >= cardY && mouseY <= cardY + cardHeight
							&& deck.getTurnedCard(i) == false) {
						if(selectedCard == 52) {
							selectedCard = i;
						}else {
							selectedCard2 = selectedCard;
							selectedCard = i;
						}
						//Flytta kort:
						if(selectedCard2 < 52 && selectedCard2 != selectedCard) {
							System.out.println("Kort " + selectedCard2 + " Ska flyttas till under kort " + selectedCard);
							int xPos = card[selectedCard].getX();
							int yPos = card[selectedCard].getY() + 15;
							int oldXPos = card[selectedCard2].getX();
							int oldYPos = card[selectedCard2].getY();
							
							//Flytta kort från spelplanen till spelplanen
							if(deck.checkIfExtraDeck(xPos, yPos) == false && deck.checkIfExtraDeck(oldXPos, oldYPos) == false) {
								deck.switchGridPos(oldXPos, oldYPos);
								deck.switchGridPos(xPos, yPos);
								System.out.println(deck.getGridPos((xPos - 110) / 85, yPos / 15 - 1));
								card[selectedCard2].setX(xPos);
								card[selectedCard2].setY(yPos);
								if(deck.getTurnedCard(selectedCard2 - 1)) {
									deck.turnCard(selectedCard2 - 1);
									card[selectedCard2 - 1] = Deck[selectedCard2 - 1];
								}
								selectedCard = 52;
								selectedCard2 = 52;
								
							//Flytta kort från extrahögen till spelplanen
							}else if(deck.checkIfExtraDeck(oldXPos, oldYPos)) {
								deck.switchGridPos(xPos, yPos);
								card[selectedCard2].setX(xPos);
								card[selectedCard2].setY(yPos);
								System.out.println("den första är i extradeck");
								deck.switchIfInExtraDeck(selectedCard2);
								selectedCard = 52;
								selectedCard2 = 52;
								
							}
						}else if(selectedCard2 == selectedCard){
							System.out.println("Inget kort är valt");
							selectedCard = 52;
							selectedCard2 = 52;
						}

						System.out.println(selectedCard);
						System.out.println(selectedCard2);
						break;	
					}

				}
			}
		}

		boolean help = false;
		if(help) {
			// Loopar igenom korten och kollar om något kort blev klickat
			for (int i = 51; i >= 0; i--) {
				ImgContainer currentCard = card[i];

				if (currentCard != null) {
					int cardX = currentCard.getX();
					int cardY = currentCard.getY();

					if (mouseX >= cardX && mouseX <= cardX + cardWidth &&
							mouseY >= cardY && mouseY <= cardY + cardHeight) {
						// Kortet klickades!
						System.out.println("Kortet klickades");

						// Exempel: Vänd kortet
						boolean isTurned = deck.getTurnedCard(i);
						if (isTurned) {
							card[i] = Deck[i]; // Framsidan
						} else {
							card[i] = deck.getBackOfCard(shuffled[i]); // Baksidan
						}

						deck.turnCard(shuffled[i]);
						System.out.printf("Card: %d %d %d, X: %d, Y: %d, Turned: %b%n",
								shuffled[i]/13, shuffled[i]%13, shuffled[i], currentCard.getX(), currentCard.getY(), deck.getTurnedCard(shuffled[i]));

						render();
						break;
					}
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void startingPos() {
		deckPos = 0;

		//Placera ut korten "på spelbrädet"
		for(int i = 0; i < 7; i++) {
			for(int k = 0; k < 18; k++) {
				if(deck.getGridPos(i,k)) {	//Kollar om det ska ligga ett kort på positionen
					if(deck.getGridPos(i,(k + 1))) {	//Kollar om det är ett kort på positionen under och vänder isåfall kortet
						deck.turnCard(deckPos);	
					}

					//Sätt position
					Deck = deck.setYPos(deckPos, k);
					Deck = deck.setXPos(deckPos, i);

					gridNumbers[i][k] = deckPos;

					card[deckPos] = Deck[deckPos];

					//Backsida eller framsida
					//					if(deck.getTurnedCard(deckPos)) {
					//						card[deckPos] = deck.getBackOfCard(deckPos);
					//					}
					//					else {
					//						card[deckPos] = Deck[deckPos];
					//					}

					// Debugging: Visa kortets data
					ImgContainer currentCard = card[deckPos];
					System.out.printf("Card: %d %d %d, X: %d, Y: %d, Turned: %b%n",
							deckPos/13, deckPos%13, deckPos, currentCard.getX(), currentCard.getY(), deck.getTurnedCard(deckPos));

					deckPos++;
				}
			}
		}

		System.out.println(gridNumbers[1][0]);

		//Lägg ut korten som ska vara i "extra" högen
		for(int i = 0; i < 24; i++) {
			//deck.turnCard(deckPos);
			Deck[deckPos].setY(15);
			Deck[deckPos].setX(15);
			deck.turnCard(i + 28);
			//card[deckPos] = deck.getBackOfCard(deckPos);
			card[deckPos] = Deck[deckPos];
			deckPos++;
		}

		for(int i = 0; i < 52; i++) {
			if(deck.getTurnedCard(i)) {
				card[i] = deck.setBackOfCard(i);
			}
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		//Stäng ner programmet med escape
		if(key == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		else if(key == KeyEvent.VK_0) {
			System.out.println();
			for (int deckPos = 0; deckPos < 52; deckPos++) {
				ImgContainer currentCard = card[deckPos];
				System.out.printf("Card: %d %d %d, X: %d, Y: %d, Turned: %b%n",
						deckPos/13, deckPos%13, deckPos, currentCard.getX(), currentCard.getY(), deck.getTurnedCard(deckPos));
			}
		}
		render();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		//Starta Spelet
		new SolitaireGame();
	}
}
