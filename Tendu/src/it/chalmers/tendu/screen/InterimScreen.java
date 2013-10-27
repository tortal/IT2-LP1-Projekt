package it.chalmers.tendu.screen;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controller.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.defaults.TextLabels;
import it.chalmers.tendu.event.C;
import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.SessionResult;
import it.chalmers.tendu.gamemodel.SimpleTimer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * The InterimScreen is a simple screen displayed between {@link MiniGame }s
 * showing the current level and the amount of bonus time carried over to the
 * next minigame.
 * 
 * It simply runs for 3 seconds and is then replaced by the next minigame (by
 * the host)
 */
public class InterimScreen implements Screen {
	private BitmapFont font;
	private final int level;
	private float time;
	private TextWidget levelText;
	private TextWidget timeText;
	private SimpleTimer timer;
	private boolean finishedSent;

	/**
	 * 
	 * @param tendu
	 *            main tendu object
	 * @param sessionResult
	 *            results from the current session so far
	 */
	public InterimScreen(SessionResult sessionResult) {
		font = new BitmapFont(Gdx.files.internal("fonts/menuFont.fnt"),
				Gdx.files.internal("fonts/menuFont.png"), false);

		time = sessionResult.getLastResult().getRemainingTime();
		time = time / 1000; // the time is in milliseconds but we display it in
							// seconds

		level = sessionResult.gamesPlayed(); // games completed

		levelText = new TextWidget(TextLabels.LEVEL + ":  " + level,
				new Vector2(120, 280), Constants.MENU_FONT_COLOR);
		timeText = new TextWidget(TextLabels.BONUS_TIME + ":  " + time,
				new Vector2(120, 180), Constants.MENU_FONT_COLOR);

		// timer that will run for 3 seconds
		timer = new SimpleTimer();
		timer.start(3000);

		finishedSent = false;

	}

	@Override
	public void render(SpriteBatch spriteBatch, OrthographicCamera camera) {
		levelText.draw(spriteBatch, font);
		timeText.draw(spriteBatch, font);
	}

	@Override
	public void tick(InputController input) {
		if (timer.isDone() && !finishedSent) {

			// Received by host in GameSessionController.
			EventMessage message = new EventMessage(C.Tag.TO_SELF,
					C.Msg.INTERIM_FINISHED);
			EventBus.INSTANCE.broadcast(message);
			// make sure to send only once
			finishedSent = true;
		}
	}

	@Override
	public void dispose() {
		font.dispose();
	}

}
