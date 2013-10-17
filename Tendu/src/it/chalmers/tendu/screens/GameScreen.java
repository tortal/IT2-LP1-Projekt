package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.defaults.PlayerColors;
import it.chalmers.tendu.gamemodel.GameState;
import it.chalmers.tendu.gamemodel.MiniGame;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * GameScreen is the main rendering class of a {@link MiniGame}.
 * 
 */
public abstract class GameScreen implements Screen {

	final Tendu tendu; // reference to the main Tendu object
	MiniGame model; // model of current minigame
	final private ShapeRenderer shapeRenderer; // used to render vector graphics
	private BitmapFont font;
	private List<Integer> otherPlayers; // 1 = left, 0 = top, 2 = right, TODO not sure about that


	/**
	 * Returns a {@link Screen} of the given {@link MiniGame}.
	 * 
	 * @param tendu
	 * @param model
	 *            The game to draw.
	 */
	public GameScreen(Tendu tendu, MiniGame model) {
		this.tendu = tendu;
		this.model = model;
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
		
		otherPlayers = model.getOtherPlayerNumbers();
		
//		for(int i = 0; i < otherPlayers.size(); i++) {
//			otherPlayers.set(i, otherPlayers.get(i).intValue()+1);
//		}
	}

	@Override
	public void render() {
		// draw common graphics while game runs, hud, timer etc...
		shapeRenderer.setProjectionMatrix(tendu.getCamera().combined);
		
		//Draw the timer
		drawTimer();
		renderPlayerIndicator();
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
	
	private void drawTimer() {
		shapeRenderer.begin(ShapeType.FilledRectangle);
		shapeRenderer.setColor(PlayerColors.getPlayerColor(model.getplayerNbr()));
		shapeRenderer.filledRect(50, 40, calculateTimerWidth(), 10);
		shapeRenderer.end();
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
			shapeRenderer.setColor(PlayerColors.getPlayerColor(otherPlayers.get(0)));
			shapeRenderer.filledRect(0, Constants.SCREEN_HEIGHT - 5,
					Constants.SCREEN_WIDTH, 5);
			shapeRenderer.end();

			shapeRenderer.begin(ShapeType.FilledCircle);
			shapeRenderer.setColor(PlayerColors.getPlayerColor(otherPlayers.get(0)));
			shapeRenderer.filledCircle(Constants.SCREEN_WIDTH / 2,
					Constants.SCREEN_HEIGHT - 5, 29);
			shapeRenderer.end();

			font.setColor(com.badlogic.gdx.graphics.Color.BLACK);
			font.draw(tendu.spriteBatch, (otherPlayers.get(0)+1) + "",
					Constants.SCREEN_WIDTH / 2 - 4,
					Constants.SCREEN_HEIGHT - 10);

		}
		if (otherPlayers.size() >= 2) {
			// second player
			shapeRenderer.begin(ShapeType.FilledRectangle);
			shapeRenderer.setColor(PlayerColors.getPlayerColor(otherPlayers.get(1)));
			shapeRenderer.filledRect(0, 0, 5, Constants.SCREEN_HEIGHT);
			shapeRenderer.end();

			shapeRenderer.begin(ShapeType.FilledCircle);
			shapeRenderer.setColor(PlayerColors.getPlayerColor(otherPlayers.get(1)));
			shapeRenderer.filledCircle(0, Constants.SCREEN_HEIGHT / 2, 29);
			shapeRenderer.end();

			font.setColor(com.badlogic.gdx.graphics.Color.BLACK);
			font.draw(tendu.spriteBatch, (otherPlayers.get(1)+1) + "", 10,
					Constants.SCREEN_HEIGHT / 2 + 5);

		}
		if (otherPlayers.size() >= 3) {
			// third player
			shapeRenderer.begin(ShapeType.FilledRectangle);
			shapeRenderer.setColor(PlayerColors.getPlayerColor(otherPlayers.get(2)));
			shapeRenderer.filledRect(Constants.SCREEN_WIDTH - 5, 0, 5,
					Constants.SCREEN_HEIGHT);
			shapeRenderer.end();

			shapeRenderer.begin(ShapeType.FilledCircle);
			shapeRenderer.setColor(PlayerColors.getPlayerColor(otherPlayers.get(2)));
			shapeRenderer.filledCircle(Constants.SCREEN_WIDTH - 5,
					Constants.SCREEN_HEIGHT / 2, 29);
			shapeRenderer.end();

			font.setColor(com.badlogic.gdx.graphics.Color.BLACK);
			font.draw(tendu.spriteBatch, (otherPlayers.get(2)+1) + "",
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
}
