package it.chalmers.tendu.controllers;

import com.badlogic.gdx.Gdx;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.gamemodel.GameId;
import it.chalmers.tendu.gamemodel.GameSession;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.C.Msg;
import it.chalmers.tendu.tbd.C.Tag;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;

public class ModelController implements Listener {
	
	private String TAG = "ModelController";

	private GameSession gameSession;
	private Tendu applicationListener;

	public ModelController(Tendu applicationListener, GameSession gameSession) {
		this.applicationListener = applicationListener;
		this.gameSession = gameSession;
		EventBus.INSTANCE.addListener(this);
	}

	public void setModel(GameSession session) {
		this.gameSession = session;
	}

	@Override
	public void onBroadcast(EventMessage message) {
		if (applicationListener.isHost()) {
			handleAsHost(message);
		} else {
			Gdx.app.log(TAG, "Message: " + (message == null));
			handleAsClient(message);
		}
	}

	private void handleAsHost(EventMessage message) {
		if (message.tag == C.Tag.CLIENT_REQUESTED || message.tag == C.Tag.ACCESS_MODEL) {
			//*********NUMBER GAME***********
			if (message.gameId == GameId.NUMBER_GAME) {
				NumberGame game = (NumberGame) gameSession.currentMiniGame;
				if (message.msg == C.Msg.NUMBER_GUESS) {
					game.checkNbr((Integer) message.content);
					gameSession.setCurrentMiniGame(game);
					message = new EventMessage(Tag.COMMAND_AS_HOST, Msg.UPDATE_MODEL, GameId.NUMBER_GAME, game);
					EventBus.INSTANCE.broadcast(message);						
				}
			}
		}
	}

	private void handleAsClient(EventMessage message) {
		if (message.tag == C.Tag.ACCESS_MODEL) {
			//*********NUMBER GAME***********
			if (message.gameId == GameId.NUMBER_GAME) {
				NumberGame game = (NumberGame) gameSession.currentMiniGame;
				if (message.msg == C.Msg.NUMBER_GUESS) {
					game.checkNbr((Integer) message.content);
					message.tag = Tag.REQUEST_AS_CLIENT;
					EventBus.INSTANCE.broadcast(message);				
				}
			}
		}

		if (message.tag == Tag.HOST_COMMANDED) {
			//*********NUMBER GAME***********
			if (message.gameId == GameId.NUMBER_GAME) {
				if(message.msg == Msg.UPDATE_MODEL) {
					NumberGame game = (NumberGame)message.content;
					gameSession.setCurrentMiniGame(game);
				}
			}
		}
	}

}