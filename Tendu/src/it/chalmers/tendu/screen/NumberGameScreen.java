package it.chalmers.tendu.screen;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controller.InputController;
import it.chalmers.tendu.controller.NumberGameController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.defaults.TextLabels;
import it.chalmers.tendu.event.C;
import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.gamemodel.GameState;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.SimpleTimer;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.gamemodel.numbergame.NumberGameSound;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

/** GameScreen for {@link NumberGame}. Contains graphics, sounds etc. **/
public class NumberGameScreen extends GameScreen {
	private ArrayList<Color> colors; // list with colors for all numbers
	private ArrayList<Integer> guessNumbers; // the correct numbers

	private ArrayList<Integer> numbers; // the correct numbers
	private SimpleTimer instructionsTimer; // used to time how long the
											// instructions should be displayed
	private int time;
	private SimpleTimer gameCompletedTimer; // makes sure the game does not end
											// the millisecond you've one or
											// lost
	int numberSpacing; // used for graphical placement of number
	int numberAlignment; // used for graphical placement of number

	private NumberGameController controller; // controller for the game

	private NumberGameSound sound;

	private BitmapFont font;
	private BitmapFont numberFont;

	// some TextWidgets for on screen text
	private TextWidget memorizeText;
	private TextWidget instructionText;
	private TextWidget timeOutText;

	private ArrayList<TextWidget> guessNumbersWidgets;
	private ArrayList<TextWidget> numberWidgets;

	/**
	 * @param tendu
	 *            the main tendu object
	 * @param model
	 *            a NumberGame model
	 */
	public NumberGameScreen(Tendu tendu, MiniGame model) {
		super(tendu, model);

		// create the controller and load the resources
		controller = new NumberGameController((NumberGame) model);
		font = new BitmapFont(Gdx.files.internal("fonts/menuFont.fnt"),
				Gdx.files.internal("fonts/menuFont.png"), false);
		numberFont = new BitmapFont(
				Gdx.files.internal("fonts/digitalTendu.fnt"),
				Gdx.files.internal("fonts/digitalTendu.png"), false);
		sound = new NumberGameSound();

		// run initial setup
		setUpGame();
	}

	// TODO divide in smaller functions
	/**
	 * Initial setup
	 */
	private void setUpGame() {
		// timers for gui related stuff
		instructionsTimer = new SimpleTimer();
		gameCompletedTimer = new SimpleTimer();

		// instruction and information TextWidgets
		memorizeText = new TextWidget(TextLabels.MEMORIZE_NUMBERS, new Vector2(
				250, 595), Constants.MENU_FONT_COLOR);
		instructionText = new TextWidget(TextLabels.ENTER_NUMBERS, new Vector2(
				150, 595), Constants.MENU_FONT_COLOR);
		timeOutText = new TextWidget(TextLabels.TIME_OUT,
				new Vector2(440, 400), Color.RED);

		guessNumbers = new ArrayList<Integer>();
		numbers = new ArrayList<Integer>();
		guessNumbersWidgets = new ArrayList<TextWidget>();
		numberWidgets = new ArrayList<TextWidget>();

		// create a list of colors which we shuffle so all numbers gets random
		// colors
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

		numberSpacing = 150; // pixels between the numbers on screen
		// position of the first number to the left on the screen
		if (getModel().getAnswerList().size() < 8) {
			numberAlignment = Constants.SCREEN_WIDTH / 2
					- (getModel().getAnswerList().size() - 1) * numberSpacing
					/ 2;
		} else {
			numberAlignment = Constants.SCREEN_WIDTH / 2 - 7 * numberSpacing
					/ 2;
		}
		// add the correct number from the answerlist
		for (int i = 0; i < getModel().getAnswerList().size(); i++) {
			numbers.add(getModel().getAnswerList().get(i));
			if (i < 8) {
				numberWidgets.add(new TextWidget(getModel().getAnswerList()
						.get(i).toString(), new Vector2(numberAlignment + i
						* numberSpacing, 470), colors.get(i)));
			} else {
				numberWidgets.add(new TextWidget(getModel().getAnswerList()
						.get(i).toString(), new Vector2(numberAlignment
						+ (i - 8) * numberSpacing, 385), colors.get(-(8 - i))));
			}
		}

		// setup the guess list
		for (int i = 0; i < getModel().getMyList().size(); i++) {
			guessNumbers.add(getModel().getMyList().get(i));
			guessNumbersWidgets.add(new TextWidget(getModel().getMyList()
					.get(i).toString(), new Vector2(72 + 150 * i, 130), colors
					.get(i), -0.15f));

			guessNumbersWidgets.get(i).expandHeight(15);
			guessNumbersWidgets.get(i).expandWidth(15);
		}

		// fewer players should have more time for better game balance
		if (numbers.size() <= 2) {
			time = 2000;
		} else if (numbers.size() <= 4) {
			time = 3500;
		} else if (numbers.size() <= 8) {
			time = 5000;
		} else if (numbers.size() <= 16) {
			time = 7500;
		}

	}

	/**
	 * Draw the numbers we've guessed correctly on
	 */
	private void drawCorrectlyGuessedNumbers() {
		for (int i = 0; i < numbers.size(); i++) {
			if (getModel().getAnsweredNbrs().contains(numbers.get(i))) {
				numberWidgets.get(i).drawAtCenterPoint(tendu.spriteBatch,
						numberFont);
			}
		}
	}

	/**
	 * Draw all the correct numberss
	 */
	private void drawAllNumbers() {
		for (int i = 0; i < numbers.size(); i++) {
			numberWidgets.get(i).drawAtCenterPoint(tendu.spriteBatch,
					numberFont);
		}
	}

	/**
	 * Draws all number we can guess among
	 */
	private void drawGuessNumbers() {
		for (int i = 0; i < guessNumbers.size(); i++) {
			guessNumbersWidgets.get(i).draw(tendu.spriteBatch, numberFont);
		}
	}

	/** Draw all graphics from here */
	@Override
	public void render() {
		// the game won't start to render anything until the controller "starts"
		// the model
		if (model.hasStarted()) {
			super.render(); // draws common ui-stuff

			if (!instructionsTimer.isDone()) {
				// Show all number and inform the player of what to do
				memorizeText.draw(tendu.spriteBatch, font);
				drawAllNumbers();
			} else {
				font.setColor(Color.BLUE);
				instructionText.draw(tendu.spriteBatch, font);

				if (model.checkGameState() == GameState.LOST) {
					timeOutText.draw(tendu.spriteBatch, font); // inform the
																// player that
																// he/she is out
																// of time
				} else {
					drawCorrectlyGuessedNumbers();
				}
				drawGuessNumbers();
			}
		}
	}

	/** All game logic goes here */
	@Override
	public void tick(InputController input) {
		model = getModel(); // make sure we have the new model (the host might
							// have changed it)

		// make sure the game has been started from the Controller
		if (model.hasStarted()) {
			if (model.checkGameState() != GameState.RUNNING) {
				// the game is finished
				model.stopTimer(); //TODO do from controller instead
				gameCompletedTimer.start(1500); // let the game run for a brief
												// time before it's ended

				if (gameCompletedTimer.isDone()) {

					// Received by GameSessionController.
					broadCastEndMessage();
				}

				return;

			} else if (model.checkGameState() == GameState.RUNNING) {
				instructionsTimer.start(time); // only starts once, makes sure
												// we don't handle any input
												// until the game actually
												// starts

				if (instructionsTimer.isDone()) {
					model.startGameTimer(); //TODO do from controller instead

					// check input and tell controller what needs to be done
					if (input.isTouchedUp()) {
						for (int i = 0; i < guessNumbers.size(); i++) {
							if (guessNumbersWidgets.get(i).collided(
									input.getCoordinates())) {
								EventBus.INSTANCE
										.broadcast(new EventMessage(
												C.Tag.TO_SELF,
												C.Msg.NUMBER_GUESS, model
														.getGameId(),
												guessNumbers.get(i)));
							}
							// reset scale on guessed number
							guessNumbersWidgets.get(i).setScale(-0.15f);
							guessNumbersWidgets.get(i).setY(130);

						}
					}

					if (input.isTouchedDown()) {
						// give visual and haptic feedback when you guess on a
						// number
						for (int i = 0; i < guessNumbers.size(); i++) {
							if (guessNumbersWidgets.get(i).collided(
									input.getCoordinates())) {
								Gdx.input.vibrate(25);
								guessNumbersWidgets.get(i).setScale(0.2f);
								guessNumbersWidgets.get(i).setY(145);
							}

						}
					}
				}
			}
		}
	}

	// this message must be sent only once
	private boolean ended = false;

	/**
	 * BroadCasts a message to the controller that the game is done
	 */
	private void broadCastEndMessage() {
		if (!ended) {
			// Received by GameSessionController.
			EventMessage message = new EventMessage(C.Tag.TO_SELF,
					C.Msg.GAME_RESULT, model.getGameResult());
			EventBus.INSTANCE.broadcast(message);
		}

		ended = true;
	}

	/**
	 * 
	 * @return the model from the Controller.
	 */
	private NumberGame getModel() {
		return controller.getModel();
	}

	@Override
	public void dispose() {
		super.dispose();
		font.dispose();
		numberFont.dispose();
		sound.unregister();
		controller.unregister();
	}
}
