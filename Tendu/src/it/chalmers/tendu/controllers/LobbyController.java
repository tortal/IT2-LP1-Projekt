package it.chalmers.tendu.controllers;

import it.chalmers.tendu.gamemodel.LobbyModel;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;

import java.util.List;

public class LobbyController implements Listener {

	private LobbyModel model;

	public LobbyController(LobbyModel model) {
		this.model = model;
	}

	@Override
	public void onBroadcast(EventMessage message) {
		if (model.isHost()) {
			// IM HOSTING
			switch (message.msg) {
			case PLAYERS_CONNECTED:
				// get mac addresses for each player.
				List<String> list = (List<String>) message.content;
				model.addPlayers(list);
				EventBus.INSTANCE.broadcast(new EventMessage(
						C.Tag.COMMAND_AS_HOST, C.Msg.LOBBY_READY));
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
