package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.defaults.TextLabels;
import it.chalmers.tendu.gamemodel.SessionResult;
import it.chalmers.tendu.gamemodel.SimpleTimer;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class InterimScreen implements Screen {
	private Tendu tendu;
	private BitmapFont font;
	private final int level;
	private float time;
	private TextWidget levelText;
	private TextWidget timeText;
	private SimpleTimer timer;
	
	public InterimScreen(Tendu tendu,  SessionResult sessionResult) {
		this.tendu = tendu;
		font = new BitmapFont(Gdx.files.internal("fonts/menuFont.fnt"),
				Gdx.files.internal("fonts/menuFont.png"), false);
		
		time = sessionResult.getLastResult().getRemainingTime();
		time = time/1000;
		
		level = sessionResult.gamesPlayed();
		
		levelText = new TextWidget(TextLabels.LEVEL + ":  " + level, new Vector2(120, 280), Constants.MENU_FONT_COLOR);
		timeText = new TextWidget(TextLabels.BONUS_TIME + ":  " + time, new Vector2(120, 180), Constants.MENU_FONT_COLOR);
		
		timer = new SimpleTimer();
		timer.start(3000);
	}

	@Override
	public void render() {
		levelText.draw(tendu.spriteBatch, font);
		timeText.draw(tendu.spriteBatch, font);
	}

	@Override
	public void tick(InputController input) {		
		if(timer.getRemainingTime() <= 0) {
			
			// Received by host in GameSessionController.
			EventMessage message = new EventMessage(C.Tag.TO_SELF, C.Msg.INTERIM_FINISHED);
			EventBus.INSTANCE.broadcast(message);
		}
	}

	@Override
	public void removed() {
		font.dispose();
	}

}
