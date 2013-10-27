package it.chalmers.tendu.controller;

import it.chalmers.tendu.event.C;
import it.chalmers.tendu.event.C.Msg;
import it.chalmers.tendu.event.C.Tag;
import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.gamemodel.GameId;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;

import com.badlogic.gdx.Gdx;

public class NumberGameController implements MiniGameController {

	private static final String TAG = "NumberGameController";
	private static final int PENALTY_TIME = -3000;
	private NumberGame numberGame;

	/**
	 * Creates a new Controller for NumberGame. Changes the model and receives
	 * broadcasts from the network.
	 * 
	 * @param model
	 */
	public NumberGameController(NumberGame model) {
		numberGame = model;
		EventBus.INSTANCE.addListener(this);

	}

	@Override
	public void onBroadcast(EventMessage message) {
		if (Player.getInstance().isHost()) {
			handleAsHost(message);
		} else {
			Gdx.app.log(TAG, "Message: " + (message == null));
			handleAsClient(message);
		}
	}

	@Override
	public void handleAsHost(EventMessage message) {
		if (message.tag == C.Tag.CLIENT_REQUESTED
				|| message.tag == C.Tag.TO_SELF) {

			if (message.msg == C.Msg.START_MINI_GAME) {
				numberGame.startGame();
			}

			// *********NUMBER GAME***********
			if (message.msg == C.Msg.NUMBER_GUESS) {
				if (numberGame.checkNbr((Integer) message.content)) {

					// Received by NumberGameSound.
					EventMessage soundMsg = new EventMessage(C.Tag.TO_SELF,
							C.Msg.SOUND_SUCCEED);
					EventBus.INSTANCE.broadcast(soundMsg);

					// Received by clients in NumberGameController through
					// the network.
					EventMessage changedMessage = new EventMessage(message,
							C.Tag.COMMAND_AS_HOST);
					EventBus.INSTANCE.broadcast(changedMessage);

					numberGame.guessedCorrectly();
				} else {

					// Received by NumberGameSound.
					EventMessage soundMsg = new EventMessage(C.Tag.TO_SELF,
							C.Msg.SOUND_FAIL);
					EventBus.INSTANCE.broadcast(soundMsg);

					// Received by clients in NumberGameController through
					// the network.
					EventMessage newMessage = new EventMessage(
							Tag.COMMAND_AS_HOST, Msg.REMOVE_TIME,
							GameId.NUMBER_GAME);
					EventBus.INSTANCE.broadcast(newMessage);

					numberGame.changeTime(PENALTY_TIME);
				}
			}

//			else if (message.msg == C.Msg.START_MINI_GAME_TIMER) {
//				numberGame.startGameTimer();
//			}
//
//			else if (message.msg == C.Msg.STOP_MINI_GAME_TIMER) {
//				numberGame.stopTimer();
//			}
		}
	}

	@Override
	public void handleAsClient(EventMessage message) {
		if (message.tag == C.Tag.TO_SELF) {
			// *********NUMBER GAME***********
			if (message.msg == C.Msg.NUMBER_GUESS) {
				EventMessage changedMessage = new EventMessage(message,
						C.Tag.REQUEST_AS_CLIENT);
				EventBus.INSTANCE.broadcast(changedMessage);
			}

		} else if (message.msg == C.Msg.START_MINI_GAME) {
			numberGame.startGame();
		}

//		else if (message.msg == C.Msg.START_MINI_GAME_TIMER) {
//			numberGame.startGameTimer();
//		}
//
//		else if (message.msg == C.Msg.STOP_MINI_GAME_TIMER) {
//			numberGame.stopTimer();
//		}

		if (message.tag == Tag.HOST_COMMANDED) {
			// *********NUMBER GAME***********
			if (message.msg == Msg.UPDATE_MODEL) {
				// TODO: Not used
				// Gdx.app.log(TAG, " Time left = " +
				// gameSession.currentMiniGame.getTimeLeft());

			} else if (message.msg == Msg.REMOVE_TIME) {
				numberGame.changeTime(PENALTY_TIME);

			} else if (message.msg == Msg.NUMBER_GUESS) {
				if (numberGame.checkNbr((Integer) message.content)) {

					// Received by NumberGameSound.
					EventMessage soundMsg = new EventMessage(C.Tag.TO_SELF,
							C.Msg.SOUND_SUCCEED);
					EventBus.INSTANCE.broadcast(soundMsg);

					numberGame.guessedCorrectly();
				} else {

					// Received by NumberGameSound.
					EventMessage soundMsg = new EventMessage(C.Tag.TO_SELF,
							C.Msg.SOUND_FAIL);
					EventBus.INSTANCE.broadcast(soundMsg);
				}
			}
		}
	}

	@Override
	public NumberGame getModel() {
		return numberGame;
	}

	@Override
	public void unregister() {
		EventBus.INSTANCE.removeListener(this);
	}
}
