package it.chalmers.tendu.defaults;

import com.badlogic.gdx.graphics.Color;

public class Constants {

	public final static int SCREEN_WIDTH = 1280;
	public final static int SCREEN_HEIGHT = 720;

	public static enum Difficulty {
		ONE, TWO, THREE, FOUR, FIVE;

	}

	public static final String APP_NAME = "Tendu";
	public static final String SERVER_NAME = " - " + APP_NAME + 'S';
	public static final String CLIENT_NAME = " - " + APP_NAME + 'C';
	
	public static final Color MENU_FONT_COLOR = Color.WHITE;
	public static final Color MENU_FONT_COLOR_PRESSED = Color.LIGHT_GRAY;
	
	public static final Color PLAYER_0_COLOR = Color.BLUE;
	public static final Color PLAYER_1_COLOR = Color.YELLOW;
	public static final Color PLAYER_2_COLOR = Color.GREEN;
	public static final Color PLAYER_3_COLOR = Color.RED;
}
