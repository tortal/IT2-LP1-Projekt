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

	private MiniGame getMiniGame(GameId gameId) {
		long extraTime = 0;

		if (sessionResult != null && sessionResult.gamesPlayed() > 0) {
			extraTime = sessionResult.getLastResult().getRemainingTime();
		}

		return MiniGameFactory.createMiniGame(extraTime, gameId, difficulty,
				players);

	}

	public MiniGame getNextMiniGame() {
		return getMiniGame(getNextGameId());

	}

	public void setCurrentMiniGame(MiniGame miniGame) {
		currentMiniGame = miniGame;
	}

	public Map<String, Integer> getPlayers() {
		return players;
	}

	public void playerWaitingToStart(String macAddress) {
		playersWaitingToStart.put(macAddress, true);
	}

	public boolean allWaiting() {
		return (players.size() == playersWaitingToStart.size());
	}

	public void nextScreen() {
		EventMessage message = new EventMessage(C.Tag.TO_SELF,
				C.Msg.CREATE_SCREEN, currentMiniGame);
		EventBus.INSTANCE.broadcast(message);
	}

	public void interimScreen() {
		EventMessage message = new EventMessage(C.Tag.TO_SELF,
				C.Msg.SHOW_INTERIM_SCREEN, sessionResult);
		EventBus.INSTANCE.broadcast(message);
	}

	public void gameOverScreen() {
		EventMessage message = new EventMessage(C.Tag.TO_SELF,
				C.Msg.SHOW_GAME_OVER_SCREEN, sessionResult);
		EventBus.INSTANCE.broadcast(message);
		sessionResult.clear();
		completedLvls = (sessionResult.gamesPlayed());
	}

	public void playerPlayAgainReady(String player) {
		if (!playerPlayAgainReady.contains(player)) {
			playerPlayAgainReady.add(player);
		}
	}

	public boolean arePlayersReady() {
		if (players.size() == playerPlayAgainReady.size()) {
			playerPlayAgainReady.clear();
			return true;
		} else {
			return false;
		}
	}

	public void enterResult(GameResult gameResult) {
		sessionResult.addResult(gameResult);
		completedLvls = (sessionResult.gamesPlayed());
	}
}
