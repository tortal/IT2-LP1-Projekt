package it.chalmers.tendu.gamemodel;

import static org.junit.Assert.assertTrue;
import it.chalmers.tendu.defaults.Constants.Difficulty;

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
		assertTrue(game != GameId.SHAPE_GAME);
	}
}
