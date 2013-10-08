package it.chalmers.tendu.network.wifip2p;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;
import android.widget.Toast;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.network.INetworkHandler;
import it.chalmers.tendu.tbd.EventMessage;

/** Handles the Wifi connection
 * 
 * @author johnpetersson
 *
 */
public class WifiHandler implements INetworkHandler, WifiP2pManager.ConnectionInfoListener {
	public static final String TAG = "WifiHandler";
	
	private Context context;
	
	WifiP2pManager mManager;
	Channel mChannel;
	//BroadcastReceiver mReceiver;

	IntentFilter mIntentFilter;
	PeerListListener myPeerListListener;
	private List<WifiP2pDevice> peers = new ArrayList();

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

		myPeerListListener = new PeerListListener() {
			
			@Override
			public void onPeersAvailable(WifiP2pDeviceList peerList) {
				peers.clear();
	            peers.addAll(peerList.getDeviceList());
	            Log.d(TAG, peers.toString());			}
		};
		
	}

	@Override
	public void hostSession() {
		// TODO Auto-generated method stub

	}

	@Override
	public void joinGame() {
		discoverPeers();
		WifiP2pDevice device = findFirstEligibleDevice(peers);
		if (device != null) {
			connectToDevice(device);
		}

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
					Log.d(TAG, "Wifi p2p enabled");
				} else {
					Log.d(TAG, "Wifi p2p NOT enabled");
				}

			} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
				// request available peers from the wifi p2p manager. This is an
			    // asynchronous call and the calling activity is notified with a
			    // callback on PeerListListener.onPeersAvailable()
			    if (mManager != null) {
			        mManager.requestPeers(mChannel, myPeerListListener);
			    }
			    Log.d(TAG, "P2P peers changed");
			} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
				// Respond to new connection or disconnections
				if (mManager == null) {
	                return;
	            }

	            NetworkInfo networkInfo = (NetworkInfo) intent
	                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

	            if (networkInfo.isConnected()) {

	                // We are connected with the other device, request connection
	                // info to find group owner IP

	                mManager.requestConnectionInfo(mChannel, WifiHandler.this);
	            }
			} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
				// Respond to this device's wifi state changing
			}
		}
	};

	private void discoverPeers() { 
		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
			@Override
			public void onSuccess() {
				Log.d(TAG, "Initiated discovery");
			}

			@Override
			public void onFailure(int reasonCode) {
				Log.d(TAG, "Error in discovery: " + reasonCode);
			}
		}); 
	}
	
	private WifiP2pDevice findFirstEligibleDevice(List<WifiP2pDevice> peers) {
		for (WifiP2pDevice device: peers) {
			if (device.deviceName.contains(Constants.SERVER_NAME)) {
				return device;
			}
		}
		return null; 
	}
	
	/**
	 * Adds a the name "TenduS" as a suffix to this device name. This is needed
	 * as identification
	 * 
	 * If the device has no name, it is set to "Name"
	 * 
	 * @param server
	 *            if this device is a server device or not
	 */
//	private void addTenduToDeviceName(final boolean isServer) {
//		if (WifiP2pManager.getName() == null) {
//			mBluetoothAdapter.setName("Name");
//		}
//
//		String newName = "No rename occured";
//		String oldName = mBluetoothAdapter.getName();
//		if (isServer && !oldName.contains(Constants.SERVER_NAME)) {
//			newName = oldName + Constants.SERVER_NAME;
//			mBluetoothAdapter.setName(newName);
//			while (!mBluetoothAdapter.getName().equals(newName)) {
//				// Loop while name changes
//			}
//		}
//	}

	/**
	 * Removes the "TenduS" suffix from the bluetooth name
	 */
//	private void removeTenduFromDeviceName() {
//		String oldName = mBluetoothAdapter.getName();
//		String newName = new String(oldName);
//
//		if (oldName.contains(Constants.SERVER_NAME)) {
//			newName = oldName.replace(Constants.SERVER_NAME, "");
//			Log.d(TAG, "Bluetooth name removal successfull? "
//					+ mBluetoothAdapter.setName(newName));
//		}
//		Log.v(TAG, "Remove: " + oldName + " -> " + newName
//				+ ". Actual adapter name: " + mBluetoothAdapter.getName());
//	}
	
	private void connectToDevice(final WifiP2pDevice device) {
		//obtain a peer from the WifiP2pDeviceList
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = device.deviceAddress;
		config.wps.setup = WpsInfo.PBC;
		mManager.connect(mChannel, config, new ActionListener() {

		    @Override
		    public void onSuccess() {
		    	// WiFiDirectBroadcastReceiver will notify us. Ignore for now.
		    }

		    @Override
		    public void onFailure(int reason) {
		    	Log.d(TAG, "Could not connect to: " + device.deviceName);
		    }
		});
	}
	
	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo info) {
		// InetAddress from WifiP2pInfo struct.
		String groupOwnerAddress = info.groupOwnerAddress.getHostAddress();
		
		// After the group negotiation, we can determine the group owner.
		if (info.groupFormed && info.isGroupOwner) {
			// Do whatever tasks are specific to the group owner.
			// One common case is creating a server thread and accepting
			// incoming connections.
			Toast.makeText(context, "Group Owner", Toast.LENGTH_SHORT).show();
		} else if (info.groupFormed) {
			// The other device acts as the client. In this case,
			// you'll want to create a client thread that connects to the group
			// owner.
			Toast.makeText(context, "Client", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
}