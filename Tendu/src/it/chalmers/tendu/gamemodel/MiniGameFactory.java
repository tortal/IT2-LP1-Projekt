package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;

/*TODO This class shall select a random minigame with the selected difficulty
 *It shall also keep track of previously played games to avoid playing the
 *same game (on the specified difficulty) more than once;
 */
public class MiniGameFactory {

	public static MiniGame createMiniGame(int bonusTime, Difficulty difficulty) {
		MiniGame miniGame = null;
		switch (difficulty) {
		case ONE:
			miniGame = new NumberGame(bonusTime, Difficulty.ONE);
		case TWO:
//			miniGame = new ShapesGame(addTime, Difficulty.TWO);
			miniGame = new NumberGame(bonusTime, Difficulty.TWO);
		case THREE:
			break;
		case FOUR:
			break;
		case FIVE:
			break;
		}
		
		return miniGame;

	}
}
