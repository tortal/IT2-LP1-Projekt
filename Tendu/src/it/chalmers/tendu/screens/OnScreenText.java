package it.chalmers.tendu.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class OnScreenText {
	private String text;
	private Vector2 position;
	private int width;
	private int height;
	private Color color;
	private float scale;

	public OnScreenText(String text, Vector2 position) {
		this(text, position, Color.WHITE, 0f);
	}

	public OnScreenText(String text, Vector2 position, Color color) {
		this(text, position, color, 0f);
	}
	
	public OnScreenText(String text, Vector2 position, float scale) {
		this(text, position, Color.WHITE, scale);
	}
	
	public OnScreenText(String text, Vector2 position, Color color, float scale) {
		this.text = text;
		this.position = position;
		this.color = color;
		this.scale = scale;
	}

	public void draw(SpriteBatch spriteBatch, BitmapFont font) {
		font.setColor(color);
		font.scale(scale);
		width = (int) font.getBounds(text).width; // Get the width of the text
													// we draw using the current
													// font
		height = (int) font.getBounds(text).height; // Get the height of the
													// text we draw using the
													// current font
		font.draw(spriteBatch, text, position.x, position.y);
		font.scale(-scale);
	}

	public boolean collided(Vector2 touchPos) {
		if (touchPos.x > position.x && touchPos.x < position.x + width) {
			if (touchPos.y < position.y && touchPos.y > position.y - height) {
				return true;
			}
		}

		return false;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setX(float x) {
		position.x = x;
	}
	
	public void setY(float y) {
		position.y = y;
	}
	
	public void addToX(float x) {
		position.x = position.x + x;
	}
	
	public void addToY(float y) {
		position.y = position.y + y;
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}

}