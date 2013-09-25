package it.chalmers.tendu.network.bluetooth;

import it.chalmers.tendu.network.INetworkHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class BluetoothHandler implements INetworkHandler {
	
	public static final int REQUEST_ENABLE_BT = 666;
	private static final String APP_NAME = "Tendu";
	
	BluetoothGameService bgs;
	Context context;
	private BluetoothAdapter mBluetoothAdapter;
	

	public BluetoothHandler(Context context){
		bgs=new BluetoothGameService(context);
		this.context=context;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		addTenduToName();
	}

	@Override
	public void hostSession() {
		enableBluetooth();
		bgs.start();
		
	}
	
	@Override
	public void joinGame() {
		enableBluetooth();
		BluetoothDevice bd = searchTeam().get(0);
		bgs.connect(bd, true);
	}



	public List<BluetoothDevice> searchTeam() {
		List<BluetoothDevice> list=new ArrayList<BluetoothDevice>();
		for( BluetoothDevice d: bgs.getDevicesList()){
			if(isDeviceValid(d))
				list.add(d);		
		}
		return list;
	}
	
	//----------------------- HELP METHODS ------------------------
	
	private void enableBluetooth() {
		Intent enableBtIntent; 
		if (!mBluetoothAdapter.isEnabled()) {
			enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			((AndroidApplication) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT); // context is wrong
		} 
	}
	
	private void addTenduToName() {
		String name = mBluetoothAdapter.getName();
		if (!name.contains(APP_NAME)) {
			mBluetoothAdapter.setName(name + " - " + APP_NAME);
		}
	}
	
	private boolean isDeviceValid(BluetoothDevice device) {
		return device.getName().contains(APP_NAME);
	}
}
