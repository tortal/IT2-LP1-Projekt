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
		this.model=(ShapesGame)model;
		this.shapeRenderer = new ShapeRenderer();
	}

	/** All graphics are drawn here */
	@Override
	public void render() {
		super.render();
		spriteBatch.setProjectionMatrix(game.getCamera().combined);
		spriteBatch.begin();

//		Color c = model.getLock(0).getLockSequence().get(0).color;
//		GeometricShape shape = model.getLock(0).getLockSequence().get(0).geometricShape;
//		switch (shape) {
//		case CIRCLE:
//			shapeRenderer.begin(ShapeType.FilledCircle);
//			shapeRenderer.filledCircle(15, 15, 15);
//			break;
//		case OCTAGON:
//
//		}

		if(model == null){
			Gdx.app.log("null", "null");
		}
		List<Shape> shapes = model.getAllInventory().get(0);
		Gdx.app.log("size", shapes.size() + "");
		int i=350;
		for (Shape s : shapes) {
			GeometricShape gs = s.geometricShape;
			switch (gs) {
			case CIRCLE:
				//Gdx.app.log("geometricshape", "Circle");
				shapeRenderer.begin(ShapeType.FilledCircle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledCircle(i, 250, 50);
				break;
			case OCTAGON:
				//Gdx.app.log("geometricshape", "Octagon");
				shapeRenderer.begin(ShapeType.FilledCircle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledCircle(i, 250, 40);
				break;
			case RHOMBOID:
				//Gdx.app.log("geometricshape", "Romb");
				shapeRenderer.begin(ShapeType.FilledCircle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledCircle(i, 250, 40);
				break;
			case SQUARE:
				//Gdx.app.log("geometricshape", "square");
				shapeRenderer.begin(ShapeType.FilledRectangle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledRect(i, 250, 60, 60);
				break;
			case TRIANGLE:
				//Gdx.app.log("geometricshape", "Triangle");
				shapeRenderer.begin(ShapeType.FilledTriangle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledTriangle(i, 250, i+30, 380, i+80, 250);
				break;
			}
			shapeRenderer.end();
			i=i+350;

		}

		spriteBatch.end();
	}
	
	private com.badlogic.gdx.graphics.Color decideColor(Shape s){
		return com.badlogic.gdx.graphics.Color.RED;
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
