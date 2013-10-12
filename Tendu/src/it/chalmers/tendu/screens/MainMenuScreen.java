package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen implements Screen {

	private BitmapFont font;
	private BitmapFont smallFont;
	private Vector3 touchPos3D = new Vector3(); //needed for camera
	private Vector2 touchPos2D = new Vector2();
	private final Tendu tendu;
	private OnScreenText hostGame;
	private OnScreenText joinGame;
	private OnScreenText testStuff;


	public MainMenuScreen(Tendu tendu) {

		this.tendu = tendu;
		// font = new BitmapFont(Gdx.files.internal("fontTest1.fnt"),
		// Gdx.files.internal("fontTest1.png"), false);
		font = new BitmapFont();
		font.scale(5);

		smallFont = new BitmapFont();
		smallFont.scale(2);
		
		hostGame = new OnScreenText("Host game", new Vector2(35, 250));
		joinGame = new OnScreenText("Join game", new Vector2(47, 150));
		testStuff = new OnScreenText("test stuff", new Vector2(600, 450));


	}

	public void tick(InputController input) {
		// process user input
		// TODO refactor and use inputclass etc. Works for now...
		if (Gdx.input.justTouched()) {
			touchPos3D.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			tendu.getCamera().unproject(touchPos3D);
			touchPos2D.set(touchPos3D.x, touchPos3D.y);
			
			if (hostGame.collided(touchPos2D)) {
					tendu.setScreen(new LobbyScreen(tendu, true));
			}

			if (joinGame.collided(touchPos2D)) {
				Gdx.app.log("Testing", "Join");
				tendu.setScreen(new LobbyScreen(tendu, false));
			}
			
			if (testStuff.collided(touchPos2D)) {
				Gdx.app.log("Testing", "test test");
				tendu.getNetworkHandler().testStuff();
			}
		}
	}

	@Override
	public void render() {
		hostGame.draw(tendu.spriteBatch, font);
		joinGame.draw(tendu.spriteBatch, font);
		testStuff.draw(tendu.spriteBatch, smallFont);

	}

	@Override
	public void removed() {
		font.dispose();
		smallFont.dispose();
	}

}
