package it.chalmers.tendu.gamemodel.shapesgame;

import java.util.ArrayList;
import java.util.List;

/**
 * An object of this Class represents a particular Geometric shape and a Color.
 */
public class Shape {

	/**
	 * Color of this shape.
	 */
	public final ShapeColor color;

	/**
	 * The enum describing the shape.
	 */
	public final GeometricShape geometricShape;

	/**
	 * If this shape has been locked into a its corresponding slot.
	 */
	private boolean isLocked;

	public Shape(ShapeColor color, GeometricShape geometricShape) {
		this.color = color;
		this.geometricShape = geometricShape;
	}

	/**
	 * No args constructor for reflection use
	 */
	@SuppressWarnings("unused")
	private Shape() {
		this.color = null;
		this.geometricShape = null;
	}

	/**
	 * @return a list containing all combinations possible with the enumerators
	 *         Color and GeometricShape.
	 */
	public static List<Shape> getAllShapes() {
		List<Shape> allShapes = new ArrayList<Shape>();

		for (GeometricShape g : GeometricShape.values()) {
			for (ShapeColor c : ShapeColor.values()) {
				Shape s = new Shape(c, g);
				allShapes.add(s); // Add this unique combination to list
			}
		}
		return allShapes;
	}

	@Override
	public String toString() {
		return "Shape [color=" + color + ", geometricShape=" + geometricShape
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result
				+ ((geometricShape == null) ? 0 : geometricShape.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shape other = (Shape) obj;
		if (color != other.color)
			return false;
		if (geometricShape != other.geometricShape)
			return false;
		return true;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return isLocked;
	}

	/**
	 * @param locked
	 *            the locked to set
	 */
	public void setLocked(boolean locked) {
		this.isLocked = locked;
	}
}