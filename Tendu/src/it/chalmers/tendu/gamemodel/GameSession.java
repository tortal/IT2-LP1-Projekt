/*-******THIS IS OUR MAIN MODEL*********/
package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;

public class GameSession {

	public MiniGame currentMinigame = null;
	private int currentLvl = 1;
	private Difficulty difficulty = Difficulty.ONE;
	// private List<Player> players;
	// private Map<Player, Integer> playerNbr;
	private Map<Integer, String> players;

	public GameSession(Map players) {
		this.players = players;
	}

	public GameSession() {
		// TODO Auto-generated constructor stub
	}

	public GameId getNextGameId() {
		if (currentLvl < 5) {
			difficulty = Difficulty.ONE;
		} else if (currentLvl < 10) {
			difficulty = Difficulty.TWO;
		}
		return MiniGameFactory.createGameId(difficulty);
	}

	public MiniGame getMiniGame(GameId gameId) {
		int bonusTime = 0;
		Gdx.app.log("gameId", " " + gameId);
		
		if(currentMinigame != null) {
			bonusTime = (int) currentMinigame.getTimeLeft();
		}
		
		currentMinigame = MiniGameFactory.createMiniGame(bonusTime, gameId,
				difficulty);
		return currentMinigame;
	}

	
}
