package it.chalmers.tendu.network;

import com.badlogic.gdx.Gdx;

import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventBusListener;
import it.chalmers.tendu.tbd.EventMessage;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public abstract class NetworkHandler implements INetworkHandler, EventBusListener {
	protected Context context;

	public static final int MAX_NUMBER_OF_PLAYERS = 3;
	public static final int CONNECTION_DELAY = 5000;

	public NetworkHandler(Context ctx) {
		context = ctx;

		// Register as listener on the eventbus
		EventBus.INSTANCE.addListener(this);
	}

	/** Broadcast a message on the event bus */
	protected void sendToEventBus(final EventMessage message) {
		Gdx.app.postRunnable(new Runnable() {

			@Override
			public void run() {
				EventBus.INSTANCE.broadcast(message);
			}
		});
	}

	@Override
	public void onBroadcast(EventMessage message) {
		// TODO Hook up to event bus	

		switch (message.tag) {
		case COMMAND_AS_HOST: 
			message.setTag(C.Tag.HOST_COMMANDED); 	// Set new tag to prevent
			// feedback loop
			broadcastMessageOverNetwork(message);
			break;
		case REQUEST_AS_CLIENT: 
			message.setTag(C.Tag.CLIENT_REQUESTED);
			broadcastMessageOverNetwork(message);
			break;
		default:
			break;
		}
	}

	protected void toastMessage(final EventMessage message) {
		Gdx.app.postRunnable(new Runnable() {

			@Override
			public void run() {
				Toast toast = Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
				toast.show();
			}
		});
	}

	/** Translates the network error codes into something interpretable */
	protected String translateErrorCodeToMessage(int eCode) {
		switch (eCode) {
		case 0: return "ERROR";
		case 1: return "UNSUPPORTED";
		case 2: return "BUSY";
		default: return "Shouldn't happen";
		}
	}
}
