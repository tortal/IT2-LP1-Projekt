package it.chalmers.tendu.gamemodel.shapesgame;

import java.util.ArrayList;
import java.util.List;

/**
 * An object of this Class represents a particular Geometric shape and a Color.
 */
public class Shape {

	public final Color color;
	public final GeometricShape geometricShape;

	private Shape(Color color, GeometricShape geometricShape) {
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
}