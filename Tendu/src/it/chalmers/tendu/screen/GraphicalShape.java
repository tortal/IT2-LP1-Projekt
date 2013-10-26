package it.chalmers.tendu.screen;

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
public class GraphicalShape {

	public final String TAG = this.getClass().getName();

	public final int HEIGHT = 150;
	public final int WIDTH = 150;

	private Rectangle bounds;
	private Color color;
	private Shape shape;

	private boolean renderAsLock;

	public GraphicalShape(Shape shape) {
		this.shape = shape;
		bounds = new Rectangle();
		bounds.x = Constants.SCREEN_WIDTH / 2;
		bounds.y = Constants.SCREEN_HEIGHT / 2;
		bounds.height = HEIGHT;
		bounds.width = WIDTH;
		this.color = determineColor();

	}

	public GraphicalShape(Shape shape, int x, int y) {
		this(shape);
		moveShape(x, y);

	}

	public void render(ShapeRenderer sr) {
		if (renderAsLock)
			renderAsLock(sr);
		else
			renderAsShape(sr);
	}

	/**
	 * @param sr
	 */
	private void renderAsShape(ShapeRenderer sr) {
		GeometricShape gs = shape.geometricShape;
		switch (gs) {
		case CIRCLE:
			sr.begin(ShapeType.FilledCircle);
			sr.setColor(color);
			sr.filledCircle(bounds.x + bounds.width / 2, bounds.y
					+ bounds.height / 2, bounds.height / 2);
			sr.end();
			break;
		case SQUARE:
			sr.begin(ShapeType.FilledRectangle);
			sr.setColor(color);
			sr.filledRect(bounds.x, bounds.y, bounds.height, bounds.width);
			sr.end();
			break;
		case TRIANGLE:
			sr.begin(ShapeType.FilledTriangle);
			sr.setColor(color);
			sr.filledTriangle(bounds.x, bounds.y, bounds.width / 2 + bounds.x,
					bounds.height + bounds.y, bounds.x + bounds.width, bounds.y);
			sr.end();
			break;
		case DIAMOND:
			sr.begin(ShapeType.FilledTriangle);
			sr.setColor(color);
			sr.filledTriangle(bounds.x + WIDTH / 2, bounds.y, bounds.x,
					bounds.y + HEIGHT / 2, bounds.x + WIDTH / 2, bounds.y
							+ HEIGHT);
			sr.end();
			sr.begin(ShapeType.FilledTriangle);
			sr.setColor(color);
			sr.filledTriangle(bounds.x + bounds.width / 2, bounds.y, bounds.x
					+ WIDTH, bounds.y + bounds.height / 2, bounds.x
					+ bounds.width / 2, bounds.y + bounds.height);
			sr.end();
			break;
		case RHOMBOID:
			int angle = 60;
			sr.begin(ShapeType.FilledTriangle);
			sr.setColor(color);
			sr.filledTriangle(bounds.x, bounds.y, bounds.x + angle, bounds.y,
					bounds.x + angle, bounds.y + bounds.height);
			sr.end();
			sr.begin(ShapeType.FilledRectangle);
			sr.setColor(color);
			sr.filledRect(bounds.x + angle, bounds.y, bounds.width - angle,
					bounds.height);
			sr.end();
			sr.begin(ShapeType.FilledTriangle);
			sr.setColor(color);
			sr.filledTriangle(bounds.x + bounds.width, bounds.y, bounds.x
					+ bounds.width, bounds.y + bounds.height, bounds.x
					+ bounds.width + angle, bounds.y + bounds.height);
			sr.end();
			break;
		default:
		}
	}

	/**
	 * @param sr
	 */
	private void renderAsLock(ShapeRenderer sr) {
		GeometricShape gs = shape.geometricShape;
		switch (gs) {
		case CIRCLE:
			sr.begin(ShapeType.Circle);
			sr.setColor(color);
			sr.circle(bounds.x + bounds.width / 2,
					bounds.y + bounds.height / 2, bounds.height / 2);
			sr.end();
			break;
		case SQUARE:
			sr.begin(ShapeType.Rectangle);
			sr.setColor(color);
			sr.rect(bounds.x, bounds.y, bounds.height, bounds.width);
			sr.end();
			break;
		case TRIANGLE:
			sr.begin(ShapeType.Triangle);
			sr.setColor(color);
			sr.triangle(bounds.x, bounds.y, bounds.width / 2 + bounds.x,
					bounds.height + bounds.y, bounds.x + bounds.width, bounds.y);
			sr.end();
			break;
		case DIAMOND:
			sr.begin(ShapeType.Line);
			sr.setColor(color);
			sr.line(bounds.x + bounds.width / 2, bounds.y, bounds.x, bounds.y
					+ bounds.height / 2);
			sr.end();
			sr.begin(ShapeType.Line);
			sr.setColor(color);
			sr.line(bounds.x, bounds.y + bounds.height / 2, bounds.x
					+ bounds.width / 2, bounds.y + bounds.height);
			sr.end();
			sr.begin(ShapeType.Line);
			sr.setColor(color);
			sr.line(bounds.x + bounds.width / 2, bounds.y + bounds.height,
					bounds.x + bounds.width, bounds.y + bounds.height / 2);
			sr.end();
			sr.begin(ShapeType.Line);
			sr.setColor(color);
			sr.line(bounds.x + bounds.width, bounds.y + bounds.height / 2,
					bounds.x + bounds.width / 2, bounds.y);
			sr.end();
			break;
		case RHOMBOID:
			int angle = 60;
			sr.begin(ShapeType.Line);
			sr.setColor(color);
			sr.line(bounds.x, bounds.y, bounds.x + angle, bounds.y
					+ bounds.height);
			sr.end();
			sr.begin(ShapeType.Line);
			sr.setColor(color);
			sr.line(bounds.x + angle, bounds.y + bounds.height, bounds.x
					+ bounds.width + angle, bounds.y + bounds.height);
			sr.end();
			sr.begin(ShapeType.Line);
			sr.setColor(color);
			sr.line(bounds.x + bounds.width + angle, bounds.y + bounds.height,
					bounds.x + bounds.width, bounds.y);
			sr.end();
			sr.begin(ShapeType.Line);
			sr.setColor(color);
			sr.line(bounds.x + bounds.width, bounds.y, bounds.x, bounds.y);
			sr.end();
			break;
		default:
		}
	}

	public void moveShape(float f, float g) {
		bounds.x = f;
		bounds.y = g;
	}

	// public static ShapeType determineGeometricShape(Shape s) {
	// GeometricShape gs = s.geometricShape;
	// switch (gs) {
	// case CIRCLE:
	// return ShapeType.FilledCircle;
	// case OCTAGON:
	// return ShapeType.Circle;
	// case RHOMBOID:
	// return ShapeType.Rectangle;
	// case SQUARE:
	// return ShapeType.FilledRectangle;
	// case TRIANGLE:
	// return ShapeType.FilledTriangle;
	// }
	// return ShapeType.FilledCircle;
	// }

	/**
	 * @return the renderAsLock
	 */
	public boolean isRenderAsLock() {
		return renderAsLock;
	}

	/**
	 * @param renderAsLock
	 *            the renderAsLock to set
	 */
	public void setRenderAsLock(boolean renderAsLock) {
		this.renderAsLock = renderAsLock;
	}

	private Color determineColor() {
		// if(s.isLocked())
		// return com.badlogic.gdx.graphics.Color.GRAY;
		switch (shape.color) {
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
	 * @param shape
	 *            the shape to set
	 */
	public void setShape(Shape shape) {
		this.shape = shape;
	}





	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + HEIGHT;
		result = prime * result + ((TAG == null) ? 0 : TAG.hashCode());
		result = prime * result + WIDTH;
		result = prime * result + ((bounds == null) ? 0 : bounds.hashCode());
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + (renderAsLock ? 1231 : 1237);
		result = prime * result + ((shape == null) ? 0 : shape.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphicalShape other = (GraphicalShape) obj;
		if (HEIGHT != other.HEIGHT)
			return false;
		if (TAG == null) {
			if (other.TAG != null)
				return false;
		} else if (!TAG.equals(other.TAG))
			return false;
		if (WIDTH != other.WIDTH)
			return false;
		if (bounds == null) {
			if (other.bounds != null)
				return false;
		} else if (!bounds.equals(other.bounds))
			return false;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (renderAsLock != other.renderAsLock)
			return false;
		if (shape == null) {
			if (other.shape != null)
				return false;
		} else if (!shape.equals(other.shape))
			return false;
		return true;
	}

	/**
	 * @param bounds
	 *            the bounds to set
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(com.badlogic.gdx.graphics.Color color) {
		this.color = color;
	}

}