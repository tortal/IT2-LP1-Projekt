package it.chalmers.tendu.screen;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controller.InputController;
import it.chalmers.tendu.controller.ShapeGameController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.defaults.TextLabels;
import it.chalmers.tendu.event.C;
import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.gamemodel.GameState;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.SimpleTimer;
import it.chalmers.tendu.gamemodel.shapesgame.NetworkShape;
import it.chalmers.tendu.gamemodel.shapesgame.Shape;
import it.chalmers.tendu.gamemodel.shapesgame.ShapeGame;
import it.chalmers.tendu.gamemodel.shapesgame.ShapeGameSound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/** GameScreen for the Shape minigame. Contains all graphics, sounds etc. **/
public class ShapeGameScreen extends GameScreen {

	public final String TAG = this.getClass().getName();
	private final int SEND_MARGIN = 5;
	private int timeForInstructions;

	private int player_num;
	private ShapeRenderer shapeRenderer; // used to render vector graphics

	// Fields used to render shapes and to compare
	// shapes in the gui to shapes in the model
	private List<GraphicalShape> shapes;
	private List<GraphicalShape> locks;
	private Map<Integer, Shape> latestAddedShape;
	private Shape latestRemovedShape;
	private GraphicalShape movingShape;

	private ShapeGameController controller;
	private ShapeGame shapeGameModel;
	private ShapeGameSound soundController;

	private SimpleTimer gameCompletedTimer;
	private SimpleTimer instructionsTimer; // used to time how long the
											// instructions should be displayed
	private List<Integer> otherPlayers;

	private static int timesPlayed = 0;

	// Assets
	private TextWidget instructions;
	private TextWidget teamInstructions;
	private TextWidget teamInstructionsContinued;
	private BitmapFont font;

	public ShapeGameScreen(MiniGame model) {
		super(model);

		controller = new ShapeGameController((ShapeGame) model);
		shapeGameModel = controller.getModel();
		this.shapeRenderer = new ShapeRenderer();

		latestAddedShape = new HashMap<Integer, Shape>();

		player_num = shapeGameModel.getplayerNbr();
		soundController = new ShapeGameSound();

		gameCompletedTimer = new SimpleTimer();
		instructionsTimer = new SimpleTimer();

		initShapes();

		otherPlayers = controller.getModel().getOtherPlayerNumbers();

		// setup the instructions
		instructions = new TextWidget(TextLabels.PLACE_THE_SHAPE, new Vector2(
				90, 595), Constants.MENU_FONT_COLOR);

		teamInstructions = new TextWidget(TextLabels.SEND_SHAPE_BY,
				new Vector2(65, 400), Constants.MENU_FONT_COLOR);
		teamInstructionsContinued = new TextWidget(
				TextLabels.SEND_SHAPE_TEAMMATES, new Vector2(65, 300),
				Constants.MENU_FONT_COLOR);
		// load the font
		font = new BitmapFont(Gdx.files.internal("fonts/menuFont.fnt"),
				Gdx.files.internal("fonts/menuFont.png"), false);

		// We only need to show the instructions once
		if (timesPlayed > 0) {
			timeForInstructions = 0;
		} else {
			timeForInstructions = 3500;
			timesPlayed++;
		}
	}

	/**
	 * Gets the shapes from the model and sets their initital positions.
	 */
	private void initShapes() {
		shapes = new ArrayList<GraphicalShape>();
		int x = Constants.SCREEN_WIDTH
				/ (controller.getModel().getLock(player_num).getLockSequence()
						.size() + 1) - 100;
		for (Shape s : controller.getModel().getAllInventory().get(player_num)) {
			GraphicalShape sgs = new GraphicalShape(s);
			sgs.moveShape(x, 500);
			shapes.add(sgs);
			x = x
					+ Constants.SCREEN_WIDTH
					/ (controller.getModel().getLock(player_num)
							.getLockSequence().size() + 1);
		}

		locks = new ArrayList<GraphicalShape>();

		x = Constants.SCREEN_WIDTH
				/ (controller.getModel().getLock(player_num).getLockSequence()
						.size() + 1) - 100;
		for (Shape s : controller.getModel().getLock(player_num)
				.getLockSequence()) {

			GraphicalShape sgs = new GraphicalShape(s);
			sgs.moveShape(x, 250);
			sgs.setRenderAsLock(true);
			locks.add(sgs);
			x = x
					+ Constants.SCREEN_WIDTH
					/ (controller.getModel().getLock(player_num)
							.getLockSequence().size() + 1);
		}
	}

	/** All graphics are drawn here */
	@Override
	public void render(SpriteBatch spriteBatch, OrthographicCamera camera) {

		if (model.hasStarted()) {
			super.render(spriteBatch, camera);
			instructionsTimer.start(timeForInstructions);
			if (!instructionsTimer.isDone()) {
				instructions.draw(spriteBatch, font);
				if (otherPlayers.size() > 0) {
					teamInstructions.draw(spriteBatch, font);
					teamInstructionsContinued.draw(spriteBatch, font);
				}

			} else {
				controller.getModel().startGameTimer();
				if (shapeGameModel.checkGameState() == GameState.RUNNING
						|| gameCompletedTimer.isRunning()) {
					shapeRenderer.setProjectionMatrix(camera.combined);
					// Renders locks
					for (GraphicalShape sgs : locks) {
						sgs.render(shapeRenderer);
					}
					// Renders shapes
					for (GraphicalShape sgs : shapes) {
						sgs.render(shapeRenderer);
					}
				}
			}
		}
	}

	/**
	 * @param s
	 */
	private void sendToTeamMate(GraphicalShape s) {
		Gdx.app.log(TAG, "SHAPE SENDING!!!!!!!!");
		if (s.getBounds().y + s.HEIGHT >= Constants.SCREEN_HEIGHT - SEND_MARGIN
				&& otherPlayers.size() >= 1) {
			EventBus.INSTANCE.broadcast(new EventMessage(/*
														 * Player.getInstance()
														 * .getMac(),
														 */C.Tag.TO_SELF,
					C.Msg.SHAPE_SENT, messageContentFactory(controller
							.getModel().getOtherPlayerNumbers().get(0),
							s.getShape())));
		} else if (s.getBounds().x <= SEND_MARGIN && otherPlayers.size() >= 2) {
			EventBus.INSTANCE.broadcast(new EventMessage(/*
														 * Player.getInstance()
														 * .getMac(),
														 */C.Tag.TO_SELF,
					C.Msg.SHAPE_SENT, messageContentFactory(
							otherPlayers.get(1), s.getShape())));
		} else if (s.getBounds().x + s.WIDTH >= Constants.SCREEN_WIDTH
				- SEND_MARGIN
				&& otherPlayers.size() >= 3) {
			EventBus.INSTANCE.broadcast(new EventMessage(/*
														 * Player.getInstance()
														 * .getMac(),
														 */C.Tag.TO_SELF,
					C.Msg.SHAPE_SENT, messageContentFactory(
							otherPlayers.get(2), s.getShape())));
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
	private NetworkShape messageContentFactory(int player, Shape shape) {
		return new NetworkShape(player, shape);
	}

	/** All game logic goes here (within the model...) */
	@Override
	public void tick(InputController input) {
		updateShapesFromModel();
		if (model.hasStarted()) {
			if (instructionsTimer.isDone()) {
				if (gameCompletedTimer.isDone()) {
					Gdx.app.log(TAG, "Brodcasting gameresult! timer done");
					// Received by GameSessionController
					sendEndMessage();
				} else if (!gameCompletedTimer.isRunning()) {
					if (controller.getModel().checkGameState() == GameState.WON) {
						gameCompletedTimer.start(750);
						controller.getModel().stopTimer();
						Gdx.app.log(TAG, "Timer started! game won");
					} else if (controller.getModel().checkGameState() == GameState.LOST) {
						gameCompletedTimer.start(1500);
					}

				}
			}

			if (input.isTouchedDown()) {
				for (GraphicalShape s : shapes) {
					if (s.getBounds().contains(input.x, input.y)
							&& !s.getShape().isLocked()) {
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
	}

	/**
	 * Adds and the latest received shape from model to gui and removes shapes
	 * from the gui that are no longer part of the model.
	 */
	private void updateShapesFromModel() {
		addLatestReceivedShape();
		removeLastSentShape();
	}

	/**
	 * Removes shapes that are no longer part of the model
	 */
	private void removeLastSentShape() {
		if (controller.getModel().getLatestSentShapes(player_num).size() >= 1)
			latestRemovedShape = controller
					.getModel()
					.getLatestSentShapes(player_num)
					.get(controller.getModel().getLatestSentShapes(player_num)
							.size() - 1);
		if (latestRemovedShape != null) {
			List<GraphicalShape> removeList = new ArrayList<GraphicalShape>();
			for (GraphicalShape gs : shapes) {
				if (latestRemovedShape.equals(gs.getShape())) {
					if (!controller.getModel().getAllInventory()
							.get(player_num).contains(latestRemovedShape)) {
						removeList.add(gs);
						Gdx.app.log(TAG, "Added to removeList" + gs.getShape());
					}
				}
			}
			for (GraphicalShape gs : removeList)
				shapes.remove(gs);
		}
	}

	/**
	 * Get that latest received shape from the model
	 */
	private void addLatestReceivedShape() {
		Map<Integer, Shape> latestModelReceivedShape = shapeGameModel
				.getLatestReceivedShape(player_num);
		if (!latestModelReceivedShape.isEmpty()) {
			if (!latestModelReceivedShape.equals(latestAddedShape)) {
				for (Map.Entry<Integer, Shape> entry : latestModelReceivedShape
						.entrySet()) {
					showShapeFromSender(entry.getValue(), entry.getKey());
					latestAddedShape = latestModelReceivedShape;
				}
			}

		}
	}

	/**
	 * Tries to snap a shape in a lock. If the model is ok with this the shape
	 * will move to the same position as the lock is. Either way, the try will
	 * be broadcasted to the network.
	 * 
	 * @param shape
	 *            The shape to put in the lock
	 * @param lock
	 *            The lock to put the shape in.
	 */
	private void snapIntoPlace(GraphicalShape shape, GraphicalShape lock) {
		// checks to see if the shape fits, and moves it if it does.
		if (shape.getBounds().overlaps(lock.getBounds())) {
			if (shapeGameModel.shapeFitIntoLock(player_num, shape.getShape(),
					lock.getShape())) {
				shape.moveShape(lock.getBounds().x, lock.getBounds().y);
				Gdx.app.log(TAG, "Animated" + "x=" + lock.getBounds().x + "y="
						+ lock.getBounds().getY());
			}
			// Creates a list the send over the network
			List<Object> content = new ArrayList<Object>();
			content.add(player_num);
			content.add(lock.getShape());
			content.add(shape.getShape());

			// Received by ShapeGameController.
			EventBus.INSTANCE.broadcast(new EventMessage(C.Tag.TO_SELF,
					C.Msg.LOCK_ATTEMPT, content));
		}
	}

	/**
	 * Used to move the shape to appear as if it was sent by the proper sender
	 * 
	 * @param shape
	 *            That was sent
	 * @param sender
	 * @return <code>true</code> everything went according to plan, sit back and
	 *         relax. <code>false</code> something went wrong, run around and
	 *         scream in utter terror
	 */
	private boolean showShapeFromSender(Shape shape, int sender) {
		GraphicalShape receivedShape = new GraphicalShape(shape);
		if (!otherPlayers.contains(sender))
			return false;

		shapes.add(receivedShape);

		if (otherPlayers.get(0) == sender) {
			receivedShape.moveShape(Constants.SCREEN_WIDTH / 2,
					Constants.SCREEN_HEIGHT - 110);
		} else if (otherPlayers.get(1) == sender) {
			receivedShape.moveShape(110, Constants.SCREEN_HEIGHT / 2);
		} else if (otherPlayers.get(2) == sender) {
			receivedShape.moveShape(Constants.SCREEN_WIDTH - 110,
					Constants.SCREEN_HEIGHT / 2);
		}

		return true;
	}

	// TODO not the best solution but it works.
	// this message must be sent only once
	private boolean ended = false;

	private void sendEndMessage() {
		if (!ended) {
			// Received by GameSessionController.
			EventMessage message = new EventMessage(C.Tag.TO_SELF,
					C.Msg.GAME_RESULT, model.getGameResult());
			EventBus.INSTANCE.broadcast(message);
		}

		ended = true;
	}

	@Override
	public void dispose() {
		super.dispose();
		shapeRenderer.dispose();
		soundController.unregister();
		controller.unregister();
		font.dispose();
	}
}
