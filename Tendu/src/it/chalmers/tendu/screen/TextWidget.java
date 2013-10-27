package it.chalmers.tendu.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Conveniance class for displaying text on screen
 * 
 */
public class TextWidget {
	private String text;
	private Vector2 position;
	private Vector2 centerPosition;
	private int width;
	private int height;
	private Color color;
	private float scale;
	private int expandHitboxX;
	private int expandHitboxY;

	private boolean drawAtCenter;

	/**
	 * 
	 * @param text
	 *            text to draw
	 * @param position
	 *            position of text on screen
	 */
	public TextWidget(String text, Vector2 position) {
		this(text, position, Color.WHITE, 0f);
	}

	/**
	 * 
	 * @param text
	 *            text to draw
	 * @param position
	 *            position of text on screen
	 * @param color
	 *            color of text
	 */
	public TextWidget(String text, Vector2 position, Color color) {
		this(text, position, color, 0f);
	}

	/**
	 * 
	 * @param text
	 *            text to draw
	 * @param position
	 *            position of text on screen
	 * @param scale
	 *            scale of font
	 */
	public TextWidget(String text, Vector2 position, float scale) {
		this(text, position, Color.WHITE, scale);
	}

	/**
	 * 
	 * @param text
	 *            text to draw
	 * @param position
	 *            position of text on screen
	 * @param color
	 *            color of text
	 * @param scale
	 *            scale of font
	 */
	public TextWidget(String text, Vector2 position, Color color, float scale) {
		this.text = text;
		this.position = position;
		this.color = color;
		this.scale = scale;

		// TODO add constructor for this
		expandHitboxX = 0;
		expandHitboxY = 0;
		drawAtCenter = false;
		this.centerPosition = new Vector2(0, 0);
	}

	/**
	 * Draws the text on screen. X = Left of first letter of text, Y = bottom of
	 * the text
	 * 
	 * @param spriteBatch
	 *            spriteBatch to draw on
	 * @param font
	 *            font to draw with
	 */
	public void draw(SpriteBatch spriteBatch, BitmapFont font) {
		drawAtCenter = false;
		font.setColor(color);
		font.scale(scale);

		width = expandHitboxX + (int) font.getBounds(text).width; // Get the
																	// width of
																	// the text
		// we draw using the current
		// font
		height = expandHitboxY + (int) font.getBounds(text).height; // Get the
																	// height of
																	// the
		// text we draw using the
		// current font
		font.draw(spriteBatch, text, position.x, position.y);
		font.scale(-scale);
	}

	/**
	 * Draws the text on screen. X = center of the text, Y = center of the text
	 * 
	 * @param spriteBatch
	 *            spriteBatch to draw on
	 * @param font
	 *            font to draw with
	 */
	public void drawAtCenterPoint(SpriteBatch spriteBatch, BitmapFont font) {
		drawAtCenter = true;
		font.setColor(color);
		font.scale(scale);

		width = (int) font.getBounds(text).width; // Get the width of the text
													// we draw using the current
													// font
		height = (int) font.getBounds(text).height; // Get the height of the
													// text we draw using the
													// current font
		centerPosition.x = (int) position.x - width / 2;
		centerPosition.y = (int) position.y - height / 2;
		width = width + expandHitboxX;
		height = height + expandHitboxY;

		font.draw(spriteBatch, text, centerPosition.x, centerPosition.y);
		font.scale(-scale);
	}

	/**
	 * 
	 * @param touchPos
	 *            Vector2 to check if collided
	 * @return true if collided, else false
	 */
	public boolean collided(Vector2 touchPos) {
		if (!drawAtCenter) {
			if (touchPos.x > position.x && touchPos.x < position.x + width) {
				if (touchPos.y < position.y && touchPos.y > position.y - height) {
					return true;
				}
			}
		} else if (drawAtCenter) {
			if (touchPos.x > centerPosition.x
					&& touchPos.x < centerPosition.x + width) {
				if (touchPos.y < centerPosition.y
						&& touchPos.y > centerPosition.y - height) {
					return true;
				}
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

	/**
	 * Use if moving placement of text
	 * 
	 * @param x
	 *            add relative to current x position
	 */
	public void addToX(float x) {
		position.x = position.x + x;
	}

	/**
	 * Use if moving placement of text
	 * 
	 * @param y
	 *            add relative to current y position
	 */
	public void addToY(float y) {
		position.y = position.y + y;
	}

	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}

	/**
	 * Expand the hitbox of the text relative to the font size width
	 * 
	 * @param extraWidth
	 */
	public void expandWidth(int extraWidth) {
		expandHitboxX = extraWidth;
	}

	/**
	 * Expand the hitbox of the text relative to the font size height
	 * 
	 * @param extraHeight
	 */
	public void expandHeight(int extraHeight) {
		expandHitboxX = extraHeight;
	}
}
