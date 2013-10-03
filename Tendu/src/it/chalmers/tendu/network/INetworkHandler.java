package it.chalmers.tendu.network;



public interface INetworkHandler {
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
	 * Send object to remote device
	 * @param o
	 */
	void sendMessage(NetworkMessage message);
	
	/**
	 * Method to be called when exiting app
	 */
	void destroy();
	
	/** Test method */
	void testStuff();

//	/**
//	 * Search for active teams. 
//	 */
//	List<Object> searchTeam();

//	/**
//	 *  Returns the game state
//	 * @return
//	 */
//	GameStateBundle pollGameState();
	
//	/**
//	 * Returns the network state
//	 * @return
//	 */
//	int pollNetworkState();
}

