
package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.MiniGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**Abstract screen class that can be extended by all minigame and menu screens*/
public abstract class GameScreen {
	public Tendu game; //reference to the main Tendu object
	public MiniGame model; //model of current minigame
	public SpriteBatch spriteBatch; //used for drawing of graphics

	/**
	 * @param game Tendu object that creates the screen
	 * @param model MiniGame model, set to null if not used (menu)
	 * @return a new GameScreen
	 */
	public GameScreen(Tendu game, MiniGame model) {
		this.game = game;
		this.model = model;

		spriteBatch = new SpriteBatch();
	}

	/**
	 * Call to change current screen to a new screen
	 * Only use when the current screen is finished
	 * @param screen the new screen */
	protected void setScreen(GameScreen screen) {
		game.setScreen(screen);
	}

	/** all rendering goes here **/
	public abstract void render();

	/** All game logic goes here */
	public abstract void tick(InputController input);

	/** clean up goes here
	 * 	make sure to call super() if overriden
	 */
	public void removed() {
		spriteBatch.dispose();
	}
}
