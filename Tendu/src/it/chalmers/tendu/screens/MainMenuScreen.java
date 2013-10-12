package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class MainMenuScreen implements Screen {

	private BitmapFont font;
	private final Tendu tendu;
	private OnScreenText hostGame;
	private OnScreenText joinGame;
	private OnScreenText testStuff;

	public MainMenuScreen(Tendu tendu) {

		this.tendu = tendu;
		font = new BitmapFont(Gdx.files.internal("fonts/menuFont.fnt"),
				Gdx.files.internal("fonts/menuFont.png"), false);

		hostGame = new OnScreenText("Host game", new Vector2(65 ,230));
		joinGame = new OnScreenText("Join game", new Vector2(70, 130));
		testStuff = new OnScreenText("test stuff", new Vector2(600, 450));

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
			
			hostGame.setColor(Color.WHITE);
			joinGame.setColor(Color.WHITE);
			testStuff.setColor(Color.WHITE);

		} else if (input.isTouchedDown()) {
			if (hostGame.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				hostGame.setColor(Color.LIGHT_GRAY);
			}

			if (joinGame.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				joinGame.setColor(Color.LIGHT_GRAY);
			}

			if (testStuff.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				testStuff.setColor(Color.LIGHT_GRAY);
			}
		}
	}

	@Override
	public void render() {
		hostGame.draw(tendu.spriteBatch, font);
		joinGame.draw(tendu.spriteBatch, font);
		testStuff.draw(tendu.spriteBatch, font);

	}

	@Override
	public void removed() {
		font.dispose();
	}

}
