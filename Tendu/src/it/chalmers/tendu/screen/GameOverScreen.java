package it.chalmers.tendu.screen;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controller.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.defaults.TextLabels;
import it.chalmers.tendu.event.C;
import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.gamemodel.SessionResult;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class GameOverScreen implements Screen {

	private Tendu tendu;
	private BitmapFont font;
	private final int level;
	private TextWidget gameOver;
	private TextWidget levelText;
	private TextWidget mainMenu;
	private TextWidget replay;

	public GameOverScreen(Tendu tendu, SessionResult sessionResult) {

		this.tendu = tendu;
		
		font = new BitmapFont(Gdx.files.internal("fonts/menuFont.fnt"),
				Gdx.files.internal("fonts/menuFont.png"), false);

		level = sessionResult.gamesPlayed();

		gameOver = new TextWidget(TextLabels.GAME_OVER, new Vector2(
				Constants.SCREEN_WIDTH / 2, 640), Constants.MENU_FONT_COLOR);
		
		levelText = new TextWidget(TextLabels.LEVEL_REACHED +": " + level, new Vector2(
				Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 2 + 60), Constants.MENU_FONT_COLOR);

		mainMenu = new TextWidget(TextLabels.MAIN_MENU, new Vector2(100, 150), Constants.MENU_FONT_COLOR);
		
		replay = new TextWidget(TextLabels.PLAY_AGAIN, new Vector2(800, 150), Constants.MENU_FONT_COLOR);
	}

	@Override
	public void render() {
		gameOver.drawAtCenterPoint(tendu.spriteBatch, font);
		levelText.drawAtCenterPoint(tendu.spriteBatch, font);
		mainMenu.draw(tendu.spriteBatch, font);
		replay.draw(tendu.spriteBatch, font);
	}

	@Override
	public void tick(InputController input) {
		if (input.isTouchedUp()) {
			if (mainMenu.collided(input.getCoordinates())) {
				
				// TODO: Restart network
				// Received by GameSessionController.
				EventMessage message = new EventMessage(C.Tag.TO_SELF,
						C.Msg.RETURN_MAIN_MENU);
				EventBus.INSTANCE.broadcast(message);
			}

			if (replay.collided(input.getCoordinates())) {
				
				// Received by GameSessionController.
				EventMessage message = new EventMessage(C.Tag.TO_SELF,
						C.Msg.PLAYER_REPLAY_READY, Player.getInstance()
								.getMac());
				EventBus.INSTANCE.broadcast(message);
			}

			mainMenu.setColor(Constants.MENU_FONT_COLOR);
			replay.setColor(Constants.MENU_FONT_COLOR);

		} else if (input.isTouchedDown()) {
			if (mainMenu.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				mainMenu.setColor(Constants.MENU_FONT_COLOR_PRESSED);
			}

			if (replay.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				replay.setColor(Constants.MENU_FONT_COLOR_PRESSED);
			}
		}
	}

	@Override
	public void dispose() {
		font.dispose();
	}

}
