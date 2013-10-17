package it.chalmers.tendu.defaults;

import com.badlogic.gdx.graphics.Color;

public class PlayerColors {

	public static Color getPlayerColor(int player) {
		if (player == 0) {
			return Constants.PLAYER_0_COLOR;
		} else if (player == 1) {
			return Constants.PLAYER_1_COLOR;
		} else if (player == 2) {
			return Constants.PLAYER_2_COLOR;
		}

		return Constants.PLAYER_3_COLOR;

	}

}
