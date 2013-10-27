package it.chalmers.tendu.network;

import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventBusListener;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.event.C;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager.BadTokenException;
import android.widget.Toast;

/** Abstract parent class for network handling */
public abstract class NetworkHandler implements INetworkHandler, EventBusListener {
	private static final String TAG = "NetworkHandler";
	
	/** Android context */
	protected Context context;

	/** Maximum number of connections */
	public static final int MAX_NUMBER_OF_PLAYERS = 3;
	/** The delay before starting to connect in millis */
	public static final int CONNECTION_DELAY = 5000;


	/**
	 * Creates a networkhandler
	 * @param ctx The android context
	 */
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
		switch (message.tag) {
		case COMMAND_AS_HOST: 
			// Set new tag to prevent
			// feedback loop
			broadcastMessageOverNetwork(new EventMessage(message, C.Tag.HOST_COMMANDED));
			break;
		case REQUEST_AS_CLIENT: 
			broadcastMessageOverNetwork(new EventMessage(message, C.Tag.CLIENT_REQUESTED));
			break;
		default:
			break;
		}
	}

	/** Toasts an EventMessage to the screen */
	protected void toastMessage(final EventMessage message) {
		toastMessage(message.toString());
	}

	/** Toasts an String to the screen */
	protected void toastMessage(final String message) {
		((AndroidApplication) context).runOnUiThread(new Runnable() {
			public void run() {
				Toast toast = Toast.makeText(context, message,
						Toast.LENGTH_SHORT); 
				toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
				toast.show();
			}
		});
	}

	/** Translates the network error codes into something readable */
	protected String translateErrorCodeToMessage(int eCode) {
		switch (eCode) {
		case 0: return "ERROR";
		case 1: return "UNSUPPORTED";
		case 2: return "BUSY";
		default: return "Shouldn't happen";
		}
	}

	protected int hostNumber = 1;
	@Override
	public int toggleHostNumber() {
		if (hostNumber == 1) {
			hostNumber = 2;
		} else {
			hostNumber = 1;
		}

		toastMessage("Host: " + hostNumber);
		return hostNumber;
	}

	@Override
	public void resetNetwork() {
		hostNumber = 1;
	}

	/**
	 * Displays a pop up to inform user that connection has been lost
	 */
	public void displayConnectionLostAlert() {
		class displayConnectionLostAlert implements Runnable {
			public void run() {
				Builder connectionLostAlert = new Builder(context);

				connectionLostAlert.setTitle("Connection lost");
				connectionLostAlert
				.setMessage("Your connection with the other players has been lost.");

				connectionLostAlert.setPositiveButton("Ok",
						new OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						// do nothing
					}
				});
				connectionLostAlert.setCancelable(false);
				try {
					connectionLostAlert.show();
				} catch (BadTokenException e) {
					Log.e(TAG, "BadTokenException", e);
				}
			}
		}
		
		// Display on UI-thread
		((AndroidApplication) context)
		.runOnUiThread(new displayConnectionLostAlert());
	}
}
