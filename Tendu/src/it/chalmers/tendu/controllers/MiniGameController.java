package it.chalmers.tendu.controllers;

import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;

public interface MiniGameController extends Listener {
	
	public void handleAsClient(EventMessage message);
	
	public void handleAsHost(EventMessage message);
	
	public MiniGame getModel();
}
