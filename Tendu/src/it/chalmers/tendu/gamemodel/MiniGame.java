package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;

import java.util.Map;

//TODO make none dependent of internal clock

public abstract class MiniGame {
	private Difficulty difficulty;
	private GameState state;
	private GameId gameId;
	private long gameTime;
	private SimpleTimer timer;
	private GameState stateBeforePause;

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
		timer = new SimpleTimer();
	}

	/** No args constructor for reflection use */
	protected MiniGame() {
	}

	public void setGameTime(long gameTime, long extraTime) {
		this.gameTime = gameTime + extraTime;
	}

	/**
	 * Gets the remaining time
	 * 
	 * @return time left in millis seconds
	 */
	public long getRemainingTime() {
		if (timer.isDone()) {
			gameLost();
		}

		return timer.getRemainingTime();
	}

	/**
	 * Call if host pushed new model
	 */
	public void reInit() {
		timer.restart(timer.getRemainingTime());
	}

	/**
	 * @param time
	 *            the change in milliseconds, can be positive or negative;
	 */
	public void changeTime(long time) {
		timer.change(time);
	}

	public long getGameTime() {
		return gameTime;
	}

	/**
	 * Integer = player id String = player MacAddress
	 */
	private Map<String, Integer> players;

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
		timer.pause();
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
	}

	public void startGameTimer() {
		timer.start(gameTime);
	}

	/**
	 * Pauses the game
	 */
	public void pauseGame() {
		timer.pause();
		stateBeforePause = getGameState();
		setState(GameState.PAUSED);
	}

	/**
	 * Resume the game
	 */
	public void resumeGame() {
		timer.resume();
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
