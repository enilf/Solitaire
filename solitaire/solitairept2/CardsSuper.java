package solitairept2;

import se.egy.graphics.ImgContainer;

public class CardsSuper {
	int xPos;
	int yPos;
	String color;
	boolean turned;
	ImgContainer card;
	ImgContainer backOfCard;
	
	public CardsSuper(int xPos, int yPos, String color, boolean turned, ImgContainer card, ImgContainer backOfCard) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.color = color;
		this.turned = turned;
		this.card = card;
		this.backOfCard = backOfCard;
	}
	
	public int getXPos() { return xPos; }
	public int getYPos() { return yPos; }
	public String getColor() { return color; }
	public boolean getTurned() { return turned; }
	public ImgContainer getCard() { return card; }
	public ImgContainer getBackOfCard() { return backOfCard; }
	
	public void setXPos(int xPos) {
		this.xPos = xPos;
	}
	
	public void setYPos(int yPos) {
		this.yPos = yPos;
	}
	
}
