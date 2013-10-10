/*-******THIS IS OUR GAME MODEL*********/
package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private List<GameResult> gameResults;

	// public GameSession(Map<String, Integer> players, String hostMac) {
	// this.players = players;
	// hostMacAddress = hostMac;
	// }
	public GameSession(Map<String, Integer> players) {
		this.players = players;
		playersWaitingToStart = new HashMap<String, Boolean>();
		currentMiniGame = getNextMiniGame();
		gameResults = new ArrayList<GameResult>();
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
		int extraTime = 0;

//		if (gameResults.size() > 0) {
//			extraTime = gameResults.get(gameResults.size()-1).getRemainingTime();
//		}

		return MiniGameFactory.createMiniGame(extraTime, gameId,
				difficulty, players);
		
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
	
	public void miniGameEnded(GameResult gameResult) {
		if(gameResult.getGameState() == GameState.WON) {
			gameResults.add(gameResult);
		} else {
			//TODO do something with the results (present to user...)
			
			//empty the results list
			gameResults.clear();
		}
		
		currentLvl = (gameResults.size()+1);
	}
}
