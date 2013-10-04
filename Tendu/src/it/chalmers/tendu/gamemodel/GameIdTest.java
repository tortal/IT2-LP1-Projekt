package it.chalmers.tendu.gamemodel;

import static org.junit.Assert.*;
import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.gamemodel.shapesgame.ShapesGame;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class GameIdTest {

	@Test
	public void test() {
		List<GameId> games = GameId.getGameIdsFor(Difficulty.ONE);
		Collections.shuffle(games);
		GameId game = games.get(0);
		assertTrue(game == GameId.NUMBER_GAME);
		assertTrue(game != GameId.SHAPES_GAME);
		
	}	

}
