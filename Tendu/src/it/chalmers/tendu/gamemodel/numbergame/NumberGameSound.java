package it.chalmers.tendu.gamemodel.numbergame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import it.chalmers.tendu.gamemodel.GameResult;
import it.chalmers.tendu.gamemodel.GameState;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;

/**
 * Class for sounds in NumberGame
 * @author johannahartman
 *
 */

public class NumberGameSound implements Listener {
	
	private Sound completedGameSound;
	private Sound lostGameSound;
	private Sound succeededSound;
	private Sound failSound;
	
	NumberGame numberGame;
	
	public NumberGameSound(NumberGame numberGame){
		
		this.numberGame = numberGame;
		
		EventBus.INSTANCE.addListener(this);
		
		completedGameSound = Gdx.audio.newSound(Gdx.files.internal("completed.wav"));
		lostGameSound = Gdx.audio.newSound(Gdx.files.internal("gamelost.wav"));
		succeededSound = Gdx.audio.newSound(Gdx.files.internal("success.wav"));
		failSound = Gdx.audio.newSound(Gdx.files.internal("fail.aiff"));
	}

	@Override
	public void onBroadcast(EventMessage message) {
		
		if (message.tag == C.Tag.TO_SELF) {
			if (message.msg == C.Msg.GAME_RESULT) {
				GameResult result = (GameResult) message.content;
				GameState state = result.getGameState();
				if (state == GameState.WON) {
					playSoundGameWon();
				} else if (state == GameState.LOST) {
					playSoundGameLost();
				}
			}else if(message.msg == C.Msg.NUMBER_GUESS){
				this.numberGame = (NumberGame) message.content;
				if(numberGame.tempCheckNumber((Integer) message.content)){
					playSoundSuccess();
				}else{
					playSoundFail();
				}
			}
		}
		
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
	
	@Override
	public void unregister() {
		EventBus.INSTANCE.removeListener(this);
	}

}
