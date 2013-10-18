package it.chalmers.tendu.controllers;

import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.EventBusListener;

public interface MiniGameController extends EventBusListener {
	
	public void handleAsClient(EventMessage message);
	
	public void handleAsHost(EventMessage message);
	
	public MiniGame getModel();
}
