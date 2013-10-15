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

	/**
	 * Enter a player as ready to start a game.
	 * @param player
	 */
	public void playerReady(String player) {
		if (!playerReady.contains(player)) {
			playerReady.add(player);
		}
	}

	/**
	 * Checks if all players that are connected are ready to start a game.
	 * @return
	 */
	public boolean arePlayersReady() {
		return (players.size() == playerReady.size());
	}

	public void createGameSession() {
		// GameSession gameSession = new GameSession(players, hostMacAddress);
		GameSession gameSession = new GameSession(players);
		new GameSessionController(gameSession);
	}

	/**
	 * Returns a list with all players that are connected.
	 * @return
	 */
	public Map<String, Integer> getLobbyMembers() {
		return new HashMap<String, Integer>(players);
	}

	/**
	 * Add a players as connected by entering their mac addresses.
	 * @param macAddress
	 */
	public void addPlayer(String macAddress) {
		// connect mac id with player
		players.put(macAddress, players.size());
	}
	
	/**
	 * Checks if maximum number of possible players are connected.
	 * @return
	 */
	public boolean isMaxPlayersConnected() {
		return players.keySet().size() == maxPlayers;
	}
}
