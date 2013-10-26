package it.chalmers.tendu.gamemodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model of the network lobby. When users are connecting, before game has
 * started, the players wait in the lobby. Not until ALL players in the lobby
 * are marked as ready may the game begin.
 * 
 */
public class LobbyModel {

	public final Map<String, Integer> players;
	public final List<String> playerReady;
	public int maxPlayers;

	/**
	 * @param maxPlayers
	 *            The max number of accepted players into this lobby (note:
	 *            Tendus games are built for a maximum of 4 players.)
	 */
	public LobbyModel(int maxPlayers) {
		players = new HashMap<String, Integer>();
		playerReady = new ArrayList<String>();
		this.maxPlayers = maxPlayers;
	}

	/** Used for reflection */
	@SuppressWarnings("unused")
	private LobbyModel() {
		playerReady = null;
		players = null;
	}

	/**
	 * Enter a player as ready to start a game.
	 * 
	 * @param player
	 */
	public void playerReady(String player) {
		if (!playerReady.contains(player)) {
			playerReady.add(player);
		}
	}

	/**
	 * Checks if all players that are connected are ready to start a game.
	 * 
	 * @return
	 */
	public boolean arePlayersReady() {
		return (players.size() == playerReady.size());
	}

	/**
	 * Returns a list with all players that are connected.
	 * 
	 * @return
	 */
	public Map<String, Integer> getLobbyMembers() {
		return new HashMap<String, Integer>(players);
	}

	/**
	 * Add a players as connected by entering their mac addresses.
	 * 
	 * @param macAddress
	 */
	public void addPlayer(String macAddress) {
		players.put(macAddress, players.size());
	}

	/**
	 * Checks if maximum number of possible players are connected.
	 * 
	 * @return
	 */
	public boolean isMaxPlayersConnected() {
		return players.keySet().size() == maxPlayers;
	}

	public void removePlayer(String playerMac) {
		players.remove(playerMac);
	}
}
