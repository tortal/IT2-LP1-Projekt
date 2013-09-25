package it.chalmers.tendu.network.bluetooth;

import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.network.INetworkHandler;
import it.chalmers.tendu.network.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class BluetoothHandler implements INetworkHandler {
	
	public static final int REQUEST_ENABLE_BT = 666;
	
	// unique identifier, UUID, for app. Randomly generated on the web
	private static final UUID UUID = java.util.UUID.fromString("a827d540-2042-11e3-8224-0800200c9a66");
	private static final String APP_NAME = "Tendu";
	
	BluetoothGameService bgs;
	Context context;
	private BluetoothAdapter mBluetoothAdapter;
	

	public BluetoothHandler(Context context){
		bgs=new BluetoothGameService(context);
		this.context=context;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	@Override
	public void hostSession() {
		enableBluetooth();
		bgs.start();
		
	}
	
	@Override
	public void joinGame() {
		BluetoothDevice bd=(BluetoothDevice) searchTeam().get(0);
		bgs.connect(bd, true);
	}



	@Override
	public List<Object> searchTeam() {
		// TODO Auto-generated method stub
		return null;
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
}
