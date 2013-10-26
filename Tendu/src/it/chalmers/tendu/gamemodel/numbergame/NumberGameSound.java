package it.chalmers.tendu.gamemodel.numbergame;

import it.chalmers.tendu.event.C;
import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.event.EventBusListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Class for sounds in NumberGame
 * @author johannahartman
 *
 */

public class NumberGameSound implements EventBusListener {
	
	private Sound completedGameSound;
	private Sound lostGameSound;
	private Sound succeededSound;
	private Sound failSound;
	
	public NumberGameSound(){
		
		EventBus.INSTANCE.addListener(this);
		
		completedGameSound = Gdx.audio.newSound(Gdx.files.internal("completed.wav"));
		lostGameSound = Gdx.audio.newSound(Gdx.files.internal("gamelost.wav"));
		succeededSound = Gdx.audio.newSound(Gdx.files.internal("success.wav"));
		failSound = Gdx.audio.newSound(Gdx.files.internal("fail.wav"));
	}

	@Override
	public void onBroadcast(EventMessage message) {
		
		if (message.tag == C.Tag.TO_SELF) {
			if (message.msg == C.Msg.SOUND_WIN) {
				playSoundGameWon();
			}else if(message.msg == C.Msg.SOUND_LOST){
				playSoundGameLost();
			}else if(message.msg == C.Msg.SOUND_SUCCEED){
				playSoundSuccess();
			}else if(message.msg == C.Msg.SOUND_FAIL){
				playSoundFail();
			}
		}
		
	}
	/**
	 * Plays sound when player succeed
	 */
	public void playSoundSuccess(){
		succeededSound.play();
	}
	/**
	 * Plays sound when player fail
	 */
	public void playSoundFail(){
		failSound.play();
	}
	/**
	 * Plays sound when player win
	 */
	public void playSoundGameWon(){
		completedGameSound.play();
	}
	/**
	 * Plays sound when player loose
	 */
	public void playSoundGameLost(){
		lostGameSound.play();
	}
	/**
	 * Releases all the sound resources.
	 */
	@Override
	public void unregister() {
		EventBus.INSTANCE.removeListener(this);
		completedGameSound.dispose();
		lostGameSound.dispose();
		succeededSound.dispose();
		failSound.dispose();
	}

}
