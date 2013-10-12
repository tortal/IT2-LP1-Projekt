package it.chalmers.tendu.screens;

import java.util.List;

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
	private int timer = 0;
	
	public InterimScreen(Tendu tendu,  SessionResult sessionResult) {
		this.tendu = tendu;
		font = new BitmapFont();
		font.scale(2.5f);
		time = sessionResult.timePlayedLastGame();
		time = time/1000;
		
		totalTime = sessionResult.totalTimePlayed();
		totalTime = totalTime/1000;
		
		level = sessionResult.gamesPlayed();
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		font.draw(tendu.spriteBatch, "Level: " + level, 105, 400);
		font.draw(tendu.spriteBatch, "Time: " + time, 105, 300);
		font.draw(tendu.spriteBatch, "Total time: " + totalTime, 105, 200);

	}

	@Override
	public void tick(InputController input) {
		timer++;
		
		if(timer == 180) {
			EventMessage message = new EventMessage(C.Tag.TO_SELF, C.Msg.INTERIM_FINISHED);
			EventBus.INSTANCE.broadcast(message);
		}
	}

	@Override
	public void removed() {
		// TODO Auto-generated method stub
		font.dispose();
	}

}
