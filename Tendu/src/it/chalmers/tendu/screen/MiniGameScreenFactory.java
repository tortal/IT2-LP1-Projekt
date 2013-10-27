package it.chalmers.tendu.screen;

import it.chalmers.tendu.gamemodel.MiniGame;

import com.badlogic.gdx.Gdx;

/**
 * Factory class for creating screens corresponding to a specified {@link MiniGame}
 */
public class MiniGameScreenFactory {

	/**
	 * 
	 * @param tendu main tendu object
	 * @param miniGame the miniGame which screen you want
	 * @return
	 */
	public static Screen createMiniGameScreen(MiniGame miniGame) {
		Gdx.app.log("MiniGameScreenFactory",
				"gameId = " + miniGame.getGameId());
		switch (miniGame.getGameId()) {
		case NUMBER_GAME:
			return new NumberGameScreen(miniGame);
		case SHAPE_GAME:
			return new ShapeGameScreen(miniGame);
		}
		return null;
	}

}
