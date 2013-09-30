package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.defaults.GameIds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Puzzle game
 * 
 * Every player has n slots (default is 4) on their screen that are initially
 * empty and are to be replaced by shapes. One uses objects (Shape class object)
 * to fill ones' individual slot puzzle. Moreover, every player has an inventory
 * of shapes, the union of all players' inventory contain all shapes needed to
 * fully fill the missing slots.
 * 
 * When beginning the game, you might have to trade shapes without fellow team
 * players until you will be able to solve YOUR puzzle.
 * 
 */
public class ShapesGame extends MiniGame {

	private final static int PLAYER_COUNT = 4;
	private final static int SLOTS_COUNT = 4;

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

		@Override
		public String toString() {
			return "Shape [color=" + color + ", geometricShape="
					+ geometricShape + "]";
		}
	}

	public static class Slots {

		private final List<Shape> shapes;
		private final Map<Shape, Boolean> slotLock;

		/**
		 * Create a Slot object. Every player has one and it should be populated
		 * with different copies of the shapes available in-game
		 */
		public Slots() {
			this.shapes = new ArrayList<Shape>(SLOTS_COUNT);
			this.slotLock = new HashMap<Shape, Boolean>();
		}

		/**
		 * As no-args constructor however lets you populate the object with a
		 * ready List of Shape-objects.
		 * 
		 * @param listOfShapes
		 */
		public Slots(List<Shape> listOfShapes) {
			this.shapes = listOfShapes;
			slotLock = new HashMap<Shape, Boolean>();
			for (Shape s : listOfShapes) {
				slotLock.put(s, false);
			}
		}

		/**
		 * @param shape
		 *            to add to the list of required shapes for a player to
		 *            break the puzzle.
		 */
		public void addSlot(Shape shape) {
			shapes.add(shape);
			slotLock.put(shape, false);
		}

		public Map<Shape, Boolean> getSlotsState() {
			Map<Shape, Boolean> slotState = new HashMap<ShapesGame.Shape, Boolean>(
					this.slotLock);
			return slotState;
		}

		public List<Shape> getSlotCombination() {
			List<Shape> slotCombo = new ArrayList<Shape>(shapes);
			return slotCombo;
		}

		public boolean isAllSlotsFilled() {
			for (Boolean slot : slotLock.values()) {
				if (slot != true)
					return false;
			}
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Slots [shapes=");
			builder.append(shapes);
			builder.append(", slotLock=");
			builder.append(slotLock);
			builder.append("]");
			return builder.toString();
		}

	}

	private Map<Integer, List<Shape>> allInventory;
	private Map<Integer, Slots> allSlots;

	public ShapesGame(int addTime, Difficulty difficulty) {
		super(addTime, difficulty, GameIds.SHAPES_GAME);

		// Get list of all combinations of shapes and colors then shuffle
		List<Shape> allShapes = Shape.getAllShapes();
		Collections.shuffle(allShapes);

		allInventory = new HashMap<Integer, List<Shape>>(PLAYER_COUNT);
		allSlots = new HashMap<Integer, Slots>(PLAYER_COUNT);

		// Every player only has an explicit number of slots to fill, so let's
		// grab
		// the need amount of shapes from our allShapes list
		List<Shape> gameShapes = new ArrayList<Shape>(SLOTS_COUNT
				* PLAYER_COUNT);

		for (int i = 0; i < SLOTS_COUNT * PLAYER_COUNT; i++) {
			Shape randomShape = allShapes.remove(0);
			gameShapes.add(randomShape);
		}

		// Clone gameShapes list, and shuffle the copy. We will use this copy to
		// randomly distribute the slots.
		List<Shape> copyOfShapes = new ArrayList<Shape>(gameShapes.size());
		copyOfShapes.addAll(gameShapes);
		Collections.shuffle(copyOfShapes);

		// Create inventory and slots lists for all players.
		for (int p = 0; p < PLAYER_COUNT; p++) {
			List<Shape> playerInventory = new ArrayList<Shape>();
			Slots playerSlot = new Slots();

			allInventory.put(p, playerInventory);
			allSlots.put(p, playerSlot);

			for (int i = 0; i < SLOTS_COUNT; i++) {
				playerInventory.add(gameShapes.remove(0));
				playerSlot.addSlot(copyOfShapes.remove(0));
			}

		}

		debug_printMembers();

	}

	private void debug_printMembers() {
		System.out.println("\t ALL INVENTORY \t");
		for (Map.Entry<Integer, List<Shape>> e : allInventory.entrySet()) {
			System.out.println("Player" + e.getKey());
			for (Shape s : e.getValue()) {
				System.out.println("\t" + s);
			}
		}
		System.out.println("SLOTS TO FILL");
		for (Map.Entry<Integer, Slots> e: allSlots.entrySet()) {
			System.out.println("Player" + e.getKey());
			
			
			for (Shape s : e.getValue().getSlotCombination()){
				System.out.println("\t" + s);
			}
	
		}

	}

	public static void main(String[] a) {
		System.out.println("Creating Shapes Game...");
		new ShapesGame(0, Difficulty.ONE);
	}

}
