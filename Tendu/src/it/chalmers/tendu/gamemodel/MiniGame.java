package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.gamemodel.shapesgame.ShapeGame;

import java.util.ArrayList;
import java.util.Map;

/**
 * All games during a {@link GameSession} is a subclass of {@link MiniGame}.
 * 
 * Every playable minigame of Tendu should extend this class. See
 * {@link NumberGame} or {@link ShapeGame} for examples.
 * 
 */
public abstract class MiniGame {
	public final static String TAG = "MiniGame";

	/**
	 * Difficulty level.
	 */
	private final Difficulty difficulty;

	/**
	 * GameID Associated with this game.
	 */
	private final GameId gameId;

	/**
	 * Total time given to complete the minigame.
	 */
	private long gameTime;

	/**
	 * Timer (stop-watch) object.
	 */
	private final SimpleTimer simpleTimer;

	private boolean isStarted;

	/**
	 * Mapping of MAC as key to player ID values.
	 */
	private Map<String, Integer> players;

	/**
	 * Creates a new minigame.
	 * 
	 * @param addTime
	 *            should be greater than 0 if extra time should be added to the game
	 * @param difficulty
	 *            the game's difficulty
	 * @param gameId associated with this minigame, see {@link GameId}.
	 */
	public MiniGame(Difficulty difficulty, GameId gameId,
			Map<String, Integer> players) {
		this.difficulty = difficulty;
		this.gameId = gameId;
		this.players = players;
		simpleTimer = new SimpleTimer();

		isStarted = false;
	}

	/** No args constructor for reflection */
	protected MiniGame() {
		simpleTimer = null;
		difficulty = null;
		gameId = null;
	}

	public void startGame() {
		isStarted = true;
	}

	public boolean hasStarted() {
		// if(startTimer.isDone()) {
		// return true;
		// }

		return isStarted;
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
		return simpleTimer.getRemainingTime();
	}

	/**
	 * Call if host pushed new model
	 */
	public void reInit() {
		simpleTimer.restart(simpleTimer.getRemainingTime());
	}

	/**
	 * @param time
	 *            the change in milliseconds, can be positive or negative;
	 */
	public void changeTime(long time) {
		simpleTimer.change(time);
	}

	public long getGameTime() {
		return gameTime;
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
	 * Gets the game id.
	 * 
	 * @return the game's id
	 */
	public GameId getGameId() {
		return gameId;
	}

	public void startGameTimer() {
		simpleTimer.start(gameTime);
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

	public ArrayList<Integer> getOtherPlayerNumbers() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < getNumberOfPlayers(); i++) {
			if (i != getplayerNbr())
				list.add(new Integer(i));
		}

		return list;
	}

	/**
	 * Get the number of players currently playing the game
	 * 
	 * @return
	 */
	public int getNumberOfPlayers() {
		return players.size();
	}

	public boolean timerIsDone() {

		return simpleTimer.isDone();
	}

	/**
	 * Returns the results of the game
	 */
	public abstract GameResult getGameResult();

	public abstract GameState checkGameState();

	public void stopTimer() {
		simpleTimer.stop();
	}
}
