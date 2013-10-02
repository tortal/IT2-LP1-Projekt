package it.chalmers.tendu.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

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
	}

	/** All game logic goes here (within the model...) */
	@Override
	public void tick(InputController input) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removed() {
		super.removed();
	}

}
