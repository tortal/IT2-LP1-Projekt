package it.chalmers.tendu.screens;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.shapesgame.Color;
import it.chalmers.tendu.gamemodel.shapesgame.GeometricShape;
import it.chalmers.tendu.gamemodel.shapesgame.ShapesGame;

public class ShapesGameScreen extends GameScreen {

	private ShapesGame model; // Model for current minigame (ShapesGame)
	private ShapeRenderer shapeRenderer; // used to render vector graphics

	public ShapesGameScreen(Tendu game, MiniGame model) {
		super(game, model);
		model = (ShapesGame) model;
	}

	/** All graphics are drawn here */
	@Override
	public void render() {
		spriteBatch.setProjectionMatrix(game.getCamera().combined);
		spriteBatch.begin();
		
		Color c=model.getLock(0).getLockSequence().get(0).color;
		GeometricShape shape=model.getLock(0).getLockSequence().get(0)
				.geometricShape;
		switch(shape){
			case CIRCLE:	
				shapeRenderer.begin(ShapeType.FilledCircle);
				shapeRenderer.filledCircle(200, 300, 40);
				break;
			case OCTAGON:
				shapeRenderer.begin(ShapeType.FilledRectangle);
				shapeRenderer.filledRect(200, 300, 300, 400);
				/**
				 * TODO
				 */
				break;
			case SQUARE:
				shapeRenderer.begin(ShapeType.FilledRectangle);
				shapeRenderer.filledRect(200, 300, 300, 400);
				break;
			case RHOMBOID:
				shapeRenderer.begin(ShapeType.FilledRectangle);
				shapeRenderer.filledRect(200, 300, 300, 400);
				/**
				 * TODO
				 */
				break;
			case TRIANGLE:
				shapeRenderer.begin(ShapeType.FilledTriangle);
				shapeRenderer.filledTriangle(10, 10, 30, 30, 50, 50);
				break;
		}
		shapeRenderer.end();
		

		spriteBatch.end();
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
