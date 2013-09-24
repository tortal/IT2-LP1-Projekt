package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.defaults.GameState;
import it.chalmers.tendu.gamemodel.MiniGame;

import com.badlogic.gdx.Screen;

public abstract class MiniGameScreen implements Screen {
	private Tendu game;
	private MiniGame model;

	public MiniGameScreen(Tendu game, MiniGame model) {
		this.game = game;
		this.model = model;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
