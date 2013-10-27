package it.chalmers.tendu.screen;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controller.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.defaults.PlayerColors;
import it.chalmers.tendu.gamemodel.MiniGame;

import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * GameScreen is the main rendering class of a {@link MiniGame}. And should be
 * extended by most minigame screens. It renders the game timer and visual
 * indicators for all other players. If a certain mini game would not benefit
 * from extending this class its mini game screen could choose to just implement
 * screen instead.
 */
public abstract class GameScreen implements Screen {

	MiniGame model; // model of current minigame
	final private ShapeRenderer shapeRenderer; // used to render vector graphics
	private List<Integer> otherPlayers; // list of other participating players,
										// used to render their visual
										// indicators

	/**
	 * Returns a {@link Screen} of the given {@link MiniGame}.
	 * 
	 * @param tendu
	 * @param model
	 *            The game to draw.
	 */
	public GameScreen(MiniGame model) {
		this.model = model;
		shapeRenderer = new ShapeRenderer();

		otherPlayers = model.getOtherPlayerNumbers();
	}

	@Override
	public void render(SpriteBatch spriteBatch, OrthographicCamera camera) {
		// draw common graphics while game runs, hud, timer etc...
		shapeRenderer.setProjectionMatrix(camera.combined);

		// Draw the timer
		drawTimer();
		renderPlayerIndicator();
	}

	/** All game logic goes here */
	@Override
	public abstract void tick(InputController input);

	/**
	 * clean up goes here make sure to call super() if overriden
	 */
	@Override
	public void dispose() {
		shapeRenderer.dispose();
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

	/**
	 * Draws the timer
	 */
	private void drawTimer() {
		shapeRenderer.begin(ShapeType.FilledRectangle);
		shapeRenderer
				.setColor(PlayerColors.getPlayerColor(model.getplayerNbr()));
		shapeRenderer.filledRect(50, 40, calculateTimerWidth(), 10);
		shapeRenderer.end();
	}

	/**
	 * Renders a visual indicator for respective player
	 */
	public void renderPlayerIndicator() {
		// First player
		if (otherPlayers.size() >= 1) {
			shapeRenderer.begin(ShapeType.FilledRectangle);
			shapeRenderer.setColor(PlayerColors.getPlayerColor(otherPlayers
					.get(0)));
			shapeRenderer.filledRect(0, Constants.SCREEN_HEIGHT - 5,
					Constants.SCREEN_WIDTH, 5);
			shapeRenderer.end();

		}
		if (otherPlayers.size() >= 2) {
			// second player
			shapeRenderer.begin(ShapeType.FilledRectangle);
			shapeRenderer.setColor(PlayerColors.getPlayerColor(otherPlayers
					.get(1)));
			shapeRenderer.filledRect(0, 0, 5, Constants.SCREEN_HEIGHT);
			shapeRenderer.end();

		}
		if (otherPlayers.size() >= 3) {
			// third player
			shapeRenderer.begin(ShapeType.FilledRectangle);
			shapeRenderer.setColor(PlayerColors.getPlayerColor(otherPlayers
					.get(2)));
			shapeRenderer.filledRect(Constants.SCREEN_WIDTH - 5, 0, 5,
					Constants.SCREEN_HEIGHT);
			shapeRenderer.end();

		}
	}
}
