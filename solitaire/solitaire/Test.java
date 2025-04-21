package solitaire;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import se.egy.graphics.GameScreen;
import se.egy.graphics.ImgContainer;

public class Test implements MouseListener{
	
	private int height = 388;
	private int width = 772;
	private GameScreen gs = new GameScreen("Game", width, height, false);
	private ImgContainer[] bilder = new ImgContainer[4];
	
	ImgContainer fiveOfClubs = new ImgContainer(340, 125, "fiveOfClubs.png");
	ImgContainer twoOfClubs = new ImgContainer(300, 100, "twoOfClubs.png");
	ImgContainer threeOfClubs = new ImgContainer(300, 150, "threeOfClubs.png");
	ImgContainer fourOfClubs = new ImgContainer(260, 125, "fourOfClubs.png");
	
	public Test(){
		loadImg();
		
	}
	
	public void loadImg() {
		gs.setBackground("solitaireBackground.png");
		
		bilder[0] = fiveOfClubs;
		bilder[3] = twoOfClubs;
		bilder[1] = threeOfClubs;
		bilder[2] = fourOfClubs;
		
		gs.render(bilder);
	}
	
	public static void main(String[] args) {
		new Test();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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

}
