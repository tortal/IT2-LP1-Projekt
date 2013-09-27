package it.chalmers.tendu.network;

import it.chalmers.tendu.gamemodel.GameStateBundle;

import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

public interface INetworkHandler {
	
	/**
	 * Send object to remote device
	 * @param o
	 */
	void sendObject(Serializable o);
	
	/**
	 *  Returns the game state
	 * @return
	 */
	GameStateBundle pollGameState();
	
	/**
	 * Returns the network state
	 * @return
	 */
	NetworkState pollNetworkState();
	
	/**
	 * Host game that other players can search
	 * for. 
	 */
	void hostSession();
	
	/**
	 * Joins a team. 
	 */
	void joinGame(); 	//Should probably have a game or a player as argument.
	
	
	/**
	 * Method to be called when exiting app
	 */
	void destroy();
	
	///** Test method */
	//void testSendGameState(GameStateBundle state);
	
	/** Test method */
	void testStuff();

	 

//	/**
//	 * Search for active teams. 
//	 */
//	List<Object> searchTeam();

}