package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.gamemodel.GameState;
import it.chalmers.tendu.gamemodel.MiniGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/** Abstract screen class that can be extended by all minigame and menu screens */
public abstract class GameScreen implements Screen {
	public Tendu tendu; // reference to the main Tendu object
	public MiniGame model; // model of current minigame
	private ShapeRenderer shapeRenderer; // used to render vector graphics
	private int count; // used to count renders for events that should be
	private BitmapFont font;

	private Sound completedGameSound;
	private Sound lostGameSound;

	// displayed a short time.

	/**
	 * @param game
	 *            Tendu object that creates the screen
	 * @param model
	 *            MiniGame model, set to null if not used (menu)
	 * @return a new GameScreen
	 */
	public GameScreen(Tendu tendu, MiniGame model) {
		this.tendu = tendu;
		this.model = model;

		completedGameSound = Gdx.audio.newSound(Gdx.files
				.internal("completed.wav"));
		lostGameSound = Gdx.audio.newSound(Gdx.files.internal("gamelost.wav"));

		if (model != null) {
			model.startGame();
		}
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
	}

	/**
	 * Call to change current screen to a new screen Only use when the current
	 * screen is finished
	 * 
	 * @param screen
	 *            the new screen
	 */
	protected void setScreen(GameScreen screen) {
		tendu.setScreen(screen);
	}

	/** all rendering goes here **/
	public void render() {
		if(model.checkGameState() != GameState.LOADING) {
			// TODO drawLoading();
			return;
		}
		
		model.checkGame();

		if (model.checkGameState() == GameState.RUNNING) {
			shapeRenderer.setProjectionMatrix(tendu.getCamera().combined);
			shapeRenderer.begin(ShapeType.FilledRectangle);

			if (count == 0) {
				shapeRenderer.setColor(Color.YELLOW);
			} else {
				shapeRenderer.setColor(Color.RED);
				count--;

			}
			// Gdx.app.log("Quota", calculateTimerWidth() + "");
			shapeRenderer.filledRect(50, 50, calculateTimerWidth(), 6);
			shapeRenderer.end();
			renderPlayerIndicators();
		}

	}

	/** All game logic goes here */
	public abstract void tick(InputController input);

	/**
	 * clean up goes here make sure to call super() if overriden
	 */
	public void removed() {
	}

	private int calculateTimerWidth() {
		double quota = (double) model.getTimeLeft()
				/ (double) model.getGameTime();
		double timerWitdth = Math.abs(quota * (Constants.SCREEN_WIDTH - 100));
		return (int) timerWitdth;

	}

	// TODO maybe the model could remove time without involving the screen?
	/**
	 * Removes time for the user and shows this buy changing color on the timer.
	 * 
	 * @param time
	 */
	public void loseTime(int time) {
		count = 60;
		model.changeTimeWith(-time);
	}

	// TODO: could probably look better.
	public void renderPlayerIndicators() {
		// Player 1
		shapeRenderer.begin(ShapeType.FilledRectangle);
		shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.BLUE);
		shapeRenderer.filledRect(0, Constants.SCREEN_HEIGHT - 5,
				Constants.SCREEN_WIDTH, 5);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.FilledCircle);
		shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.BLUE);
		shapeRenderer.filledCircle(Constants.SCREEN_WIDTH / 2,
				Constants.SCREEN_HEIGHT - 5, 29);
		shapeRenderer.end();

		font.setColor(com.badlogic.gdx.graphics.Color.BLACK);
		font.draw(tendu.spriteBatch, "1", Constants.SCREEN_WIDTH / 2 - 4,
				Constants.SCREEN_HEIGHT - 10);

		// Player 2
		shapeRenderer.begin(ShapeType.FilledRectangle);
		shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.RED);
		shapeRenderer.filledRect(0, 0, 5, Constants.SCREEN_HEIGHT);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.FilledCircle);
		shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.RED);
		shapeRenderer.filledCircle(0, Constants.SCREEN_HEIGHT / 2, 29);
		shapeRenderer.end();

		font.setColor(com.badlogic.gdx.graphics.Color.BLACK);
		font.draw(tendu.spriteBatch, "2", 10,
				Constants.SCREEN_HEIGHT / 2 + 5);

		// Player 3
		shapeRenderer.begin(ShapeType.FilledRectangle);
		shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.GREEN);
		shapeRenderer.filledRect(Constants.SCREEN_WIDTH - 5, 0, 5,
				Constants.SCREEN_HEIGHT);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.FilledCircle);
		shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.GREEN);
		shapeRenderer.filledCircle(Constants.SCREEN_WIDTH - 5,
				Constants.SCREEN_HEIGHT / 2, 29);
		shapeRenderer.end();

		font.setColor(com.badlogic.gdx.graphics.Color.BLACK);
		font.draw(tendu.spriteBatch, "3", Constants.SCREEN_WIDTH - 13,
				Constants.SCREEN_HEIGHT / 2 + 5);

	}

	public void showGameResult() {
		if (model.checkGameState() == GameState.WON) {
			font.setColor(Color.GREEN);
			font.scale(2);
			font.draw(tendu.spriteBatch, "You won!", 300, 300);
			font.scale(-2);
			completedGameSound.play();
		} else if (model.checkGameState() == GameState.LOST) {
			font.setColor(Color.RED);
			font.scale(2);
			font.draw(tendu.spriteBatch, "You Lost!", 300, 300);
			font.scale(-2);
			lostGameSound.play();
		}
	}

}
