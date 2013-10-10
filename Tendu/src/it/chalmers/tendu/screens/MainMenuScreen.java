package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen implements Screen {

	private BitmapFont font;
	private BitmapFont smallFont;
	private Vector3 touchPos = new Vector3();
	private final Tendu tendu;

	public MainMenuScreen(Tendu tendu) {

		this.tendu = tendu;
		font = new BitmapFont();
		font.scale(5);

		smallFont = new BitmapFont();
		smallFont.scale(2);

	}

	public void tick(InputController input) {
		// process user input
		// TODO refactor and use inputclass etc. Works for now...
		if (Gdx.input.justTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			tendu.getCamera().unproject(touchPos);

			if (touchPos.x > 35 && touchPos.x < 435) {
				if (touchPos.y >= 180 && touchPos.y < 250) {
					Gdx.app.log("Testing", "Host");
					tendu.setScreen(new LobbyScreen(tendu, true));
				}

				if (touchPos.y >= 80 && touchPos.y < 150) {
					Gdx.app.log("Testing", "Join");
					tendu.setScreen(new LobbyScreen(tendu, false));
				}
			} else if (touchPos.x > 600 && touchPos.y > 390) {
				Gdx.app.log("Testing", "test test");
				tendu.getNetworkHandler().testStuff();
			}
		}
	}

	@Override
	public void render() {

		font.draw(tendu.spriteBatch, "Host game", 35, 250);
		font.draw(tendu.spriteBatch, "Join game", 47, 150);

		smallFont.draw(tendu.spriteBatch, "test stuff", 600, 450);

	}

	@Override
	public void removed() {
		font.dispose();
		smallFont.dispose();
	}

}
