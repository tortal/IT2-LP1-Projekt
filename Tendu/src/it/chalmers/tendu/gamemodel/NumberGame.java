package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Defaults;
import it.chalmers.tendu.defaults.GameIds;

public class NumberGame extends MiniGame {
	
	public NumberGame(int addTime, int difficulty) {
		super(addTime, difficulty, GameIds.NUMBER_GAME);
		

		switch (difficulty) {
		case Defaults.DIFFICULTY_ONE:
			this.addTime(35);
			break;
		case Defaults.DIFFICULTY_TWO:
			this.addTime(20);
			break;
		}
	}

}
