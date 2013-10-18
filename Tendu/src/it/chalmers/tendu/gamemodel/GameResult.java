package it.chalmers.tendu.gamemodel;

/**
 * The result of a finished minigame.
 * 
 */
public class GameResult {

	private final long timePlayed;
	private final long remainingTime;
	private final GameId gameId;
	private final GameState gameState;

	/**
	 * Construct this when a minigame has been completed and its results are to be
	 * saved in cache/persistence.
	 * 
	 * @param gameId
	 * @param timeSpent
	 * @param remainingTime
	 * @param gameState
	 */
	public GameResult(GameId gameId, long timeSpent, long remainingTime,
			GameState gameState) {
		this.gameId = gameId;
		this.timePlayed = timeSpent;
		this.remainingTime = remainingTime;
		this.gameState = gameState;
	}

	// for reflection
	@SuppressWarnings("unused")
	private GameResult() {
		timePlayed = 0;
		remainingTime = 0;
		gameId = null;
		gameState = null;
	}

	/**
	 * @return total time played.
	 */
	public long getTimePlayed() {
		return timePlayed;
	}

	/**
	 * @return remaining time.
	 */
	public long getRemainingTime() {
		return remainingTime;
	}

	/**
	 * @return {@link GameId} of this game.
	 */
	public GameId getGameId() {
		return gameId;
	}

	/**
	 * @return {@link GameState} of this game.
	 */
	public GameState getGameState() {
		return gameState;
	}

}
