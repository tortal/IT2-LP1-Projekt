package it.chalmers.tendu.controllers;

import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;

public interface MiniGameController extends Listener {
	
	void handleAsClient(EventMessage message);
	
	void handleAsHost(EventMessage message);
	
	MiniGame getModel();
}
