package it.chalmers.tendu.defaults;

import com.badlogic.gdx.graphics.Color;

/**
 * Convenience method to return corresponding libgdx color.
 *
 */
public class PlayerColors {

	public static Color getPlayerColor(int player) {
		if (player == 0) {
			return Constants.PLAYER_0_COLOR;
		} else if (player == 1) {
			return Constants.PLAYER_1_COLOR;
		} else if (player == 2) {
			return Constants.PLAYER_2_COLOR;
		} else if (player == 3) {
			return Constants.PLAYER_3_COLOR;
		}
		
		return null;

	}

}
