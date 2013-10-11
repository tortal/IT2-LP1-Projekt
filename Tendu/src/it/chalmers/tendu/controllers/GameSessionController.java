package it.chalmers.tendu.controllers;

import it.chalmers.tendu.gamemodel.GameSession;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.tbd.C;
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
		gameSession.nextScreen();
	}

	public void setModel(GameSession session) {
		this.gameSession = session;
	}

	@Override
	public void onBroadcast(EventMessage message) {
		if (Player.getInstance().isHost()) {
			handleAsHost(message);
		} else {
			handleAsClient(message);
		}
	}

	private void handleAsHost(EventMessage message) {
		if (message.tag == C.Tag.CLIENT_REQUESTED
				|| message.tag == C.Tag.TO_SELF) {

			switch (message.msg) {

			case WAITING_TO_START_GAME:
				String macAddress = (String) message.content;
				gameSession.playerWaitingToStart(macAddress);
				if (gameSession.allWaiting()) {
					EventMessage msg = new EventMessage(C.Tag.COMMAND_AS_HOST,
							C.Msg.START_MINI_GAME);
					EventBus.INSTANCE.broadcast(msg);
					msg.tag = C.Tag.TO_SELF;
					EventBus.INSTANCE.broadcast(msg);
				}
				break;

			case GAME_WON:
				gameSession.miniGameWon();
				initNextGame();
				break;

			case GAME_LOST:
				gameSession.miniGameLost();
				initNextGame();
				break;

			default:
				break;
			}
		}
	}

	private void initNextGame() {
		MiniGame miniGame = gameSession.getNextMiniGame();
		gameSession.setCurrentMiniGame(miniGame);
		EventMessage eventMessage = new EventMessage(C.Tag.COMMAND_AS_HOST,
				C.Msg.LOAD_THIS_GAME, miniGame);
		EventBus.INSTANCE.broadcast(eventMessage);
	}

	private void handleAsClient(EventMessage message) {
		if (message.tag == C.Tag.TO_SELF) {
			if (message.msg == C.Msg.WAITING_TO_START_GAME) {
				message.tag = C.Tag.REQUEST_AS_CLIENT;
				EventBus.INSTANCE.broadcast(message);
			}
			if (message.msg == C.Msg.GAME_WON) {
				gameSession.miniGameWon();
				message.tag = C.Tag.REQUEST_AS_CLIENT;
			}
			if (message.msg == C.Msg.GAME_LOST) {
				gameSession.miniGameLost();
				message.tag = C.Tag.REQUEST_AS_CLIENT;
			}

		} else if (message.tag == Tag.HOST_COMMANDED) {
			if (message.msg == C.Msg.LOAD_THIS_GAME) {
				MiniGame miniGame = (MiniGame) message.content;
				gameSession.setCurrentMiniGame(miniGame);
			}
			if (message.msg == C.Msg.START_MINI_GAME) {
				message.tag = C.Tag.TO_SELF;
				EventBus.INSTANCE.broadcast(message);
			}
		}
	}

	@Override
	public void unregister() {
		EventBus.INSTANCE.removeListener(this);
	}
}
