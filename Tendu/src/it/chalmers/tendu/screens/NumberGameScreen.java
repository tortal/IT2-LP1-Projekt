package it.chalmers.tendu.screens;

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
import com.badlogic.gdx.math.Vector2;

/** GameScreen for the number minigame. Contains all graphics, sounds etc. **/
public class NumberGameScreen extends GameScreen {
	private ArrayList<Color> colors; // list with colors for all numbers
	private ArrayList<Integer> guessNumbers; // the correct numbers

	private ArrayList<Integer> numbers; // the correct numbers
	private SimpleTimer instructionsTimer; // used to time how long the
											// instructions should be displayed
	private SimpleTimer gameCompletedTimer; // makes sure the game does not end
											// the millisecond you've one or
											// lost
	private int numberAlignment; // start position of first number to the left
									// on the screen
	private NumberGameController controller;

	private NumberGameSound sound;

	private BitmapFont font;
	private BitmapFont numberFont;

	private TextWidget memorizeText;
	private TextWidget instructionText;
	private ArrayList<TextWidget> guessNumbersWidgets;
	private TextWidget numberWidget;

	/**
	 * @param tendu
	 *            the applicationlistener
	 * @param model
	 *            the MiniGame model associated with the screen
	 */
	public NumberGameScreen(Tendu tendu, MiniGame model) {
		super(tendu, model);

		controller = new NumberGameController((NumberGame) model);
		font = new BitmapFont(Gdx.files.internal("fonts/menuFont.fnt"),
				Gdx.files.internal("fonts/menuFont.png"), false);
		numberFont = new BitmapFont(Gdx.files.internal("fonts/digitalTendu.fnt"),
				Gdx.files.internal("fonts/digitalTendu.png"), false);
		sound = new NumberGameSound();

		setUpGame();
	}

	/**
	 * Initial setup
	 */
	private void setUpGame() {
		instructionsTimer = new SimpleTimer();
		gameCompletedTimer = new SimpleTimer();

		memorizeText = new TextWidget("Memorize the numbers", new Vector2(295,
				595));
		instructionText = new TextWidget(

				"Enter the numbers in the correct order", new Vector2(50, 595));

		guessNumbers = new ArrayList<Integer>();
		numbers = new ArrayList<Integer>();
		guessNumbersWidgets = new ArrayList<TextWidget>();
		numberWidget = new TextWidget("Numbers will be drawn with this widget", new Vector2(0,470));

		// TODO more natural colors
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
			guessNumbers.add(getModel().getMyList().get(i));
			guessNumbersWidgets.add(new TextWidget(getModel().getMyList()
					.get(i).toString(), new Vector2(72 + 140 * i, 130), colors
					.get(i), -0.15f));

			guessNumbersWidgets.get(i).expandHeight(5);
			guessNumbersWidgets.get(i).expandWidth(5);
		}

		// TODO check number of numbers instead
		if (model.getDifficulty() == Constants.Difficulty.ONE) {
			numberAlignment = 240;
		} else if (model.getDifficulty() == Constants.Difficulty.TWO) {
			numberAlignment = 50;
		} else if (model.getDifficulty() == Constants.Difficulty.THREE) {
			numberAlignment = 240;
		} else if (model.getDifficulty() == Constants.Difficulty.FOUR) {
			numberAlignment = 50;
		} else if (model.getDifficulty() == Constants.Difficulty.FIVE) {
			numberAlignment = 50;
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
		int spacing = 150;
		int start = Constants.SCREEN_WIDTH/2 - (numbers.size()-1)*spacing/2;
		
		if (showAll) {
			for (int i = 0; i < numbers.size() ; i++) {
				numberWidget.setColor(colors.get(i));
				numberWidget.setText(numbers.get(i).toString());
				numberWidget.setX(start+i*spacing);
				numberWidget.drawAtCenterPoint(tendu.spriteBatch, numberFont);
			}
		} else {
			for (int i = 0; i < numbers.size() ; i++)  {
				if (getModel().getAnsweredNbrs().contains(numbers.get(i))) {
					numberWidget.setColor(colors.get(i));
					numberWidget.setText(numbers.get(i).toString());
					numberWidget.setX(start+i*spacing);
					numberWidget.drawAtCenterPoint(tendu.spriteBatch, numberFont);
				}
			}
		}
	}

	/**
	 * Draws all guess numbers
	 */
	private void drawGuessNumbers() {
		for (int i = 0; i < guessNumbers.size(); i++) {
			guessNumbersWidgets.get(i).draw(tendu.spriteBatch, numberFont);
		}
	}

	/** Draw all graphics from here */
	@Override
	public void render() {
		if (model.checkGameState() != GameState.WAITING) {
			super.render(); // draws common ui-stuff

			if (!instructionsTimer.isDone()) {
				memorizeText.draw(tendu.spriteBatch, font);
				drawNumbers(true);

			} else {
				font.setColor(Color.BLUE);
				instructionText.draw(tendu.spriteBatch, font);

				drawNumbers(false);
				drawGuessNumbers();
			}
		}

	}

	/** All game logic goes here */
	@Override
	public void tick(InputController input) {
		model = getModel(); // make sure we have the new model (the host might
							// have changed it)

		if (model.checkGameState() != GameState.RUNNING) {
			if (model.checkGameState() == GameState.WON
					|| model.checkGameState() == GameState.LOST) {
				gameCompletedTimer.start(1500);

				if (gameCompletedTimer.isDone()) {
					EventMessage message = new EventMessage(C.Tag.TO_SELF,
							C.Msg.GAME_RESULT, model.getGameResult());
					EventBus.INSTANCE.broadcast(message);
				}
			}

			return;

		} else if (model.checkGameState() == GameState.RUNNING) {
			instructionsTimer.start(4000); // only starts once

			if (instructionsTimer.isDone()) {
				model.startGameTimer();
				if (input.isTouchedUp()) {
					for (int i = 0; i < guessNumbers.size(); i++) {
						if (guessNumbersWidgets.get(i).collided(
								input.getCoordinates())) {
							EventBus.INSTANCE.broadcast(new EventMessage(
									C.Tag.TO_SELF, C.Msg.NUMBER_GUESS, model
											.getGameId(), guessNumbers.get(i)));
						}
						guessNumbersWidgets.get(i).setScale(-0.15f);
						guessNumbersWidgets.get(i).setY(130);
						
					}
				}

				if (input.isTouchedDown()) {
					for (int i = 0; i < guessNumbers.size(); i++) {
						if (guessNumbersWidgets.get(i).collided(
								input.getCoordinates())) {
							Gdx.input.vibrate(25);
							guessNumbersWidgets.get(i).setScale(0.2f);
							guessNumbersWidgets.get(i).setY(145);
							;
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
	}
}
