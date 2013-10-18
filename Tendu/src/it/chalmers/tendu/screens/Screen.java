package it.chalmers.tendu.screens;

import it.chalmers.tendu.controllers.InputController;

public interface Screen {

	/** all rendering goes here **/
	public abstract void render();

	/** All game logic goes here */
	public abstract void tick(InputController input);

	/**
	 * clean up goes here make sure to call super() if overriden
	 */
	public abstract void removed();

}