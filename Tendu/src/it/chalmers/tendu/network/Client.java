package it.chalmers.tendu.network;

import it.chalmers.tendu.gamemodel.MiniGame;

import java.util.List;

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
