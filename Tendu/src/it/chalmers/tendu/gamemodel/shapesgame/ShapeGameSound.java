package it.chalmers.tendu.gamemodel.shapesgame;

import it.chalmers.tendu.event.C;
import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventBusListener;
import it.chalmers.tendu.event.EventMessage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Class for sounds in ShapesGame
 * 
 */
public class ShapeGameSound implements EventBusListener {

	private static final String FAIL_WAV = "fail.wav";
	private static final String SUCCESS_WAV = "success.wav";
	private static final String GAMELOST_WAV = "gamelost.wav";
	private static final String COMPLETED_WAV = "completed.wav";

	private final Sound completedGameSound;
	private final Sound lostGameSound;
	private final Sound succeededSound;
	private final Sound failSound;

	public ShapeGameSound() {

		EventBus.INSTANCE.addListener(this);

		completedGameSound = Gdx.audio.newSound(Gdx.files
				.internal(COMPLETED_WAV));
		lostGameSound = Gdx.audio.newSound(Gdx.files.internal(GAMELOST_WAV));
		succeededSound = Gdx.audio.newSound(Gdx.files.internal(SUCCESS_WAV));
		failSound = Gdx.audio.newSound(Gdx.files.internal(FAIL_WAV));
	}

	@Override
	public void onBroadcast(EventMessage message) {
		// When a broadcast, checks what sound to play
		if (message.tag == C.Tag.TO_SELF) {
			if (message.msg == C.Msg.SOUND_WIN) {
				playSoundGameWon();
			} else if (message.msg == C.Msg.SOUND_LOST) {
				playSoundGameLost();
			} else if (message.msg == C.Msg.SOUND_SUCCEED) {
				playSoundSuccess();
			} else if (message.msg == C.Msg.SOUND_FAIL) {
				playSoundFail();
			}
		}

	}

	/**
	 * Plays sound when player succeed
	 */
	public void playSoundSuccess() {
		succeededSound.play();
	}

	/**
	 * Plays sound when player fail
	 */
	public void playSoundFail() {
		failSound.play();
	}

	/**
	 * Plays sound when player win
	 */
	public void playSoundGameWon() {
		completedGameSound.play();
	}

	/**
	 * Plays sound when player loose
	 */
	public void playSoundGameLost() {
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
