package it.chalmers.tendu.screens;

//TODO refactor
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class NumberCircle {
	private int xPos; // center of circle
	private int yPos; // center of circle
	private int radius;
	private int number;
	private int numberXPos;
	private int numberYPos;
	Color color;
	private int leftX;
	private int rightX;
	private int topY;
	private int bottomY;
	float scale;

	public NumberCircle(int number, int x, int y, int radius, Color color) {
		this.setNumber(number);
		this.setX(x);
		this.setY(y);
		this.setRadius(radius);

		if (number < 10) {
			numberXPos = x - 9;
		} else {
			numberXPos = x - 16;
		}

		numberYPos = y + 15;

		leftX = xPos - radius;
		rightX = xPos + radius;

		topY = yPos + radius;
		bottomY = yPos - radius;
		this.color = color;
		scale = 1;
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
		return numberXPos;
	}

	public void setNumberX(int numberX) {
		this.numberXPos = numberX;
	}

	public int getNumberY() {
		return numberYPos;
	}

	public void setNumberY(int numberY) {
		this.numberYPos = numberY;
	}

	public boolean collided(Vector3 touchPos) {
		if (touchPos.x > leftX && touchPos.x < rightX) {
			if (touchPos.y < topY && touchPos.y > bottomY) {
				return true;
			}
		}

		return false;
	}
}
