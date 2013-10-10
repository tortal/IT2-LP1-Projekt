package it.chalmers.tendu.gamemodel.shapesgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;

public class ShapesGameSound implements Listener {
	
	private Sound completedGameSound;
	private Sound lostGameSound;
	private Sound succeededSound;
	private Sound failSound;
	
	public ShapesGameSound(){
		completedGameSound = Gdx.audio.newSound(Gdx.files.internal("completed.wav"));
		lostGameSound = Gdx.audio.newSound(Gdx.files.internal("gamelost.wav"));
		succeededSound = Gdx.audio.newSound(Gdx.files.internal("success.wav"));
		failSound = Gdx.audio.newSound(Gdx.files.internal("fail.aiff"));
	}

	@Override
	public void onBroadcast(EventMessage message) {
		// TODO Auto-generated method stub

	}
	
	public void playSoundSuccess(){
		succeededSound.play();
	}
	
	public void playSoundFail(){
		failSound.play();
	}
	
	public void playSoundGameWon(){
		completedGameSound.play();
	}
	
	public void playSoundGameLost(){
		lostGameSound.play();
	}

}
