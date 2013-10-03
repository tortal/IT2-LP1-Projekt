/*-******THIS IS OUR MAIN MODEL*********/
package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;

import java.util.List;
import java.util.Map;

public class GameSession {

	public MiniGame currentMinigame;
	private int currentLvl;
	private Difficulty difficulty;
	// private List<Player> players;
	// private Map<Player, Integer> playerNbr;
	private Map<Integer, String> players;

	public GameSession(Map players) {
		this.players = players;
	}

	public GameId getNextGame() {
		if (currentLvl < 5) {
			difficulty = Difficulty.ONE;
		} else if (currentLvl < 10) {
			difficulty = Difficulty.TWO;
		}
		return MiniGameFactory.createGameId(difficulty);
	}

	public MiniGame getGame(GameId gameId) {
		int bonusTime = currentMinigame.getTimeLeft();
		currentMinigame = MiniGameFactory.createMiniGame(bonusTime, gameId,
				difficulty);
		return currentMinigame;
	}

	
}
