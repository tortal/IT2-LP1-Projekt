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

	// public String hostMacAddress;
	public MiniGame currentMiniGame = null;
	private int currentLvl = 1;
	private Difficulty difficulty = Difficulty.ONE;
	/**
	 * Integer = player id String = player MacAddress
	 */
	private Map<String, Integer> players;
	private Map<String, Boolean> playersWaitingToStart;

	// public GameSession(Map<String, Integer> players, String hostMac) {
	// this.players = players;
	// hostMacAddress = hostMac;
	// }
	public GameSession(Map<String, Integer> players) {
		this.players = players;
		playersWaitingToStart = new HashMap<String, Boolean>();
		currentMiniGame = getNextMiniGame();
	}

	private GameSession() {
		// TODO Auto-generated constructor stub
	}

	private GameId getNextGameId() {
		if (currentLvl < 5) {
			difficulty = Difficulty.ONE;
		} // TODO add more lvls
		// } else if (currentLvl < 10) {
		// difficulty = Difficulty.TWO;
		// }
		else {
			difficulty = Difficulty.TWO;
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

	public MiniGame getNextMiniGame() {
		return getMiniGame(getNextGameId());

	}

	public void setCurrentMiniGame(MiniGame miniGame) {
		currentMiniGame = miniGame;
		nextScreen();
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

	public void miniGameWon() {
		currentLvl++;
	}

	public void miniGameLost() {
		currentLvl = 1;
	}
}
