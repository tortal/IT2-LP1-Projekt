package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.controllers.ModelController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyModel {

	public String hostMacAddress;
	public Map<String, Integer> players;
	public Map<Integer, Boolean> playerReady;

	public LobbyModel() {
		players = new HashMap<String, Integer>();
		playerReady = new HashMap<Integer, Boolean>();
	}

	public boolean isHost() {
		if (Player.getInstance().getMac().equals(hostMacAddress)) {
			return true;
		}
		return false;
	}

	public void addHost(String myMacAddress) {
		this.hostMacAddress = myMacAddress;
	}

	public void playerReady(String player, boolean ready) {
		// TODO
//		playerReady.put(player, ready);
	}

	public boolean allPlayersReady() {
		boolean allReady = true;
		for (Boolean rdy : playerReady.values()) {
			if (!rdy)
				allReady = false;
		}
		return allReady;
	}

	public void createGameSession() {
		GameSession gameSession = new GameSession(players, hostMacAddress);
		new ModelController(gameSession);
	}

	public Map<String, Integer> getLobbyMembers() {
		return new HashMap<String, Integer>(players);
	}

	public void addPlayers(List<String> macAddress) {
		// connect mac id with player
		for (int i = 0; i < macAddress.size(); i++) {
			players.put(macAddress.get(i), (Integer) i);
		}
	}

}
