package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;

//TODO make none dependent of internal clock

public abstract class MiniGame {
	private Difficulty difficulty;
	private GameState state;
	private GameId gameId;
	private long totalTime; // never changes once set
	private long remainingTime;
	private long endTime;
	private GameState stateBeforePause;

	public void setStartTime(long gameTime, long extraTime) {
		totalTime = gameTime + extraTime;
		remainingTime = totalTime;
	}

	/**
	 * sets the actual time for the minigame to end do once in startGame and in
	 * reInit if new model pushed from server
	 * 
	 * @param time
	 */
	private void setEndTime(long time) {
		this.endTime = time + System.currentTimeMillis();
	}

	/**
	 * Gets the remaining time
	 * 
	 * @return time left in millis seconds
	 */
	public long getRemainingTime() {
		if(getGameState() == GameState.RUNNING) {
			updateTime();
		}
		return remainingTime;
	}

	/**
	 * updates the remaining time if GameState = RUNNING
	 */
	private void updateTime() {
		remainingTime = endTime - System.currentTimeMillis();
		if (remainingTime <= 0) {
			gameLost();
		}
	}

	/**
	 * Call if host pushed new model
	 */
	public void reInit() {
		setEndTime(remainingTime);
	}

	/**
	 * @param time
	 * 			the change in milliseconds, can be positive or negative;
	 */
	public void changeTime(long time) {
		endTime = endTime + time;
		updateTime();
	}

	public long getTotalTime() {
		return totalTime;
	}

	/**
	 * Integer = player id String = player MacAddress
	 */
	private Map<String, Integer> players;

	/** No args constructor for reflection use */
	protected MiniGame() {
	}

	/**
	 * Creates a new minigame.
	 * 
	 * @param addTime
	 *            > 0 if extra time should be added
	 * @param difficulty
	 *            the game's difficulty
	 * @param gameId
	 */
	public MiniGame(Difficulty difficulty, GameId gameId,
			Map<String, Integer> players) {
		this.difficulty = difficulty;
		this.setGameId(gameId);
		this.setState(GameState.WAITING);
		this.players = players;
	}

	/**
	 * Gets the difficulty of the game
	 * 
	 * @return the difficulty
	 */
	public Difficulty getDifficulty() {
		return difficulty;
	}

	/**
	 * Sets the difficulty
	 * 
	 * @param difficulty
	 *            the difficulty
	 */
	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	/**
	 * Checks and returns the state of the game.
	 * 
	 * @return the game's state
	 */
	public GameState checkGameState() {
		return getGameState();
	}

	private void gameLost() {
		setState(GameState.LOST);
	}

	protected void gameWon() {
		setState(GameState.WON);
	}

	public void setGameState(GameState g) {
		setState(g);
	}

	/**
	 * Gets the game id.
	 * 
	 * @return the game's id
	 */
	public GameId getGameId() {
		return gameId;
	}

	/**
	 * Set the game id.
	 * 
	 * @param gameId
	 *            requested game id.
	 */
	public void setGameId(GameId gameId) {
		this.gameId = gameId;
	}

	/**
	 * Starts the game
	 */
	public void startGame() {
		setState(GameState.RUNNING);
		setEndTime(totalTime);
	}

	/**
	 * Pauses the game
	 */
	public void pauseGame() {
		stateBeforePause = getGameState();
		setState(GameState.PAUSED);
	}

	/**
	 * Resume the game
	 */
	public void resumeGame() {
		setEndTime(remainingTime);
		setState(stateBeforePause);
	}

	/**
	 * Get the player number corresponding to your own macAddress.
	 * 
	 * @return
	 */
	public int getplayerNbr() {
		String myMac = Player.getInstance().getMac();
		int playerNbr = players.get(myMac);
		return playerNbr;
	}

	/**
	 * Get the number of players currently playing the game
	 * 
	 * @return
	 */
	public int getNumberOfPlayers() {
		return players.size();
	}

	/**
	 * Returns the results of the game
	 */
	public abstract GameResult getGameResult();

	protected GameState getGameState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}
}
