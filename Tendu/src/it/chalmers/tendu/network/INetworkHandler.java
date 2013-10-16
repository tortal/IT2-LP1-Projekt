package it.chalmers.tendu.network;

import it.chalmers.tendu.tbd.EventMessage;

public interface INetworkHandler {
	/**
	 * Host game that other players can search for.
	 */
	void hostSession();

	/**
	 * Joins a team.
	 */
	void joinGame(); // Should probably have a game or a player as argument.

	/**
	 * Send object to remote device
	 * 
	 * @param o
	 */
	void broadcastMessageOverNetwork(EventMessage message);

	/**
	 * Method to be called when exiting app
	 */
	void destroy();

	/** Test method */
	void testStuff();

	/** Returns the units mac address */
	public String getMacAddress();

	/** Returns network to virgin state */
	public void resetNetwork();
	
	/** Stops the network from accepting any 
	 * more incoming connections. No more matchmaking */
	public void stopAcceptingConnections();
	// /**
	// * Search for active teams.
	// */
	// List<Object> searchTeam();

	// /**
	// * Returns the game state
	// * @return
	// */
	// GameStateBundle pollGameState();

	// /**
	// * Returns the network state
	// * @return
	// */
	// int pollNetworkState();
}
