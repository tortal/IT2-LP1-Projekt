package it.chalmers.tendu.network.bluetooth;

import it.chalmers.tendu.TestObject;
import it.chalmers.tendu.gamemodel.GameStateBundle;
import it.chalmers.tendu.network.INetworkHandler;
import it.chalmers.tendu.network.NetworkState;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class BluetoothHandler implements INetworkHandler {
	private boolean D = true; // Debug flag
	private String TAG = "BluetoothHandler";

	private PropertyChangeSupport pcs;

	/** Identifying Variables */
	public static final int REQUEST_ENABLE_BT = 666;
	private static final String APP_NAME = "Tendu";

	BluetoothGameService bgs;
	/** Context in which the handler was declared */
	Context context;
	private BluetoothAdapter mBluetoothAdapter;
	/** All devices that has been discovered */
	private Set<BluetoothDevice> devicesSet;

	// Test object
	private GameStateBundle gameState = new GameStateBundle(5, "MeegaTest");
	
	/**
	 * Using the context provided by the class declaring this object, initiates
	 * all parameters needed to establish both a connection to a running
	 * bluetooth server and acting as a server itself.
	 * 
	 * @param <code>Context</code> in which the handler was declared
	 */

	public BluetoothHandler(Context context) {
		this.context = context;
		pcs = new PropertyChangeSupport(this);

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			enableBluetooth();
		}

		bgs = new BluetoothGameService(context, mHandler);
		devicesSet = new HashSet();
		registerBroadcastReceiver();

		addTenduToDeviceName();
	}

	@Override
	public void hostSession() {
		beDiscoverable();
		bgs.start();

	}

	@Override
	public void joinGame() {
		if (D) Log.d(TAG, "joinGame() called");
		this.mBluetoothAdapter.startDiscovery();

		// 
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				BluetoothDevice bd = findFirstAvailableDevice();
				if (bd != null) {
					Log.d(TAG, "Will now try and connect to: " + bd.getName());
					bgs.connect(bd, true);
				} else {
					Log.d(TAG, "No device to connect to");
				}	
			}
			
		}, 5000);
	}
	//git sucks fat balls

	/**
	 * Goes through the list of discovered devices and checks if they are valid
	 * "Tendu" players. Then adds these to a list of "team members".
	 * 
	 * @return list of team members
	 * @see {@link devicesList}, {@link isDeviceValid}
	 */
	public Set<BluetoothDevice> searchTeam() {
		
		Set<BluetoothDevice> devices = new HashSet();
		for (BluetoothDevice d : devicesSet) { // bgs.getDevicesList()){
			if (isDeviceValid(d)) {
				devices.add(d);
			}
		}
		return devices;
	}

	// ----------------------- HELP METHODS ------------------------

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
					enableBtIntent, REQUEST_ENABLE_BT); // context is wrong
		}
	}

	/**
	 * Adds a the name "Tendu" as a suffix to this device name. This is needed
	 * as identification
	 * 
	 * If the device has no name, it is set to "Tendu"
	 */
	private void addTenduToDeviceName() {
		if (mBluetoothAdapter.getName() == null)
			mBluetoothAdapter.setName(APP_NAME + "");
		else {
			String name = mBluetoothAdapter.getName();
			if (!name.contains(APP_NAME)) {
				if(mBluetoothAdapter.setName(name + " - " + APP_NAME + " ")) Log.d(TAG, "Device name changed succesfully to: " + mBluetoothAdapter.getName());
				else Log.d(TAG, "Device namechange failed: " + mBluetoothAdapter.getName());
			}
		}
	}

	private void removeTenduFromDeviceName() {
		if (mBluetoothAdapter.getName().contains(APP_NAME)) {
			String name = mBluetoothAdapter.getName();
			String newName = name.replace(" - " + APP_NAME + " ", "");
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
	private boolean isDeviceValid(BluetoothDevice device) {
		if (device == null)
			return false;
		if (device.getName() == null)
			return false;
		return device.getName().contains(APP_NAME);
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
				devicesSet.add(device);

			}
		}
	};

	// Temporary test method
	private BluetoothDevice findFirstAvailableDevice() {
//		// First look among the paired devices
//		Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
//		for (BluetoothDevice device: devices) {
//			if (isDeviceValid(device)) {
//				return device;
//			}
//		}
//		// Then among the ones that have been discovered

		// Return the first eligible device among the available devices set
		Iterator<BluetoothDevice> iter = devicesSet.iterator();
		while (iter.hasNext()) {
			BluetoothDevice device = iter.next(); 
			if (isDeviceValid(device)) {
				return device;
			}
		}
		Log.d(TAG, "No eligible devices found");
		return null;
	}

	private void beDiscoverable() {
		Intent discoverableIntent = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(
				BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		context.startActivity(discoverableIntent);
	}

	public void sendPing() {
		String testString = "Super communication skills";
		bgs.write(testString.getBytes());
	}

	@Override
	public void sendObject(Serializable o) {
		bgs.kryoWrite(o);

	}
	
	@Override
	public void destroy() {
		Log.d(TAG, "++++++ON DESTROY++++");
		removeTenduFromDeviceName();
		context.unregisterReceiver(mReceiver);
		bgs.stop();
		
	}

	@Override
	public void testStuff() {
		testSendGameState(gameState);
	}
	
	//@Override
	public void testSendGameState(GameStateBundle state) {
		sendObject(state);
	}

	// Message handler
	private final Handler mHandler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		if (msg.what == BluetoothGameService.MESSAGE_READ) {
    			GameStateBundle newGameStateBundle = gameState;
    			String s;
    			s = newGameStateBundle.equals(msg.obj)? "Success":"Failure";
    			
    			Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    		}
    		
    	}
    };

	@Override
	public GameStateBundle pollGameState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkState pollNetworkState() {
		// TODO Auto-generated method stub
		return null;
	}
}
