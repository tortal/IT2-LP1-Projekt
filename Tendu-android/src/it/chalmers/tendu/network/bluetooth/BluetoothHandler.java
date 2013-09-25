package it.chalmers.tendu.network.bluetooth;

import it.chalmers.tendu.network.INetworkHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class BluetoothHandler implements INetworkHandler {
	private boolean D = true; // Debug flag
	private String TAG = "BluetoothHandler";
	
	/** Identifying Variables */
	public static final int REQUEST_ENABLE_BT = 666;
	private static final String APP_NAME = "Tendu";
	
	BluetoothGameService bgs;
	/** Context in which the handler was declared */
	Context context;
	private BluetoothAdapter mBluetoothAdapter;
	/** All devices that has been discovered */
	private List<BluetoothDevice> devicesList;
	
	/**
	 * Using the context provided by the class declaring 
	 * this object, initiates all parameters needed to establish
	 * both a connection to a running bluetooth server
	 * and acting as a server itself.
	 * @param <code>Context</code> in which the handler was declared
	 */
	public BluetoothHandler(Context context){
		this.context=context;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			enableBluetooth(); 
		}
		
		bgs=new BluetoothGameService(context);
		devicesList = new ArrayList();
		registerBroadcastReceiver();
		
		addTenduToName();
	}

	@Override
	public void hostSession() {
		beDiscoverable();
		bgs.start();
		
	}
	
	@Override
	public void joinGame() {
		
		BluetoothDevice bd = findFirstAvailableDevice();
		if (bd != null) { 
			bgs.connect(bd, true);
		} else { 
			Log.d(TAG, "No device to connect to");
		}
	}


	/**
	 * Goes through the list of discovered devices and checks if they are valid "Tendu" players.
	 * Then adds these to a list of "team members".
	 * @return list of team members
	 * @see {@link devicesList}, {@link isDeviceValid}
	 */
	public List<BluetoothDevice> searchTeam() {
		List<BluetoothDevice> list=new ArrayList<BluetoothDevice>();
		for( BluetoothDevice d: devicesList) { // bgs.getDevicesList()){
			if(isDeviceValid(d)) {
				list.add(d);		
			}
		}
		return list;
	}

	//----------------------- HELP METHODS ------------------------
	
	/**
	 * Checks if bluetooth is enabled.
	 * If <code>true</code> does nothing.
	 * If <code>false</code> prompts the user to enable bluetooth.
	 */
	private void enableBluetooth() {
		Intent enableBtIntent; 
		if (!mBluetoothAdapter.isEnabled()) {
			enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			((AndroidApplication) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT); // context is wrong
		} 
	}

	/**
	 * Adds a the name "Tendu" as a suffix to this device name.
	 * This is needed as identification
	 */
	private void addTenduToName() {
		String name = mBluetoothAdapter.getName();
		if (!name.contains(APP_NAME)) {
			mBluetoothAdapter.setName(name + " - " + APP_NAME);
		}
	}
	
	/**
	 * Checks if the given device is using "Tendu", rather then just having Bluetooth enabled
	 * @param remote {@link BluetoothDevice} to validate
	 * @return <code>true</code> if valid
	 * 			<code>false</code> if non-valid
	 */
	private boolean isDeviceValid(BluetoothDevice device) {
		return device.getName().contains(APP_NAME);
	}
	
	
	// Create a BroadcastReceiver for ACTION_FOUND
		private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					// Add the name and address to an array adapter to show in a ListView
					Log.v("Bluetooth", "Device: " + device.getName() + "Adress: " + device.getAddress());
					devicesList.add(device);			
				}
			}
		};
		
		
		/**
		 * Getter
		 * @return {@link devicesList}
		 */
		private List<BluetoothDevice> getDevicesList() {
			return devicesList;
		}


		private void registerBroadcastReceiver() {
			// Register the BroadcastReceiver
			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			context.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
		}
		
		// Temporary test method
		private BluetoothDevice findFirstAvailableDevice() {
			List<BluetoothDevice> devices = searchTeam();
			if (devices.isEmpty()) {
				return null;
			} else return devices.get(0);
		}

		private void beDiscoverable() {
			Intent discoverableIntent = new
					Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			context.startActivity(discoverableIntent);
		}
}
