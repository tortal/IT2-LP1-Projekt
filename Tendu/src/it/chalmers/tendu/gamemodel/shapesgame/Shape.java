package it.chalmers.tendu.gamemodel.shapesgame;

import java.util.ArrayList;
import java.util.List;

/**
 * An object of this Class represents a particular Geometric shape and a Color.
 */
public class Shape {

	public final Color color;
	public final GeometricShape geometricShape;
	public boolean locked;

	public Shape(Color color, GeometricShape geometricShape) {
		this.color = color;
		this.geometricShape = geometricShape;
	}

	/**
	 * @return a list containing all combinations possible with the enumerators
	 *         Color and GeometricShape.
	 */
	public static List<Shape> getAllShapes() {
		List<Shape> allShapes = new ArrayList<Shape>();

		for (GeometricShape g : GeometricShape.values()) {
			for (Color c : Color.values()) {
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
		return locked;
	}

	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}