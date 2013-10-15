package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.gamemodel.GameState;
import it.chalmers.tendu.gamemodel.MiniGame;

import java.util.ArrayList;
import java.util.List;

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
	private List<Integer> otherPlayers;

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
		} else if (model.checkGameState() != GameState.WAITING) {
			// draw common graphics while game runs, hud, timer etc...
			shapeRenderer.setProjectionMatrix(tendu.getCamera().combined);
			shapeRenderer.begin(ShapeType.FilledRectangle);

			shapeRenderer.filledRect(50, 50, calculateTimerWidth(), 6);
			shapeRenderer.end();

			otherPlayers = new ArrayList<Integer>();
			for (int i = 1; i < model.getNumberOfPlayers() + 1; i++) {
				if (!(i - 1 == model.getplayerNbr()))
					otherPlayers.add(new Integer(i));
			}
			shapeRenderer.setColor(Color.GRAY);
			renderPlayerIndicator();

		}
	}

	/** All game logic goes here */
	public abstract void tick(InputController input);

	/**
	 * clean up goes here make sure to call super() if overriden
	 */
	public void removed() {
		shapeRenderer.dispose();
		font.dispose();
	}

	/**
	 * Calculates the width of the countdown time relative to the total amount
	 * of time
	 */
	private int calculateTimerWidth() {
		double quota = (double) model.getRemainingTime()
				/ (double) model.getGameTime();
		double timerWitdth = Math.abs(quota * (Constants.SCREEN_WIDTH - 100));
		return (int) timerWitdth;
	}

	// TODO: could probably look better.
	/**
	 * Renders a visual indicator for respective player
	 */
	public void renderPlayerIndicator() {
		font.scale(-2);
		// First player
		if (otherPlayers.size() >= 1) {
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
			font.draw(tendu.spriteBatch, otherPlayers.get(0) + "",
					Constants.SCREEN_WIDTH / 2 - 4,
					Constants.SCREEN_HEIGHT - 10);

		}
		if (otherPlayers.size() >= 2) {
			// second player
			shapeRenderer.begin(ShapeType.FilledRectangle);
			shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.RED);
			shapeRenderer.filledRect(0, 0, 5, Constants.SCREEN_HEIGHT);
			shapeRenderer.end();

			shapeRenderer.begin(ShapeType.FilledCircle);
			shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.RED);
			shapeRenderer.filledCircle(0, Constants.SCREEN_HEIGHT / 2, 29);
			shapeRenderer.end();

			font.setColor(com.badlogic.gdx.graphics.Color.BLACK);
			font.draw(tendu.spriteBatch, otherPlayers.get(1) + "", 10,
					Constants.SCREEN_HEIGHT / 2 + 5);

		}
		if (otherPlayers.size() >= 3) {
			// third player
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
			font.draw(tendu.spriteBatch, otherPlayers.get(2) + "",
					Constants.SCREEN_WIDTH - 13,
					Constants.SCREEN_HEIGHT / 2 + 5);

		}
		font.scale(2);

	}

	/**
	 * Called every frame. Make sure to call super() from subclass
	 */
	public void tick() {
	}

	/**
	 * @return the otherPlayers
	 */
	public List<Integer> getOtherPlayers() {
		return otherPlayers;
	}
}
