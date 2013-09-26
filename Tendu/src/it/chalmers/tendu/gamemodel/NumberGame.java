package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.defaults.GameIds;

import com.badlogic.gdx.Gdx;

public class NumberGame extends MiniGame {

	public NumberGame(int addTime, Difficulty difficulty) {
		super(addTime, difficulty, GameIds.NUMBER_GAME);

		switch (difficulty) {
		case ONE:
			this.addTime(35);
			break;
		case TWO:
			this.addTime(20);
			break;
		default:
			// TODO:
			Gdx.app.debug("NumberGame Class", "Fix this switch case");
		}

	}

}
