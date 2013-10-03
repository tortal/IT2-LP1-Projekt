package it.chalmers.tendu.network.bluetooth;

import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.network.INetworkHandler;
import it.chalmers.tendu.network.NetworkMessage;
import it.chalmers.tendu.network.clicklinkcompete.Connection;
import it.chalmers.tendu.network.clicklinkcompete.Connection.OnConnectionLostListener;
import it.chalmers.tendu.network.clicklinkcompete.Connection.OnConnectionServiceReadyListener;
import it.chalmers.tendu.network.clicklinkcompete.Connection.OnIncomingConnectionListener;
import it.chalmers.tendu.network.clicklinkcompete.Connection.OnMaxConnectionsReachedListener;
import it.chalmers.tendu.network.clicklinkcompete.Connection.OnMessageReceivedListener;
import it.chalmers.tendu.unused.BluetoothGameService;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager.BadTokenException;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class BluetoothHandler implements INetworkHandler {
	private boolean D = true; // Debug flag
	private String TAG = "BluetoothHandler";

	/** Identifying Variables */
	public static final int REQUEST_ENABLE_BT = 666;
	private static final int MAX_NUMBER_OF_PLAYERS = 3;
	private static final int CONNECTION_DELAY = 5000;

	// Handles the bluetooth connections
	private Connection connection;
	/** Context in which the handler was declared */
	private Context context;
	/** Connection to android bluetooth hardware */
	private BluetoothAdapter mBluetoothAdapter;
	/** All devices that has been discovered */
	private Set<BluetoothDevice> availableDevices;
	/** Connected devices */
	private Set<BluetoothDevice> connectedDevices;

	/**
	 * Using the context provided by the class declaring this object, initiates
	 * all parameters needed to establish both a connection to a running
	 * bluetooth server and acting as a server itself.
	 * 
	 * @param <code>Context</code> in which the handler was declared
	 */

	public BluetoothHandler(Context context) {
		this.context = context;

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			enableBluetooth();
		}

		connection = new Connection(this.context, serviceReadyListener);
		availableDevices = new HashSet<BluetoothDevice>();
		registerBroadcastReceiver();

	}

	private OnMessageReceivedListener dataReceivedListener = new OnMessageReceivedListener() {
		public void OnMessageReceived(BluetoothDevice device,
				final NetworkMessage message) {
			Log.d(TAG, "Received Message: " + message + " From device: "
					+ device);
			// For testing
			// OnMessageReceived is called from a network thread.
			// Has to be added to the UI-threads message queue in order to be
			// displayed.
			((AndroidApplication) context).runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(context, message.toString(),
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	};


	private OnMaxConnectionsReachedListener maxConnectionsListener = new OnMaxConnectionsReachedListener() {
		public void OnMaxConnectionsReached() {
			Log.d(TAG, "Max connections reached");
			// TODO Let libgdx class know it can start the game
		}
	};
	private OnIncomingConnectionListener connectedListener = new OnIncomingConnectionListener() {
		public void OnIncomingConnection(final BluetoothDevice device) {
			Log.d(TAG, "Incoming connection: " + device.getName());
			// TODO Send on message to libgdx about who has connected so it can
			// be displayed

			((AndroidApplication) context).runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(context, "Connected to: " + device.getName(),
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	};


	private OnConnectionLostListener disconnectedListener = new OnConnectionLostListener() {
		public void OnConnectionLost(BluetoothDevice device) {
			Log.d(TAG, "Connection lost: " + device);

			// Show a dialogue notifying user it got disconnected
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
							// TODO Let app terminate itself?
							// finish();
						}
					});
					connectionLostAlert.setCancelable(false);
					try {
						connectionLostAlert.show();
					} catch (BadTokenException e) {
						// Something really bad happened here;
						// seems like the Activity itself went away before
						// the runnable finished.
						// Bail out gracefully here and do nothing.
					}
				}
			}
			// Display on UI-thread
			((AndroidApplication) context)
			.runOnUiThread(new displayConnectionLostAlert());

			// shutdown EVERYTHING!
			destroy();
		}
	};

	private OnConnectionServiceReadyListener serviceReadyListener = new OnConnectionServiceReadyListener() {
		public void OnConnectionServiceReady() {
			Log.d(TAG, "Connection service ready");
		}
	};

	public void hostSession() {
		((AndroidApplication)context).runOnUiThread(new Runnable() {
			public void run()
			{
				Toast.makeText(context, "Hosting Game", Toast.LENGTH_SHORT).show();
			}
		});
		addTenduToDeviceName(true);
		connection.startServer(MAX_NUMBER_OF_PLAYERS, connectedListener,
				maxConnectionsListener, dataReceivedListener,
				disconnectedListener);
	}

	public void joinGame() {
		((AndroidApplication)context).runOnUiThread(new Runnable() {
			public void run()
			{ 
				Toast.makeText(context, "Joining Game", Toast.LENGTH_SHORT).show();
			}
		});
		if (D) Log.d(TAG, "joinGame() called");
		this.mBluetoothAdapter.startDiscovery();

		// Wait awhile for the handset to discover units
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
					BluetoothDevice bd = findAvailableServerDevice();
					if (bd != null) {
						Log.d(TAG,
								"Will now try and connect to: " + bd.getName());
						connection.connect(bd, dataReceivedListener,
								disconnectedListener);
					} else {
						Log.d(TAG, "No device to connect to");
					}
			}
		}, CONNECTION_DELAY);
	}

	/**
	 * Goes through the list of discovered devices and checks if they are valid
	 * "Tendu" players. Then adds these to a list of "team members".
	 * 
	 * @return list of team members
	 * @see {@link devicesList}, {@link isDeviceValid}
	 */
	public Set<BluetoothDevice> searchTeam() {

		Set<BluetoothDevice> devices = new HashSet<BluetoothDevice>();
		for (BluetoothDevice d : availableDevices) {
			if (isDeviceValidClient(d)) {
				devices.add(d);
			}
		}
		return devices;
	}

	// ----------------------- HELPER METHODS ------------------------

	/**
	 * Checks if bluetooth is enabled. If <code>true</code> does nothing. If
	 * <code>false</code> prompts the user to enable bluetooth.
	 */
	private void enableBluetooth() {
		Intent enableBtIntent;
		if (!mBluetoothAdapter.isEnabled()) {
			enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			((AndroidApplication) context).startActivityForResult(
					enableBtIntent, REQUEST_ENABLE_BT); // context is wrong?
		}
	}

	/**
	 * Adds a the name "Tendu" as a suffix to this device name. This is needed
	 * as identification
	 * 
	 * If the device has no name, it is set to "Tendu"
	 * 
	 * @param server
	 *            if this device is a server device or not
	 */
	private void addTenduToDeviceName(boolean isServer) {
		if (mBluetoothAdapter.getName() == null) {
			mBluetoothAdapter.setName("");
		} else {
			removeTenduFromDeviceName();
		}

		String oldName = mBluetoothAdapter.getName();
		String newName = new String(oldName);

		if (isServer) {
			if (!oldName.contains(Constants.SERVER_NAME)) {
				newName = oldName + Constants.SERVER_NAME;
			} 
		} else {
			if (!oldName.contains(Constants.CLIENT_NAME)) {
				newName = oldName + Constants.CLIENT_NAME;
			} 
		}
		
		boolean nameWasChanged = mBluetoothAdapter.setName(newName);
		if (nameWasChanged)
			Log.d(TAG, "Device name changed succesfully to: "
					+ mBluetoothAdapter.getName());
		else
			Log.d(TAG,
					"Device namechange failed: " + mBluetoothAdapter.getName());
	}

	private void removeTenduFromDeviceName() {
		String oldName = mBluetoothAdapter.getName();
		if (oldName.contains(Constants.SERVER_NAME)) {
			String newName = oldName.replace(Constants.SERVER_NAME, "");
			mBluetoothAdapter.setName(newName);
			oldName = newName;
		} 
		  
		if (oldName.contains(Constants.CLIENT_NAME)) {
			String newName = oldName.replace(Constants.CLIENT_NAME, "");
			mBluetoothAdapter.setName(newName);
		}
	}

	/**
	 * Checks if the given device is using "Tendu", rather than just having
	 * Bluetooth enabled
	 * 
	 * @param remote
	 *            {@link BluetoothDevice} to validate
	 * @return <code>true</code> if valid <code>false</code> if non-valid
	 */
	private boolean isDeviceValidClient(BluetoothDevice device) {
		if (device == null)
			return false;
		if (device.getName() == null)
			return false;
		return device.getName().contains(Constants.CLIENT_NAME);
	}

	private boolean isDeviceValidServer(BluetoothDevice device) {
		if (device == null)
			return false;
		if (device.getName() == null)
			return false;
		return device.getName().contains(Constants.SERVER_NAME);
	}

	private void registerBroadcastReceiver() {
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		context.registerReceiver(mReceiver, filter); // Don't forget to
		// unregister during
		// onDestroy
	}

	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (D)
					Log.v(TAG, "Device found: " + device.getName() + "Adress: "
							+ device.getAddress());
				// Add the device to a list
				availableDevices.add(device);
			}
		}
	};

	// Temporary test method
	private BluetoothDevice findAvailableServerDevice() {
		// Return the first eligible device among the available devices set
		Iterator<BluetoothDevice> iter = availableDevices.iterator();
		while (iter.hasNext()) {
			BluetoothDevice device = iter.next();
			if (isDeviceValidServer(device)) {
				return device;
			}
		}
		Log.d(TAG, "No eligible Servers found");
		return null;
	}

	// TODO Needed?
	private BluetoothDevice findFirstAvailableDevice() {
		// Return the first eligible device among the available devices set
		Iterator<BluetoothDevice> iter = availableDevices.iterator();
		while (iter.hasNext()) {
			BluetoothDevice device = iter.next();
			if (isDeviceValidClient(device)) {
				return device;
			}
		}
		Log.d(TAG, "No eligible devices found");
		return null;
	}

	// TODO Needed?
	private void beDiscoverable() {
		Intent discoverableIntent = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(
				BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		context.startActivity(discoverableIntent);
	}

	public void sendMessage(NetworkMessage message) {
		connection.broadcastMessage(message);

	}

	public void destroy() {
		Log.d(TAG, "++++++ON DESTROY++++");
		removeTenduFromDeviceName();
		if (mReceiver != null) {
			context.unregisterReceiver(mReceiver);
		}
		connection.shutdown();
	}

	// Test Method
	public void testStuff() {
		connection.broadcastMessage(new NetworkMessage());
	}

	// Message handler
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == BluetoothGameService.MESSAGE_READ) {
				// Do nothing for now
			}
		}
	};

	/**
	 * @return the connectedDevices
	 */
	public Set<BluetoothDevice> getConnectedDevices() {
		return connectedDevices;
	}
}
