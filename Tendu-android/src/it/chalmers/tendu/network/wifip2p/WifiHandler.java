package it.chalmers.tendu.network.wifip2p;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;
import it.chalmers.tendu.network.INetworkHandler;
import it.chalmers.tendu.tbd.EventMessage;

/** Handles the Wifi connection
 * 
 * @author johnpetersson
 *
 */
public class WifiHandler implements INetworkHandler {
	public static final String TAG = "WifiHandler";
	
	private Context context;

	WifiP2pManager mManager;
	Channel mChannel;
	//BroadcastReceiver mReceiver;

	IntentFilter mIntentFilter;

	public WifiHandler(Context ctx) {
		context = ctx;

		mManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(context, context.getMainLooper(), null);
		// mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, context);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

	}

	@Override
	public void hostSession() {
		// TODO Auto-generated method stub

	}

	@Override
	public void joinGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void broadcastMessageOverNetwork(EventMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	//	@Override
	public void onResume() {
		/* register the broadcast receiver with the intent values to be matched */
		context.registerReceiver(mReceiver, mIntentFilter);
	}

	//	@Override
	protected void onPause() {
		/* unregister the broadcast receiver */
		context.unregisterReceiver(mReceiver);
	}

	@Override
	public void testStuff() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getMacAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		//		public void onReceive(Context context, Intent intent) {
		//			String action = intent.getAction();
		//			// When discovery finds a device
		//			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
		//				// Get the BluetoothDevice object from the Intent
		//				BluetoothDevice device = intent
		//						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		//				if (D)
		//					Log.v(TAG, "Device found: " + device.getName() + "Adress: "
		//							+ device.getAddress());
		//				// Add the device to a list
		//				availableDevices.add(device);
		//			}
		//		}

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

				int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
				if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
					// Wifi P2P is enabled
				} else {
					// Wi-Fi P2P is not enabled
				}

			} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
				// Call WifiP2pManager.requestPeers() to get a list of current peers
			} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
				// Respond to new connection or disconnections
			} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
				// Respond to this device's wifi state changing
			}
		}
	};

	private void discoverPeers() { 
		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
			@Override
			public void onSuccess() {
				Log.d(TAG, "discovered something");
			}

			@Override
			public void onFailure(int reasonCode) {
				Log.d(TAG, "Error in discovery: " + reasonCode);
			}
		}); 
	}
}