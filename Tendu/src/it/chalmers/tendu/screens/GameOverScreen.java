package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.SessionResult;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

		gameOver = new TextWidget("GAME OVER!", new Vector2(120, 420));
		levelText = new TextWidget("You reached level: " + level, new Vector2(
				120, 380));

		mainMenu = new TextWidget("Main menu", new Vector2(65, 230));
		replay = new TextWidget("GoGoGo", new Vector2(65, 130));
	}

	@Override
	public void render() {
		gameOver.draw(tendu.spriteBatch, font);
		levelText.draw(tendu.spriteBatch, font);
		mainMenu.draw(tendu.spriteBatch, font);
		replay.draw(tendu.spriteBatch, font);
	}

	@Override
	public void tick(InputController input) {
		if (input.isTouchedUp()) {
			if (mainMenu.collided(input.getCoordinates())) {
				// TODO: back to main menu, restart network
			}

			if (replay.collided(input.getCoordinates())) {
				EventMessage message = new EventMessage(C.Tag.TO_SELF, C.Msg.PLAYER_READY);
				EventBus.INSTANCE.broadcast(message);
			}

			mainMenu.setColor(Color.WHITE);
			replay.setColor(Color.WHITE);

		} else if (input.isTouchedDown()) {
			if (mainMenu.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				mainMenu.setColor(Color.LIGHT_GRAY);
			}

			if (replay.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				replay.setColor(Color.LIGHT_GRAY);
			}
		}
	}

	@Override
	public void removed() {
		font.dispose();
	}

}
