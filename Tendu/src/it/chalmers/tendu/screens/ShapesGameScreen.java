package it.chalmers.tendu.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.shapesgame.Color;
import it.chalmers.tendu.gamemodel.shapesgame.GeometricShape;
import it.chalmers.tendu.gamemodel.shapesgame.Shape;
import it.chalmers.tendu.gamemodel.shapesgame.ShapesGame;

public class ShapesGameScreen extends GameScreen {

	private Rectangle rect;

	private List<Rectangle> positions;

	private ShapeRenderer shapeRenderer; // used to render vector graphics
	private ShapesGame model;

	public ShapesGameScreen(Tendu game, MiniGame model) {
		super(game, model);
		this.model = (ShapesGame) model;
		this.shapeRenderer = new ShapeRenderer();
		rect = new Rectangle();
		rect.x = Constants.SCREEN_WIDTH / 2;
		rect.y = Constants.SCREEN_HEIGHT / 2;
		rect.height = 100;
		rect.width = 100;

		// Creates logical representations of the shapes
		positions = new ArrayList<Rectangle>();
		int i = 0;
		for (Shape s : this.model.getAllInventory().get(0)) {
			positions.add(new Rectangle(i + 200, 200, 100, 100));
			i = i + 200;
		}

	}

	/** All graphics are drawn here */
	@Override
	public void render() {
		super.render();
		spriteBatch.setProjectionMatrix(game.getCamera().combined);
		spriteBatch.begin();

		shapeRenderer.setProjectionMatrix(game.getCamera().combined);

		Color c = model.getLock(0).getLockSequence().get(0).color;
		GeometricShape shape = model.getLock(0).getLockSequence().get(0).geometricShape;
		switch (shape) {
		case CIRCLE:
			shapeRenderer.begin(ShapeType.FilledCircle);
			shapeRenderer.filledCircle(15, 15, 15);
			break;
		case OCTAGON:

		}

		// if (Gdx.input.isTouched()) {
		// Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),
		// 0);
		//
		// game.getCamera().unproject(touchPos);
		// Gdx.app.log("Rect:", rect.toString() + "");
		// Gdx.app.log("XY: ", "" + touchPos.x + "+" +touchPos.y);
		// Gdx.app.log("contains xy",
		// "" + rect.contains(touchPos.x, touchPos.y));
		// if (touchPos.x - rect.x <= 100 &&
		// touchPos.x - rect.x >= -80 &&
		// touchPos.y - rect.y <= 100 &&
		// touchPos.y - rect.y >= -80) {
		//
		//
		// rect.x = touchPos.x;
		// rect.y = touchPos.y;
		//
		// }
		// Gdx.app.log("X+Y", rect.getX() + "+" + rect.getY());
		// }

		renderShapes();
		renderLock();
		// shapeRenderer.begin(ShapeType.FilledRectangle);
		// shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.GREEN);
		// shapeRenderer.filledRect(rect.x-50, rect.y- 50, 100, 100);
		// shapeRenderer.end();

		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),
					0);
			game.getCamera().unproject(touchPos);
			for (int i = 0; i < positions.size(); i++) {
				if (touchPos.x - positions.get(i).x <= 100
						&& touchPos.x - positions.get(i).x >= -80
						&& touchPos.y - positions.get(i).y <= 100
						&& touchPos.y - positions.get(i).y >= -80) {
					positions.get(i).x = touchPos.x;
					positions.get(i).y = touchPos.y;
					break;
				}
			}
		}
		

		spriteBatch.end();
	}

	private void renderShapes() {
		List<Shape> shapes = model.getAllInventory().get(0);
		// Gdx.app.log("size", shapes.size() + "");
		int i = 0;
		for (Shape s : shapes) {
			GeometricShape gs = s.geometricShape;
			switch (gs) {
			case CIRCLE:
				// Gdx.app.log("geometricshape", "Circle");
				shapeRenderer.begin(ShapeType.FilledCircle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledCircle(positions.get(i).x - 50,
						positions.get(i).y - 50, 50);
				break;
			case OCTAGON:
				// Gdx.app.log("geometricshape", "Octagon");
				shapeRenderer.begin(ShapeType.FilledCircle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledCircle(positions.get(i).x - 50,
						positions.get(i).y - 50, 50);
				break;
			case RHOMBOID:
				// Gdx.app.log("geometricshape", "Romb");
				shapeRenderer.begin(ShapeType.FilledCircle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledCircle(positions.get(i).x - 50,
						positions.get(i).y - 50, 50);
				break;
			case SQUARE:
				// Gdx.app.log("geometricshape", "square");
				shapeRenderer.begin(ShapeType.FilledRectangle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledRect(positions.get(i).x - 50,
						positions.get(i).y - 50, 100, 100);
				break;
			case TRIANGLE:
				// Gdx.app.log("geometricshape", "Triangle");
				shapeRenderer.begin(ShapeType.FilledTriangle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledTriangle(positions.get(i).x - 50,
						positions.get(i).y - 50, positions.get(i).x,
						positions.get(i).y + 50, positions.get(i).x + 50,
						positions.get(i).y - 50);
				break;
			}
			shapeRenderer.end();
			i++;

		}
	}

	private void renderLock() {
		List<Shape> shapes = model.getLock(0).getLockSequence();
		int i=0;
		for (Shape s : shapes) {
			GeometricShape gs = s.geometricShape;
			switch (gs) {
			case CIRCLE:
				// Gdx.app.log("geometricshape", "Circle");
				shapeRenderer.begin(ShapeType.FilledCircle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledCircle(250 + i, 400, 50);
				break;
			case OCTAGON:
				// Gdx.app.log("geometricshape", "Octagon");
				shapeRenderer.begin(ShapeType.FilledCircle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledCircle(250 + i, 400, 50);
				break;
			case RHOMBOID:
				// Gdx.app.log("geometricshape", "Romb");
				shapeRenderer.begin(ShapeType.FilledCircle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledCircle(250 + i, 400, 50);
				break;
			case SQUARE:
				// Gdx.app.log("geometricshape", "square");
				shapeRenderer.begin(ShapeType.FilledRectangle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledRect(200 + i, 350, 100, 100);
				break;
			case TRIANGLE:
				// Gdx.app.log("geometricshape", "Triangle");
				shapeRenderer.begin(ShapeType.FilledTriangle);
				shapeRenderer.setColor(decideColor(s));
				shapeRenderer.filledTriangle(200 + i, 350, 200 + i + 50,
						350 + 100, 200 + i + 100, 350);
				break;
			}
			shapeRenderer.end();
			i=i+151;

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
