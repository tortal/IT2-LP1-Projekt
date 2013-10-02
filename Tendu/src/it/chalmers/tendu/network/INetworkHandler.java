package it.chalmers.tendu.network;


import java.io.Serializable;

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
	int pollNetworkState();
	
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