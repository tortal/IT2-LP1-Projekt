/*-******THIS IS OUR MAIN MODEL*********/
package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;

public class GameSession {

	public String hostMacAddress;
	public MiniGame currentMiniGame = null;
	private int currentLvl = 1;
	private Difficulty difficulty = Difficulty.ONE;
	/**
	 * Integer = player id
	 * String = player MacAddress 
	 */
	private Map<String, Integer> players;

	public GameSession(Map<String, Integer> players, String hostMac) {
		this.players = players;
		hostMacAddress = hostMac;
	}

	public GameSession() {
		// TODO Auto-generated constructor stub
	}

	private GameId getNextGameId() {
		if (currentLvl < 5) {
			difficulty = Difficulty.ONE;
		} else if (currentLvl < 10) {
			difficulty = Difficulty.TWO;
		}
		return MiniGameFactory.createGameId(difficulty);
	}

	private MiniGame getMiniGame(GameId gameId) {
		int bonusTime = 0;
		Gdx.app.log("gameId", " " + gameId);
		
		if(currentMiniGame != null) {
			bonusTime = (int) currentMiniGame.getTimeLeft();
		}
		
		currentMiniGame = MiniGameFactory.createMiniGame(bonusTime, gameId,
				difficulty);
		return currentMiniGame;
	}
	
	public MiniGame getNextMiniGame() {
		return getMiniGame(getNextGameId());
		
	}
	
	public void setCurrentMiniGame(MiniGame miniGame) {
		currentMiniGame = miniGame;
	}
	
	public Map<String, Integer> getPlayers(){
		return players;
	}
	
	public void startGame(){
		
	}
	
	public boolean isHost() {
		if (Player.getInstance().getMac().equals(hostMacAddress)) {
			return true;
		}
		return false;
	}

	
}
