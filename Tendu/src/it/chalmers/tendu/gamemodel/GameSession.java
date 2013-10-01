/*-******THIS IS OUR MAIN MODEL*********/
package it.chalmers.tendu.gamemodel;


import java.util.List;

public class GameSession {

	private final GameSessionSettings gameSessionSettings;
	private MiniGame currentMinigame;
	private List<Player> players;

	public GameSession(GameSessionSettings gameRoundSettings) {
		this.gameSessionSettings = gameRoundSettings;
	}
}
