/*-******THIS IS OUR GAME MODEL*********/
package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;

public class GameSession {

	public MiniGame currentMiniGame;;
	private int currentLvl;
	private Difficulty difficulty;
	
	/**
	 * Map of players MAC-address (key) and their player number (value).
	 */
	private Map<String, Integer> players;
	
	/**
	 * Map of players MAC-addresses that have loaded game resources.
	 */
	private Map<String, Boolean> playerIsReady;

	@SuppressWarnings("unused")
	private GameSession() {
		// For reflection.
	}

	public GameSession(Map<String, Integer> players) {
		currentMiniGame = getNextMiniGame();
		currentLvl = 1;
		
		this.players = players;
		playerIsReady = new HashMap<String, Boolean>();
	}

	public Map<String, Integer> getPlayers() {
		return players;
	}

	public void playerWaitingToStart(String macAddress) {
		playerIsReady.put(macAddress, true);
	}

	public boolean allWaiting() {
		return (players.size() == playerIsReady.size());
	}

	public void miniGameWon() {
		currentLvl++;
	}

	public void miniGameLost() {
		currentLvl = 1;
	}

	public void setCurrentMiniGame(MiniGame miniGame) {
		currentMiniGame = miniGame;
		nextGameScreen();
	}

	public void nextGameScreen() {
		EventMessage message = new EventMessage(C.Tag.TO_SELF,
				C.Msg.CREATE_SCREEN, currentMiniGame);
		EventBus.INSTANCE.broadcast(message);
	}

	public MiniGame getNextMiniGame() {
		return getMiniGame(getNextGameId());
	
	}

	private GameId getNextGameId() {
		if (currentLvl < 5) {
			difficulty = Difficulty.ONE;
		} else if (currentLvl < 10) {
			difficulty = Difficulty.TWO;
		} else if (currentLvl < 15) {
			difficulty = Difficulty.THREE;
		} else if (currentLvl < 20) {
			difficulty = Difficulty.FOUR;
		} else {
			difficulty = Difficulty.FIVE;
		}
		return MiniGameFactory.createGameId(difficulty);
	}

	private MiniGame getMiniGame(GameId gameId) {
		int bonusTime = 0;
		Gdx.app.log("gameId", " " + gameId);
	
		if (currentMiniGame != null) {
			bonusTime = (int) currentMiniGame.getTimeLeft();
		}
	
		currentMiniGame = MiniGameFactory.createMiniGame(bonusTime, gameId,
				difficulty, players);
		return currentMiniGame;
	}
}
