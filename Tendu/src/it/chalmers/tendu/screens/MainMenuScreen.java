package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.defaults.TextLabels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class MainMenuScreen implements Screen {

	private BitmapFont font;
	private final Tendu tendu;
	private TextWidget hostGame;
	private TextWidget joinGame;
	private TextWidget testStuff;

	private boolean dark;

	public MainMenuScreen(Tendu tendu) {

		this.tendu = tendu;
		font = new BitmapFont(Gdx.files.internal("fonts/mainMenuTendu.fnt"),
				Gdx.files.internal("fonts/mainMenuTendu.png"), false);

		hostGame = new TextWidget(TextLabels.HOST, new Vector2(90, 270),
				Constants.MENU_FONT_COLOR);
		joinGame = new TextWidget(TextLabels.JOIN, new Vector2(90, 150),
				Constants.MENU_FONT_COLOR);
		testStuff = new TextWidget("test stuff", new Vector2(785, 680),
				Constants.MENU_FONT_COLOR);

		dark = true;
	}

	public void tick(InputController input) {
		// process user input
		if (input.isTouchedUp()) {
			if (hostGame.collided(input.getCoordinates())) {
				tendu.setScreen(new LobbyScreen(tendu, true));
			}

			if (joinGame.collided(input.getCoordinates())) {
				tendu.setScreen(new LobbyScreen(tendu, false));
			}

			if (testStuff.collided(input.getCoordinates())) {
				tendu.getNetworkHandler().testStuff();
			}

			if (input.x < 100 && input.y > Constants.SCREEN_HEIGHT - 100) {

				if (dark == true) {
					Constants.BG_RED = 0.8f;
					Constants.BG_GREEN = 0.8f;
					Constants.BG_BLUE = 0.8f;
					Constants.MENU_FONT_COLOR = Color.WHITE;
					Constants.MENU_FONT_COLOR_PRESSED = Color.GRAY;
					dark = false;
				} else {
					Constants.BG_RED = 0f;
					Constants.BG_GREEN = 0f;
					Constants.BG_BLUE = 0f;
					Constants.MENU_FONT_COLOR = Color.WHITE;
					Constants.MENU_FONT_COLOR_PRESSED = Color.LIGHT_GRAY;
					dark = true;
				}
			}

			hostGame.setColor(Constants.MENU_FONT_COLOR);
			joinGame.setColor(Constants.MENU_FONT_COLOR);
			testStuff.setColor(Constants.MENU_FONT_COLOR);

		} else if (input.isTouchedDown()) {
			if (hostGame.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				hostGame.setColor(Constants.MENU_FONT_COLOR_PRESSED);
			}

			if (joinGame.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				joinGame.setColor(Constants.MENU_FONT_COLOR_PRESSED);
			}

			if (testStuff.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				testStuff.setColor(Constants.MENU_FONT_COLOR_PRESSED);
			}
		}
	}

	@Override
	public void render() {
		hostGame.draw(tendu.spriteBatch, font);
		joinGame.draw(tendu.spriteBatch, font);
		// testStuff.draw(tendu.spriteBatch, font);

	}

	@Override
	public void removed() {
		font.dispose();
	}

}
