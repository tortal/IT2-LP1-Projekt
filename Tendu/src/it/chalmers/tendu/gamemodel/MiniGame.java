package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.GameIds;
import it.chalmers.tendu.defaults.GameState;

public abstract class MiniGame {
	private int difficulty;
	private int timeLeft;
	private GameState state;
	private GameIds gameId;
	
	public MiniGame(int addTime, int difficulty, GameIds gameId) {
		setTimeLeft(addTime);
		this.difficulty=difficulty;
		this.setGameId(gameId);
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
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
	
	/**Decrease time by how many milliseconds?*/
	public void decreaseTime() {
		//setTimeLeft(timeLeft-1);
		if(getTimeLeft() == 0) {
			gameLost();
		}
	}
	
	public GameState checkGameState() {
		return state;
	}
	
	private void gameLost() {
		state = GameState.LOST;
	}

	public GameIds getGameId() {
		return gameId;
	}

	public void setGameId(GameIds gameId) {
		this.gameId = gameId;
	}
}
