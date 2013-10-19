package it.chalmers.tendu.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;

import it.chalmers.tendu.network.bluetooth.BluetoothHandler;
import it.chalmers.tendu.network.wifip2p.WifiHandler;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventBusListener;
import it.chalmers.tendu.tbd.EventMessage;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class NetworkHandler implements INetworkHandler, EventBusListener {
	protected Context context;

	public static final int MAX_NUMBER_OF_PLAYERS = 3;
	public static final int CONNECTION_DELAY = 5000;

	private NetworkHandler networkHandler;
	
	public NetworkHandler(Context ctx) {
		context = ctx;

		selectBluetooth();
		// Register as listener on the eventbus
		EventBus.INSTANCE.addListener(this);
	}

	public void selectBluetooth() {
		if (networkHandler != null) {
			networkHandler.destroy();
		}
		networkHandler = new BluetoothHandler(context);
	}
	
	public void selectWifi() {
		if (networkHandler != null) {
			networkHandler.destroy();
		}
		networkHandler = new WifiHandler(context);
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

	protected void toastMessage(final EventMessage message) {
		((AndroidApplication) context).runOnUiThread(new Runnable() {
			public void run() {
				Toast toast = Toast.makeText(context, message.toString(),
						Toast.LENGTH_SHORT); 
				toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
				toast.show();
			}
		});
		
//		Gdx.app.postRunnable(new Runnable() {
//		
//			@Override
//			public void run() {
//				Toast toast = Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT);
//				toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
//				toast.show();
//			}
//		});
	}
	
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

	/** Translates the network error codes into something interpretable */
	protected String translateErrorCodeToMessage(int eCode) {
		switch (eCode) {
		case 0: return "ERROR";
		case 1: return "UNSUPPORTED";
		case 2: return "BUSY";
		default: return "Shouldn't happen";
		}
	}

	@Override
	public void unregister() {
		networkHandler.unregister();
	}

	@Override
	public void hostSession() {
		networkHandler.hostSession();
	}

	@Override
	public void joinGame() {
		networkHandler.joinGame();
	}

	@Override
	public void broadcastMessageOverNetwork(EventMessage message) {
		networkHandler.broadcastMessageOverNetwork(message);
	}

	@Override
	public void destroy() {
		networkHandler.destroy();
	}

	@Override
	public void testSendMessage() {
		networkHandler.testSendMessage();
	}

	@Override
	public String getMacAddress() {
		return networkHandler.getMacAddress();
	}

	@Override
	public void onPause() {
		networkHandler.onPause();
	}

	@Override
	public void onResume() {
		networkHandler.onResume();
		
	}

	@Override
	public void resetNetwork() {
		networkHandler.resetNetwork();
		
	}

	@Override
	public void stopAcceptingConnections() {
		networkHandler.stopAcceptingConnections();
		
	}

	@Override
	public int toggleHostNumber() {
		return networkHandler.toggleHostNumber();
	}
}
