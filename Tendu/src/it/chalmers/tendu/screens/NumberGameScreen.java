package it.chalmers.tendu.screens;

//TODO needs major refactoring
import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.gamemodel.GameId;
import it.chalmers.tendu.gamemodel.GameState;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
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
import com.badlogic.gdx.math.Vector3;


/** GameScreen for the number minigame. Contains all graphics, sounds etc. **/
public class NumberGameScreen extends GameScreen {
	public static final int TEMP_PLAYERNUMBER = 0; // Temporary hardcoded player number
	
	private ShapeRenderer shapeRenderer; // used to render vector graphics
	private BitmapFont numberFont; // for rendering fonts
	private NumberGame model; // Model for current minigame (number)

	private ArrayList<Color> colors;

	private ArrayList<NumberCircle> numberCircles;
	private ArrayList<Integer> numbers;

	private Vector3 touchPos; // used to store coordinates for on screen touches

	private int time; // used to time certain "events" during the game.
	private int numberAlignment; // start position of first number to the left
									// on the screen

	public NumberGameScreen(Tendu game, MiniGame model) {
		super(game, model);

		shapeRenderer = new ShapeRenderer();

		numberFont = new BitmapFont();
		touchPos = new Vector3();
		this.model = (NumberGame) model;

		setUpGame();
	}

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

		for (Integer number : model.getAnswerList()) {
			numbers.add(number.intValue());
		}

		// TODO chooses a static player atm.
		for (int i = 0; i < model.getPlayerList(TEMP_PLAYERNUMBER).size(); i++) {
			numberCircles.add(new NumberCircle(model.getPlayerList(TEMP_PLAYERNUMBER).get(i),
					(90 + 95 * i), 120, 35, colors.get(i)));
		}

		if (model.getDifficulty() == Constants.Difficulty.ONE) {
			numberAlignment = 240;
		} else {
			numberAlignment = 25;
		}
	}

	private void drawNumbers(boolean showAll) {
		numberFont.scale(1.6f);

		if (showAll) {
			for (int i = 0; i < numbers.size(); i++) {
				numberFont.setColor(colors.get(i));
				numberFont.draw(game.spriteBatch, "" + numbers.get(i),
						numberAlignment + i * 105, 300);
			}
		} else {
			for (int i = 0; i < numbers.size(); i++) {
				if (model.getAnsweredNbrs().contains(numbers.get(i))) {
					numberFont.setColor(colors.get(i));
					numberFont.draw(game.spriteBatch, ""
							+ numbers.get(i), numberAlignment + i * 105,
							300);
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
		numberFont.draw(game.spriteBatch, "" + circle.getNumber(),
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
		shapeRenderer.setProjectionMatrix(game.getCamera().combined);
		shapeRenderer.begin(ShapeType.Circle);

		if (time < 240) {
			numberFont.setColor(Color.BLUE);
			numberFont.draw(game.spriteBatch, "Memorize the numbers", 200, 400);
			drawNumbers(true);

		} else {
			if (model.checkGameState() == GameState.RUNNING) {
				numberFont.setColor(Color.BLUE);
				numberFont.draw(game.spriteBatch,
						"Enter the numbers in the correct order", 60, 400);
				
				drawNumbers(false);
				drawNumberCircles();
			}
		}

		if (model.checkGameState() == GameState.WON) {
			numberFont.setColor(Color.GREEN);
			numberFont.scale(2);
			numberFont.draw(game.spriteBatch, "You won!", 300, 300);
			numberFont.scale(-2);
			loadNext();
		} else if (model.checkGameState() == GameState.LOST) {
			numberFont.setColor(Color.RED);
			numberFont.scale(2);
			numberFont.draw(game.spriteBatch, "You Lost!", 300, 300);
			numberFont.scale(-2);
			loadNext();
		}

		shapeRenderer.end();
	}
	
	//TODO remove this method
	//used to create infinite loop of number games for testing
	private void loadNext() {
		if(game.isHost()) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			GameId gameId = game.gameSession.getNextGameId();
			MiniGame nextGame = game.gameSession.getMiniGame(gameId);
			EventMessage evMsg = new EventMessage(C.Tag.COMMAND_AS_HOST, C.Msg.LOAD_THIS_GAME, nextGame);
			EventBus.INSTANCE.broadcast(evMsg);
			
			game.setScreen(MiniGameScreenFactory.createMiniGameScreen(game, game.gameSession.currentMiniGame));
		}
	}

	/** All game logic goes here */
	@Override
	public void tick(InputController input) {
		//TODO maybe not the best solution...
		model = (NumberGame) game.gameSession.currentMiniGame;
		
		if (model.checkGameState() != GameState.RUNNING)
			return;

		if (time < 240) {
			time++;
		} else {
			if (input.isTouchedUp()) {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				game.getCamera().unproject(touchPos);

				for (NumberCircle circle : numberCircles) {
					if (circle.collided(touchPos)) {
						Gdx.input.vibrate(25);
						EventBus.INSTANCE.broadcast(new EventMessage(
								C.Tag.ACCESS_MODEL, C.Msg.NUMBER_GUESS, model
										.getGameId(), circle.getNumber()));
					}
					circle.scale = 1;
				}
			}

			if (input.isTouchedDown()) {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				game.getCamera().unproject(touchPos);

				for (NumberCircle circle : numberCircles) {
					if (circle.collided(touchPos)) {
						circle.scale = 1.5f;
					}
				}
			}
		}
	}

	@Override
	public void removed() {
		super.removed();
		shapeRenderer.dispose();
		numberFont.dispose();
	}
}
