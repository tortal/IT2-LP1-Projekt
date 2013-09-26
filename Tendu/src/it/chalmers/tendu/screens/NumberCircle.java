package it.chalmers.tendu.screens;

import org.w3c.dom.css.Rect;

public class NumberCircle {
	private int xPos; //center of circle
	private int yPos; //center of circle
	private int radius; 
	private int number; 
	private int numberXpos;
	private int numberYpos;
	int leftX;
	int rightX;
	public NumberCircle(int number, int x, int y, int radius) {
		this.setNumber(number);
		this.setX(x);
		this.setY(y);
		this.setRadius(radius);
		
		if(number < 10) {
			numberXpos = x - 9;
		} else {
			numberXpos = x - 16;
		}
		
		numberYpos = y + 15;
		
		leftX = xPos-radius;
		rightX = xPos+radius;
	}
	public int getX() {
		return xPos;
	}
	public void setX(int x) {
		this.xPos = x;
	}
	public int getY() {
		return yPos;
	}
	public void setY(int y) {
		this.yPos = y;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public int getNumberX() {
		return numberXpos;
	}
	public void setNumberX(int numberX) {
		this.numberXpos = numberX;
	}
	public int getNumberY() {
		return numberYpos;
	}
	public void setNumberY(int numberY) {
		this.numberYpos = numberY;
	}
}
