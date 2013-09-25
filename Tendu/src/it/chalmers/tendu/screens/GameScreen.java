package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.gamemodel.MiniGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public abstract class GameScreen {
	private Tendu game;
	private MiniGame model;
	public SpriteBatch spriteBatch;

	public GameScreen(Tendu game, MiniGame model) {
		this.game = game;
		this.model = model;
		
		Matrix4 projection = new Matrix4();
		projection.setToOrtho(0, Constants.SCREEN_HEIGHT, Constants.SCREEN_WIDTH, 0, -1, 1);
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(projection);
	}
	
	protected void setScreen (GameScreen screen) {
		game.setScreen(screen);
	}
	
	/**all rendering goes here**/
	public abstract void render();
	
	/**All game logic goes here (within the model...)*/
	public abstract void tick(InputController input);
	
	public void removed () {
		spriteBatch.dispose();
	}

}
