package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;

import java.util.List;

public abstract class MiniGame {
	private Difficulty difficulty;
	private int timeLeft;
	private GameState state;
	private GameId gameId;
	
	private List<Player> players;

	public MiniGame(int addTime, Difficulty difficulty, GameId gameId) {
		setTimeLeft(addTime);
		this.difficulty = difficulty;
		this.setGameId(gameId);
		this.state = GameState.RUNNING;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public void addTime(int time) {
		setTimeLeft(timeLeft + time);
	}

	/** Decrease time by how many milliseconds? */
	public void decreaseTime() {
		// setTimeLeft(timeLeft-1);
		if (getTimeLeft() == 0) {
			gameLost();
		}
	}

	public GameState checkGameState() {
		return state;
	}

	private void gameLost() {
		state = GameState.LOST;
	}

	protected void gameWon() {
		state = GameState.WON;
	}
	
	public void setGameState(GameState g){
		state = g;
	}

	public GameId getGameId() {
		return gameId;
	}

	public void setGameId(GameId gameId) {
		this.gameId = gameId;
	}
}
