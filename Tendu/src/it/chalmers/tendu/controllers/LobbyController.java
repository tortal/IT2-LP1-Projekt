package it.chalmers.tendu.controllers;

import it.chalmers.tendu.gamemodel.LobbyModel;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;

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
			// IM HOSTING
			switch (message.msg) {
			case ALL_PLAYERS_CONNECTED:
				
				// get mac addresses for each player.
				List<String> list = (List<String>) message.content;
				model.addPlayers(list);
				EventBus.INSTANCE.broadcast(new EventMessage(
						C.Tag.COMMAND_AS_HOST, C.Msg.LOBBY_READY));
				break;
			case PLAYER_READY:
				model.playerReady(Player.getInstance().getMac(), true);
			default:
				Gdx.app.error(TAG, "Incorrect C.msg broadcasted");
				break;
			}
		} else {
			switch (message.msg) {

			default:
				break;
			}
			// NOT HOST
		}
	}

}
