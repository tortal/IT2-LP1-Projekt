package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.MiniGame;

public class ShapesGameScreen extends GameScreen {

	public ShapesGameScreen(Tendu game, MiniGame model) {
		super(game, model);
		// TODO Auto-generated constructor stub
	}

	/** All graphics are drawn here */
	@Override
	public void render() {
		spriteBatch.begin();

		spriteBatch.end();

	}

	/** All game logic goes here (within the model...) */
	@Override
	public void tick(InputController input) {
		// TODO Auto-generated method stub

	}

}
