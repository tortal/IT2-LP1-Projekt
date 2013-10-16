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
	int numberSpacing;
	int numberAlignment;

	private NumberGameController controller;

	private NumberGameSound sound;

	private BitmapFont font;
	private BitmapFont numberFont;

	private TextWidget memorizeText;
	private TextWidget instructionText;
	private ArrayList<TextWidget> guessNumbersWidgets;
	private ArrayList<TextWidget> numberWidgets;

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
				595), Constants.MENU_FONT_COLOR);
		instructionText = new TextWidget("Enter the numbers in the correct order", new Vector2(50, 595), Constants.MENU_FONT_COLOR);

		guessNumbers = new ArrayList<Integer>();
		numbers = new ArrayList<Integer>();
		guessNumbersWidgets = new ArrayList<TextWidget>();
		numberWidgets = new ArrayList<TextWidget>();

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

		numberSpacing = 150; //pixels between the numbers on screen
		//position of the first number to the left on the screen
		numberAlignment = Constants.SCREEN_WIDTH/2 - (getModel().getAnswerList().size()-1)*numberSpacing/2;
		
		// add the correct number from the answerlist
		for (int i = 0; i <  getModel().getAnswerList().size(); i++) {
			numbers.add(getModel().getAnswerList().get(i));
			numberWidgets.add(new TextWidget(getModel().getAnswerList().get(i).toString(), new Vector2(numberAlignment+i*numberSpacing, 470), colors.get(i)));
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
		

	}

	/**
	 * Draws the correct numbers list to the screen
	 * 
	 * @param showAll
	 *            set to false if only correctly answered numbers should be
	 *            drawn
	 */
	private void drawNumbers(boolean showAll) {
		
		if (showAll) {
			for (int i = 0; i < numbers.size() ; i++) {
				numberWidgets.get(i).draw(tendu.spriteBatch, numberFont);
			}
		} else {
			for (int i = 0; i < numbers.size() ; i++)  {
				if (getModel().getAnsweredNbrs().contains(numbers.get(i))) {
					numberWidgets.get(i).draw(tendu.spriteBatch, numberFont);
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
