package it.chalmers.tendu.controllers;

import it.chalmers.tendu.gamemodel.GameSession;
import it.chalmers.tendu.gamemodel.LobbyModel;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.C.Msg;
import it.chalmers.tendu.tbd.C.Tag;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;

import com.badlogic.gdx.Gdx;

public class LobbyController implements Listener {
	public static final String TAG = "LobbyController";

	private LobbyModel model;

	public LobbyController(LobbyModel model) {
		EventBus.INSTANCE.addListener(this);
		this.model = model;
	}

	@Override
	public void onBroadcast(EventMessage message) {
		if (Player.getInstance().isHost()) {
			Gdx.app.log(TAG, "Are we host yet?");
			handleAsHost(message);
		} else {
			Gdx.app.log(TAG, "Message: " + (message == null));
			handleAsClient(message);
		}
	}

	private void handleAsHost(EventMessage message) {

		if (message.tag == C.Tag.CLIENT_REQUESTED
				|| message.tag == C.Tag.TO_SELF) {
			switch (message.msg) {
			case PLAYER_CONNECTED:
				if (model.isMaxPlayersConnected())
					break;

				Gdx.app.log(TAG, "Player connected should be seen on screen");
				model.addPlayer((String) message.content);
				EventBus.INSTANCE
						.broadcast(new EventMessage(C.Tag.COMMAND_AS_HOST,
								C.Msg.UPDATE_LOBBY_MODEL, model));
				break;
			case PLAYER_READY:

				// Message content is the players macID
				String playerMac = (String) message.content;
				model.setPlayerReady(playerMac);

				EventMessage updateModel = new EventMessage(
						C.Tag.COMMAND_AS_HOST, C.Msg.UPDATE_LOBBY_MODEL, model);
				EventBus.INSTANCE.broadcast(updateModel);

				// Start the game for all players if they are ready.
				if (model.arePlayersReady()) {
					Gdx.app.log(TAG, "ALL PLAYERS ARE READY");
					GameSession gameSession = new GameSession(
							model.getLobbyMembers());
					// MiniGame miniGame = gameSession.getNextMiniGame();
					// gameSession.setCurrentMiniGame(miniGame);
					new GameSessionController(gameSession);

					EventMessage newGameSession = new EventMessage(
							C.Tag.COMMAND_AS_HOST, C.Msg.GAME_SESSION_MODEL,
							gameSession);
					EventBus.INSTANCE.broadcast(newGameSession);
					EventBus.INSTANCE.removeListener(this);
				}
				break;
			default:
				Gdx.app.error(TAG, "Incorrect C.msg broadcasted");
				break;
			}
		}
	}

	private void handleAsClient(EventMessage message) {
		if (message.tag == C.Tag.TO_SELF) {

			if (message.msg == C.Msg.PLAYER_READY) {
				EventBus.INSTANCE.broadcast(new EventMessage(
						C.Tag.REQUEST_AS_CLIENT, C.Msg.PLAYER_READY, Player
								.getInstance().getMac()));
			}
		} else if (message.tag == Tag.HOST_COMMANDED) {
			if (message.msg == Msg.GAME_SESSION_MODEL) {
				new GameSessionController((GameSession) message.content);
				EventBus.INSTANCE.removeListener(this);
			}
			if (message.msg == Msg.UPDATE_LOBBY_MODEL) {
				LobbyModel lModel = (LobbyModel) message.content;
				Gdx.app.debug("DEBUG LOBBY", "new Model:"
						+ lModel.players.keySet().toString());
				this.model = lModel;
			}
		}
	}

	public LobbyModel getModel() {
		return model;
	}

	@Override
	public void unregister() {
		EventBus.INSTANCE.removeListener(this);
	}
}
