package it.chalmers.tendu.screen;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.gamemodel.MiniGame;

import com.badlogic.gdx.Gdx;

public class MiniGameScreenFactory {

	public static Screen createMiniGameScreen(Tendu game, MiniGame miniModel) {
		Gdx.app.log("MiniGameScreenFactory",
				"gameId = " + miniModel.getGameId());
		switch (miniModel.getGameId()) {
		case NUMBER_GAME:
			return new NumberGameScreen(game, miniModel);
		case SHAPE_GAME:
			return new ShapeGameScreen(game, miniModel);
		}
		return null;
	}

}
