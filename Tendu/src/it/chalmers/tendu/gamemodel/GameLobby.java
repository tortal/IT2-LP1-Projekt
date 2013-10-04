package it.chalmers.tendu.gamemodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.Listener;
import it.chalmers.tendu.tbd.EventMessage;

public class GameLobby implements Listener {

	Map<Integer, String> players = new HashMap<Integer, String>();

	public GameSession getGameSession() {
		return new GameSession(players);
	}

	private void addPlayers(List<String> macAddress) {
		// connect mac id with player
		for (int i = 0; i < macAddress.size(); i++) {
			players.put((Integer)i, macAddress.get(i));
		}
	}

	@Override
	public void onBroadcast(EventMessage message) {
		// TODO Auto-generated method stub
		if (message.msg.equals(C.Msg.PLAYERS_CONNECTED)) {
			//get mac addresses for each player.
			List<String> list = (List)message.content;
			addPlayers(list);
			EventBus.INSTANCE.broadcast(new EventMessage(null, C.Msg.LOBBY_READY));
		}
	}
}
