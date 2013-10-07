package it.chalmers.tendu.screens;


import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.gamemodel.shapesgame.GeometricShape;
import it.chalmers.tendu.gamemodel.shapesgame.Shape;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

/**
 * 
 * @author Markus
 * 
 */
public class ShapesGameShape {

	private Rectangle bounds;
	private ShapeType shapeType;
	private Color color;
	private Shape shape;

	public ShapesGameShape(Shape shape) {
		this.shape = shape;
		bounds = new Rectangle();
		bounds.x = Constants.SCREEN_WIDTH / 2;
		bounds.y = Constants.SCREEN_HEIGHT / 2;
		bounds.height = 100;
		bounds.width = 100;
		this.shapeType = determineGeometricShape(shape);
		this.color = determineColor(shape);

	}

	public void renderShape(ShapeRenderer sr) {
		switch (shapeType) {
		case FilledCircle:
			sr.begin(ShapeType.FilledCircle);
			sr.setColor(color);
			sr.filledCircle(bounds.x + bounds.width/2, bounds.y + bounds.height/2, bounds.height / 2);
			sr.end();
			break;
		case FilledRectangle:
			sr.begin(ShapeType.FilledRectangle);
			sr.setColor(color);
			sr.filledRect(bounds.x, bounds.y, bounds.height, bounds.width);
			sr.end();
			break;
		case FilledTriangle:
			sr.begin(ShapeType.FilledTriangle);
			sr.setColor(color);
			sr.filledTriangle(bounds.x, bounds.y, bounds.width / 2 + bounds.x,
					bounds.height + bounds.y, bounds.x + bounds.width, bounds.y);
			sr.end();
			break;
		default:
		}
		
		
	}

	public void moveShape(float f, float g) {
		bounds.x = f;
		bounds.y = g;
	}

	public static ShapeType determineGeometricShape(Shape s) {
		GeometricShape gs = s.geometricShape;
		switch (gs) {
		case CIRCLE:
			return ShapeType.FilledCircle;
		case OCTAGON:
			return ShapeType.FilledCircle;
		case RHOMBOID:
			return ShapeType.FilledCircle;
		case SQUARE:
			return ShapeType.FilledRectangle;
		case TRIANGLE:
			return ShapeType.FilledTriangle;
		}
		return ShapeType.FilledCircle;
	}

	public static Color determineColor(Shape s) {
		switch (s.color) {
		case BLUE:
			return com.badlogic.gdx.graphics.Color.BLUE;
		case GREEN:
			return com.badlogic.gdx.graphics.Color.GREEN;
		case RED:
			return com.badlogic.gdx.graphics.Color.RED;
		case YELLOW:
			return com.badlogic.gdx.graphics.Color.YELLOW;
		default:
			return com.badlogic.gdx.graphics.Color.WHITE;
		}
	}

	/**
	 * @return the bounds
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * @return the shape
	 */
	public Shape getShape() {
		return shape;
	}

	/**
	 * @param shape the shape to set
	 */
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	/**
	 * @param bounds the bounds to set
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(com.badlogic.gdx.graphics.Color color) {
		this.color = color;
	}

}
