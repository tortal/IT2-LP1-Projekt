package it.chalmers.tendu.gamemodel.shapesgame;

import java.util.List;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.audio.Sound;

import it.chalmers.tendu.gamemodel.GameId;
import it.chalmers.tendu.gamemodel.GameResult;
import it.chalmers.tendu.gamemodel.GameState;
import it.chalmers.tendu.screens.ShapeGameScreen;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;
import it.chalmers.tendu.tbd.C.Msg;
import it.chalmers.tendu.tbd.C.Tag;

/**
 * Class for sounds in ShapesGame
 * @author johannahartman
 * 
 */
public class ShapeGameSound implements Listener {

	private Sound completedGameSound;
	private Sound lostGameSound;
	private Sound succeededSound;
	private Sound failSound;
	private ShapeGame shapeGame;
	
	private final String TAG = "ShapeGameSound";

	public ShapeGameSound(ShapeGame shapeGame) {

		this.shapeGame = shapeGame;
		EventBus.INSTANCE.addListener(this);

		completedGameSound = Gdx.audio.newSound(Gdx.files
				.internal("completed.wav"));
		lostGameSound = Gdx.audio.newSound(Gdx.files.internal("gamelost.wav"));
		succeededSound = Gdx.audio.newSound(Gdx.files.internal("success.wav"));
		failSound = Gdx.audio.newSound(Gdx.files.internal("fail.aiff"));
		
		
	}

	@Override
	public void onBroadcast(EventMessage message) {
		Gdx.app.log(TAG, "broadcasting to sound");
		if (message.tag == C.Tag.TO_SELF) {
			if(message.msg == C.Msg.LOCK_ATTEMPT){
				if(shapeFitIntoLock(message.content)){
					playSoundSuccess();
				}else{
					playSoundFail();
				}
			}else if (message.msg == C.Msg.GAME_RESULT) {
				GameResult result = (GameResult) message.content;
				GameState state = result.getGameState();
				if (state == GameState.WON) {
					playSoundGameWon();
				} else if (state == GameState.LOST) {
					playSoundGameLost();
				}	
			}
		}
	}

	public void playSoundSuccess() {
		succeededSound.play();
	}

	public void playSoundFail() {
		failSound.play();
	}

	public void playSoundGameWon() {
		completedGameSound.play();
	}

	public void playSoundGameLost() {
		lostGameSound.play();
	}
	@Override
	public void unregister() {
		EventBus.INSTANCE.removeListener(this);
	}

	private boolean shapeFitIntoLock(Object content) {
		List<Object> messageContent = (List) content;
		int player = (Integer) messageContent.get(0);
		Shape lockShape = (Shape) messageContent.get(1);
		Shape shape = (Shape) messageContent.get(2);

		return shapeGame.shapeFitIntoLock(player, shape, lockShape);
	}	
}