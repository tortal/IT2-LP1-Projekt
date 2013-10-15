package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.controllers.ShapeGameModelController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.gamemodel.GameState;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.gamemodel.shapesgame.Shape;
import it.chalmers.tendu.gamemodel.shapesgame.ShapeGame;
import it.chalmers.tendu.gamemodel.shapesgame.ShapeGameSound;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
public class ShapeGameScreen extends GameScreen {

	public final String TAG = this.getClass().getName();

	private int player_num;
	private ShapeRenderer shapeRenderer; // used to render vector graphics
	
	private List<GraphicalShape> shapes;
	private List<GraphicalShape> locks;

	private GraphicalShape movingShape;

	private ShapeGameModelController controller;

	private ShapeGameSound sound;

	// For debug
	int count = 0;

	public ShapeGameScreen(Tendu game, MiniGame model) {
		super(game, model);
		controller = new ShapeGameModelController((ShapeGame) model);
		this.shapeRenderer = new ShapeRenderer();

		player_num = controller.getModel().getplayerNbr();
		sound = new ShapeGameSound();

		shapes = new ArrayList<GraphicalShape>();
		int x = 150;
		for (Shape s : controller.getModel().getAllInventory().get(player_num)) {
			GraphicalShape sgs = new GraphicalShape(s);
			sgs.moveShape(x, 150);
			shapes.add(sgs);
			x = x + 151;
		}

		locks = new ArrayList<GraphicalShape>();
		x = 150;
		for (Shape s : controller.getModel().getLock(player_num)
				.getLockSequence()) {
			GraphicalShape sgs = new GraphicalShape(s);
			sgs.moveShape(x, 300);
			locks.add(sgs);
			x = x + 151;
		}

	}

	/** All graphics are drawn here */
	@Override
	public void render() {
		super.render();
		if (controller.getModel().checkGameState() == GameState.RUNNING) {
			shapeRenderer.setProjectionMatrix(tendu.getCamera().combined);
			// Renders locks
			for (GraphicalShape sgs : locks) {
				sgs.renderShape(shapeRenderer);
			}
			// Renders shapes
			for (GraphicalShape sgs : shapes) {
				sgs.renderShape(shapeRenderer);
			}

		} else {
			// showGameResult();
		}
	}

	/**
	 * @param s
	 */
	private void sendToTeamMate(GraphicalShape s) {
		if (s.getBounds().x <= 10 &&
				getOtherPlayers().size() >= 2) {
			EventBus.INSTANCE.broadcast(new EventMessage(Player.getInstance()
					.getMac(), C.Tag.TO_SELF, C.Msg.SHAPE_SENT, controller
					.getModel().getGameId(), messageContentFactory(getOtherPlayers().get(1)-1,
					s.getShape())));
		}
		if (s.getBounds().x >= Constants.SCREEN_WIDTH - 60 && 
				getOtherPlayers().size() >= 3) {
			EventBus.INSTANCE.broadcast(new EventMessage(Player.getInstance()
					.getMac(), C.Tag.TO_SELF, C.Msg.SHAPE_SENT, controller
					.getModel().getGameId(), messageContentFactory(getOtherPlayers().get(2)-1,
					s.getShape())));

		}
		if (s.getBounds().y >= Constants.SCREEN_HEIGHT - 60 &&
				getOtherPlayers().size() >= 1) {
			EventBus.INSTANCE.broadcast(new EventMessage(Player.getInstance()
					.getMac(), C.Tag.TO_SELF, C.Msg.SHAPE_SENT, controller
					.getModel().getGameId(), messageContentFactory(getOtherPlayers().get(0)-1,
					s.getShape())));
		}
	}

	/**
	 * 
	 * @param player
	 *            That will receive the shape
	 * @param shape
	 *            shape to be sent
	 * @return
	 */
	private List<Object> messageContentFactory(int player, Shape shape) {

		List<Object> l = new ArrayList<Object>();
		l.add(player);
		l.add(shape);
		return l;

	}

	/** All game logic goes here (within the model...) */
	@Override
	public void tick(InputController input) {
		updateShapesFromModel();

		// Vector3 touchPos = new Vector3(input.x, input.y, +0);
		// tendu.getCamera().unproject(touchPos);

		// TODO nullpointer movingShape
		if (input.isTouchedDown()) {
			for (GraphicalShape s : shapes) {
				if (s.getBounds().contains(input.x, input.y) && !s.getShape().isLocked()) {
					movingShape = s;
				}
			}
		}

		if (input.isTouchedUp()) {
			if (movingShape != null) {
				for (GraphicalShape lock : locks) {
					snapIntoPlace(movingShape, lock);
				}
				sendToTeamMate(movingShape);
				movingShape = null;
			}
		}

		if (input.isDragged()) {
			if (movingShape != null) {
				if (!movingShape.getShape().isLocked()) {
					movingShape.moveShape(input.x
							- movingShape.getBounds().width / 2, input.y
							- movingShape.getBounds().height / 2);
				}
			}
		}
	}

	@Override
	public void removed() {
		super.removed();
		sound.unregister();
	}

	// TODO : Adds a new shape if any shape has changed color.
	private void updateShapesFromModel() {
		// Adds shapes to the gui that are no longer part
		// of the model.
		for (Shape s : controller.getModel().getAllInventory().get(player_num)) {
			if (!shapes.contains(new GraphicalShape(s))) {
				shapes.add(new GraphicalShape(s));
				Gdx.app.log(TAG, "new Shape!");
			}
		}

		// Removes shapes that are no longer parts of the
		// model.
		List<GraphicalShape> removeList = new ArrayList<GraphicalShape>();
		for (GraphicalShape gs : shapes) {
			if (!controller.getModel().getAllInventory().get(player_num)
					.contains(gs.getShape())) {
				removeList.add(gs);
				Gdx.app.log(TAG, "Shape removed!");
			}
		}

		for (GraphicalShape gs : removeList)
			shapes.remove(gs);

	}

	public boolean snapIntoPlace(GraphicalShape shape, GraphicalShape lock) {
		boolean result = false;
		if (shape.getBounds().overlaps(lock.getBounds())) {
			if (controller.getModel().shapeFitIntoLock(player_num,
					shape.getShape(), lock.getShape())) {
				shape.moveShape(lock.getBounds().x, lock.getBounds().y);
				//shape.getShape().setLocked(true);
				result = true;
				Gdx.app.log(TAG, "Animated" + "x=" + lock.getBounds().x + "y="
						+ lock.getBounds().getY());

			}
			List<Object> content = new ArrayList<Object>();
			content.add(player_num);
			content.add(lock.getShape());
			content.add(shape.getShape());
			EventBus.INSTANCE.broadcast(new EventMessage(Player.getInstance()
					.getMac(), C.Tag.TO_SELF, C.Msg.LOCK_ATTEMPT, controller
					.getModel().getGameId(), content));
		}

		return result;

	}
}
