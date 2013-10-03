package it.chalmers.tendu.gamemodel;

import java.util.Collections;
import java.util.List;

import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.gamemodel.shapesgame.ShapesGame;

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

	public static MiniGame createMiniGame(int bonusTime, GameId gameId,
			Difficulty difficulty) {

		MiniGame miniGame = null;

		switch (gameId) {
		case NUMBER_GAME:
			miniGame = new NumberGame(bonusTime, difficulty);
		case SHAPES_GAME:
			miniGame = new ShapesGame(bonusTime, difficulty);
		}
		return miniGame;
	}
}
