/*-******THIS IS OUR MAIN MODEL*********/
package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.settings.GameRoundSettings;
import it.chalmers.tendu.settings.Player;

import java.util.ArrayList;

public class GameSession {

	private final GameRoundSettings gameRoundSettings;
	private int currentLevel;
	private ArrayList<Player> players;

	public GameSession(GameRoundSettings gameRoundSettings) {
		this.gameRoundSettings = gameRoundSettings;
	}
}
