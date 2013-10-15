package it.chalmers.tendu.gamemodel.numbergame.test;

import static org.junit.Assert.assertTrue;
import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.gamemodel.shapesgame.Lock;
import it.chalmers.tendu.gamemodel.shapesgame.Shape;
import it.chalmers.tendu.gamemodel.shapesgame.ShapeGame;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ShapesGameTest {

	ShapeGame shapeGame;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testShapesGame() {
		// crash
		shapeGame = new ShapeGame(30000, Difficulty.ONE, null);
		shapeGame = new ShapeGame(30000, Difficulty.TWO, null);

		List<Shape> shapes = shapeGame.getAllInventory().get(0);
		for (Shape s : shapes) {
			assertTrue(s.color != null);
			assertTrue(s.geometricShape != null);
		}

		// Check that all players get's the same amount of shapes.
		assertTrue(shapeGame.getAllInventory().get(0).size() == shapeGame
				.getAllInventory().get(1).size());
		assertTrue(shapeGame.getAllInventory().get(1).size() == shapeGame
				.getAllInventory().get(2).size());
		assertTrue(shapeGame.getAllInventory().get(2).size() == shapeGame
				.getAllInventory().get(3).size());
	}

	@Test
	public void testMove() {
		// Check if one shape moves correctly from player0 to player1.
		Shape shape = shapeGame.getAllInventory().get(0).get(0);
		shapeGame.move(shape, 1);
		assertTrue(shapeGame.getOwnerOf(shape) == 1);
	}

	@Test
	public void testInsertShapeIntoSlot() {
		Lock lock = shapeGame.getLock(0);
		Shape shape = lock.getLockSequence().get(0);
		assertTrue(shapeGame.insertShapeIntoSlot(0, shape, shape));
	}

}
