package it.chalmers.tendu.network;

import it.chalmers.tendu.network.bluetooth.BluetoothHandler;
import it.chalmers.tendu.network.wifip2p.WifiHandler;
import it.chalmers.tendu.tbd.EventMessage;
import android.content.Context;

public class Network implements INetworkHandler {
	private NetworkHandler networkHandler;
	private Context context;
	
	public Network(Context ctx) {
		context = ctx;

		selectBluetooth();
	}
	
	@Override
	public void selectBluetooth() {
		if (networkHandler != null) {
			networkHandler.destroy();
		}
		networkHandler = new BluetoothHandler(context);
	}
	
	@Override
	public void selectWifi() {
		if (networkHandler != null) {
			networkHandler.destroy();
		}
		networkHandler = new WifiHandler(context);
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
