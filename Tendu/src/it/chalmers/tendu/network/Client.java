package it.chalmers.tendu.network;

import java.util.List;

import it.chalmers.tendu.gamemodel.MiniGame;

public interface Client {
	
	void sendObject(Object o);
	
	
	/**
	 * Joins a team. 
	 */
	void joinGame(MiniGame miniGame);
	
	
	/**
	 * Search for active teams. 
	 */
	List<Server> searchTeam();


}
