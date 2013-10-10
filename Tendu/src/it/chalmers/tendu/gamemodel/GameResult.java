package it.chalmers.tendu.gamemodel;

public class GameResult {
	
	private final float timeSpent;
	private final float remainingTime;
	private final GameId gameId;
	private final GameState gameState;
	
	public GameResult(GameId gameId, float timeSpent, float remainingTime, GameState gameState) {
		this.gameId = gameId;
		this.timeSpent = timeSpent;
		this.remainingTime = remainingTime;
		this.gameState = gameState;
	}
	
	//for reflection
	@SuppressWarnings("unused")
	private GameResult() {
		timeSpent = 0;
		remainingTime = 0;
		gameId = null;
		gameState = null;
	}

	public float getTimeSpent() {
		return timeSpent;
	}

	public float getRemainingTime() {
		return remainingTime;
	}
	
	public GameId getGameId() {
		return gameId;
	}

	public GameState getGameState() {
		return gameState;
	}

}
