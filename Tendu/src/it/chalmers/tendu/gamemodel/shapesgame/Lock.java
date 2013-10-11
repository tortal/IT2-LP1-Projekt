package it.chalmers.tendu.gamemodel.shapesgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The puzzle to be solved. Every player has a lock which represents n numbers
 * of slots to be fitted with the given shapes. Every player should have one of
 * these.
 * 
 */
public class Lock {

	private final List<Shape> lockSequence;
	private final Map<Shape, Boolean> slotLock;

	/**
	 * New lock.
	 */
	public Lock() {
		this.lockSequence = new ArrayList<Shape>();
		this.slotLock = new HashMap<Shape, Boolean>();
	}

	/**
	 * Directly populates this lock with the given sequence
	 * 
	 * @param listOfShapes
	 */
	public Lock(List<Shape> listOfShapes) {
		this.lockSequence = listOfShapes;
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
		lockSequence.add(shape);
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
		List<Shape> slotCombo = new ArrayList<Shape>(lockSequence);
		return slotCombo;
	}

	/**
	 * @return true if player has matched all slots in the lock sequence. Hint:
	 *         use this to check if player is GAME OVER.
	 */
	public boolean isAllSlotsFilled() {
		for (Boolean slot : slotLock.values()) {
			if (slot != true)
				return false;
		}
		return true;
	}

	public boolean fitsIntoSlot(Shape shape, Shape lockShape) {
		if (this.lockSequence.contains(lockShape)) {
			if (shape.geometricShape == lockShape.geometricShape
					&& lockShape.color == shape.color) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param shape
	 *            to fill the lock with
	 * @return true if the slot was empty and the shape fits.
	 */
	public boolean fillSlot(Shape shape, Shape lockShape) {
		if (!fitsIntoSlot(shape, lockShape)) {
			return false;
		}
		boolean curState = slotLock.get(lockShape);
		if (curState == true) {
			return false; // TODO: Debugging
		}
		if (shape.equals(lockShape)) {
			slotLock.put(shape, true);
			shape.setLocked(true);
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("Lock: [\n");
		for (Shape shape : lockSequence) {
			s.append("\t");
			s.append(shape);
			s.append(" : ");
			s.append(slotLock.get(shape));
			s.append("\n");
		}
		s.append("\t]");

		return s.toString();
	}

}