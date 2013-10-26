package it.chalmers.tendu.network;

import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.gamemodel.LobbyModel;

public interface INetworkHandler {

	/**
	 * Starts a Game Lobby that other players can connect to. See
	 * {@link LobbyModel}.
	 */
	public void hostSession();

	/**
	 * Connects the client to a lobby.
	 */
	public void joinLobby(); // TODO: Why? ->
						// "Should probably have a game or a player as argument."

	/**
	 * Transmits an {@link EventMessage} to connected devices.
	 * 
	 * @param message
	 *            to be sent over the implemented network.
	 */
	public void broadcastMessageOverNetwork(EventMessage message);

	/**
	 * Method to be called when exiting the application.
	 */
	public void destroy();

	/** Test method */
	void testSendMessage();

	/**
	 * Returns this unit's MAC address
	 */
	public String getMacAddress();

	/** Gets called in onPause() in the libgdx lifecycle */
	void onPause();

	/** Gets called in onPause() in the libgdx lifecycle */
	void onResume();

	/**
	 * Returns network to virgin state (i.e. release all threads and dispose all
	 * connections)
	 */
	public void resetNetwork();

	/**
	 * Halts this device from accepting any additional connection attempts. No more
	 * matchmaking!
	 */
	public void stopAcceptingConnections();

	/**
	 * Method for easing multiple simultaneous testing of app
	 * 
	 * @return
	 */
	public int toggleHostNumber();
}
