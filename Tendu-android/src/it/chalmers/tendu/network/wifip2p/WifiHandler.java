package it.chalmers.tendu.network.wifip2p;

import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.event.C;
import it.chalmers.tendu.event.C.Msg;
import it.chalmers.tendu.event.C.Tag;
import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.gamemodel.GameId;
import it.chalmers.tendu.gamemodel.GameResult;
import it.chalmers.tendu.gamemodel.GameSession;
import it.chalmers.tendu.gamemodel.GameState;
import it.chalmers.tendu.gamemodel.LobbyModel;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.SessionResult;
import it.chalmers.tendu.gamemodel.SimpleTimer;
import it.chalmers.tendu.gamemodel.SimpleTimer.STATE;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;
import it.chalmers.tendu.gamemodel.shapesgame.GeometricShape;
import it.chalmers.tendu.gamemodel.shapesgame.Lock;
import it.chalmers.tendu.gamemodel.shapesgame.NetworkShape;
import it.chalmers.tendu.gamemodel.shapesgame.Shape;
import it.chalmers.tendu.gamemodel.shapesgame.ShapeColor;
import it.chalmers.tendu.gamemodel.shapesgame.ShapeGame;
import it.chalmers.tendu.network.NetworkHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

/** Handles the Wifi connection
 * 
 * @author johnpetersson
 *
 */
// TODO Is wifi available- method
public class WifiHandler extends NetworkHandler implements WifiP2pManager.ConnectionInfoListener {
	public static final String TAG = "WifiHandler";

	private static final int MAX_KRYONET_BLOCKING_TIME = 5000;
	private static final int TCP_PORT = 54555;
	private Client client;
	private Server server;

	// Client/Server logic gets loaded prematurely when listeners fire on startup,
	// this flag prevents this
	private boolean isReadyToConnect = false; 

	WifiP2pManager mManager;
	Channel mChannel;

	IntentFilter mIntentFilter;

	private Handler mHandler = new Handler();

	public WifiHandler(Context ctx) {
		super(ctx);

		mManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(context, context.getMainLooper(), null);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		forgetAnyExistingWifiGroup();

		notfiyIfApiLevelTooLow();
	}

	@Override
	public void hostSession() {
		isReadyToConnect = true;
		createNewWifiGroup();
		startRegistration();
	}

	@Override
	public void joinLobby() {
		isReadyToConnect = true;

		resetConnection();
		mManager.requestConnectionInfo(mChannel, this);
	}

	@Override
	public void broadcastMessageOverNetwork(EventMessage message) {
		if (client != null) {
			client.sendTCP(message);
		}
		if (server != null) {
			server.sendToAllTCP(message);
		}
	}

	@Override
	public void destroy() {
		unregisterBroadcastReceiver();
		resetNetwork();
	}

	@Override
	public void onResume() {
		/* register the broadcast receiver with the intent values to be matched */
		context.registerReceiver(mReceiver, mIntentFilter);
	}

	@Override
	public void onPause() {
		unregisterBroadcastReceiver();
	}

	@Override
	public void testSendMessage() {
		Map<String, Integer> players = new HashMap<String, Integer>();
		players.put("Test", 1);
		EventMessage message = new EventMessage(C.Tag.TEST, C.Msg.TEST, new GameSession(players));
		broadcastMessageOverNetwork(message);
	}

	@Override
	public String getMacAddress() {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wInfo = wifiManager.getConnectionInfo();
		String macAddress = wInfo.getMacAddress(); 
		return macAddress;
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
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
				Log.d(TAG, "P2P peers changed");
				if (mManager != null) {
					mManager.requestPeers(mChannel, myPeerListListener);
				}
			} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
				// Respond to new connection or disconnections
				Log.d(TAG, "Connection changed");
				if (mManager == null) {
					return;
				}

				NetworkInfo networkInfo = (NetworkInfo) intent
						.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

				if (networkInfo.isConnected()) {
					// We are connected with the other device, request connection
					// info to find group owner IP
					mManager.requestConnectionInfo(mChannel, WifiHandler.this); // (This is done once in join() already)
				}
			} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
				Log.d(TAG, "This device's wifi state changed");
				// Respond to this device's wifi state changing
			}
		}
	};

	private void unregisterBroadcastReceiver() {
		if (mReceiver != null) {
			try {
				context.unregisterReceiver(mReceiver);				
			} catch(IllegalArgumentException e) {
				Log.d(TAG, "Receiver not registered, can't be deleted");
			}
		}
	}

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo info) {
		if (!isReadyToConnect) {
			// Ignore this method call if starting the app 
			Log.d(TAG, "Not ready to connect");
			return;
		}
		String groupOwnerAddress = null;
		if (info.groupOwnerAddress != null) {
			groupOwnerAddress = info.groupOwnerAddress.getHostAddress();
		}

		// After the group negotiation, we can determine the group owner.
		if (info.groupFormed && info.isGroupOwner) {
			Log.d(TAG, "Acting as server");
			if (server == null) {
				startKryoNetServer();
			}
		} else if (info.groupFormed) {
			Log.d(TAG, "Acting as client");
			new StartKryoNetClientTask().execute(groupOwnerAddress); // Has to be run in another thread for now
		} else { 
			// No group is formed, connect to the first unit broadcasting the Tendu service
			Log.d(TAG, "No group formed, doing discovery/connect");
			discoverService();
		}
	}

	private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
	private PeerListListener myPeerListListener = new PeerListListener() {

		@Override
		public void onPeersAvailable(WifiP2pDeviceList peerList) {
			peers.clear();
			peers.addAll(peerList.getDeviceList());		
			if (peers.size() == 0) {
				Log.d(TAG, "No devices found");
				return;
			}
		}
	};

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void resetConnection() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			mManager.stopPeerDiscovery(mChannel, new WifiP2pManager.ActionListener() {

				@Override
				public void onSuccess() {
					// do nothing
				}

				@Override
				public void onFailure(int reason) {
					Log.d(TAG, "Couldn't stop peer deiscovery: " + translateErrorCodeToMessage(reason));
				}
			});
		}
	}

	private void discoverPeers() { 
		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
			@Override
			public void onSuccess() {
				Log.d(TAG, "Initiated discovery");
			}

			@Override
			public void onFailure(int reasonCode) {
				Log.d(TAG, "Discovery failed: " + translateErrorCodeToMessage(reasonCode));

			}
		}); 
	}

	private WifiP2pDevice findFirstEligibleDevice(List<WifiP2pDevice> peers) {
		for (WifiP2pDevice device: peers) {
			return device;
		}
		return null; 
	}

	private void connectToFirstAvailable() {
		// Wait a minute while available units are discovered
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				WifiP2pDevice device = findFirstEligibleDevice(peers);
				if (device != null) {
					Log.d(TAG, "Will now try and connect to: " + device.deviceName);
					connectToDevice(device);
				} else {
					Log.d(TAG, "No device to connect to");
				}
			}
		}, CONNECTION_DELAY);
	}

	private String hostMacAddress = null;
	private void connectToDevice(final WifiP2pDevice device) {
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = device.deviceAddress;

		mManager.connect(mChannel, config, new ActionListener() {

			@Override
			public void onSuccess() {
				// WiFiDirectBroadcastReceiver will notify us. Ignore for now.
				Log.d(TAG, "Connection initiated to: " + device.deviceName);
				hostMacAddress = device.deviceAddress;
			}

			@Override
			public void onFailure(int reason) {
				Log.d(TAG, "Could not connect to: " + device.deviceName + ": " + translateErrorCodeToMessage(reason));
				toastMessage("Connection failed. Retry");
			}
		});
	}

	public void resetNetwork() {
		removeWifiGroup();
		clearServices();

		if (server != null) {
			server.stop();
			server = null;
		}
		if (client != null) {
			client.stop();
			client = null;
		}
		mChannel = mManager.initialize(context, context.getMainLooper(), null);
	}

	private void removeWifiGroup() {
		Log.d(TAG, "Removing wifi group");
		mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {

			@Override
			public void onFailure(int reason) {
				Log.d(TAG, "Failed to remove group: " + translateErrorCodeToMessage(reason));
			}

			@Override
			public void onSuccess() {
				// Do nothing
			}
		});		
	}

	private void createNewWifiGroup() {
		mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {

			@Override
			public void onSuccess() {
				// Do nothing
			}
			
			@Override
			public void onFailure(int reason) {
				Log.d(TAG, "Group creation failed: " + translateErrorCodeToMessage(reason));
			}
		});
	}

	private void forgetAnyExistingWifiGroup() {
		Log.d(TAG, "Requesting group info to forget");
		mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {

			@Override
			public void onGroupInfoAvailable(WifiP2pGroup group) {
				if (group != null) {
					removeWifiGroup();
				}
			}
		});
	}

	// ********************* Service Handling  *********************
	// This all requires Api level 16 - Jelly Bean
	// More or less Copy and pasted from developer.android.com 

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void startRegistration() {
		//  String map containing information about the service.
		Map<String, String> record = new HashMap<String, String>();
		record.put("name", Constants.SERVER_NAME);

		// Service information.
		WifiP2pDnsSdServiceInfo serviceInfo =
				WifiP2pDnsSdServiceInfo.newInstance("Temp instance", "temp protocol name", record);

		mManager.addLocalService(mChannel, serviceInfo, new ActionListener() {
			@Override
			public void onSuccess() {
				Log.d(TAG, "Adding local service");
			}

			@Override
			public void onFailure(int reason) {
				Log.d(TAG, "Service adding failed: " + translateErrorCodeToMessage(reason));
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void discoverService() {
		DnsSdTxtRecordListener txtListener = new DnsSdTxtRecordListener() {
			@Override

			public void onDnsSdTxtRecordAvailable(
					String fullDomain, Map<String, String> record, WifiP2pDevice device) {
				Log.d(TAG, "DnsSdTxtRecord available -" + record.toString());

				if (record.get("name").equals(Constants.SERVER_NAME)) {
					// Connect to the service found if eligible 
					connectToDevice(device);
				}
			}
		};

		DnsSdServiceResponseListener servListener = new DnsSdServiceResponseListener() {
			@Override
			public void onDnsSdServiceAvailable(String instanceName, String registrationType,
					WifiP2pDevice resourceType) {
				// Not used
			}
		};

		mManager.setDnsSdResponseListeners(mChannel, servListener, txtListener);

		WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
		mManager.addServiceRequest(mChannel,
				serviceRequest,
				new ActionListener() {
			@Override
			public void onSuccess() {
				Log.d(TAG, "Adding service request");
			}

			@Override
			public void onFailure(int reason) {
				Log.d(TAG, "Service request failed: " + translateErrorCodeToMessage(reason));
			}
		});

		mManager.discoverServices(mChannel, new ActionListener() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "Trying to discover a service");
			}

			@Override
			public void onFailure(int reason) {
				Log.d(TAG, "Service discovery failed: " + translateErrorCodeToMessage(reason));
				toastMessage("Something went wrong. Try again.");
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void clearServices() {
		mManager.clearServiceRequests(mChannel, new WifiP2pManager.ActionListener() {

			@Override
			public void onFailure(int reason) {
				Log.d(TAG, "Service clearing failed: " + translateErrorCodeToMessage(reason));				
			}

			@Override
			public void onSuccess() {
			}
		});

	}

	private void notfiyIfApiLevelTooLow() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			toastMessage("Api version too low. Need Jelly Bean (Api level 16)");
		}
	}

	// ********************** Kryo *********************************

	private void startKryoNetServer() {
		//Creates a Server with a write buffer size of 16384 and an object buffer size of 2048.
		
		server = new Server(16384*10, 2048*10);
		Kryo kryo = server.getKryo();
		registerKryoClasses(kryo);
		server.start();
		try {
			server.bind(TCP_PORT);	
			Log.d(TAG, "Kryonet server started");
		} catch (IOException e) {
			Log.d(TAG, "KryoNet Server creation failure");
			e.printStackTrace();
		}

		server.addListener(new Listener() {
			@Override
			public void received (Connection connection, Object object) {
				if (object instanceof EventMessage) {
					EventMessage message = (EventMessage)object;
					Log.d(TAG, "Received: " + message.toString());
					toastMessage(message);
					sendToEventBus(message);
				}
			}
			@Override
			public void disconnected(Connection connection) {
				connection.close();
				if (server.getConnections().length == 0) {
					displayConnectionLostAlert();
					resetNetwork();
					EventBus.INSTANCE.broadcast(new EventMessage(Tag.NETWORK_NOTIFICATION, Msg.CONNECTION_LOST));
				} else {
					EventBus.INSTANCE.broadcast(new EventMessage(Tag.NETWORK_NOTIFICATION, Msg.PLAYER_DISCONNECTED));
				}
			}
		});
	}

	private class StartKryoNetClientTask extends AsyncTask<String, Void, Object> {
		@Override
		protected Object doInBackground(String... addresses) {
			//Creates a Client with a write buffer size of 8192 and an object buffer size of 2048.
			String address = addresses[0];
			client = new Client(8192*10, 2048*10);
			Kryo kryo = client.getKryo();
			registerKryoClasses(kryo);
			new Thread(client).start(); // Possible daemon thread-related bug fix
			try {
				Log.d(TAG, "KryoNet will now connct to address: " + address);
				client.connect(MAX_KRYONET_BLOCKING_TIME, address, TCP_PORT);
			} catch (IOException e) {
				Log.d(TAG, "Error in connecting via KryoNet");
				e.printStackTrace();
			}

			client.setKeepAliveTCP(100);
			client.addListener(new Listener() {
				@Override
				public void received(com.esotericsoftware.kryonet.Connection connection, Object object) {
					if (object instanceof EventMessage) {
						EventMessage message = (EventMessage)object;
						Log.d(TAG, "Received: " + message.toString());
						toastMessage(message);
						sendToEventBus(message);
					}
				}
				@Override
				public void disconnected(Connection connection) {
					connection.close();
					displayConnectionLostAlert();
					EventBus.INSTANCE.broadcast(new EventMessage(Tag.NETWORK_NOTIFICATION, Msg.CONNECTION_LOST));
					resetNetwork();
				}
			});
			// Send own mac address to host
			broadcastMessageOverNetwork(new EventMessage(Tag.CLIENT_REQUESTED, Msg.PLAYER_CONNECTED, getMacAddress()));
			return null;
		}

	}

	/** Register the classes we want to send over the network */
	private void registerKryoClasses(Kryo kryo) {
		kryo.register(EventMessage.class);
		kryo.register(GameId.class);
		kryo.register(C.class);
		kryo.register(C.Msg.class);
		kryo.register(C.Tag.class);
		kryo.register(LobbyModel.class);
		kryo.register(Shape.class);
		kryo.register(ShapeGame.class);
		kryo.register(ShapeColor.class);
		kryo.register(ArrayList.class);
		kryo.register(HashMap.class);
		kryo.register(GameSession.class);
		kryo.register(GeometricShape.class);
		kryo.register(MiniGame.class);
		kryo.register(NumberGame.class);
		kryo.register(Lock.class);
		kryo.register(Difficulty.class);
		kryo.register(SimpleTimer.class);
		kryo.register(STATE.class);
		kryo.register(SessionResult.class);
		kryo.register(NetworkShape.class);
		kryo.register(GameResult.class);
		kryo.register(GameState.class);
	}	

	@Override
	public void stopAcceptingConnections() {
		// Removes the server name broadcasting service so no one else will find the game
		clearServices();
	}

	@Override
	public void unregister() {
		EventBus.INSTANCE.removeListener(this);
	}
}
