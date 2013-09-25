package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.gamemodel.MiniGame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public abstract class GameScreen {
	private Tendu game;
	private MiniGame model;
    public OrthographicCamera camera;
	public SpriteBatch spriteBatch;

	public GameScreen(Tendu game, MiniGame model) {
		this.game = game;
		this.model = model;		
		
		spriteBatch = new SpriteBatch();
		
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
	}

	protected void setScreen(GameScreen screen) {
		game.setScreen(screen);
	}

	/** all rendering goes here **/
	public abstract void render();

	/** All game logic goes here (within the model...) */
	public abstract void tick(InputController input);

	public void removed() {
		spriteBatch.dispose();
	}

}
