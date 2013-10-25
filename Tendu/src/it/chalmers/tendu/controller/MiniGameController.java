package it.chalmers.tendu.controller;

import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.event.Listener;
import it.chalmers.tendu.gamemodel.MiniGame;

public interface MiniGameController extends Listener {
	
	public void handleAsClient(EventMessage message);
	
	public void handleAsHost(EventMessage message);
	
	public MiniGame getModel();
}
