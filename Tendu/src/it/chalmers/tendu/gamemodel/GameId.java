package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;

import java.util.ArrayList;
import java.util.List;

/**
 * Every implementation of {@link MiniGame} has an enum associated with it.
 * Abstractions and game-balance can be controlled with the parameters of
 * difficulty in these constructors.
 * 
 */
public enum GameId {

	NUMBER_GAME(Difficulty.ONE, Difficulty.TWO, Difficulty.THREE,
			Difficulty.FOUR, Difficulty.FIVE

	), SHAPE_GAME(Difficulty.ONE, Difficulty.TWO, Difficulty.THREE,
			Difficulty.FOUR, Difficulty.FIVE);

	/**
	 * Levels that are needed to make this game possible.
	 */
	private List<Difficulty> acceptedDifficulties;

	private GameId(Difficulty... difficulty) {
		acceptedDifficulties = new ArrayList<Difficulty>();

		for (Difficulty d : difficulty) {
			acceptedDifficulties.add(d);
		}

	}

	/**
	 * @param difficulty
	 * @return a list of GameIds that accepts the given {@link Difficulty} value
	 */
	public static List<GameId> getGameIdsFor(Difficulty difficulty) {
		List<GameId> validGames = new ArrayList<GameId>();
		for (GameId g : values()) {
			if (g.acceptedDifficulties.contains(difficulty))
				validGames.add(g);
		}
		return validGames;
	}

}
