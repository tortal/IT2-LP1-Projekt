package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.gamemodel.shapesgame.ShapesGame;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/*TODO This class shall select a random minigame with the selected difficulty
 *It shall also keep track of previously played games to avoid playing the
 *same game (on the specified difficulty) more than once;
 */
public class MiniGameFactory {

	public static GameId createGameId(Difficulty difficulty) {
		List<GameId> games = GameId.getGameIdsFor(difficulty);
		Collections.shuffle(games);
		GameId game = games.get(0);
		return game;
	}

	public static MiniGame createMiniGame(long bonusTime, GameId gameId,
			Difficulty difficulty, Map<String, Integer> players) {

		MiniGame miniGame = null;

		switch (gameId) {
		case NUMBER_GAME:
			miniGame = new NumberGame(bonusTime, difficulty, players);
			break;
		case SHAPES_GAME:
			miniGame = new ShapesGame(bonusTime, difficulty, players);
			break;
		}
		return miniGame;
	}
}
