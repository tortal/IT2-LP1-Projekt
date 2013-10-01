/*-******THIS IS OUR MAIN MODEL*********/
package it.chalmers.tendu.gamemodel;


import java.util.List;
import java.util.Map;

public class GameSession {

	private final GameRoundSettings gameSessionSettings;
	private MiniGame currentMinigame;
	private List<Player> players;
	private Map<Player, Integer> playerNbr;
	

	public GameSession(GameRoundSettings gameRoundSettings) {
		this.gameSessionSettings = gameRoundSettings;
	}
}
