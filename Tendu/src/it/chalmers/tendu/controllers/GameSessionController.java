package it.chalmers.tendu.controllers;

import it.chalmers.tendu.gamemodel.GameId;
import it.chalmers.tendu.gamemodel.GameSession;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.C.Msg;
import it.chalmers.tendu.tbd.C.Tag;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;

import com.badlogic.gdx.Gdx;

public class GameSessionController implements Listener {

	private String TAG = getClass().getSimpleName();

	private GameSession gameSession;

	public GameSessionController(GameSession gameSession) {
		this.gameSession = gameSession;
		EventBus.INSTANCE.addListener(this);
	}

	private GameSessionController() {
	}

	public void setModel(GameSession session) {
		this.gameSession = session;
	}

	@Override
	public void onBroadcast(EventMessage message) {
		if (Player.getInstance().isHost()) {
			handleAsHost(message);
		} else {
			Gdx.app.log(TAG, "Message: " + (message == null));
			handleAsClient(message);
		}
	}

	private void handleAsHost(EventMessage message) {
		if (message.tag == C.Tag.CLIENT_REQUESTED
				|| message.tag == C.Tag.TO_SELF) {
			if (message.msg == C.Msg.WAITING_TO_START_GAME) {
				String macAddress = (String) message.content;
				gameSession.playerWaitingToStart(macAddress);
				if (gameSession.allWaiting()) {
					EventMessage msg = new EventMessage(C.Tag.COMMAND_AS_HOST,
							C.Msg.START_MINI_GAME);
					EventBus.INSTANCE.broadcast(msg);
					msg.tag = C.Tag.TO_SELF;
					EventBus.INSTANCE.broadcast(msg);
				}
			}
		}
	}

	private void handleAsClient(EventMessage message) {
		if (message.tag == C.Tag.TO_SELF) {
			if (message.msg == C.Msg.WAITING_TO_START_GAME) {
				message.tag = C.Tag.REQUEST_AS_CLIENT;
				EventBus.INSTANCE.broadcast(message);
			}

		} else if (message.tag == Tag.HOST_COMMANDED) {
			if (message.msg == C.Msg.START_MINI_GAME) {
				message.tag = C.Tag.TO_SELF;
				EventBus.INSTANCE.broadcast(message);			}
		}
	}

}
