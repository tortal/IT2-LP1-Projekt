package it.chalmers.tendu.gamemodel.numbergame.test;

import static org.junit.Assert.*;

import java.util.List;

import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.gamemodel.shapesgame.Lock;
import it.chalmers.tendu.gamemodel.shapesgame.Shape;
import it.chalmers.tendu.gamemodel.shapesgame.ShapesGame;

import org.junit.Before;
import org.junit.Test;


public class ShapesGameTest {
	
	ShapesGame shapesGame;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testShapesGame() {
		shapesGame = new ShapesGame(30000, Difficulty.ONE);
		shapesGame = new ShapesGame(30000, Difficulty.TWO);
		
		List <Shape> shapes= shapesGame.getAllInventory().get(0);
		for(Shape s: shapes){
			assertTrue(s.color != null);
			assertTrue(s.geometricShape != null);		
		}
		
		//Check that all players get's the same amount of shapes. 
		assertTrue(shapesGame.getAllInventory().get(0).size() == shapesGame.getAllInventory().get(1).size());
		assertTrue(shapesGame.getAllInventory().get(1).size() == shapesGame.getAllInventory().get(2).size());
		assertTrue(shapesGame.getAllInventory().get(2).size() == shapesGame.getAllInventory().get(3).size());
	}

	@Test
	public void testMove() {
		//Check if one shape moves correctly from player0 to player1. 
		Shape shape = shapesGame.getAllInventory().get(0).get(0);
		shapesGame.move(shape, 1);
		assertTrue(shapesGame.getOwnerOf(shape) == 1);
	}

	@Test
	public void testInsertShapeIntoSlot() {
		//
		Lock lock= shapesGame.getLock(0);
		Shape shape= lock.getLockSequence().get(0);
		assertTrue(shapesGame.insertShapeIntoSlot(0, shape));
	}


}
