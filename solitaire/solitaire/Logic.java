package solitaire;

import java.util.Arrays;
import java.util.Comparator;

import se.egy.graphics.ImgContainer;

public class Logic {

	//variabler
	int[] xPositions = new int [7];	
	boolean[][] grid = new boolean[7][18];
	int[] yPos = new int [52];
	ImgContainer[][] gridPictures = new ImgContainer[7][18];
	boolean[] turned = new boolean[52];
	ImgContainer[] backOfCards = new ImgContainer[52];
	ImgContainer[] Kortlek = new ImgContainer[52];
	int [][] gridNumbers = new int [7][18];
	boolean [] isInExtraDeck = new boolean [52];

	public Logic(){
		// Skapa alla kort i kortleken som ImgContainer
		
		// Clubs (Klubbar)
		ImgContainer aceOfClubs = new ImgContainer(0, 0, "aceOfClubs.png");
		ImgContainer twoOfClubs = new ImgContainer(0, 0, "twoOfClubs.png");
		ImgContainer threeOfClubs = new ImgContainer(0, 0, "threeOfClubs.png");
		ImgContainer fourOfClubs = new ImgContainer(0, 0, "fourOfClubs.png");
		ImgContainer fiveOfClubs = new ImgContainer(0, 0, "fiveOfClubs.png");
		ImgContainer sixOfClubs = new ImgContainer(0, 0, "sixOfClubs.png");
		ImgContainer sevenOfClubs = new ImgContainer(0, 0, "sevenOfClubs.png");
		ImgContainer eightOfClubs = new ImgContainer(0, 0, "eightOfClubs.png");
		ImgContainer nineOfClubs = new ImgContainer(0, 0, "nineOfClubs.png");
		ImgContainer tenOfClubs = new ImgContainer(0, 0, "tenOfClubs.png");
		ImgContainer jackOfClubs = new ImgContainer(0, 0, "jackOfClubs.png");
		ImgContainer queenOfClubs = new ImgContainer(0, 0, "queenOfClubs.png");
		ImgContainer kingOfClubs = new ImgContainer(0, 0, "kingOfClubs.png");

		// Diamonds (Rutor)
		ImgContainer aceOfDiamonds = new ImgContainer(0, 0, "aceOfDiamonds.png");
		ImgContainer twoOfDiamonds = new ImgContainer(0, 0, "twoOfDiamonds.png");
		ImgContainer threeOfDiamonds = new ImgContainer(0, 0, "threeOfDiamonds.png");
		ImgContainer fourOfDiamonds = new ImgContainer(0, 0, "fourOfDiamonds.png");
		ImgContainer fiveOfDiamonds = new ImgContainer(0, 0, "fiveOfDiamonds.png");
		ImgContainer sixOfDiamonds = new ImgContainer(0, 0, "sixOfDiamonds.png");
		ImgContainer sevenOfDiamonds = new ImgContainer(0, 0, "sevenOfDiamonds.png");
		ImgContainer eightOfDiamonds = new ImgContainer(0, 0, "eightOfDiamonds.png");
		ImgContainer nineOfDiamonds = new ImgContainer(0, 0, "nineOfDiamonds.png");
		ImgContainer tenOfDiamonds = new ImgContainer(0, 0, "tenOfDiamonds.png");
		ImgContainer jackOfDiamonds = new ImgContainer(0, 0, "jackOfDiamonds.png");
		ImgContainer queenOfDiamonds = new ImgContainer(0, 0, "queenOfDiamonds.png");
		ImgContainer kingOfDiamonds = new ImgContainer(0, 0, "kingOfDiamonds.png");

		// Hearts (Hjärter)
		ImgContainer aceOfHearts = new ImgContainer(0, 0, "aceOfHearts.png");
		ImgContainer twoOfHearts = new ImgContainer(0, 0, "twoOfHearts.png");
		ImgContainer threeOfHearts = new ImgContainer(0, 0, "threeOfHearts.png");
		ImgContainer fourOfHearts = new ImgContainer(0, 0, "fourOfHearts.png");
		ImgContainer fiveOfHearts = new ImgContainer(0, 0, "fiveOfHearts.png");
		ImgContainer sixOfHearts = new ImgContainer(0, 0, "sixOfHearts.png");
		ImgContainer sevenOfHearts = new ImgContainer(0, 0, "sevenOfHearts.png");
		ImgContainer eightOfHearts = new ImgContainer(0, 0, "eightOfHearts.png");
		ImgContainer nineOfHearts = new ImgContainer(0, 0, "nineOfHearts.png");
		ImgContainer tenOfHearts = new ImgContainer(0, 0, "tenOfHearts.png");
		ImgContainer jackOfHearts = new ImgContainer(0, 0, "jackOfHearts.png");
		ImgContainer queenOfHearts = new ImgContainer(0, 0, "queenOfHearts.png");
		ImgContainer kingOfHearts = new ImgContainer(0, 0, "kingOfHearts.png");

		// Spades (Spader)
		ImgContainer aceOfSpades = new ImgContainer(0, 0, "aceOfSpades.png");
		ImgContainer twoOfSpades = new ImgContainer(0, 0, "twoOfSpades.png");
		ImgContainer threeOfSpades = new ImgContainer(0, 0, "threeOfSpades.png");
		ImgContainer fourOfSpades = new ImgContainer(0, 0, "fourOfSpades.png");
		ImgContainer fiveOfSpades = new ImgContainer(0, 0, "fiveOfSpades.png");
		ImgContainer sixOfSpades = new ImgContainer(0, 0, "sixOfSpades.png");
		ImgContainer sevenOfSpades = new ImgContainer(0, 0, "sevenOfSpades.png");
		ImgContainer eightOfSpades = new ImgContainer(0, 0, "eightOfSpades.png");
		ImgContainer nineOfSpades = new ImgContainer(0, 0, "nineOfSpades.png");
		ImgContainer tenOfSpades = new ImgContainer(0, 0, "tenOfSpades.png");
		ImgContainer jackOfSpades = new ImgContainer(0, 0, "jackOfSpades.png");
		ImgContainer queenOfSpades = new ImgContainer(0, 0, "queenOfSpades.png");
		ImgContainer kingOfSpades = new ImgContainer(0, 0, "kingOfSpades.png");
		
		Kortlek[0] = aceOfClubs;
		Kortlek[1] = twoOfClubs;
		Kortlek[2] = threeOfClubs;
		Kortlek[3] = fourOfClubs;
		Kortlek[4] = fiveOfClubs;
		Kortlek[5] = sixOfClubs;
		Kortlek[6] = sevenOfClubs;
		Kortlek[7] = eightOfClubs;
		Kortlek[8] = nineOfClubs;
		Kortlek[9] = tenOfClubs;
		Kortlek[10] = jackOfClubs;
		Kortlek[11] = queenOfClubs;
		Kortlek[12] = kingOfClubs;

		Kortlek[26] = aceOfDiamonds;
		Kortlek[27] = twoOfDiamonds;
		Kortlek[28] = threeOfDiamonds;
		Kortlek[29] = fourOfDiamonds;
		Kortlek[30] = fiveOfDiamonds;
		Kortlek[31] = sixOfDiamonds;
		Kortlek[32] = sevenOfDiamonds;
		Kortlek[33] = eightOfDiamonds;
		Kortlek[34] = nineOfDiamonds;
		Kortlek[35] = tenOfDiamonds;
		Kortlek[36] = jackOfDiamonds;
		Kortlek[37] = queenOfDiamonds;
		Kortlek[38] = kingOfDiamonds;

		Kortlek[39] = aceOfHearts;
		Kortlek[40] = twoOfHearts;
		Kortlek[41] = threeOfHearts;
		Kortlek[42] = fourOfHearts;
		Kortlek[43] = fiveOfHearts;
		Kortlek[44] = sixOfHearts;
		Kortlek[45] = sevenOfHearts;
		Kortlek[46] = eightOfHearts;
		Kortlek[47] = nineOfHearts;
		Kortlek[48] = tenOfHearts;
		Kortlek[49] = jackOfHearts;
		Kortlek[50] = queenOfHearts;
		Kortlek[51] = kingOfHearts;

		Kortlek[13] = aceOfSpades;
		Kortlek[14] = twoOfSpades;
		Kortlek[15] = threeOfSpades;
		Kortlek[16] = fourOfSpades;
		Kortlek[17] = fiveOfSpades;
		Kortlek[18] = sixOfSpades;
		Kortlek[19] = sevenOfSpades;
		Kortlek[20] = eightOfSpades;
		Kortlek[21] = nineOfSpades;
		Kortlek[22] = tenOfSpades;
		Kortlek[23] = jackOfSpades;
		Kortlek[24] = queenOfSpades;
		Kortlek[25] = kingOfSpades;

		
		//x-värden på "Spelplanen"
		for(int i = 0; i < 7; i++) {
			xPositions[i] = (i - 1) * 85 + 110;
		}
		
		//Standard y-värde
		for(int i = 0; i < 52; i++) {
			yPos[i] = 20;
		}
		
		//Grid för att visa vilka platser som är upptagna
		for(int i = 0; i < 7; i++) {
			for(int k = 0; k < 18; k++) {
				grid[i][k] = false;
			}
		}	
		
		//Upptagna platser från början
		for(int i = 0; i < 7; i++) {
			for(int k = 0; k < (i + 1); k++) {
				grid[i][k] = true;
			}
		}
		
		//Sätt alla kort som vända med baksidan upp
		for(int i = 0; i < 52; i++) {
			turned[i] = false;
		}
		
		//Array med bara baksidor
		for(int i = 0; i < 52; i++) {
			backOfCards[i] = new ImgContainer(0, 0, "backOfCard.png");
		}
		
		//Array som visar vilka kort som är i extrahögen
		for(int i = 0; i < 52; i++) {
			if(i < 28) {
				isInExtraDeck[i] = false;
			}else {
				isInExtraDeck[i] = true;
			}
		}
	}
	
	//Blanda kortleken
	public int[] shuffleCards(){
		//Skapa en array med 0-51 i
		int shuffle[] = new int[52];
		for(int i = 0; i < 52; i++) {shuffle[i] = i;}
		
		//Rör runt siffrorna i arrayen
		for(int i = 0; i < 52; i++) {
			int s = (int)(Math.random() * 51);	//Väljer en plats i kortleken
			int temp = shuffle[i];	//Sparar siffran på plats "i" i en temporär variabel
			shuffle[i] = shuffle[s];	//Byter plats på siffrorna
			shuffle[s] = temp;
		}
		
		return shuffle;
	}
	
	//Hämta arrayen med korten i
	public ImgContainer[] getDeck(){
		return Kortlek;
	}
	
	//Hämta förutbestämda x-värden
	public int[] getXPositions() {
		return xPositions;
	}
	
	//Hämta förutbestämda y-värden
	public int[] getYPos() {
		return yPos;
	}
	
	//Byter plats på kortet i x-led
	public ImgContainer[] setXPos(int deckPos, int xPos){
		int deckPos1 = deckPos / 13;
		int deckPos2 = deckPos % 13;
		ImgContainer temp = Kortlek[deckPos];
		temp.setX(xPos * 85 + 110);
		Kortlek[deckPos] = temp;
		return Kortlek;
	}
	
	//Byter plats på kortet i y-led
	public ImgContainer[] setYPos(int deckPos, int yPos){
		int deckPos1 = deckPos / 13;
		int deckPos2 = deckPos % 13;
		ImgContainer temp = Kortlek[deckPos];
		temp.setY((yPos + 1) * 15);
		Kortlek[deckPos] = temp;
		return Kortlek;
	}
	
	//Kollar om det finns ett kort på platsen
	public boolean getGridPos(int i, int k){
		boolean gridPos = grid[i][k];
		return gridPos;
	}
	
	public void switchGridPos(int xPos, int yPos) {
		if(grid[(xPos - 110) / 85][yPos / 15 -1]) {
			grid[(xPos - 110) / 85][yPos / 15 -1] = false;
		}else {
			grid[(xPos - 110) / 85][yPos / 15 -1] = true;
		}
	}
	
//	public ImgContainer gridPicture(int deckPos, int xPos, int yPos) {
//		int deckPos1 = deckPos / 13;
//		int deckPos2 = deckPos % 13;
//		ImgContainer card = Kortlek[deckPos1][deckPos2];
//		return card;
//	}
	
	//Vänd på kortet i arrayen som säger om kortet har baksidan upp eller inte
	public boolean turnCard(int deckPos) {
		boolean turn;
		if(turned[deckPos]) {
			turned[deckPos] = false;
			turn = false;
		}
		else { 
			turned[deckPos] = true;
			turn = true;
		}
		return turn;
	}
	
	//Hämta om kortet har baksidan upp eller inte
	public boolean getTurnedCard(int deckPos) {
		boolean turn = turned[deckPos];
		return turn;
	}
	
	//"Vänd på kortet" Tar en bild på baksidan och byter ut den på samma ställe som framsidan skulle legat på
	public ImgContainer getBackOfCard(int deckPos) {
		System.out.println("kortet vändes");
		int deckPos1 = deckPos / 13;
		int deckPos2 = deckPos % 13;
		ImgContainer temp = Kortlek[deckPos];
		int xPos = temp.getX();
		int yPos = temp.getY();
		ImgContainer back = backOfCards[deckPos];
		//back.setX(xPos);
		//back.setY(yPos);
		return back;
	}
	
	//kollar om man trycker innanför "spelplanen"
	public boolean checkIfInGrid(int xPos, int yPos) {
		//if(xPos >= 110 && xPos <= 677) {
		if(xPos <= 677) {
			return true;	
		} else {
			return false;
		}
	}
	
	//Kollar om man trycker på extrahögen för att vända upp ett kort
	public boolean checkClickInExtraDeck(int xPos, int yPos) {
		if(xPos >= 15 && yPos >= 15 && xPos <= (15 + 57) && yPos <= (15 + 82)) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean checkIfExtraDeck(int xPos, int yPos){
		if(xPos <= 72) {
			return true;
		}else {
			return false;
		}
	}
	
	public ImgContainer[] orderByY(ImgContainer[] order) {
		ImgContainer Imgtemp;
		int temp1;
		int temp2;
		
		for(int i = 51; i > 0; i--) {
			for(int k = 0; k < i; k++) {
				temp1 = order[k].getY();
				temp2 = order[k + 1].getY();
				if(temp1 > temp2) {
					Imgtemp = order[k];
					order[k] = order[k + 1];
					order[k + 1] = Imgtemp;
				}
			}
		}
//	    int[] Card = new int[51];
//	    
//	    for(int i = 0; i < 51; i++) {
//	    	Card[i] = order[i].getY();
//	    }
//
//	    Arrays.sort(order, Comparator.comparingInt(ImgContainer::getY)); 
		return order;
	}
	
	public ImgContainer setBackOfCard(int i) {
		ImgContainer back = backOfCards[i];
		ImgContainer front = Kortlek[i];
		int xPos = front.getX();
		int yPos = front.getY();
		back.setX(xPos);
		back.setY(yPos);
		return back;
	}
	
	public boolean checkIfInExtraDeck(int deckPos) {
		return isInExtraDeck[deckPos];
	}
	
	public void switchIfInExtraDeck(int deckPos) {
		if(isInExtraDeck[deckPos]) {
			isInExtraDeck[deckPos] = false;
		}else {
			isInExtraDeck[deckPos] = true;
		}
	}
	
}
