package it.chalmers.tendu.network;

import java.util.List;

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
	 * Search for active teams. 
	 */
	List<Object> searchTeam();
}
