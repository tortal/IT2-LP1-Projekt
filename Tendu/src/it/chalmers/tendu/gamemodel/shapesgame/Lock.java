package it.chalmers.tendu.gamemodel.shapesgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The puzzle to be solved. Every player has a lock which represents n
 * numbers of slots to be fitted with the given shapes. Every player should
 * have one of these.
 * 
 */
public class Lock {

	private final List<Shape> shapes;
	private final Map<Shape, Boolean> slotLock;

	/**
	 * New lock.
	 */
	public Lock() {
		this.shapes = new ArrayList<Shape>();
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
	boolean fillSlot(Shape shape) {
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
		StringBuffer s = new StringBuffer();
		s.append("Lock: [");
		for (Shape shape : shapes){
			System.out.println(s);
		}
		
		return "";
	}

}