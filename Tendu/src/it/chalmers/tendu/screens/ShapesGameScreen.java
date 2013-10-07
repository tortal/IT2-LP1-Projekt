package it.chalmers.tendu.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.shapesgame.Shape;
import it.chalmers.tendu.gamemodel.shapesgame.ShapesGame;

public class ShapesGameScreen extends GameScreen {

	private ShapeRenderer shapeRenderer; // used to render vector graphics
	private ShapesGame model;
	private List<ShapesGameShape> shapes;
	private List<ShapesGameShape> locks;

	public ShapesGameScreen(Tendu game, MiniGame model) {
		super(game, model);
		this.model = (ShapesGame) model;
		this.shapeRenderer = new ShapeRenderer();

		shapes = new ArrayList<ShapesGameShape>();
		int x = 150;
		for (Shape s : this.model.getAllInventory().get(0)) {
			ShapesGameShape sgs = new ShapesGameShape(s);
			sgs.moveShape(x, 150);
			shapes.add(sgs);
			x = x + 151;
		}

		locks = new ArrayList<ShapesGameShape>();
		x = 150;
		for (Shape s : this.model.getLock(0).getLockSequence()) {
			ShapesGameShape sgs = new ShapesGameShape(s);
			sgs.moveShape(x, 300);
			locks.add(sgs);
			x = x + 151;
		}

	}

	/** All graphics are drawn here */
	@Override
	public void render() {
		super.render();
		spriteBatch.setProjectionMatrix(game.getCamera().combined);
		spriteBatch.begin();

		shapeRenderer.setProjectionMatrix(game.getCamera().combined);

		for (ShapesGameShape sgs : shapes) {
			sgs.renderShape(shapeRenderer);
		}

		for (ShapesGameShape sgs : locks) {
			sgs.renderShape(shapeRenderer);
		}

		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),
					+0);
			game.getCamera().unproject(touchPos);
			for (ShapesGameShape s : shapes) {
				// TODO: Shoulnt prio the shape that is first by index.
				if (touchPos.x - s.getBounds().x <= s.getBounds().width
						&& touchPos.y - s.getBounds().y <= s.getBounds()
								.getHeight()
						&& touchPos.x - s.getBounds().x >= 0
						&& touchPos.y - s.getBounds().y >= 0) {
					s.moveShape(touchPos.x - s.getBounds().width / 2,
							touchPos.y - s.getBounds().height / 2);
					for (ShapesGameShape lock : locks) {
						if (s.getBounds().overlaps(lock.getBounds())) {
							if (model.getLock(0).fillSlot(s.getShape(),
									lock.getShape())) {
								lock.setColor(Color.WHITE);
								s.setColor(Color.BLACK);
							}
						}
					}
					break;
				}

			}
		}
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
