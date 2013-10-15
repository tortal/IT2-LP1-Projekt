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
	private TextWidget hostGame;
	private TextWidget joinGame;
	private TextWidget testStuff;

	public MainMenuScreen(Tendu tendu) {

		this.tendu = tendu;
		font = new BitmapFont(Gdx.files.internal("fonts/mainMenuTendu.fnt"),
				Gdx.files.internal("fonts/mainMenuTendu.png"), false);

		hostGame = new TextWidget("Host game", new Vector2(90 ,270));
		joinGame = new TextWidget("Join game", new Vector2(90, 150));
		testStuff = new TextWidget("test stuff", new Vector2(785, 680));

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
		//testStuff.draw(tendu.spriteBatch, font);

	}

	@Override
	public void removed() {
		font.dispose();
	}

}
