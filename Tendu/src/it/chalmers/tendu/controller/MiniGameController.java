package it.chalmers.tendu.controller;

import it.chalmers.tendu.event.EventBusListener;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.gamemodel.MiniGame;

public interface MiniGameController extends EventBusListener {

	/**
	 * Messages from eventbus are handled here if the player is clients.
	 * 
	 * @param message
	 *            from eventbus.
	 */
	public void handleAsClient(EventMessage message);

	/**
	 * Messages from eventbus are handled here if the player is host.
	 * 
	 * @param message
	 *            from eventbus.
	 */
	public void handleAsHost(EventMessage message);

	/**
	 * @return the current minigame model.
	 */
	public MiniGame getModel();
}
