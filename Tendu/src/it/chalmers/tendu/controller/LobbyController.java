package it.chalmers.tendu.controller;

import it.chalmers.tendu.event.C;
import it.chalmers.tendu.event.C.Msg;
import it.chalmers.tendu.event.C.Tag;
import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventBusListener;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.gamemodel.GameSession;
import it.chalmers.tendu.gamemodel.LobbyModel;
import it.chalmers.tendu.gamemodel.Player;

import com.badlogic.gdx.Gdx;

/**
 * Lobby controller handles the lobby model and creates a game session when all
 * player are connected and ready.
 */
public class LobbyController implements EventBusListener {
	public static final String TAG = "LobbyController";

	private LobbyModel model;

	/**
	 * Creates a new Controller for LobbyModel. Changes the model and receives
	 * broadcasts from the network.
	 * 
	 * @param model
	 */
	public LobbyController(LobbyModel model) {
		EventBus.INSTANCE.addListener(this);
		this.model = model;
	}

	/**
	 * Receives messages from the eventbus and directs them to the appropriate
	 * methods.
	 */
	@Override
	public void onBroadcast(EventMessage message) {
		if (Player.getInstance().isHost()) {
			handleAsHost(message);
		} else {
			Gdx.app.log(TAG, "Message: " + (message == null));
			handleAsClient(message);
		}
	}

	/**
	 * Messages from eventbus are handled here if the player is host.
	 * 
	 * @param message
	 *            from eventbus.
	 */
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
				model.playerReady(playerMac);

				// Received by clients in LobbyController through the network.
				EventMessage updateModel = new EventMessage(
						C.Tag.COMMAND_AS_HOST, C.Msg.UPDATE_LOBBY_MODEL, model);
				EventBus.INSTANCE.broadcast(updateModel);

				// Start the game for all players if they are ready.
				if (model.arePlayersReady()) {
					Gdx.app.log(TAG, "ALL PLAYERS ARE READY");

					// Received by Tendu.
					EventMessage stopMessage = new EventMessage(C.Tag.TO_SELF,
							C.Msg.STOP_ACCEPTING_CONNECTIONS);
					EventBus.INSTANCE.broadcast(stopMessage);

					GameSession gameSession = new GameSession(
							model.getLobbyMembers());

					// MiniGame miniGame = gameSession.getNextMiniGame();
					// gameSession.setCurrentMiniGame(miniGame);

					new GameSessionController(gameSession);

					// Received by clients in LobbyController through the
					// network.
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
		} else if (message.tag == C.Tag.NETWORK_NOTIFICATION) {
			if (message.msg == C.Msg.PLAYER_DISCONNECTED) {
				model.removePlayer((String) message.content);
			}
		}
	}

	/**
	 * Messages from eventbus are handled here if the player is client.
	 * 
	 * @param message
	 *            from eventbus.
	 */
	private void handleAsClient(EventMessage message) {
		if (message.tag == C.Tag.TO_SELF) {

			if (message.msg == C.Msg.PLAYER_READY) {
				// Received by host in LobbyController through the network.
				EventMessage msg = new EventMessage(C.Tag.REQUEST_AS_CLIENT,
						C.Msg.PLAYER_READY, Player.getInstance().getMac());
				EventBus.INSTANCE.broadcast(msg);
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

	/**
	 * @return the current lobby model.
	 */
	public LobbyModel getModel() {
		return model;
	}

	@Override
	public void unregister() {
		EventBus.INSTANCE.removeListener(this);
	}
}
