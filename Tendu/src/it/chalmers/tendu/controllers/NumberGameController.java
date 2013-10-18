package it.chalmers.tendu.controllers;

import it.chalmers.tendu.gamemodel.GameId;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.screens.GameScreen;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.C.Msg;
import it.chalmers.tendu.tbd.C.Tag;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;

import com.badlogic.gdx.Gdx;

public class NumberGameController implements MiniGameController {

	private static final String TAG = "NumberGameController";
	private static final int PENALTY_TIME = -3000;
	private NumberGame numberGame;

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
			if (message.gameId == GameId.NUMBER_GAME) {
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
			}
		}
	}

	@Override
	public void handleAsClient(EventMessage message) {
		if (message.tag == C.Tag.TO_SELF) {
			// *********NUMBER GAME***********
			if (message.gameId == GameId.NUMBER_GAME) {
				if (message.msg == C.Msg.NUMBER_GUESS) {
					EventMessage changedMessage = new EventMessage(message,
							C.Tag.REQUEST_AS_CLIENT);
					EventBus.INSTANCE.broadcast(changedMessage);
				}

			} else if (message.msg == C.Msg.START_MINI_GAME) {
				numberGame.startGame();
			}
		}

		if (message.tag == Tag.HOST_COMMANDED) {
			// *********NUMBER GAME***********
			if (message.gameId == GameId.NUMBER_GAME) {

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

	}

	public NumberGame getModel() {
		return numberGame;
	}

	@Override
	public void unregister() {
		EventBus.INSTANCE.removeListener(this);
	}
}
