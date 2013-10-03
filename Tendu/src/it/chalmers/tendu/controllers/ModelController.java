package it.chalmers.tendu.controllers;

import com.badlogic.gdx.Gdx;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.gamemodel.GameSession;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.tbd.C.Msg;
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
		} else {
			handleMessageAsClient(message);
		}
	}
	
	private void handleMessageAsHost(Message message) {
		
	}
	
	private void handleMessageAsClient(Message message) {
		//*********NUMBER GAME HANDLING***********
		if(message.msg == Msg.NUMBER_GUESS) {
			NumberGame numberGame = (NumberGame) session.currentMinigame;
			Gdx.app.log("KUUUK", " " + (Integer)message.content);
			//numberGame.checkNbr();
		}
	}

}
