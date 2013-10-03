package it.chalmers.tendu.controllers;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.gamemodel.GameSession;
import it.chalmers.tendu.tbd.Listener;
import it.chalmers.tendu.tbd.Message;

public class ModelController implements Listener {
	private GameSession session;
	private Tendu game;
	
	public ModelController(Tendu game, GameSession session) {
		this.game = game;
		this.session = session;
	}
	
	public void setModel(GameSession session) {
		this.session = session;
	}

	@Override
	public void onBroadcast(Message message) {
		if(game.getHost()) {
			handleMessageAsHost(message);
		} else {
			handleMessageAsClient(message);
		}
	}
	
	private void handleMessageAsHost(Message message) {
		
	}
	
	private void handleMessageAsClient(Message message) {
		
	}

}
