package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;

import java.util.List;
import java.util.Map;

//TODO make none dependent of internal clock

public abstract class MiniGame {
	private Difficulty difficulty;
	private GameState state;
	private GameId gameId;
	private float remainingTime;
	private float startTime;
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
	public MiniGame(Difficulty difficulty, GameId gameId, Map<String, Integer> players) {
		this.difficulty = difficulty;
		this.setGameId(gameId);
		this.state = GameState.WAITING;
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
	 * Sets total amount of time when game starts 
	 */
	public void setStartTime(float gameTime, float extraTime) {
		startTime = gameTime+extraTime;
		setRemainingTime(startTime);
	}
	
	public float getStartTime() {
		return startTime;
	}

	/**
	 * Gets the time left.
	 * 
	 * @return the time in milliseconds.
	 */
	public float getRemainingTime() {
		return remainingTime;
	}
	
	private void setRemainingTime(float gameTime) {
		this.remainingTime = gameTime;
	}

	/**
	 * Decreases the time.
	 * 
	 * @param time
	 *            Decrease the time with requested amounts (positive number) of seconds.
	 *            
	 */
	public void decreaseTime(float time) {
		if(time < 0) return;
		setRemainingTime(getRemainingTime()-time);
	}
	
	/**
	 * Increases the time.
	 * 
	 * @param time
	 *            Increase the time with requested amounts (positive number) of seconds.
	 *            
	 */
	public void increaseTime(float time) {
		if(time < 0)
		setRemainingTime(getRemainingTime()+time);
	}
	
	

	/**
	 * Checks and returns the state of the game.
	 * 
	 * @return the game's state
	 */
	public GameState checkGameState() {
		if(getRemainingTime() <= 0) {
			gameLost();
		}
		return state;
	}

	private void gameLost() {
		state = GameState.LOST;
	}

	protected void gameWon() {
		state = GameState.WON;
	}

	public void setGameState(GameState g) {
		state = g;
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
		state = GameState.RUNNING;
	}

	/**
	 * Pauses the game
	 */
	public void pauseGame() {
	}

	/**
	 * Resume the game
	 */
	public void resumeGame() {
	}
	
	/**
	 * Get the player number corresponding to your own macAddress.
	 * @return
	 */
	public int getplayerNbr() {
		String myMac = Player.getInstance().getMac();
		int playerNbr = players.get(myMac);
		return playerNbr;
	}
	
	/**
	 * Get the number of players currently playing the game
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
}
