package it.chalmers.tendu;

import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.network.INetworkHandler;
import it.chalmers.tendu.screens.GameScreen;
import it.chalmers.tendu.screens.MainMenuScreen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Tendu implements ApplicationListener {
	private GameScreen screen;
	private float accum = 0;
	private InputController input;
	private OrthographicCamera camera;
	
	private INetworkHandler networkHandler;
	
	public Tendu(INetworkHandler netCom) {
		networkHandler = netCom;
	}

	@Override
	public void create() {
		setScreen(new MainMenuScreen(this, null));
		input = new InputController();
		Gdx.input.setInputProcessor(input);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.SCREEN_WIDTH,
				Constants.SCREEN_HEIGHT);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		accum += Gdx.graphics.getDeltaTime();
		while (accum > 1.0f / 60.0f) {
			screen.tick(input);
			input.tick();
			accum -= 1.0f / 60.0f;
		}

		camera.update();
		screen.render();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	// Tror inte att vi behöver denna metod
	// public void miniGameFinished(GameState state) {
	// if (state == GameState.WON) {
	// // vi vann, gör något
	// } else if (state == GameState.LOST) {
	// // vi förlorade, gör något
	// }
	// }

	public void setScreen(GameScreen newScreen) {
		if (screen != null) {
			screen.removed();
		}
		screen = newScreen;

	}

	public OrthographicCamera getCamera() {
		return camera;
	}
}
