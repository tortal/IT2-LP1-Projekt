/**
 * 
 */
package it.chalmers.tendu.controllers;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.gamemodel.GameId;
import it.chalmers.tendu.gamemodel.GameSession;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.gamemodel.shapesgame.ShapesGame;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.C.Msg;
import it.chalmers.tendu.tbd.C.Tag;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;
import it.chalmers.tendu.tbd.Listener;

import com.badlogic.gdx.Gdx;

/**
 * 
 *
 */
public class ShapeGameModelController implements Listener {

	private final String TAG = "ShapeGameModelController";
	private GameSession gameSession;
	private Tendu tendu;

	// TODO: not sure if all controllers need tendu, perhaps only
	// gamesessioncontroller.

	public ShapeGameModelController(Tendu tendu, GameSession gameSession) {
		this.tendu = tendu;
		this.gameSession = gameSession;

		EventBus.INSTANCE.addListener(this);
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
				|| message.tag == C.Tag.ACCESS_MODEL) {
			// *********NUMBER GAME***********
			if (message.gameId == GameId.SHAPES_GAME) {
				ShapesGame game = (ShapesGame) gameSession.currentMiniGame;
				if (message.msg == C.Msg.LOCK_ATTEMPT) {
					
					gameSession.setCurrentMiniGame(game);
					message = new EventMessage(Tag.COMMAND_AS_HOST,
							Msg.UPDATE_MODEL, GameId.NUMBER_GAME,
							gameSession.currentMiniGame);
					EventBus.INSTANCE.broadcast(message);
				}
			}
		}
	}

	private void handleAsClient(EventMessage message) {
		if (message.tag == C.Tag.ACCESS_MODEL) {
			// *********NUMBER GAME***********
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
			// *********NUMBER GAME***********
			if (message.gameId == GameId.NUMBER_GAME) {
				if (message.msg == Msg.UPDATE_MODEL) {
					NumberGame game = (NumberGame) message.content;
					gameSession.setCurrentMiniGame(game);
				}
			}
		}
	}

}
