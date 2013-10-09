package it.chalmers.tendu.gamemodel;

import it.chalmers.tendu.controllers.GameSessionController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyModel {

	public Map<String, Integer> players;
	public List<String> playerReady;
	public int maxPlayers;

	public LobbyModel(int maxPlayers) {
		players = new HashMap<String, Integer>();
		playerReady = new ArrayList<String>();
		this.maxPlayers = maxPlayers;
	}

	// For reflection
	@SuppressWarnings("unused")
	private LobbyModel() {
	}

	public void playerReady(String player) {
		if (!playerReady.contains(player)) {
			playerReady.add(player);
		}
	}

	public boolean arePlayersReady() {
		return (players.size() == playerReady.size());
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
