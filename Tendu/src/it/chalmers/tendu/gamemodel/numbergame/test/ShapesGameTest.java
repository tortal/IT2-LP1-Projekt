package it.chalmers.tendu.gamemodel.numbergame.test;

import static org.junit.Assert.assertTrue;
import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.gamemodel.shapesgame.Lock;
import it.chalmers.tendu.gamemodel.shapesgame.Shape;
import it.chalmers.tendu.gamemodel.shapesgame.ShapeGame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ShapesGameTest {

	ShapeGame shapeGameEasy;
	ShapeGame shapeGameHard;

	@Before
	public void setUp() throws Exception {
		Map<String, Integer> players = new HashMap<String, Integer>();
		players.put("player1", 0);
		players.put("player2", 1);

		shapeGameEasy = new ShapeGame(30000, Difficulty.ONE, players);
		shapeGameHard = new ShapeGame(30000, Difficulty.TWO, players);
	}

	@Test
	public void testShapesGame() {

		// Checks so that all of the shapes has a color and a form.
		List<Shape> shapes = shapeGameEasy.getAllInventory().get(0);
		for (Shape s : shapes) {
			assertTrue(s.color != null);
			assertTrue(s.geometricShape != null);
		}

		// Check that all players get's the same amount of shapes.
		assertTrue(shapeGameEasy.getAllInventory().get(0).size() == shapeGameEasy
				.getAllInventory().get(1).size());

	}

	@Test
	public void testMove() {
		// Check if one shape moves correctly from player0 to player1.
		Shape shape = shapeGameEasy.getAllInventory().get(0).get(0);
		shapeGameEasy.move(shape, 1);
		assertTrue(shapeGameEasy.getOwnerOf(shape) == 1);
	}

	@Test
	public void testInsertShapeIntoSlot() {
		Lock lock = shapeGameEasy.getLock(0);
		Shape shape = lock.getLockSequence().get(0);
		assertTrue(shapeGameEasy.insertShapeIntoSlot(0, shape, shape));
	}

}
