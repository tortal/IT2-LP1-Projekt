package it.chalmers.tendu.screens;

//TODO needs major refactoring
import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.controllers.NumberGameController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.gamemodel.GameState;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.tbd.C;
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
	private ArrayList<Color> colors; // list with colors for all numbers

	private ArrayList<NumberCircle> numberCircles; // numbers we can interact
													// with (guess)
	private ArrayList<Integer> numbers; // the correct numbers

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

		touchPos = new Vector3();
		controller = new NumberGameController((NumberGame) model);

		setUpGame();
	}

	/**
	 * Initial setup
	 */
	private void setUpGame() {
		time = 0;
		font.scale(2); // scale up font relative to the previous scale, -2
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
	 * Draws the correct numbers list to the screen
	 * 
	 * @param showAll
	 *            set to false if only correctly answered numbers should be
	 *            drawn
	 */
	private void drawNumbers(boolean showAll) {
		font.scale(1.6f);

		if (showAll) {
			for (int i = 0; i < numbers.size(); i++) {
				font.setColor(colors.get(i));
				font.draw(tendu.spriteBatch, "" + numbers.get(i),
						numberAlignment + i * 105, 300);
			}
		} else {
			for (int i = 0; i < numbers.size(); i++) {
				if (getModel().getAnsweredNbrs().contains(numbers.get(i))) {
					font.setColor(colors.get(i));
					font.draw(tendu.spriteBatch, "" + numbers.get(i),
							numberAlignment + i * 105, 300);
				}
			}
		}
		font.scale(-1.6f);
	}

	/**
	 * Draws one NumberCircle
	 * 
	 * @param circle
	 *            circle to draw
	 */
	private void drawNumberCircle(NumberCircle circle) {
		shapeRenderer.setColor(circle.color);
		font.setColor(circle.color);

		for (int i = 0; i < 5; i++) {
			shapeRenderer.circle(circle.getX(), circle.getY(),
					(circle.getRadius() - i) * circle.scale);
		}
		font.draw(tendu.spriteBatch, "" + circle.getNumber(),
				circle.getNumberX(), circle.getNumberY());
	}

	/**
	 * Draws all NumberCircles
	 */
	private void drawNumberCircles() {
		shapeRenderer.begin(ShapeType.Circle);

		font.scale(-0.8f);
		for (int i = 0; i < numberCircles.size(); i++) {
			drawNumberCircle(numberCircles.get(i));
		}
		font.scale(0.8f);

		shapeRenderer.end();
	}

	/** Draw all graphics from here */
	@Override
	public void render() {
		super.render(); // draws common ui-stuff
		shapeRenderer.setProjectionMatrix(tendu.getCamera().combined);

		if (time < 240) {
			font.setColor(Color.BLUE);
			font.draw(tendu.spriteBatch, "Memorize the numbers", 200, 400);
			drawNumbers(true);

		} else {
			if (model.checkGameState() == GameState.RUNNING) {
				font.setColor(Color.BLUE);
				font.draw(tendu.spriteBatch,
						"Enter the numbers in the correct order", 60, 400);

				drawNumbers(false);
				drawNumberCircles();
			}
		}

	}

	/** All game logic goes here */
	@Override
	public void tick(InputController input) {
		super.tick();
		// TODO maybe not the best solution...
		model = getModel();
		if (model.checkGameState() != GameState.RUNNING) {
			// TODO refactor
			if (model.checkGameState() == GameState.WON
					|| model.checkGameState() == GameState.LOST) {
				time++;
				
				//Game is done, broadcast results
				if (time == 360) {
						EventMessage message = new EventMessage(C.Tag.TO_SELF,
								C.Msg.GAME_RESULT, model.getGameResult());
						EventBus.INSTANCE.broadcast(message);
				}
			}
			return;
		}

		if (time < 240) {
			time++;
			return;
		}

		if (time == 240) {
			if (input.isTouchedUp()) {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				tendu.getCamera().unproject(touchPos);

				for (NumberCircle circle : numberCircles) {
					if (circle.collided(touchPos)) {
						Gdx.input.vibrate(25);
						EventBus.INSTANCE.broadcast(new EventMessage(
								C.Tag.TO_SELF, C.Msg.NUMBER_GUESS, model
										.getGameId(), circle.getNumber()));
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
		font.dispose();
		controller.unregister();
		shapeRenderer.dispose();
	}
}
