package it.chalmers.tendu.network;

import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.network.bluetooth.BluetoothHandler;
import it.chalmers.tendu.network.wifip2p.WifiHandler;
import android.content.Context;
import android.os.Build;

public class Network implements INetwork {
	private INetworkHandler networkHandler;
	private Context context;
	
	public Network(Context ctx) {
		context = ctx;

		//selectWifi();
		selectBluetooth();
	}
	
	@Override
	public void selectBluetooth() {
		if (networkHandler == null) {
			networkHandler = new BluetoothHandler(context);
		} else if (!(networkHandler instanceof BluetoothHandler)) {
			networkHandler.destroy();
			networkHandler = new BluetoothHandler(context);
		}
	}
	
	@Override
	public void selectWifi() {
		if (networkHandler == null) {
			networkHandler = new BluetoothHandler(context);
		} else if (!(networkHandler instanceof BluetoothHandler)) {
			networkHandler.destroy();
			networkHandler = new WifiHandler(context);
		}
	}
	
	@Override
	public boolean isWifip2pAvailable() {
		return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN);
	}
	
	@Override
	public void hostSession() {
		networkHandler.hostSession();
	}

	@Override
	public void joinLobby() {
		networkHandler.joinLobby();
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
