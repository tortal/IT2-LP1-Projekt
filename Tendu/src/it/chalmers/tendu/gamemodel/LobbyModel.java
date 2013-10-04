package it.chalmers.tendu.gamemodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyModel {

	private Map<Integer, String> players;
	private Map<Integer, Boolean> playerReady;
	public boolean isHost;

	public LobbyModel() {
		players = new HashMap<Integer, String>();
		playerReady = new HashMap<Integer, Boolean>();
		isHost = false;
	}

	public boolean isHost() {
		return isHost;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
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
		if (players.size() > 0)
			return new GameSession(players);
		else
			return null;
	}

	public Map<Integer, String> getLobbyMembers() {
		return new HashMap<Integer, String>(players);
	}

	public void addPlayers(List<String> macAddress) {
		// connect mac id with player
		for (int i = 0; i < macAddress.size(); i++) {
			players.put((Integer) i, macAddress.get(i));
		}
	}

}
