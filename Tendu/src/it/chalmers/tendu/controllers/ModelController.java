package it.chalmers.tendu.controllers;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.gamemodel.GameSession;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.C.Tag;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.Listener;
import it.chalmers.tendu.tbd.Message;

public class ModelController implements Listener {
	private GameSession session;
	private Tendu game;
	
	public ModelController(Tendu game, GameSession session) {
		this.game = game;
		this.session = session;
		EventBus.INSTANCE.addListener(this);
	}
	
	public void setModel(GameSession session) {
		this.session = session;
	}

	@Override
	public void onBroadcast(Message message) {
		if(game.getHost()) {
			handleMessageAsHost(message);
		} 
		
		handleMessageAsClient(message);
	}
	
	private void handleMessageAsHost(Message message) {
		if(message.tag == C.Tag.REQUEST)  {
			
		}
	}
	
	private void handleMessageAsClient(Message message) {
		if(message.tag == C.Tag.TO_SELF)  {
		}
		
		if(message.tag == Tag.COMMAND) {
		}
	}

}
