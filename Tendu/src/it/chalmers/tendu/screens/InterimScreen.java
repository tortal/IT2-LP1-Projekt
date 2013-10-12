package it.chalmers.tendu.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.GameResult;
import it.chalmers.tendu.gamemodel.SessionResult;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;

public class InterimScreen implements Screen {
	private Tendu tendu;
	private BitmapFont font;
	private final int level;
	private float time;
	private float totalTime;
	private long endTime;
	
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
		
	}

	@Override
	public void render() {
		font.draw(tendu.spriteBatch, "Level: " + level, 120, 380);
		font.draw(tendu.spriteBatch, "Time: " + time, 120, 280);
		font.draw(tendu.spriteBatch, "Total time: " + totalTime, 120, 180);

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
