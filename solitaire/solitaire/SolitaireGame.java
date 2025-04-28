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
	private int width = 770;
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
	private int prevExtraDeckAmount = 24;
	private int extraDeckTopCard = 0;
	private int selectedCard = 52;
	private int selectedCard2 = 52;
	private int [][] gridNumbers = new int [7][18];
	private boolean underst = true;
	private int xPos;
	private int yPos;
	private int oldXPos;
	private int oldYPos;
	private int extraDeckY = 0;

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
		//renderCards = deck.orderDeckByShuffle(shuffled, renderCards);
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
			for(int i = 28; i < 52; i++) {
				int currentCard = shuffled[i];
				//Vänd upp nytt kort
				if(card[currentCard].getY() == 15 && card[currentCard].getX() == 15 && deck.checkIfInExtraDeck(currentCard)) {
					deck.turnCard(currentCard);
					Deck[currentCard].setY(100 + extraDeckY);
					card[currentCard] = Deck[currentCard];
					System.out.println(extraDeckY);
					extraDeckY++;
					render();
					System.out.println(prevExtraDeckAmount);
					break;
				//Extrahögen återgår till startposition
				}else if(card[currentCard].getY() == 99 + prevExtraDeckAmount && deck.checkIfExtraDeck(mouseX, mouseY)) {
					for(int k = 52 - prevExtraDeckAmount; k < 52; k++) {
						int currentCard2 = shuffled[k];
						if(deck.checkIfInExtraDeck(currentCard2)) {
							card[currentCard2] = deck.getBackOfCard(currentCard2);
							deck.turnCard(currentCard2);
						}
					}
					prevExtraDeckAmount = extraDeckAmount;
					extraDeckY = 0;
					for(int k = 28; k < 52; k++) {
						int currentCard2 = shuffled[k];
						if(card[currentCard2].getY() >= 100 && deck.checkIfInExtraDeck(currentCard2)) {
							card[currentCard2].setY(15);
							deck.turnCard(currentCard2);
						}
					}
				}
			}
			
		}

		// Loopar igenom korten
		for (int i = 51; i >= 0; i--) {
			ImgContainer currentCard = card[shuffled[i]];
			int cardX = currentCard.getX();
			int cardY = currentCard.getY();
			cardWidth = 57;
			cardHeight = 82;
			//Kollar om det finns kort under och gör hitboxen mindre
			for(int k = 3; k >= 1; k--) {
				if(deck.checkIfInGrid(cardX, cardY) && deck.getGridPos((cardX - 110) / 85, (cardY + 15 * k) / 15 - 1)) {
					cardHeight = 15 * k;
				}
			}

			//Kollar om ett kort blev klickat
			if(mouseX >= cardX && mouseX <= cardX + cardWidth &&
					mouseY >= cardY && mouseY <= cardY + cardHeight
					&& deck.getTurnedCard(shuffled[i]) == false && mouseX < 700) {
				//Om inget kort är valt, välj det klicakde kortet
				if(selectedCard == 52) {
					selectedCard = shuffled[i];
					//Om ett kort redan är valt, välj ett andra kort
				}else {
					selectedCard2 = selectedCard;
					selectedCard = shuffled[i];
				}

				//Flytta kort:
				//Om man valt två olika kort så ska kort flyttas från det först valda till det andra
				if(selectedCard2 < 52 && selectedCard2 != selectedCard) {
					System.out.println("Kort " + selectedCard2 + " Ska flyttas till under kort " + selectedCard);
					xPos = card[selectedCard].getX();
					yPos = card[selectedCard].getY() + 15;
					oldXPos = card[selectedCard2].getX();
					oldYPos = card[selectedCard2].getY();

					//Flytta kort från spelplanen till spelplanen
					if(deck.checkIfExtraDeck(xPos, yPos) == false && deck.checkIfExtraDeck(oldXPos, oldYPos) == false) {
						deck.switchGridPos(oldXPos, oldYPos);
						deck.switchGridPos(xPos, yPos);
						System.out.println(deck.getGridPos((xPos - 110) / 85, yPos / 15 - 1));
						card[selectedCard2].setX(xPos);
						card[selectedCard2].setY(yPos);
						if(oldYPos >= 0) {
							for(int k = 0; k < 52; k++) {
								int turnCard = shuffled[k];
								int checkX = card[turnCard].getX();
								int checkY = card[turnCard].getY();
								if(checkX == oldXPos && checkY == oldYPos - 15 && deck.getTurnedCard(turnCard)) {
									deck.turnCard(turnCard);
									card[turnCard] = Deck[turnCard];
								}
							}
						}
//						if(selectedCard2 - 1 >= 0) {
//							if(deck.getTurnedCard(selectedCard2 - 1)) {
//								deck.turnCard(selectedCard2 - 1);
//								card[selectedCard2 - 1] = Deck[selectedCard2 - 1];
//							}
//						}
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
						extraDeckAmount--;
						if(extraDeckY == prevExtraDeckAmount) {
							prevExtraDeckAmount--;
						}
					}
					//De-select om samma kort klickas två gånger
				}else if(selectedCard2 == selectedCard){
					System.out.println("Inget kort är valt");
					selectedCard = 52;
					selectedCard2 = 52;
				}

				System.out.println(selectedCard);
				System.out.println(selectedCard2);
				break;	

			//Om man inte klickar på ett kort
			}else if(i == 0) {
				//Har valt ett kort och klickar inte på ett till
				if(selectedCard < 52 && selectedCard2 == 52 ) {
					//Flytta kort till toppen på spelplanen
					if(deck.clickTopOfGrid(mouseX, mouseY)) {
						oldXPos = card[selectedCard].getX();
						oldYPos = card[selectedCard].getY();
						yPos = 15;
						xPos = deck.getTopXPos(mouseX);
						deck.switchGridPos(xPos, yPos);
						if(deck.checkIfInGrid(oldXPos, oldYPos)) {
							deck.switchGridPos(oldXPos, oldYPos);
						}else if(deck.checkIfExtraDeck(oldXPos, oldYPos)) {
							deck.switchIfInExtraDeck(selectedCard);
						}
						card[selectedCard].setX(xPos);
						card[selectedCard].setY(yPos);
						if(oldYPos >= 0) {
							for(int k = 0; k < 52; k++) {
								int turnCard = shuffled[k];
								int checkX = card[turnCard].getX();
								int checkY = card[turnCard].getY();
								if(checkX == oldXPos && checkY == oldYPos - 15 && deck.getTurnedCard(turnCard)) {
									deck.turnCard(turnCard);
									card[turnCard] = Deck[turnCard];
								}
							}
						}
						selectedCard = 52;
						selectedCard2 = 52;
						System.out.println("Top Position");
					//Flytta kort till sin slutplats (Till höger)
					}else if(deck.clickInEndPosition(mouseX, mouseY)) { //End positions: x=700 y=20 + 90 * i
						oldXPos = card[selectedCard].getX();
						oldYPos = card[selectedCard].getY();
						xPos = 700;
						yPos = deck.getEndYPos(mouseY);
						if(deck.checkIfInGrid(oldXPos, oldYPos)) {
							deck.switchGridPos(oldXPos, oldYPos);
						}else if(deck.checkIfExtraDeck(oldXPos, oldYPos)) {
							deck.switchIfInExtraDeck(selectedCard);
						}
						card[selectedCard].setX(xPos);
						card[selectedCard].setY(yPos);
						if(oldYPos >= 0) {
							for(int k = 0; k < 52; k++) {
								int turnCard = shuffled[k];
								int checkX = card[turnCard].getX();
								int checkY = card[turnCard].getY();
								if(checkX == oldXPos && checkY == oldYPos - 15 && deck.getTurnedCard(turnCard)) {
									deck.turnCard(turnCard);
									card[turnCard] = Deck[turnCard];
								}
							}
						}
						selectedCard = 52;
						selectedCard2 = 52;
						System.out.println("End Position");
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
						deck.turnCard(shuffled[deckPos]);	
					}

					//Sätt position
					Deck = deck.setYPos(shuffled[deckPos], k);
					Deck = deck.setXPos(shuffled[deckPos], i);

					gridNumbers[i][k] = shuffled[deckPos];

					card[shuffled[deckPos]] = Deck[shuffled[deckPos]];

					// Debugging: Visa kortets data
					ImgContainer currentCard = card[shuffled[deckPos]];
					System.out.printf("Card: %d, X: %d, Y: %d, Turned: %b%n",
							deckPos, currentCard.getX(), currentCard.getY(), deck.getTurnedCard(shuffled[deckPos]));

					deckPos++;
				}
			}
		}

		//Lägg ut korten som ska vara i "extra" högen
		for(int i = 28; i < 52; i++) {
			//deck.turnCard(deckPos);
			Deck[shuffled[deckPos]].setY(15);
			Deck[shuffled[deckPos]].setX(15);
			deck.turnCard(shuffled[i]);
			//card[deckPos] = deck.getBackOfCard(deckPos);
			card[shuffled[deckPos]] = Deck[shuffled[deckPos]];
			deck.switchIfInExtraDeck(shuffled[deckPos]);
			deckPos++;
		}

		//Vänder på korten som ska visa baksidan
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
			for (int deckPos = 28; deckPos < 52; deckPos++) {
				ImgContainer currentCard = card[shuffled[deckPos]];
				System.out.printf("Card: %d, X: %d, Y: %d, Turned: %b%n",
						shuffled[deckPos], currentCard.getX(), currentCard.getY(), deck.checkIfInExtraDeck(shuffled[deckPos]));
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
