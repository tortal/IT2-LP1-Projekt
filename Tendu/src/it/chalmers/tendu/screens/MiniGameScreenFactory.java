package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.gamemodel.MiniGame;

public class MiniGameScreenFactory {

	public static GameScreen createMiniGameScreen(Tendu game, MiniGame miniModel) {
		switch (miniModel.getGameId()) {
		case NUMBER_GAME:
			return new NumberGameScreen(game, miniModel);
		case SHADES_GAME:
			return new ShapesGameScreen(game, miniModel);
		}
		return null;
	}

}
