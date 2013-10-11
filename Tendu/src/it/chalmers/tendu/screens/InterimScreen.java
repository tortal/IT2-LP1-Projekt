package it.chalmers.tendu.screens;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.GameResult;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;

public class InterimScreen implements Screen {
	private Tendu tendu;
	private List<GameResult> gameResults;
	private BitmapFont font;
	private final int level;
	private final float time;
	private float totalTime;
	private int timer = 0;
	
	public InterimScreen(Tendu tendu,  List<GameResult> gameResults) {
		this.tendu = tendu;
		this.gameResults = gameResults;
		font = new BitmapFont();
		level = gameResults.size();
		time = gameResults.get(level-1).getTimeSpent()/1000;
		
		for(GameResult result: gameResults) {
			totalTime = totalTime + result.getTimeSpent();
		}
		
		totalTime = totalTime/1000;
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		font.draw(tendu.spriteBatch, "Level: " + level, 105, 400);
		font.draw(tendu.spriteBatch, "Time: " + gameResults.size(), 105, 300);
		font.draw(tendu.spriteBatch, "Total time: " + gameResults.size(), 105, 200);

	}

	@Override
	public void tick(InputController input) {
//		time++;
//		
//		if(time == 240) {
//			EventMessage message = new EventMessage(C.Tag.TO_SELF, C.Msg.)
//			EventBus.INSTANCE.broadcast(message);
//		}
	}

	@Override
	public void removed() {
		// TODO Auto-generated method stub
		font.dispose();
	}

}
