package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.SessionResult;
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
	private float totalTime;
	private long endTime;
	private OnScreenText levelText;
	private OnScreenText timeText;
	private OnScreenText totalTimeText;
	
	public InterimScreen(Tendu tendu,  SessionResult sessionResult) {
		this.tendu = tendu;
		font = new BitmapFont(Gdx.files.internal("fonts/menuFont.fnt"),
				Gdx.files.internal("fonts/menuFont.png"), false);
		time = sessionResult.timePlayedLastGame();
		time = time/1000;
		
		totalTime = sessionResult.totalTimePlayed();
		totalTime = totalTime/1000;
		
		level = sessionResult.gamesPlayed();
		endTime = System.currentTimeMillis() + 3000;
		
		levelText = new OnScreenText("Level: " + level, new Vector2(120, 380));
		timeText = new OnScreenText("Time: " + time, new Vector2(120, 280));
		totalTimeText = new OnScreenText("Total time: " + totalTime, new Vector2(120, 180));
		
	}

	@Override
	public void render() {
		levelText.draw(tendu.spriteBatch, font);
		timeText.draw(tendu.spriteBatch, font);
		totalTimeText.draw(tendu.spriteBatch, font);


	}

	@Override
	public void tick(InputController input) {		
		if(endTime - System.currentTimeMillis() <= 0) {
			EventMessage message = new EventMessage(C.Tag.TO_SELF, C.Msg.INTERIM_FINISHED);
			EventBus.INSTANCE.broadcast(message);
		}
	}

	@Override
	public void removed() {
		font.dispose();
	}

}
