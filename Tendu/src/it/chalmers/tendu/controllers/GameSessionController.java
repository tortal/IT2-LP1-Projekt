package it.chalmers.tendu.controllers;

import it.chalmers.tendu.gamemodel.GameId;
import it.chalmers.tendu.gamemodel.GameSession;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.C.Msg;
import it.chalmers.tendu.tbd.C.Tag;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;

import com.badlogic.gdx.Gdx;

public class GameSessionController implements Listener {

	private String TAG = getClass().getSimpleName();

	private GameSession gameSession;

	public GameSessionController(GameSession gameSession) {
		this.gameSession = gameSession;
		EventBus.INSTANCE.addListener(this);
	}

	private GameSessionController() {
	}

	public void setModel(GameSession session) {
		this.gameSession = session;
	}

	@Override
	public void onBroadcast(EventMessage message) {

		// if (applicationListener.isHost()) {
		// handleAsHost(message);
		// } else {
		// handleAsClient(message);
		// }

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
				NumberGame game = (NumberGame) gameSession.currentMiniGame;
				if (message.msg == C.Msg.NUMBER_GUESS) {
					if (game.checkNbr((Integer) message.content)) {
						gameSession.setCurrentMiniGame(game);
						// message = new EventMessage(Tag.COMMAND_AS_HOST,
						// Msg.UPDATE_MODEL, GameId.NUMBER_GAME,
						// gameSession.currentMiniGame);
						message.tag = Tag.COMMAND_AS_HOST;
						EventBus.INSTANCE.broadcast(message);
					} else {
						gameSession.setCurrentMiniGame(game);
						message = new EventMessage(Tag.COMMAND_AS_HOST,
								Msg.REMOVE_TIME, GameId.NUMBER_GAME, null);
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
				NumberGame game = (NumberGame) gameSession.currentMiniGame;
				if (message.msg == C.Msg.NUMBER_GUESS) {
					// game.checkNbr((Integer) message.content);
					message.tag = Tag.REQUEST_AS_CLIENT;
					EventBus.INSTANCE.broadcast(message);
				}
			}
		}

		if (message.tag == Tag.HOST_COMMANDED) {
			// *********NUMBER GAME***********
			// TODO do we need to check what MiniGame we are playing in order to
			// update the MiniGameModel?
			if (message.gameId == GameId.NUMBER_GAME) {

				if (message.msg == Msg.UPDATE_MODEL) {
					// NumberGame game = ;
					gameSession
							.setCurrentMiniGame((NumberGame) message.content);
					// Gdx.app.log(TAG, " Time left = " +
					// gameSession.currentMiniGame.getTimeLeft());
				} else if (message.msg == Msg.REMOVE_TIME) {
					gameSession.currentMiniGame.changeTimeWith(-3000);
				} else if (message.msg == Msg.NUMBER_GUESS) {
					NumberGame game = (NumberGame) gameSession.currentMiniGame;
					game.checkNbr((Integer) message.content);
				}
			}
		}
	}

}
