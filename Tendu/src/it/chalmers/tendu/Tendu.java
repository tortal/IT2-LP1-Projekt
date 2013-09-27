package it.chalmers.tendu;

import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.gamemodel.NumberGame;
import it.chalmers.tendu.network.INetworkHandler;
import it.chalmers.tendu.network.NetworkState;
import it.chalmers.tendu.screens.GameScreen;
import it.chalmers.tendu.screens.MainMenuScreen;
import it.chalmers.tendu.screens.NumberGameScreen;
import it.chalmers.tendu.screens.ShapesGameScreen;

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
		setNetworkHandler(netCom);
	}

	@Override
	public void create() {
		//setScreenByNetworkState();
		setScreen(new MainMenuScreen(this, null));
//		setScreen(new NumberGameScreen(this, new NumberGame(0, Constants.Difficulty.ONE)));
		input = new InputController();
		Gdx.input.setInputProcessor(input);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.SCREEN_WIDTH,
				Constants.SCREEN_HEIGHT);
	}

	@Override
	public void dispose() {
		networkHandler.destroy();
		
	}
	
	@Override
	public void render() {
		setScreenByNetworkState();
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

	// Tror inte att vi behver denna metod
	// public void miniGameFinished(GameState state) {
	// if (state == GameState.WON) {
	// // vi vann, gr ngot
	// } else if (state == GameState.LOST) {
	// // vi frlorade, gr ngot
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

	public INetworkHandler getNetworkHandler() {
		return networkHandler;
	}

	public void setNetworkHandler(INetworkHandler networkHandler) {
		this.networkHandler = networkHandler;
	}
	
	private void setScreenByNetworkState() {
		int state = networkHandler.pollNetworkState();
		// Change screen depending on network state (Maybe not the proper place for this)
		switch (state) {
		case NetworkState.STATE_NONE: 
			if (screen instanceof NumberGameScreen) {
				setScreen(new MainMenuScreen(this, null));
			}
			break;
		case NetworkState.STATE_CONNECTED: 
			if (screen instanceof MainMenuScreen) {
				setScreen(new NumberGameScreen(this, new NumberGame(0, Constants.Difficulty.ONE)));
		}
			break;
		}
		
					
	}
}
