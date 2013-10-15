package it.chalmers.tendu.gamemodel;

public class GameResult {
	
	private final long timePlayed;
	private final long remainingTime;
	private final GameId gameId;
	private final GameState gameState;
	
	public GameResult(GameId gameId, long timeSpent, long remainingTime, GameState gameState) {
		this.gameId = gameId;
		this.timePlayed = timeSpent;
		this.remainingTime = remainingTime;
		this.gameState = gameState;
	}
	
	//for reflection
	@SuppressWarnings("unused")
	private GameResult() {
		timePlayed = 0;
		remainingTime = 0;
		gameId = null;
		gameState = null;
	}

	public long getTimePlayed() {
		return timePlayed;
	}

	public long getRemainingTime() {
		return remainingTime;
	}
	
	public GameId getGameId() {
		return gameId;
	}

	public GameState getGameState() {
		return gameState;
	}

}
