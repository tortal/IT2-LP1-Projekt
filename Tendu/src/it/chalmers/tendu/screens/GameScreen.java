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

/** Abstract screen class that can be extended by all minigame screens */
public abstract class GameScreen implements Screen {
	protected Tendu tendu; // reference to the main Tendu object
	protected MiniGame model; // model of current minigame
	private ShapeRenderer shapeRenderer; // used to render vector graphics
	private int count; // used to count renders for events that should be
	protected BitmapFont font;

	private Sound completedGameSound;
	private Sound lostGameSound;

	// makes sure end sounds only plays once
	private boolean playCompletedSound = true;

	/**
	 * @param game
	 *            Tendu object that creates the screen
	 * @param model
	 *            MiniGame model
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

	/** all rendering goes here **/
	public void render() {
		if (model.checkGameState() == GameState.WAITING) {
			// TODO drawWaiting();
			// TODO Maybe unnecessary
			return;
		} else if (model.checkGameState() == GameState.RUNNING) {
			// checks if time has run out and if so changes the game state to
			// lost
			// TODO maybe not in render, but in tick instead?
			model.checkGameOver();

			// draw common graphics while game runs, hud, timer etc...
			shapeRenderer.setProjectionMatrix(tendu.getCamera().combined);
			shapeRenderer.begin(ShapeType.FilledRectangle);

			// currently does nothing
			if (count == 0) {
				shapeRenderer.setColor(Color.YELLOW);
			} else {
				shapeRenderer.setColor(Color.RED);
				count--;

			}
			// Gdx.app.log("Quota", calculateTimerWidth() + "");
			shapeRenderer.filledRect(50, 50, calculateTimerWidth(), 6);
			shapeRenderer.end();
			//TODO refactor
			
			for(int i = 1; i < model.getNumberOfPlayers(); i++) {
				renderPlayerIndicator(i);
			}
			
		} else {
			showGameResult();
		}

	}

	/** All game logic goes here */
	public abstract void tick(InputController input);

	/**
	 * clean up goes here make sure to call super() if overriden
	 */
	public void removed() {
		shapeRenderer.dispose();
		completedGameSound.dispose();
		font.dispose();
		lostGameSound.dispose();
	}

	/**
	 * Calculates the width of the countdown time relative to the total amount
	 * of time
	 */
	private int calculateTimerWidth() {
		double quota = (double) model.getTimeLeft()
				/ (double) model.getGameTime();
		double timerWitdth = Math.abs(quota * (Constants.SCREEN_WIDTH - 100));
		return (int) timerWitdth;

	}

	// TODO maybe the model could remove time without involving the screen?
	// TODO not currently used, remove?
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
	// TODO: show only connected players
	/**
	 * Renders a visual indicator for respective player
	 */
	public void renderPlayerIndicator(int player) {
		font.scale(-2);
		// Player 1
		if (player == 1) {
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
		} else if (player == 2) {

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
		} else if (player == 3) {

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

		font.scale(2);

	}

	/**
	 * Call when game ends shows either a success or a failure message and plays
	 * the corresponding sound
	 */
	public void showGameResult() {
		if (model.checkGameState() == GameState.WON) {
			font.setColor(Color.GREEN);
			font.scale(2);
			font.draw(tendu.spriteBatch, "You won!", 300, 300);
			font.scale(-2);
			if (playCompletedSound) {
				completedGameSound.play();
				playCompletedSound = false;
			}
		} else if (model.checkGameState() == GameState.LOST) {
			font.setColor(Color.RED);
			font.scale(2);
			font.draw(tendu.spriteBatch, "You Lost!", 300, 300);
			font.scale(-2);
			if (playCompletedSound) {
				lostGameSound.play();
				playCompletedSound = false;
			}
		}
	}
}
