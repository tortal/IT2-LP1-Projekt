package it.chalmers.tendu;

import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.defaults.GameState;
import it.chalmers.tendu.gamemodel.NumberGame;
import it.chalmers.tendu.screens.GameScreen;
import it.chalmers.tendu.screens.NumberGameScreen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class Tendu implements ApplicationListener {
	private GameScreen screen;
	private float accum = 0;
	private InputController input;

	@Override
	public void create() {
		setScreen(new NumberGameScreen(this, new NumberGame(0,
				Constants.Difficulty.ONE)));
		input = new InputController();
		Gdx.input.setInputProcessor(input);
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

	public void miniGameFinished(GameState state) {
		if (state == GameState.WON) {
			// vi vann, gör något
		} else if (state == GameState.LOST) {
			// vi förlorade, gör något
		}
	}

	public void setScreen(GameScreen newScreen) {
		if (screen != null) {
			screen.removed();
		}
		screen = newScreen;

	}
}
