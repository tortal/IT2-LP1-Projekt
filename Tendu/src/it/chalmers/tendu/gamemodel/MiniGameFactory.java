package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.gamemodel.shapesgame.ShapeGame;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Factory class used to randomly create a {@link MiniGame} of a selected
 * difficulty Use createGameId to get a random {@link GameId} and then use this
 * {@link GameId} with createMiniGame to get a randomly created {@link MiniGame}
 */
public class MiniGameFactory {

	/**
	 * @param difficulty
	 *            game returned will support this {@link Difficulty}
	 * @return a random GameId within the selected {@link Difficulty}
	 */
	public static GameId createGameId(Difficulty difficulty) {
		List<GameId> games = GameId.getGameIdsFor(difficulty);
		Collections.shuffle(games);
		GameId game = games.get(0);
		return game;
	}

	/**
	 * @param bonusTime
	 *            time remaining from previously played MiniGame
	 * @param gameId
	 *            {@link GameId} of {@link MiniGame} wanted
	 * @param difficulty
	 *            of {@link MiniGame}
	 * @param players
	 *            list of all players who will participate in the game
	 * @return {@link MiniGame}
	 */
	public static MiniGame createMiniGame(long bonusTime, GameId gameId,
			Difficulty difficulty, Map<String, Integer> players) {

		MiniGame miniGame = null;

		switch (gameId) {
		case NUMBER_GAME:
			miniGame = new NumberGame(bonusTime, difficulty, players);
			break;
		case SHAPE_GAME:
			miniGame = new ShapeGame(bonusTime, difficulty, players);
			break;
		}
		return miniGame;
	}
}
