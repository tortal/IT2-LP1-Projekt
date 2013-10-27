package it.chalmers.tendu.screen;

import it.chalmers.tendu.controller.GameSessionController;
import it.chalmers.tendu.controller.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.defaults.TextLabels;
import it.chalmers.tendu.event.C;
import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.gamemodel.SessionResult;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * The GameOverScreen is a simple screen showing the results of the game session
 * and offering the players the choice of either play again or returning back to
 * the main menu
 * 
 * Unlike the {@link GameScreen }s, GameOverScreen is not associated with an
 * exclusive controller. Instead {@link GameSessionController} acts as the
 * controller for this screen
 */
public class GameOverScreen implements Screen {

	private BitmapFont font;
	private final int level;
	private TextWidget gameOver;
	private TextWidget levelText;
	private TextWidget mainMenu;
	private TextWidget playAgain;

	/**
	 * 
	 * @param tendu
	 *            main tendu object
	 * @param sessionResult
	 *            all the results of the current game round/session
	 */
	public GameOverScreen(SessionResult sessionResult) {

		// load resources and create som TextWidgets for on screen text
		font = new BitmapFont(Gdx.files.internal("fonts/menuFont.fnt"),
				Gdx.files.internal("fonts/menuFont.png"), false);

		level = sessionResult.gamesPlayed(); // number of levels completed

		gameOver = new TextWidget(TextLabels.GAME_OVER, new Vector2(
				Constants.SCREEN_WIDTH / 2, 640), Constants.MENU_FONT_COLOR);

		levelText = new TextWidget(TextLabels.LEVEL_REACHED + ": " + level,
				new Vector2(Constants.SCREEN_WIDTH / 2,
						Constants.SCREEN_HEIGHT / 2 + 60),
				Constants.MENU_FONT_COLOR);

		mainMenu = new TextWidget(TextLabels.MAIN_MENU, new Vector2(100, 150),
				Constants.MENU_FONT_COLOR);

		playAgain = new TextWidget(TextLabels.PLAY_AGAIN,
				new Vector2(800, 150), Constants.MENU_FONT_COLOR);
	}

	@Override
	public void render(SpriteBatch spriteBatch, OrthographicCamera camera) {
		// some simple TextWidgets offering the players information and options
		// to take new actions
		gameOver.drawAtCenterPoint(spriteBatch, font);
		levelText.drawAtCenterPoint(spriteBatch, font);
		mainMenu.draw(spriteBatch, font);
		playAgain.draw(spriteBatch, font);
	}

	@Override
	public void tick(InputController input) {
		if (input.isTouchedUp()) {
			if (mainMenu.collided(input.getCoordinates())) {
				// Return to the main menu
				// Received by GameSessionController.
				EventMessage message = new EventMessage(C.Tag.TO_SELF,
						C.Msg.RETURN_MAIN_MENU);
				EventBus.INSTANCE.broadcast(message);
			}

			if (playAgain.collided(input.getCoordinates())) {
				// broadcast notification that you are ready to play again.
				// Received by GameSessionController.
				EventMessage message = new EventMessage(C.Tag.TO_SELF,
						C.Msg.PLAY_AGAIN_READY, Player.getInstance().getMac());
				EventBus.INSTANCE.broadcast(message);
			}

			// revert back the font colors of the clickable objects in case
			// they've previously have been changed
			mainMenu.setColor(Constants.MENU_FONT_COLOR);
			playAgain.setColor(Constants.MENU_FONT_COLOR);

		} else if (input.isTouchedDown()) {
			// give a small haptic feedback and change the font color when
			// touching a "clickable" option
			if (mainMenu.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				mainMenu.setColor(Constants.MENU_FONT_COLOR_PRESSED);
			}

			if (playAgain.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				playAgain.setColor(Constants.MENU_FONT_COLOR_PRESSED);
			}
		}
	}

	@Override
	public void dispose() {
		font.dispose();
	}

}
