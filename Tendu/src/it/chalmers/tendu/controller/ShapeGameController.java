package it.chalmers.tendu.controller;

import it.chalmers.tendu.event.C;
import it.chalmers.tendu.event.C.Tag;
import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.gamemodel.GameId;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.gamemodel.shapesgame.NetworkShape;
import it.chalmers.tendu.gamemodel.shapesgame.Shape;
import it.chalmers.tendu.gamemodel.shapesgame.ShapeGame;

import java.util.List;

import com.badlogic.gdx.Gdx;

public class ShapeGameController implements MiniGameController {

	private final String TAG = "ShapeGameModelController";
	private ShapeGame shapeGame;

	/**
	 * Creates a new Controller for shapeGame. Changes the model and receives
	 * broadcasts from the network.
	 * 
	 * @param model
	 */
	public ShapeGameController(ShapeGame model) {
		shapeGame = model;
		EventBus.INSTANCE.addListener(this);
	}

	@Override
	public ShapeGame getModel() {
		return shapeGame;
	}

	@Override
	public void onBroadcast(EventMessage message) {
		if (Player.getInstance().isHost()) {
			handleAsHost(message);
		} else {
			// Gdx.app.log(TAG, "Message: " + (message == null));
			handleAsClient(message);
		}
	}

	@Override
	public void handleAsHost(EventMessage message) {
		if (message.tag == C.Tag.CLIENT_REQUESTED
				|| message.tag == C.Tag.TO_SELF) {
			if (message.msg == C.Msg.START_MINI_GAME) {
				shapeGame.startGame();
			}

			// Lock attempt
			if (message.msg == C.Msg.LOCK_ATTEMPT) {
				if (insertIntoSlot(message.content)) {

					// Received by NumberGameSound.
					EventMessage soundMsg = new EventMessage(C.Tag.TO_SELF,
							C.Msg.SOUND_SUCCEED);
					EventBus.INSTANCE.broadcast(soundMsg);
				} else {

					// Received by NumberGameSound.
					EventMessage soundMsg = new EventMessage(C.Tag.TO_SELF,
							C.Msg.SOUND_FAIL);
					EventBus.INSTANCE.broadcast(soundMsg);
				}

				// Received by clients in ShapeGameController through the
				// network.
				EventMessage changedMessage = new EventMessage(message,
						C.Tag.COMMAND_AS_HOST);
				EventBus.INSTANCE.broadcast(changedMessage);

			}
			// Send object
			if (message.msg == C.Msg.SHAPE_SENT) {
				sendShape(message.content);

				// Received by clients in ShapeGameController through the
				// network.
				EventMessage changedMessage = new EventMessage(message,
						C.Tag.COMMAND_AS_HOST);
				EventBus.INSTANCE.broadcast(changedMessage);

			}
		}
	}

	@Override
	public void handleAsClient(EventMessage message) {
		if (message.tag == C.Tag.TO_SELF) {
			if (message.msg == C.Msg.LOCK_ATTEMPT
					|| message.msg == C.Msg.SHAPE_SENT) {
				Gdx.app.log(TAG, "Sending shape to host, i am client"
						+ Player.getInstance().isHost());
				// Received by host in ShapeGameController through the
				// network.
				EventMessage changedMessage = new EventMessage(message,
						C.Tag.REQUEST_AS_CLIENT);
				EventBus.INSTANCE.broadcast(changedMessage);
			}

		} else if (message.msg == C.Msg.START_MINI_GAME) {
			shapeGame.startGame();
		}

		if (message.tag == Tag.HOST_COMMANDED) {

			// Gdx.app.log(TAG, "Recived from host");
			// Lock attempt
			if (message.msg == C.Msg.LOCK_ATTEMPT) {
				if (insertIntoSlot(message.content)) {

					// Received by NumberGameSound.
					EventMessage soundMsg = new EventMessage(C.Tag.TO_SELF,
							C.Msg.SOUND_SUCCEED);
					EventBus.INSTANCE.broadcast(soundMsg);
					Gdx.app.log(TAG, "Client changed model");
				} else {

					// Received by NumberGameSound.
					EventMessage soundMsg = new EventMessage(C.Tag.TO_SELF,
							C.Msg.SOUND_FAIL);
					EventBus.INSTANCE.broadcast(soundMsg);
				}
			}
			if (message.msg == C.Msg.SHAPE_SENT) {
				sendShape(message.content);
			}
		}
	}

	/**
	 * Tells the model to try to lock a shape into a lock.
	 * 
	 * @param content
	 *            Contains the shape, the lock and the players that is making
	 *            the attempt.
	 * @return True if the attempt was successful.
	 */
	private boolean insertIntoSlot(Object content) {
		List<Object> messageContent = (List) content;
		int player = (Integer) messageContent.get(0);
		Shape lockShape = (Shape) messageContent.get(1);

		// Since we send objects, their references no longer matches our model
		// we have to see which of the objects in "our" model that was sent.
		for (Shape l : shapeGame.getLock(player).getLockSequence()) {
			if (l.equals(lockShape))
				lockShape = l;
		}
		Shape shape = (Shape) messageContent.get(2);
		for (Shape s : shapeGame.getAllInventory().get(player)) {
			if (s.equals(shape))
				shape = s;
		}
		return shapeGame.insertShapeIntoSlot(player, shape, lockShape);
	}

	@Override
	public void unregister() {
		EventBus.INSTANCE.removeListener(this);
	}

	/**
	 * Tells the model to move a shape
	 * 
	 * @param content
	 *            Contains a shape and a players number
	 */
	private void sendShape(Object content) {
		NetworkShape networkShape = (NetworkShape) content;

		Shape shape = networkShape.shape;
		int player = networkShape.player;

		shapeGame.move(shape, player);

	}
}
