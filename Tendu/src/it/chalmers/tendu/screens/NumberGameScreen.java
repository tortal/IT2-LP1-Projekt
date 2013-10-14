package it.chalmers.tendu.screens;

//TODO needs major refactoring
import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.controllers.NumberGameController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.gamemodel.GameState;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.SimpleTimer;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.gamemodel.numbergame.NumberGameSound;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

/** GameScreen for the number minigame. Contains all graphics, sounds etc. **/
public class NumberGameScreen extends GameScreen {
	private ShapeRenderer shapeRenderer; // used to render vector graphics
	private ArrayList<Color> colors; // list with colors for all numbers

	private ArrayList<NumberCircle> numberCircles; // numbers we can interact
													// with (guess)
	private ArrayList<Integer> numbers; // the correct numbers
	private SimpleTimer instructionsTimer;
	private int numberAlignment; // start position of first number to the left
									// on the screen
	private NumberGameController controller;

	
	private NumberGameSound sound;

	private BitmapFont font;
	private BitmapFont numberFont;

	private TextWidget memorizeText;
	private TextWidget instructionText;
	private TextWidget numberText;


	/**
	 * @param tendu
	 *            the applicationlistener
	 * @param model
	 *            the MiniGame model associated with the screen
	 */
	public NumberGameScreen(Tendu tendu, MiniGame model) {
		super(tendu, model);

		shapeRenderer = new ShapeRenderer();
		controller = new NumberGameController((NumberGame) model);

		font = new BitmapFont(Gdx.files.internal("fonts/menuFont.fnt"),
				Gdx.files.internal("fonts/menuFont.png"), false);
		numberFont = new BitmapFont(Gdx.files.internal("fonts/numberFont.fnt"),
				Gdx.files.internal("fonts/numberFont.png"), false);
		
		setUpGame();
	}

	/**
	 * Initial setup
	 */
	private void setUpGame() {
		instructionsTimer = new SimpleTimer();

		memorizeText = new TextWidget("Memorize the numbers", new Vector2(
				145, 400), -0.2f);
		instructionText = new TextWidget(
				"Enter the numbers in the correct order", new Vector2(50, 400),
				-0.35f);

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

		// add the correct number from the answerlist
		for (Integer number : getModel().getAnswerList()) {
			numbers.add(number.intValue());
		}

		// setup the guess list
		for (int i = 0; i < getModel().getMyList().size(); i++) {
			numberCircles.add(new NumberCircle(getModel().getMyList().get(i),
					(90 + 95 * i), 120, 35, colors.get(i)));
		}

		// TODO check number of numbers instead
		if (model.getDifficulty() == Constants.Difficulty.ONE) {
			numberAlignment = 240;
		} else if (model.getDifficulty() == Constants.Difficulty.TWO) {
			numberAlignment = 25;
		} else if (model.getDifficulty() == Constants.Difficulty.THREE) {
			numberAlignment = 240;
		} else if (model.getDifficulty() == Constants.Difficulty.FOUR) {
			numberAlignment = 25;
		} else if (model.getDifficulty() == Constants.Difficulty.FIVE) {
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
		// TODO redo properly so number always centered
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
		if (model.checkGameState() == GameState.RUNNING) {
			super.render(); // draws common ui-stuff
			shapeRenderer.setProjectionMatrix(tendu.getCamera().combined);

			if (instructionsTimer.getRemainingTime() > 0) {
				memorizeText.draw(tendu.spriteBatch, font);
				drawNumbers(true);

			} else {
				font.setColor(Color.BLUE);
				instructionText.draw(tendu.spriteBatch, font);

				drawNumbers(false);
				drawNumberCircles();
			}
		}

		shapeRenderer.end();
	}

	/** All game logic goes here */
	@Override
	public void tick(InputController input) {
		model = getModel(); // make sure we have to new model (the host maybe
							// changed it)
		// super.tick(); not used


		if (model.checkGameState() != GameState.RUNNING) {
			// TODO refactor
			if (model.checkGameState() == GameState.WON
					|| model.checkGameState() == GameState.LOST) {
				EventMessage message = new EventMessage(C.Tag.TO_SELF,
						C.Msg.GAME_RESULT, model.getGameResult());
				EventBus.INSTANCE.broadcast(message);
			}
			return;
		} else if (model.checkGameState() == GameState.RUNNING) {
			instructionsTimer.start(4000);
//			Gdx.app.log(this.getClass().getSimpleName(), "time left = " + instructionsTimer.getRemainingTime());
			if (instructionsTimer.getRemainingTime() <= 0) {
				if (input.isTouchedUp()) {
					for (NumberCircle circle : numberCircles) {
						if (circle.collided(input.getCoordinates())) {
							Gdx.input.vibrate(25);
							EventBus.INSTANCE.broadcast(new EventMessage(C.Tag.TO_SELF, C.Msg.NUMBER_GUESS, model.getGameId(), circle.getNumber()));
						}
						circle.scale = 1;
					}
				}

				if (input.isTouchedDown()) {
					for (NumberCircle circle : numberCircles) {
						if (circle.collided(input.getCoordinates())) {
							circle.scale = 1.5f;
						}
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
		numberFont.dispose();
		sound.unregister();
		controller.unregister();
		shapeRenderer.dispose();
	}
}
