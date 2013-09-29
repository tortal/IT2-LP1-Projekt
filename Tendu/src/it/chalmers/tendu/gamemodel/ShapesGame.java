package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.defaults.GameIds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapesGame extends MiniGame {

	private final int PLAYER_COUNT = 4;
	private final int SLOTS_COUNT = 4;

	public enum Color {
		BLUE, GREEN, RED, YELLOW;
	}

	public enum GeometricShape {
		CIRCLE, SQUARE, RHOMBOID, TRIANGLE;
	}

	/**
	 * An object of this Class represents a particular Geometric shape and a
	 * Color.
	 */
	public static class Shape {
		final Color color;
		final GeometricShape geometricShape;

		public Shape(Color color, GeometricShape geometricShape) {
			this.color = color;
			this.geometricShape = geometricShape;
		}

		/**
		 * @return a list containing all combinations possible with the
		 *         enumerators Color and GeometricShape.
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
	}

	private Map<Integer, List<Shape>> allInventory;
	private Map<Integer, List<Shape>> allSlots;

	public ShapesGame(int addTime, Difficulty difficulty) {
		super(addTime, difficulty, GameIds.SHAPES_GAME);

		// Get list of all combinations of shapes and colors then shuffle
		List<Shape> allShapes = Shape.getAllShapes();
		Collections.shuffle(allShapes);

		allInventory = new HashMap<Integer, List<Shape>>(PLAYER_COUNT);
		allSlots = new HashMap<Integer, List<Shape>>(SLOTS_COUNT);

		// Every player only has an explicit number of slots to fill, so let's
		// grab
		// the need amount of shapes from our allShapes list
		List<Shape> gameShapes = new ArrayList<Shape>();
		for (int i = 0; i < SLOTS_COUNT * PLAYER_COUNT; i++) {
			Shape randomShape = allShapes.remove(0);
			gameShapes.add(randomShape);
		}

		List<Shape> copyOfShapes = new ArrayList<Shape>();
		copyOfShapes.addAll(gameShapes);
		Collections.shuffle(copyOfShapes);

		for (int p = 0; p < PLAYER_COUNT; p++) {
			List<Shape> playerInventory = new ArrayList<Shape>();
			List<Shape> playerSlot = new ArrayList<Shape>();

			allInventory.put(p, playerInventory);
			allSlots.put(p, playerSlot);

			for (int i = 0; i < SLOTS_COUNT; i++) {
				playerInventory.add(gameShapes.remove(0));
				playerSlot.add(copyOfShapes.remove(0));
			}

		}

	}
	
	public static void main(String[] a){
		System.out.println("yo");
		new ShapesGame(0, Difficulty.ONE);
	}
	
}
