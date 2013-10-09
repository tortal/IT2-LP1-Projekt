package it.chalmers.tendu.screens;

//TODO needs major refactoring
import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.controllers.NumberGameController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.gamemodel.GameState;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.C.Tag;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

/** GameScreen for the number minigame. Contains all graphics, sounds etc. **/
public class NumberGameScreen extends GameScreen {
	private ShapeRenderer shapeRenderer; // used to render vector graphics
	private BitmapFont numberFont; // for rendering fonts

	private ArrayList<Color> colors;

	private ArrayList<NumberCircle> numberCircles;
	private ArrayList<Integer> numbers;

	private Vector3 touchPos; // used to store coordinates for on screen touches

	private int time; // used to time certain "events" during the game.
	private int numberAlignment; // start position of first number to the left
									// on the screen

	private NumberGameController controller;

	/**
	 * @param tendu
	 *            the applicationlistener
	 * @param model
	 *            the MiniGame model associated with the screen
	 */
	public NumberGameScreen(Tendu tendu, MiniGame model) {
		super(tendu, model);

		shapeRenderer = new ShapeRenderer();

		numberFont = new BitmapFont();
		touchPos = new Vector3();
		controller = new NumberGameController((NumberGame) model);

		setUpGame();
	}

	/**
	 * Initiall setup
	 */
	private void setUpGame() {
		time = 0;
		numberFont.scale(2); // scale up font relative to the previous scale, -2
								// scales it back

		numberCircles = new ArrayList<NumberCircle>();
		numbers = new ArrayList<Integer>();

		colors = new ArrayList<Color>();
		colors.add(Color.BLUE);
		colors.add(Color.MAGENTA);
		colors.add(Color.GREEN);
		colors.add(Color.YELLOW);
		colors.add(Color.ORANGE);
		colors.add(Color.WHITE);
		colors.add(Color.PINK);
		colors.add(Color.RED);
		Collections.shuffle(colors);

		for (Integer number : getModel().getAnswerList()) {
			numbers.add(number.intValue());
		}

		for (int i = 0; i < getModel().getMyList().size(); i++) {
			numberCircles.add(new NumberCircle(getModel().getMyList().get(i),
					(90 + 95 * i), 120, 35, colors.get(i)));
		}

		if (model.getDifficulty() == Constants.Difficulty.ONE) {
			numberAlignment = 240;
		} else {
			numberAlignment = 25;
		}
	}

	/**
	 * Draws the current number list to the screen
	 * 
	 * @param showAll
	 *            set to false if only correctly answered numbers should be
	 *            drawn
	 */
	private void drawNumbers(boolean showAll) {
		numberFont.scale(1.6f);

		if (showAll) {
			for (int i = 0; i < numbers.size(); i++) {
				numberFont.setColor(colors.get(i));
				numberFont.draw(tendu.spriteBatch, "" + numbers.get(i),
						numberAlignment + i * 105, 300);
			}
		} else {
			for (int i = 0; i < numbers.size(); i++) {
				if (getModel().getAnsweredNbrs().contains(numbers.get(i))) {
					numberFont.setColor(colors.get(i));
					numberFont.draw(tendu.spriteBatch, "" + numbers.get(i),
							numberAlignment + i * 105, 300);
				}
			}
		}
		numberFont.scale(-1.6f);
	}

	private void drawNumberCircle(NumberCircle circle) {
		shapeRenderer.setColor(circle.color);
		numberFont.setColor(circle.color);

		for (int i = 0; i < 5; i++) {
			shapeRenderer.circle(circle.getX(), circle.getY(),
					(circle.getRadius() - i) * circle.scale);
		}
		numberFont.draw(tendu.spriteBatch, "" + circle.getNumber(),
				circle.getNumberX(), circle.getNumberY());
	}

	private void drawNumberCircles() {
		numberFont.scale(-0.8f);
		for (int i = 0; i < numberCircles.size(); i++) {
			drawNumberCircle(numberCircles.get(i));
		}
		numberFont.scale(0.8f);
	}

	/** Draw all graphics here */
	@Override
	public void render() {
		super.render();
		shapeRenderer.setProjectionMatrix(tendu.getCamera().combined);
		shapeRenderer.begin(ShapeType.Circle);

		if (time < 240) {
			numberFont.setColor(Color.BLUE);
			numberFont
					.draw(tendu.spriteBatch, "Memorize the numbers", 200, 400);
			drawNumbers(true);

		} else {
			if (model.checkGameState() == GameState.RUNNING) {
				numberFont.setColor(Color.BLUE);
				numberFont.draw(tendu.spriteBatch,
						"Enter the numbers in the correct order", 60, 400);

				drawNumbers(false);
				drawNumberCircles();
			}
		}

		if (model.checkGameState() == GameState.WON) {
			numberFont.setColor(Color.GREEN);
			numberFont.scale(2);
			numberFont.draw(tendu.spriteBatch, "You won!", 300, 300);
			numberFont.scale(-2);
		} else if (model.checkGameState() == GameState.LOST) {
			numberFont.setColor(Color.RED);
			numberFont.scale(2);
			numberFont.draw(tendu.spriteBatch, "You Lost!", 300, 300);
			numberFont.scale(-2);

		}

		showGameResult();

		shapeRenderer.end();
	}

	/** All game logic goes here */
	@Override
	public void tick(InputController input) {
		// TODO maybe not the best solution...
		// model = (NumberGame) game.gameSession.currentMiniGame;
		if (controller.getModel().checkGameState() != GameState.RUNNING)
			model = getModel();
		if (model.checkGameState() != GameState.RUNNING)
			return;
		if (controller.getModel().checkSound()) {
			playSound();
		}
		if (time < 240) {
			time++;
		} else {
			if (input.isTouchedUp()) {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				tendu.getCamera().unproject(touchPos);

				for (NumberCircle circle : numberCircles) {
					if (circle.collided(touchPos)) {
						Gdx.input.vibrate(25);
						EventBus.INSTANCE.broadcast(new EventMessage(Player
								.getInstance().getMac(), C.Tag.TO_SELF,
								C.Msg.NUMBER_GUESS, model.getGameId(), circle
										.getNumber()));
					}
					circle.scale = 1;
				}
			}

			if (input.isTouchedDown()) {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				tendu.getCamera().unproject(touchPos);

				for (NumberCircle circle : numberCircles) {
					if (circle.collided(touchPos)) {
						circle.scale = 1.5f;
					}
				}
			}
		}
	}

	private NumberGame getModel() {
		return controller.getModel();
	}

	@Override
	public void removed() {
		super.removed();
		shapeRenderer.dispose();
		numberFont.dispose();
	}
}
