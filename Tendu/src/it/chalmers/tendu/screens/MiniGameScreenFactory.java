package it.chalmers.tendu.screens;

import com.badlogic.gdx.Gdx;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.gamemodel.MiniGame;

public class MiniGameScreenFactory {

	public static Screen createMiniGameScreen(Tendu game, MiniGame miniModel) {
		Gdx.app.log("MiniGameScreenFactory", "gameId = " + miniModel.getGameId());
		switch (miniModel.getGameId()) {
		case NUMBER_GAME:
			return new NumberGameScreen(game, miniModel);
		case SHAPES_GAME:
			return new ShapesGameScreen(game, miniModel);
		}
		return null;
	}

}
