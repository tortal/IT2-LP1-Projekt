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
		CIRCLE, SQUARE, RHOMBOID, TRIANGLE, OCTAGON;
	}

	/**
	 * An object of this Class represents a particular Geometric shape and a
	 * Color.
	 */
	public static class Shape {

		public final Color color;
		public final GeometricShape geometricShape;

		private Shape(Color color, GeometricShape geometricShape) {
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

	/**
	 * The puzzle to be solved. Every player has a lock which represents n
	 * numbers of slots to be fitted with the given shapes. Every player should
	 * have one of these.
	 * 
	 */
	public static class Lock {

		private final List<Shape> shapes;
		private final Map<Shape, Boolean> slotLock;

		/**
		 * New lock.
		 */
		public Lock() {
			this.shapes = new ArrayList<Shape>(SLOTS_COUNT);
			this.slotLock = new HashMap<Shape, Boolean>();
		}

		/**
		 * Directly populates this lock with the given sequence
		 * 
		 * @param listOfShapes
		 */
		public Lock(List<Shape> listOfShapes) {
			this.shapes = listOfShapes;
			slotLock = new HashMap<Shape, Boolean>();
			for (Shape s : listOfShapes) {
				slotLock.put(s, false);
			}
		}

		/**
		 * @param shape
		 *            to add to the lock sequence.
		 */
		public void addSlot(Shape shape) {
			shapes.add(shape);
			slotLock.put(shape, false);
		}

		/**
		 * @return all of the lock sequence and their states
		 */
		public Map<Shape, Boolean> getLockState() {
			Map<Shape, Boolean> slotState = new HashMap<Shape, Boolean>(
					this.slotLock);
			return slotState;
		}

		/**
		 * @return the lock sequence needed to crack this puzzle.
		 */
		public List<Shape> getLockSequence() {
			List<Shape> slotCombo = new ArrayList<Shape>(shapes);
			return slotCombo;
		}

		/**
		 * @return true if player has matched all slots in the lock sequence.
		 *         Hint: use this to check if player is GAME OVER.
		 */
		public boolean isAllSlotsFilled() {
			for (Boolean slot : slotLock.values()) {
				if (slot != true)
					return false;
			}
			return true;
		}

		/**
		 * @param shape
		 *            to fill the lock with
		 * @return true if the slot was empty and the shape fits.
		 */
		private boolean fillSlot(Shape shape) {
			if (!this.shapes.contains(shape))
				return false;
			boolean curState = slotLock.get(shape);
			if (curState == true)
				return false; // TODO: Debugging
			slotLock.put(shape, true);
			return true;
		}

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

	/**
	 * All shapes for all players mapped by player number (Integer).
	 */
	private Map<Integer, List<Shape>> allInventory;
	/**
	 * Every players lock, mapped by player number (Integer)
	 */
	private Map<Integer, Lock> allLocks;

	/**
	 * This will create a ShapesGame. It creates a list of all possible
	 * combinations of the enums {@link GeometricShape} and link {@link Color}
	 * and then reduces this randomly to a subset that suffice for the game
	 * settings (player count and lock seqeuence length)
	 */
	public ShapesGame(int addTime, Difficulty difficulty) {
		super(addTime, difficulty, GameIds.SHAPES_GAME);

		// Get list of all combinations of shapes and colors then shuffle
		List<Shape> allShapes = Shape.getAllShapes();
		Collections.shuffle(allShapes);

		allInventory = new HashMap<Integer, List<Shape>>(PLAYER_COUNT);
		allLocks = new HashMap<Integer, Lock>(PLAYER_COUNT);

		// Every player only has an explicit number of slots to fill, so let's
		// grab the need amount of shapes from our allShapes list
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
			Lock playerLock = new Lock();

			allInventory.put(p, playerInventory);
			allLocks.put(p, playerLock);

			for (int i = 0; i < SLOTS_COUNT; i++) {
				playerInventory.add(gameShapes.remove(0));
				playerLock.addSlot(copyOfShapes.remove(0));
			}

		}

	}

	/**
	 * Will move a referenced shape to the inventory of another player.
	 * 
	 * @param shape
	 *            to move
	 * @param player
	 *            to move the shape to
	 * @return <code>-1</code> if that player already owned that shape.
	 *         <code>0</code> on successful move.
	 */
	public int move(Shape shape, int player) {
		int owner = getOwnerOf(shape);
		if (owner == player)
			return -1;
		else {
			List<Shape> oldLocation = allInventory.get(owner);
			List<Shape> newLocation = allInventory.get(player);
			if (!oldLocation.remove(shape)) // TODO: for debugging.
				return -2;

			newLocation.add(shape);
			return 0;
		}
	}

	/**
	 * @param player
	 *            that is inserting the shape
	 * @param shape
	 *            to be inserted into the players lock.
	 * @return <code>true</code> if shape and slot fitted.
	 */
	public boolean insertShapeIntoSlot(int player, Shape shape) {
		Lock lock = this.allLocks.get(player);
		if (lock.fillSlot(shape))
			return true;

		return false;
	}

	/**
	 * @param shape
	 *            to search for
	 * @return the player that owns the searched shape. <code>-1</code> if the
	 *         shape was not found.
	 */
	public int getOwnerOf(Shape shape) {
		int owner = -1;
		for (Map.Entry<Integer, List<Shape>> e : allInventory.entrySet()) {
			for (Shape s : e.getValue()) {
				if (shape.equals(s)) {
					owner = e.getKey();
					return owner;
				}
			}
		}
		return owner;
	}

	/**
	 * @param lock
	 *            to search owner of
	 * @return the owner of the lock.
	 */
	public int getOwnerOf(Lock lock) {
		int owner = -1;
		for (Map.Entry<Integer, Lock> e : allLocks.entrySet()) {
			if (lock.equals(e.getValue())) {
				owner = e.getKey();
			}
		}
		return owner;
	}

	/**
	 * DEBUG METHOD
	 */
	void debug_printMembers() {
		System.out.println("\t ALL INVENTORY \t");
		for (Map.Entry<Integer, List<Shape>> e : allInventory.entrySet()) {
			System.out.println("Player" + e.getKey());
			for (Shape s : e.getValue()) {
				System.out.println("\t" + s);
			}
		}
		System.out.println("SLOTS TO FILL");
		for (Map.Entry<Integer, Lock> e : allLocks.entrySet()) {
			System.out.println("Player" + e.getKey());

			for (Shape s : e.getValue().getLockSequence()) {
				System.out.println("\t" + s);
			}

			for (Boolean b : e.getValue().getLockState().values()) {
				System.out.println("\t " + b);
			}

		}

	}

	/**
	 * TODO: DEBUG MAIN and testing.
	 */
	public static void main(String[] a) {
		Lock s = new Lock();
		List<Shape> allShapes = Shape.getAllShapes();
		Collections.shuffle(allShapes);
		s.addSlot(allShapes.remove(0));
		s.addSlot(allShapes.remove(0));

		System.out.println(s.slotLock);
		System.out.println(s.shapes);

		System.out.println("\n\n\n\n\n");
		System.out.println("Creating Shapes Game...");
		ShapesGame g = new ShapesGame(0, Difficulty.ONE);
		g.debug_printMembers();
	}

}
