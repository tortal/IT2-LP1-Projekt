package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.defaults.Defaults;

/*TODO This class shall select a random minigame with the selected difficulty
 *It shall also keep track of previously played games to avoid playing the
 *same game (on the specified difficulty) more than once;
 */
public class MiniGameFactory {

	public MiniGame createMiniGame(int addTime, int difficulty) {
		switch (difficulty) {
		case Defaults.DIFFICULTY_ONE:
			return new NumberGame(addTime, Defaults.DIFFICULTY_ONE);
		case Defaults.DIFFICULTY_TWO:
			return new ShapesGame(addTime, Defaults.DIFFICULTY_ONE);
		case Defaults.DIFFICULTY_THREE:
			break;
		case Defaults.DIFFICULTY_FOUR:
			break;
		case Defaults.DIFFICULTY_FIVE:
			break;
		}
		return null;
		
	}
}
