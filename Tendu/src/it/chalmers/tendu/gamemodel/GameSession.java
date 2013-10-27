//TODO add comments/javadoc

package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.event.C;
import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Game Session is responsible for a whole game session, keeping track of
 * levels, current minigame, results and the players.
 */
public class GameSession {

	public MiniGame currentMiniGame = null;
	private int completedLvls;
	private Difficulty difficulty = Difficulty.ONE;
	/**
	 * Integer = player id String = player MacAddress
	 */
	private Map<String, Integer> players;
	private Map<String, Boolean> playersWaitingToStart;
	private SessionResult sessionResult;

	public List<String> playerPlayAgainReady;

	public GameSession(Map<String, Integer> players) {
		completedLvls = 0;
		this.players = players;
		playersWaitingToStart = new HashMap<String, Boolean>();
		currentMiniGame = getNextMiniGame();
		sessionResult = new SessionResult();
		playerPlayAgainReady = new ArrayList<String>();

	}

	// for reflection
	@SuppressWarnings("unused")
	private GameSession() {
	}

	/**
	 * @return a gameId with the appropriate difficulty depending on level
	 *         reached.
	 */
	private GameId getNextGameId() {
		if (completedLvls < 3) {
			difficulty = Difficulty.ONE;
		} else if (completedLvls < 6) {
			difficulty = Difficulty.TWO;
		} else if (completedLvls < 9) {
			difficulty = Difficulty.THREE;
		} else if (completedLvls < 12) {
			difficulty = Difficulty.FOUR;
		} else {
			difficulty = Difficulty.FIVE;
		}
		return MiniGameFactory.createGameId(difficulty);
	}

	/**
	 * @param gameId
	 * @return a miniGame corresponding to the gameId sent in.
	 */
	private MiniGame getMiniGame(GameId gameId) {
		long extraTime = 0;

		if (sessionResult != null && sessionResult.gamesPlayed() > 0) {
			extraTime = sessionResult.getLastResult().getRemainingTime();
		}

		return MiniGameFactory.createMiniGame(extraTime, gameId, difficulty,
				players);

	}

	/**
	 * @return a random miniGame with difficulty depending on level.
	 */
	public MiniGame getNextMiniGame() {
		return getMiniGame(getNextGameId());

	}

	/**
	 * Replaces current mini game with the one sent in.
	 * 
	 * @param miniGame
	 */
	public void setCurrentMiniGame(MiniGame miniGame) {
		currentMiniGame = miniGame;
	}

	/**
	 * @return a map with Strings representing players macaddress as keys and
	 *         integers representing player number as values.
	 */
	public Map<String, Integer> getPlayers() {
		return players;
	}

	/**
	 * Set a player as ready to start a new game.
	 * 
	 * @param macAddress
	 */
	public void playerWaitingToStart(String macAddress) {
		playersWaitingToStart.put(macAddress, true);
	}

	/**
	 * Return true if all players are ready to start a new game.
	 * 
	 * @return
	 */
	public boolean allWaiting() {
		return (players.size() == playersWaitingToStart.size());
	}

	/**
	 * Broadcasts a message to Tendu containing a miniGame, telling Tendu to
	 * switch screen to said miniGame.
	 */
	public void nextScreen() {
		EventMessage message = new EventMessage(C.Tag.TO_SELF,
				C.Msg.CREATE_SCREEN, currentMiniGame);
		EventBus.INSTANCE.broadcast(message);
	}

	/**
	 * Broadcasts a message to Tendu to show the InterimScreen with the
	 * sessionresults.
	 */
	public void interimScreen() {
		EventMessage message = new EventMessage(C.Tag.TO_SELF,
				C.Msg.SHOW_INTERIM_SCREEN, sessionResult);
		EventBus.INSTANCE.broadcast(message);
	}

	/**
	 * Broadcasts a message to Tendu to show the GameOverScreen with the
	 * sessionresults. Once that is done the current sessionResult is cleared
	 */
	public void gameOverScreen() {
		EventMessage message = new EventMessage(C.Tag.TO_SELF,
				C.Msg.SHOW_GAME_OVER_SCREEN, sessionResult);
		EventBus.INSTANCE.broadcast(message);
		sessionResult.clear();
		completedLvls = (sessionResult.gamesPlayed());
	}

	/**
	 * Add a player as ready to play again after a game over.
	 * 
	 * @param player
	 *            macaddress
	 */
	public void playerPlayAgainReady(String player) {
		if (!playerPlayAgainReady.contains(player)) {
			playerPlayAgainReady.add(player);
		}
	}

	/**
	 * @return true if all players are ready to play again after a game over.
	 */
	public boolean arePlayersReady() {
		if (players.size() == playerPlayAgainReady.size()) {
			playerPlayAgainReady.clear();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Save the results from a mini game.
	 * 
	 * @param gameResult
	 */
	public void enterResult(GameResult gameResult) {
		sessionResult.addResult(gameResult);
		completedLvls = (sessionResult.gamesPlayed());
	}
}
