package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.controllers.GameSessionController;

import java.awt.Container;
import java.util.HashMap;
import java.util.Map;

public class LobbyModel {

	public Map<String, Integer> players;
	public Map<String, Boolean> playerReady;
	public int maxPlayers;

	public LobbyModel(int maxPlayers) {
		players = new HashMap<String, Integer>();
		playerReady = new HashMap<String, Boolean>();
		this.maxPlayers = maxPlayers;
	}

	// For reflection
	@SuppressWarnings("unused")
	private LobbyModel() {
	}

	public void setPlayerReady(String mac) {
		playerReady.put(mac, true);
	}

	public boolean arePlayersReady() {

		for (String p : players.keySet()) {
			if (playerReady.get(p) == null || playerReady.get(p) == false)
				return false;
		}

		// All players are ready
		return true;
	}

	public void createGameSession() {
		// GameSession gameSession = new GameSession(players, hostMacAddress);
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
