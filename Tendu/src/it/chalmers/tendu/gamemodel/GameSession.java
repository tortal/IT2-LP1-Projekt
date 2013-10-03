/*-******THIS IS OUR MAIN MODEL*********/
package it.chalmers.tendu.gamemodel;


import it.chalmers.tendu.defaults.Constants.Difficulty;

import java.util.List;
import java.util.Map;

public class GameSession {

	private MiniGame currentMinigame;
	private int currentLvl;
	private Difficulty difficulty;
	private List<Player> players;
	private Map<Player, Integer> playerNbr;	

	public GameSession() {
	}
	
	public MiniGame getNextGame(){
		int bonusTime = currentMinigame.getTimeLeft();
		if (currentLvl < 5){
			difficulty = Difficulty.ONE;
		}else if (currentLvl <10){
			difficulty = Difficulty.TWO;
		}
		currentMinigame = MiniGameFactory.createMiniGame(bonusTime, difficulty);
		return currentMinigame;
	}
		
}
