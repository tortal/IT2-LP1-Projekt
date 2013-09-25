/*-******THIS IS OUR MAIN MODEL*********/
package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.settings.ISettings;
import it.chalmers.tendu.settings.Player;

public class GameRound {
	
	ISettings settings;
	int level;
	Player[] players;
	

	
	public GameRound(ISettings settings) {
		this.settings = settings;
	}
}
