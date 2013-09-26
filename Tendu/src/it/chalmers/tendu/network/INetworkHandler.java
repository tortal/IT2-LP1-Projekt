package it.chalmers.tendu.network;

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
	 * Host game that other players can search
	 * for. 
	 */
	void hostSession();
	
	/**
	 * Joins a team. 
	 */
	void joinGame(); 	//Should probably have a game or a player as argument.
	
//	/**
//	 * Search for active teams. 
//	 */
//	List<Object> searchTeam();
	
	/**
	 * Register a listener for network events
	 * @param listener
	 */
	void addListener(PropertyChangeListener listener);
	
	/** 
	 * Remove a listener
	 * @param listener
	 */
	void removeListener(PropertyChangeListener listener);
	 
}
