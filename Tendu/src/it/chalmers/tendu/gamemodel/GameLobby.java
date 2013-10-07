package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;

public class GameLobby implements Listener {

	private Map<Integer, String> players;
	private Map<Integer, Boolean> playerReady;

	public GameLobby() {
		players = new HashMap<Integer, String>();
		playerReady = new HashMap<Integer, Boolean>();
	}

	public boolean playerReady(int player, boolean ready) {
		return playerReady.put(player, ready);
	}

	public boolean allPlayersReady() {
		boolean allReady = true;
		for (Boolean rdy : playerReady.values()) {
			if (!rdy)
				allReady = false;
		}
		return allReady;
	}

	public GameSession getGameSession() {
		//Gdx.app.log("Players = ", "" + players.size());
		//if (players.size() > 0)
			return new GameSession(players);
//		else
//			return null;
	}

	private void addPlayers(List<String> macAddress) {
		// connect mac id with player
		for (int i = 0; i < macAddress.size(); i++) {
			players.put((Integer) i, macAddress.get(i));
		}
	}

	@Override
	public void onBroadcast(EventMessage message) {
		if (message.msg == C.Msg.PLAYERS_CONNECTED) {
			Gdx.app.log("Lobby", "Players Connected");
			// get mac addresses for each player.
			List<String> list = (List<String>) message.content;
			addPlayers(list);
			EventBus.INSTANCE.broadcast(new EventMessage(null, C.Msg.LOBBY_READY));
			//EventBus.INSTANCE.broadcast(new EventMessage(C.Tag.COMMAND_AS_HOST, C.Msg.LOBBY_READY));

		}
	}
}
