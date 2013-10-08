package it.chalmers.tendu.controllers;

import it.chalmers.tendu.gamemodel.GameId;
import it.chalmers.tendu.gamemodel.GameSession;
import it.chalmers.tendu.gamemodel.LobbyModel;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;
import it.chalmers.tendu.tbd.C.Msg;
import it.chalmers.tendu.tbd.C.Tag;

import java.util.List;

import com.badlogic.gdx.Gdx;

public class LobbyController implements Listener {
	public static final String TAG = "LobbyController";

	private LobbyModel model;

	public LobbyController(LobbyModel model) {
		this.model = model;
	}

	@Override
	public void onBroadcast(EventMessage message) {
		if (model.isHost()) {
			handleAsHost(message);
		} else {
			Gdx.app.log(TAG, "Message: " + (message == null));
			handleAsClient(message);
		}
	}

	private void handleAsHost(EventMessage message) {
		if (message.tag == C.Tag.CLIENT_REQUESTED
				|| message.tag == C.Tag.ACCESS_MODEL) {
			switch (message.msg) {
			case PLAYER_CONNECTED:
				model.addPlayer((String)message.content);
				EventBus.INSTANCE.broadcast(new EventMessage(C.Tag.COMMAND_AS_HOST, C.Msg.UPDATE_LOBBY_MODEL, model));
				if(model.allConnected()){
					GameSession gameSession = new GameSession();
					new ModelController(gameSession);
					gameSession.setCurrentMiniGame(gameSession.getNextMiniGame());
					EventBus.INSTANCE.broadcast(new EventMessage(C.Tag.COMMAND_AS_HOST, C.Msg.GAME_SESSION));
				}
				break;
			case ALL_PLAYERS_CONNECTED:

				// get mac addresses for each player.
				List<String> list = (List<String>) message.content;
//				model.addPlayer(list);

				// Create GameSeeion and ModelController and choose miniGame
				// TODO maybe this should be done in LobbyModel.
				GameSession gameSession = new GameSession();
				new ModelController(gameSession);
				gameSession.setCurrentMiniGame(gameSession.getNextMiniGame());
				
				// Tells everybody to create a GameSession and a ModelController
				EventBus.INSTANCE.broadcast(new EventMessage(
						C.Tag.COMMAND_AS_HOST, C.Msg.LOBBY_READY));

				// push updated GameSession to clients
				message = new EventMessage(Tag.COMMAND_AS_HOST,
						Msg.UPDATE_MODEL, GameId.NUMBER_GAME, gameSession);

				// Update LobbyModel
				EventBus.INSTANCE
						.broadcast(new EventMessage(C.Tag.COMMAND_AS_HOST,
								C.Msg.UPDATE_LOBBY_MODEL, model));
				break;
			case PLAYER_READY:

				// Message content is the players macID
				model.playerReady((String) message.content, true);
				EventBus.INSTANCE
						.broadcast(new EventMessage(C.Tag.COMMAND_AS_HOST,
								C.Msg.UPDATE_LOBBY_MODEL, model));
				
				// Start the game for all players if they are ready.
				if (model.allPlayersReady()) {
					EventBus.INSTANCE.broadcast(new EventMessage(
							C.Tag.COMMAND_AS_HOST, C.Msg.START_MINI_GAME));
				}
			default:
				Gdx.app.error(TAG, "Incorrect C.msg broadcasted");
				break;
			}
		}
	}

	private void handleAsClient(EventMessage message) {
		if (message.tag == C.Tag.ACCESS_MODEL) {
			if (message.msg == C.Msg.PLAYER_READY) {
				model.playerReady(Player.getInstance().getMac(), true);
				EventBus.INSTANCE.broadcast(new EventMessage(
						C.Tag.REQUEST_AS_CLIENT, C.Msg.PLAYER_READY, Player
								.getInstance().getMac()));
			}

			if (message.tag == Tag.HOST_COMMANDED) {
				if (message.msg == Msg.LOBBY_READY) {
					createGameSession();
				}
			}
		}
	}

	private void createGameSession() {
		GameSession gameSession = new GameSession();
		new ModelController(gameSession);
	}
}
