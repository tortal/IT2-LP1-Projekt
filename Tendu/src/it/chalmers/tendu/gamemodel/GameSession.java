/*-******THIS IS OUR MAIN MODEL*********/
package it.chalmers.tendu.gamemodel;


import java.util.List;

public class GameSession {

	private final GameRoundSettings gameSessionSettings;
	private MiniGame currentMinigame;
	private List<Player> players;

	public GameSession(GameRoundSettings gameRoundSettings) {
		this.gameSessionSettings = gameRoundSettings;
	}
}
