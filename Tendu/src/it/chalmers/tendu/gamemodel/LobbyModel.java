package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.controllers.GameSessionController;

import java.util.HashMap;
import java.util.Map;

public class LobbyModel {
	
	public Map<String, Integer> players;
	public Map<Integer, Boolean> playerReady;
	public int maxPlayers;

	public LobbyModel(int maxPlayers) {
		players = new HashMap<String, Integer>();
		playerReady = new HashMap<Integer, Boolean>();
		this.maxPlayers = maxPlayers;
	}

	private LobbyModel() {
		// For kryo
	}

//	public boolean isHost() {
//		if (Player.getInstance().getMac().equals(hostMacAddress)) {
//			return true;
//		}
//		return false;
//	}

//	public void addHost(String myMacAddress) {
//		this.hostMacAddress = myMacAddress;
//		addPlayer(myMacAddress);
//
//	}

	public void playerReady(String player, boolean ready) {
		int playerNbr = players.get(player);
		playerReady.put(playerNbr, ready);
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
//		GameSession gameSession = new GameSession(players, hostMacAddress);
		GameSession gameSession = new GameSession(players);
		new GameSessionController(gameSession);
	}

	public Map<String, Integer> getLobbyMembers() {
		return new HashMap<String, Integer>(players);
	}

	public void addPlayer(String macAddress) {
		// connect mac id with player
		players.put(macAddress, players.size());
	}

	public boolean isMaxPlayersConnected() {
		return players.keySet().size() == maxPlayers;
	}
}
