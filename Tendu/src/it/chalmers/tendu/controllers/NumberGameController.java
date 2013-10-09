package it.chalmers.tendu.controllers;

import com.badlogic.gdx.Gdx;

import it.chalmers.tendu.gamemodel.GameId;
import it.chalmers.tendu.gamemodel.GameSession;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;
import it.chalmers.tendu.tbd.C.Msg;
import it.chalmers.tendu.tbd.C.Tag;

public class NumberGameController implements Listener {

	private static final String TAG = "NumberGameController";
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

	private void handleAsHost(EventMessage message) {
		if (message.tag == C.Tag.CLIENT_REQUESTED
				|| message.tag == C.Tag.ACCESS_MODEL) {
			if (message.msg == C.Msg.START_MINI_GAME) {
				// TODO: gameSession.startGame();
			}
			// *********NUMBER GAME***********
			if (message.gameId == GameId.NUMBER_GAME) {
				if (message.msg == C.Msg.NUMBER_GUESS) {
					if (numberGame.checkNbr((Integer) message.content)) {
						message.tag = Tag.COMMAND_AS_HOST;
						EventBus.INSTANCE.broadcast(message);
					} else {
						message = new EventMessage(Tag.COMMAND_AS_HOST,
								Msg.REMOVE_TIME, GameId.NUMBER_GAME);
						EventBus.INSTANCE.broadcast(message);
					}
				}
			}
		}
	}

	private void handleAsClient(EventMessage message) {
		if (message.tag == C.Tag.ACCESS_MODEL) {
			// *********NUMBER GAME***********
			if (message.gameId == GameId.NUMBER_GAME) {
				if (message.msg == C.Msg.NUMBER_GUESS) {
					message.tag = Tag.REQUEST_AS_CLIENT;
					EventBus.INSTANCE.broadcast(message);
				}
			}
		}

		if (message.tag == Tag.HOST_COMMANDED) {
			// *********NUMBER GAME***********
			if (message.gameId == GameId.NUMBER_GAME) {

				if (message.msg == Msg.UPDATE_MODEL) {
					// Gdx.app.log(TAG, " Time left = " +
					// gameSession.currentMiniGame.getTimeLeft());
				} else if (message.msg == Msg.REMOVE_TIME) {
					numberGame.changeTimeWith(-3000);
				} else if (message.msg == Msg.NUMBER_GUESS) {
					numberGame.checkNbr((Integer) message.content);
				}
			}
		}
	}

	public NumberGame getModel() {
		return numberGame;
	}
}