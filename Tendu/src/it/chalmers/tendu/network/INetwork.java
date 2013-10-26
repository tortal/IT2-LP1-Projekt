package it.chalmers.tendu.network;

import it.chalmers.tendu.event.EventMessage;

public interface INetwork {
	/**
	 * Host game that other players can search for.
	 */
	void hostSession();

	/**
	 * Joins a team.
	 */
	void joinLobby(); // Should probably have a game or a player as argument.

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
	void testSendMessage();

	/** Returns the units mac address */
	public String getMacAddress();

	/** Gets called in onPause() in the libgdx lifecycle */
	void onPause();

	/** Gets called in onPause() in the libgdx lifecycle */
	void onResume();

	/** Returns network to virgin state */
	public void resetNetwork();
	
	/** Stops the network from accepting any 
	 * more incoming connections. No more matchmaking */
	public void stopAcceptingConnections();
	
	/** Method for easing multiple simultaneous testing of app 
	 * @return */ 
	public int toggleHostNumber();
	
	public void selectWifi();
	
	public void selectBluetooth();
	
}
