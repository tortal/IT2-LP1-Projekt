package it.chalmers.tendu.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.shapesgame.Color;
import it.chalmers.tendu.gamemodel.shapesgame.GeometricShape;
import it.chalmers.tendu.gamemodel.shapesgame.Shape;
import it.chalmers.tendu.gamemodel.shapesgame.ShapesGame;

public class ShapesGameScreen extends GameScreen {

	private ShapeRenderer shapeRenderer; // used to render vector graphics
	private ShapesGame model;

	public ShapesGameScreen(Tendu game, MiniGame model) {
		super(game, model);
		this.model = (ShapesGame) model;
		this.shapeRenderer = new ShapeRenderer();
	}

	/** All graphics are drawn here */
	@Override
	public void render() {
		super.render();
		spriteBatch.setProjectionMatrix(game.getCamera().combined);
		spriteBatch.begin();
		
	
		shapeRenderer.end();
		

		shapeRenderer.setProjectionMatrix(game.getCamera().combined);

		// Color c = model.getLock(0).getLockSequence().get(0).color;
		// GeometricShape shape =
		// model.getLock(0).getLockSequence().get(0).geometricShape;
		// switch (shape) {
		// case CIRCLE:
		// shapeRenderer.begin(ShapeType.FilledCircle);
		// shapeRenderer.filledCircle(15, 15, 15);
		// break;
		// case OCTAGON:
		//
		// }

		
		renderShapes();
		spriteBatch.end();
	}

	private void renderShapes() {
		List<Shape> shapes = model.getAllInventory().get(0);
		Gdx.app.log("size", shapes.size() + "");
		int i = 50;
		for (Shape s : shapes) {
			GeometricShape gs = s.geometricShape;
			switch (gs) {
			case CIRCLE:
				// Gdx.app.log("geometricshape", "Circle");
				shapeRenderer.begin(ShapeType.FilledCircle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledCircle(i, 250, 50);
				break;
			case OCTAGON:
				// Gdx.app.log("geometricshape", "Octagon");
				shapeRenderer.begin(ShapeType.FilledCircle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledCircle(i, 250, 40);
				break;
			case RHOMBOID:
				// Gdx.app.log("geometricshape", "Romb");
				shapeRenderer.begin(ShapeType.FilledCircle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledCircle(i, 250, 40);
				break;
			case SQUARE:
				// Gdx.app.log("geometricshape", "square");
				shapeRenderer.begin(ShapeType.FilledRectangle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledRect(i, 250, 60, 60);
				break;
			case TRIANGLE:
				// Gdx.app.log("geometricshape", "Triangle");
				shapeRenderer.begin(ShapeType.FilledTriangle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledTriangle(i, 250, i + 30, 380, i + 80, 250);
				break;
			}
			shapeRenderer.end();
			i = i + 151;

		}
	}

	private com.badlogic.gdx.graphics.Color decideColor(Shape s) {
		switch (s.color) {
		case BLUE:
			return com.badlogic.gdx.graphics.Color.BLUE;
		case GREEN:
			return com.badlogic.gdx.graphics.Color.GREEN;
		case RED:
			return com.badlogic.gdx.graphics.Color.RED;
		case YELLOW:
			return com.badlogic.gdx.graphics.Color.YELLOW;
		default:
			return com.badlogic.gdx.graphics.Color.WHITE;
		}
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
