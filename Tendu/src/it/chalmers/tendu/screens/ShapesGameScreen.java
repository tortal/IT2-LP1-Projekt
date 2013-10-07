package it.chalmers.tendu.screens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.GameState;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.shapesgame.Shape;
import it.chalmers.tendu.gamemodel.shapesgame.ShapesGame;

public class ShapesGameScreen extends GameScreen {

	private ShapeRenderer shapeRenderer; // used to render vector graphics
	private ShapesGame model;
	private List<GraphicalShape> shapes;
	private List<GraphicalShape> locks;

	public ShapesGameScreen(Tendu game, MiniGame model) {
		super(game, model);
		this.model = (ShapesGame) model;
		this.shapeRenderer = new ShapeRenderer();

		shapes = new ArrayList<GraphicalShape>();
		int x = 150;
		for (Shape s : this.model.getAllInventory().get(0)) {
			GraphicalShape sgs = new GraphicalShape(s);
			sgs.moveShape(x, 150);
			shapes.add(sgs);
			x = x + 151;
		}

		locks = new ArrayList<GraphicalShape>();
		x = 150;
		for (Shape s : this.model.getLock(0).getLockSequence()) {
			GraphicalShape sgs = new GraphicalShape(s);
			sgs.moveShape(x, 300);
			locks.add(sgs);
			x = x + 151;
		}

	}

	/** All graphics are drawn here */
	@Override
	public void render() {
		if (model.checkGameState() == GameState.RUNNING) {
			super.render();
			spriteBatch.setProjectionMatrix(game.getCamera().combined);
			spriteBatch.begin();

			shapeRenderer.setProjectionMatrix(game.getCamera().combined);

			for (GraphicalShape sgs : shapes) {
				sgs.renderShape(shapeRenderer);
			}

			for (GraphicalShape sgs : locks) {
				sgs.renderShape(shapeRenderer);
			}

			if (Gdx.input.isTouched()) {
				Vector3 touchPos = new Vector3(Gdx.input.getX(),
						Gdx.input.getY(), +0);
				game.getCamera().unproject(touchPos);
				for (GraphicalShape s : shapes) {
					// TODO: Should not prio the shape that is first by index.
					if (s.getBounds().contains(touchPos.x, touchPos.y)) {
						Collections.swap(shapes, 0, shapes.indexOf(s));
						s.moveShape(touchPos.x - s.getBounds().width / 2,
								touchPos.y - s.getBounds().height / 2);
						for (GraphicalShape lock : locks) {
							if (snapIntoPlace(s, lock)) {
								// TODO: is game completed?
							}
						}
						break;
					}

				}
			}
			model.checkGame();
			spriteBatch.end();
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

	public boolean snapIntoPlace(GraphicalShape shape, GraphicalShape lock) {
		if (shape.getBounds().overlaps(lock.getBounds())) {
			if (model.getLock(0).fillSlot(shape.getShape(), lock.getShape())) {
				lock.setColor(Color.WHITE);
				shapes.remove(shape);
				return true;
			}

		}
		return false;
	}

}
