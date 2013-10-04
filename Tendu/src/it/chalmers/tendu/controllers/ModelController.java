package it.chalmers.tendu.controllers;

import com.badlogic.gdx.Gdx;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.gamemodel.GameId;
import it.chalmers.tendu.gamemodel.GameSession;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.C.Tag;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;

public class ModelController implements Listener {

	private GameSession session;
	private Tendu applicationListener;

	public ModelController(Tendu applicationListener, GameSession gameSession) {
		this.applicationListener = applicationListener;
		this.session = gameSession;
		EventBus.INSTANCE.addListener(this);
	}

	public void setModel(GameSession session) {
		this.session = session;
	}

	@Override
	public void onBroadcast(EventMessage message) {
		if (applicationListener.isHost()) {
			handleAsHost(message);
		}

		handleAsClient(message);
	}

	private void handleAsHost(EventMessage message) {
		if (message.tag == C.Tag.REQUEST) {
			
		}
	}

	private void handleAsClient(EventMessage msg) {
		if (msg.tag == C.Tag.ACCESS_MODEL) {
			//*********NUMBER GAME***********
			if (msg.gameId == GameId.NUMBER_GAME) {
				NumberGame game = (NumberGame) this.session.currentMiniGame;
				if (msg.msg == C.Msg.NUMBER_GUESS) {
					game.checkNbr((Integer) msg.content);
					//EventBus.INSTANCE.broadcast();		

					
				}
			}
		}

		if (msg.tag == Tag.COMMAND) {
		}
	}

}
